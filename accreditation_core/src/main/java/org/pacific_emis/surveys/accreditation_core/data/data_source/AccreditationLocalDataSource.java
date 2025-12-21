package org.pacific_emis.surveys.accreditation_core.data.data_source;

import android.content.Context;

import androidx.room.Room;

import org.pacific_emis.surveys.accreditation_core.data.model.AccreditationSurvey;
import org.pacific_emis.surveys.accreditation_core.data.model.Answer;
import org.pacific_emis.surveys.accreditation_core.data.model.Category;
import org.pacific_emis.surveys.accreditation_core.data.model.Criteria;
import org.pacific_emis.surveys.accreditation_core.data.model.ObservationInfo;
import org.pacific_emis.surveys.accreditation_core.data.model.ObservationLogRecord;
import org.pacific_emis.surveys.accreditation_core.data.model.Standard;
import org.pacific_emis.surveys.accreditation_core.data.model.SubCriteria;
import org.pacific_emis.surveys.accreditation_core.data.model.mutable.MutableAccreditationSurvey;
import org.pacific_emis.surveys.accreditation_core.data.model.mutable.MutableObservationLogRecord;
import org.pacific_emis.surveys.accreditation_core.data.persistence.AccreditationDatabase;
import org.pacific_emis.surveys.accreditation_core.data.persistence.dao.AnswerDao;
import org.pacific_emis.surveys.accreditation_core.data.persistence.dao.CategoryDao;
import org.pacific_emis.surveys.accreditation_core.data.persistence.dao.ObservationLogRecordDao;
import org.pacific_emis.surveys.accreditation_core.data.persistence.dao.PhotoDao;
import org.pacific_emis.surveys.accreditation_core.data.persistence.dao.SurveyDao;
import org.pacific_emis.surveys.accreditation_core.data.persistence.entity.RoomAccreditationSurvey;
import org.pacific_emis.surveys.accreditation_core.data.persistence.entity.RoomAnswer;
import org.pacific_emis.surveys.accreditation_core.data.persistence.entity.RoomCategory;
import org.pacific_emis.surveys.accreditation_core.data.persistence.entity.RoomCriteria;
import org.pacific_emis.surveys.accreditation_core.data.persistence.entity.RoomObservationLogRecord;
import org.pacific_emis.surveys.accreditation_core.data.persistence.entity.RoomPhoto;
import org.pacific_emis.surveys.accreditation_core.data.persistence.entity.RoomStandard;
import org.pacific_emis.surveys.accreditation_core.data.persistence.entity.RoomSubCriteria;
import org.pacific_emis.surveys.accreditation_core.data.persistence.entity.relative.RelativeRoomSurvey;
import org.pacific_emis.surveys.core.data.exceptions.WrongAppRegionException;
import org.pacific_emis.surveys.core.data.local_data_source.CoreLocalDataSource;
import org.pacific_emis.surveys.core.data.model.Photo;
import org.pacific_emis.surveys.core.data.model.Survey;
import org.pacific_emis.surveys.core.data.model.mutable.MutablePhoto;
import org.pacific_emis.surveys.core.preferences.entities.AppRegion;
import org.pacific_emis.surveys.core.preferences.entities.UploadState;
import org.pacific_emis.surveys.core.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class AccreditationLocalDataSource extends CoreLocalDataSource implements AccreditationDataSource {

    private static final String DATABASE_NAME = "accreditation.database";
    private static final String TEMPLATE_DATABASE_NAME = "accreditation.template_database";

    private final SurveyDao surveyDao;
    private final AnswerDao answerDao;
    private final PhotoDao photoDao;

    private final AccreditationDatabase templateDatabase;
    private final AccreditationDatabase database;

    public AccreditationLocalDataSource(Context applicationContext) {
        super(applicationContext);

        database = Room
                .databaseBuilder(applicationContext, AccreditationDatabase.class, DATABASE_NAME)
                .addMigrations(
                        AccreditationDatabase.MIGRATION_1_2,
                        AccreditationDatabase.MIGRATION_2_3,
                        AccreditationDatabase.MIGRATION_3_4,
                        AccreditationDatabase.MIGRATION_4_5,
                        AccreditationDatabase.MIGRATION_5_6
                ).build();
        templateDatabase = Room.databaseBuilder(applicationContext, AccreditationDatabase.class, TEMPLATE_DATABASE_NAME)
                .addMigrations(
                        AccreditationDatabase.MIGRATION_1_2,
                        AccreditationDatabase.MIGRATION_2_3,
                        AccreditationDatabase.MIGRATION_3_4,
                        AccreditationDatabase.MIGRATION_4_5,
                        AccreditationDatabase.MIGRATION_5_6
                ).build();
        surveyDao = database.getSurveyDao();
        answerDao = database.getAnswerDao();
        photoDao = database.getPhotoDao();
    }

    public void closeConnections() {
        super.closeConnections();
        database.close();
        templateDatabase.close();
    }

    @Override
    public Completable rewriteTemplateSurvey(Survey survey) {
        return Completable.fromAction(() -> {
            templateDatabase.getSurveyDao().deleteAllForAppRegion(survey.getAppRegion());
            saveSurvey(templateDatabase, (AccreditationSurvey) survey, false);
        });
    }

    private long saveSurvey(AccreditationDatabase database, AccreditationSurvey accreditationSurvey, boolean shouldCreateAnswers) {
        RoomAccreditationSurvey roomSurvey = new RoomAccreditationSurvey(accreditationSurvey);
        roomSurvey.uid = 0;
        long id = database.getSurveyDao().insert(roomSurvey);
        if (accreditationSurvey.getCategories() != null) {
            saveCategories(database, accreditationSurvey.getCategories(), id, shouldCreateAnswers);
        }
        return id;
    }

    private void saveCategories(AccreditationDatabase database,
                                List<? extends Category> categories,
                                long surveyId,
                                boolean shouldCreateAnswers) {
        for (Category category : categories) {
            RoomCategory roomCategory = new RoomCategory(category);
            roomCategory.uid = 0;
            roomCategory.surveyId = surveyId;
            long id = database.getCategoryDao().insert(roomCategory);
            if (category.getStandards() != null) {
                saveStandards(database, category.getStandards(), id, shouldCreateAnswers);
            }
            if (!CollectionUtils.isEmpty(category.getLogRecords())) {
                saveObservationLogRecords(database, category.getLogRecords(), id);
            }
        }
    }

    private void saveObservationLogRecords(AccreditationDatabase database,
                                           List<? extends ObservationLogRecord> records,
                                           long categoryId) {
        for (ObservationLogRecord record : records) {
            RoomObservationLogRecord roomRecord = RoomObservationLogRecord.from(record);
            roomRecord.uid = 0;
            roomRecord.categoryId = categoryId;
            database.getObservationLogRecordDao().insert(roomRecord);
        }
    }

    private void saveStandards(AccreditationDatabase database,
                               List<? extends Standard> standards,
                               long categoryId,
                               boolean shouldCreateAnswers) {
        for (Standard standard : standards) {
            RoomStandard roomStandard = new RoomStandard(standard);
            roomStandard.uid = 0;
            roomStandard.categoryId = categoryId;
            long id = database.getStandardDao().insert(roomStandard);
            if (standard.getCriterias() != null) {
                saveCriterias(database, standard.getCriterias(), id, shouldCreateAnswers);
            }
        }
    }

    private void saveCriterias(AccreditationDatabase database,
                               List<? extends Criteria> criterias,
                               long standardId,
                               boolean shouldCreateAnswers) {
        for (Criteria criteria : criterias) {
            RoomCriteria roomCriteria = new RoomCriteria(criteria);
            roomCriteria.uid = 0;
            roomCriteria.standardId = standardId;
            long id = database.getCriteriaDao().insert(roomCriteria);
            if (criteria.getSubCriterias() != null) {
                saveSubCriterias(database, criteria.getSubCriterias(), id, shouldCreateAnswers);
            }
        }
    }

    private void saveSubCriterias(AccreditationDatabase database,
                                  List<? extends SubCriteria> subCriterias,
                                  long criteriaId,
                                  boolean shouldCreateAnswers) {
        for (SubCriteria subCriteria : subCriterias) {
            RoomSubCriteria roomSubCriteria = new RoomSubCriteria(subCriteria);
            roomSubCriteria.uid = 0;
            roomSubCriteria.criteriaId = criteriaId;
            long id = database.getSubcriteriaDao().insert(roomSubCriteria);
            Answer answer = subCriteria.getAnswer();

            if (answer != null) {
                RoomAnswer roomAnswer = new RoomAnswer(answer);
                roomAnswer.subCriteriaId = id;
                long answerId = database.getAnswerDao().insert(roomAnswer);

                if (answer.getPhotos() != null) {
                    savePhotos(database, answer.getPhotos(), answerId);
                }
            } else if (shouldCreateAnswers) {
                database.getAnswerDao().insert(new RoomAnswer(id));
            }
        }
    }

    private void savePhotos(AccreditationDatabase database,
                            List<? extends Photo> photos,
                            long answerId) {
        photos.forEach(photo -> {
            RoomPhoto roomPhoto = new RoomPhoto(photo);
            roomPhoto.answerId = answerId;
            database.getPhotoDao().insert(roomPhoto);
        });
    }

    @Override
    public Single<Survey> getTemplateSurvey(AppRegion appRegion) {
        // TODO replace temp region
        return Single.fromCallable(() -> {
                    RelativeRoomSurvey survey = templateDatabase.getSurveyDao().getFirstFilled(appRegion);
                    if (survey == null) {
                        survey = templateDatabase.getSurveyDao().getFirstFilled(AppRegion.FSM);
                    }
                    return survey;
                })
                .map(RelativeRoomSurvey::toMutableSurvey);
    }

    @Override
    public Single<Survey> loadSurvey(AppRegion appRegion, long surveyId) {
        return Single.fromCallable(() -> surveyDao.getFilledById(surveyId))
                .map(RelativeRoomSurvey::toMutableSurvey);
    }

    @Override
    public Single<List<Survey>> loadAllSurveys(AppRegion appRegion) {
        return Single.fromCallable(() -> surveyDao.getAllFilled(appRegion))
                .flatMapObservable(Observable::fromIterable)
                .map(RelativeRoomSurvey::toMutableSurvey)
                .cast(Survey.class)
                .toList();
    }

    @Override
    public Single<List<Survey>> loadSurveys(String schoolId, AppRegion appRegion, String surveyTag) {
        return Single.fromCallable(() -> surveyDao.getSurveys(schoolId, appRegion, surveyTag))
                .flatMapObservable(Observable::fromIterable)
                .map(MutableAccreditationSurvey::new)
                .cast(Survey.class)
                .toList();
    }

    @Override
    public Single<Survey> createSurvey(
            String schoolId, String schoolName, Date createDate, String surveyTag, String userEmail, AppRegion appRegion, String tabletId
    ) {
        return getTemplateSurvey(appRegion)
                .flatMap(survey -> {
                    MutableAccreditationSurvey mutableSurvey = new MutableAccreditationSurvey((AccreditationSurvey) survey);
                    mutableSurvey.setId(0);
                    mutableSurvey.setSchoolName(schoolName);
                    mutableSurvey.setSchoolId(schoolId);
                    mutableSurvey.setCreateDate(createDate);
                    mutableSurvey.setSurveyTag(surveyTag);
                    mutableSurvey.setCreateUser(userEmail);
                    mutableSurvey.setLastEditedUser(userEmail);
                    mutableSurvey.setTabletId(tabletId);
                    mutableSurvey.setAppRegion(appRegion);
                    long id = saveSurvey(database, mutableSurvey, true);
                    return loadSurvey(appRegion, id);
                });
    }

    @Override
    public Completable deleteSurvey(long surveyId) {
        return Completable.fromAction(() -> surveyDao.deleteById(surveyId));
    }

    @Override
    public Single<Answer> updateAnswer(Answer answer) {
        return Single.fromCallable(() -> {
                    RoomAnswer existingAnswer = answerDao.getById(answer.getId());
                    existingAnswer.comment = answer.getComment();
                    existingAnswer.state = answer.getState();
                    answerDao.update(existingAnswer);
                    return answerDao.getFilledById(existingAnswer.uid).toMutableAnswer();
                })
                .flatMap(mutableAnswer -> {
                    List<MutablePhoto> existingPhotos = mutableAnswer.getPhotos();
                    List<MutablePhoto> expectedPhotos = answer.getPhotos() == null ? new ArrayList<>() : answer.getPhotos().stream()
                            .map(MutablePhoto::new).collect(Collectors.toList());
                    // Note: update photo === delete + create
                    List<MutablePhoto> photosToDelete = new ArrayList<>();
                    List<MutablePhoto> photosToCreate = new ArrayList<>();
                    List<MutablePhoto> photosToUpdate = new ArrayList<>();

                    for (MutablePhoto existingPhoto : existingPhotos) {
                        Optional<MutablePhoto> updatedPhoto = expectedPhotos.stream()
                                .filter(p -> p.getId() == existingPhoto.getId())
                                .findFirst();

                        if (!updatedPhoto.isPresent()) {
                            photosToDelete.add(existingPhoto);
                            continue;
                        }

                        expectedPhotos.remove(updatedPhoto.get());

                        if (!existingPhoto.equals(updatedPhoto.get())) {
                            photosToUpdate.add(updatedPhoto.get());
                        }
                    }

                    photosToCreate.addAll(expectedPhotos);

                    return updatePhotos(photosToUpdate, mutableAnswer.getId())
                            .andThen(deletePhotos(photosToDelete))
                            .andThen(createPhotos(photosToCreate, mutableAnswer.getId()))
                            .andThen(Single.fromCallable(() -> answerDao.getFilledById(mutableAnswer.getId()).toMutableAnswer()));
                });
    }

    private Completable updatePhotos(List<MutablePhoto> photos, long answerId) {
        return Observable.fromIterable(photos)
                .map(photo -> {
                    RoomPhoto roomPhoto = new RoomPhoto(photo);
                    roomPhoto.answerId = answerId;
                    photoDao.update(roomPhoto);
                    return photo;
                })
                .toList()
                .ignoreElement();
    }

    private Completable deletePhotos(List<MutablePhoto> photos) {
        return Observable.fromIterable(photos)
                .map(photo -> {
                    photoDao.deleteById(photo.getId());
                    return photo;
                })
                .toList()
                .ignoreElement();
    }

    private Completable createPhotos(List<MutablePhoto> photos, long answerId) {
        return Observable.fromIterable(photos)
                .map(photo -> {
                    RoomPhoto roomPhoto = new RoomPhoto(photo);
                    roomPhoto.answerId = answerId;
                    photoDao.insert(roomPhoto);
                    return photo;
                })
                .toList()
                .ignoreElement();
    }

    @Override
    public Single<Photo> createPhoto(Photo photo, long answerId) {
        return Single.fromCallable(() -> {
            RoomPhoto roomPhoto = new RoomPhoto(photo);
            roomPhoto.answerId = answerId;
            long photoId = photoDao.insert(roomPhoto);
            return new MutablePhoto(photoDao.getById(photoId));
        });
    }

    @Override
    public Completable deletePhoto(long photoId) {
        return Completable.fromAction(() -> photoDao.deleteById(photoId));
    }

    @Override
    public Completable deleteCreatedSurveys() {
        return Completable.fromAction(surveyDao::deleteAll);
    }

    @Override
    public Completable createPartiallySavedSurvey(AppRegion appRegion, Survey survey) {
        return Completable.fromAction(() -> {
            MutableAccreditationSurvey accreditationSurvey = ((AccreditationSurvey) survey).toMutable();
            if (appRegion == accreditationSurvey.getAppRegion()) {
                accreditationSurvey.setUploadState(UploadState.SUCCESSFULLY);
                saveSurvey(database, accreditationSurvey, true);
            } else {
                throw new WrongAppRegionException();
            }
        });
    }

    @Override
    public void updateSurvey(Survey survey) {
        database.getSurveyDao().update(new RoomAccreditationSurvey((AccreditationSurvey) survey))
                .subscribeOn(Schedulers.io())
                .subscribe(() -> {}, Throwable::printStackTrace);
    }

    @Override
    public List<Photo> getPhotos(Survey survey) {
        return ((AccreditationSurvey) survey).getCategories().stream()
                .flatMap(c -> c.getStandards().stream())
                .flatMap(s -> s.getCriterias().stream())
                .flatMap(c -> c.getSubCriterias().stream())
                .flatMap(sc -> {
                    List<? extends Photo> photos = sc.getAnswer().getPhotos();
                    if (CollectionUtils.isEmpty(photos)) {
                        return Stream.empty();
                    }
                    return photos.stream();
                })
                .collect(Collectors.toList());
    }

    @Override
    public Completable updatePhotoWithRemote(Photo photo, String remoteFileId) {
        return Completable.fromAction(() -> {
            RoomPhoto roomPhoto = photoDao.getById(photo.getId());
            roomPhoto.remoteUrl = remoteFileId;
            photoDao.update(roomPhoto);
        });
    }

    @Override
    public void setSurveyUploadState(Survey survey, UploadState uploadState) {
        MutableAccreditationSurvey mutableSurvey = ((AccreditationSurvey) survey).toMutable();
        mutableSurvey.setUploadState(uploadState);
        updateSurvey(mutableSurvey);
    }

    @Override
    public void setSurveyDriveFileId(Survey survey, String driveFileId) {
        MutableAccreditationSurvey mutableSurvey = ((AccreditationSurvey) survey).toMutable();
        mutableSurvey.setDriveFileId(driveFileId);
        updateSurvey(mutableSurvey);
    }

    @Override
    public Completable updateObservationInfo(ObservationInfo observationInfo, long categoryId) {
        return Completable.fromAction(() -> {
            CategoryDao dao = database.getCategoryDao();
            RoomCategory category = dao.getById(categoryId);
            category.observationInfoTeacherName = observationInfo.getTeacherName();
            category.observationInfoTeacherId = observationInfo.getTeacherId();
            category.observationInfoGrade = observationInfo.getGrade();
            category.observationInfoTotalStudentsPresent = observationInfo.getTotalStudentsPresent();
            category.observationInfoSubject = observationInfo.getSubject();
            category.observationInfoDate = observationInfo.getDate();
            dao.update(category);
        });
    }

    @Override
    public Single<List<MutableObservationLogRecord>> getLogRecordsForCategoryWithId(long categoryId) {
        return Single.fromCallable(() -> {
            ObservationLogRecordDao dao = database.getObservationLogRecordDao();
            List<RoomObservationLogRecord> roomRecords = dao.getAllForCategoryWithId(categoryId);
            return roomRecords.stream().map(MutableObservationLogRecord::from).collect(Collectors.toList());
        });
    }

    @Override
    public Completable updateObservationLogRecord(ObservationLogRecord record) {
        return Completable.fromAction(() -> {
            ObservationLogRecordDao dao = database.getObservationLogRecordDao();
            RoomObservationLogRecord storedRecord = dao.getById(record.getId());
            storedRecord.date = record.getDate();
            storedRecord.studentsAction = record.getStudentsActions();
            storedRecord.teacherAction = record.getTeacherActions();
            dao.update(storedRecord);
        });
    }

    @Override
    public Completable deleteObservationLogRecord(long recordId) {
        return Completable.fromAction(() -> {
            ObservationLogRecordDao dao = database.getObservationLogRecordDao();
            dao.deleteById(recordId);
        });
    }

    @Override
    public Single<MutableObservationLogRecord> createEmptyLogRecord(long categoryId, Date date) {
        return Single.fromCallable(() -> {
            ObservationLogRecordDao dao = database.getObservationLogRecordDao();
            RoomObservationLogRecord newRecord = new RoomObservationLogRecord(categoryId, date, null, null);
            final long id = dao.insert(newRecord);
            return MutableObservationLogRecord.from(dao.getById(id));
        });
    }

    @Override
    public Completable createLogRecords(long categoryId, List<? extends ObservationLogRecord> records) {
        return Completable.fromAction(() -> {
            ObservationLogRecordDao dao = database.getObservationLogRecordDao();
            for (ObservationLogRecord record : records) {
                RoomObservationLogRecord newRecord = new RoomObservationLogRecord(
                        categoryId,
                        record.getDate(),
                        record.getTeacherActions(),
                        record.getStudentsActions()
                );
                dao.insert(newRecord);
            }
        });
    }
}