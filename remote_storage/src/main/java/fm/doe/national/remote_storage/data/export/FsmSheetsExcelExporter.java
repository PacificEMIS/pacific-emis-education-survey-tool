package fm.doe.national.remote_storage.data.export;

import android.content.Context;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import fm.doe.national.accreditation_core.data.model.Criteria;
import fm.doe.national.accreditation_core.data.model.EvaluationForm;
import fm.doe.national.accreditation_core.data.model.Standard;
import fm.doe.national.fsm_report.model.SchoolAccreditationLevel;
import fm.doe.national.remote_storage.data.model.ReportBundle;
import fm.doe.national.report_core.model.recommendations.CategoryRecommendation;
import fm.doe.national.report_core.model.recommendations.CriteriaRecommendation;
import fm.doe.national.report_core.model.recommendations.FlattenRecommendationsWrapper;
import fm.doe.national.report_core.model.recommendations.Recommendation;
import io.reactivex.Completable;

public class FsmSheetsExcelExporter extends SheetsExcelExporter {

    private static final String MARK_RECOMMENDATION = "√";
    private static final String MARK_NOT_RECOMMENDATION = " ";
    private static final String FORMAT_CELL_RECOMMENDATION = "$%sRec%s-%d.%d";
    private static final String SUFFIX_POSITIVE = "Pos";
    private static final String SUFFIX_NEGATIVE = "Neg";
    private static final String CELL_SCHOOL_EVALUATION_OBTAINED_SCORE = "$seObtScore";
    private static final String CELL_CLASSROOM_OBSERVATION_OBTAINED_SCORE = "$coObtScore";
    private static final String CELL_TOTAL_OBTAINED_SCORE = "$totalObtScore";
    private static final String CELL_SCHOOL_EVALUATION_MULTIPLIER = "$seMultiplier";
    private static final String CELL_CLASSROOM_OBSERVATION_MULTIPLIER = "$coMultiplier";
    private static final String CELL_SCHOOL_EVALUATION_FINAL_SCORE = "$seScore";
    private static final String CELL_CLASSROOM_OBSERVATION_FINAL_SCORE = "$coScore";
    private static final String CELL_TOTAL_FINAL_SCORE = "$totalScore";
    private static final String CELL_LEVEL_DETERMINATION = "$levelDet";

    public FsmSheetsExcelExporter(Context appContext, Sheets sheetsApi) {
        super(appContext, sheetsApi);
    }

    @Override
    public Completable fillReportSheet(String spreadsheetId, String sheetName, ReportBundle reportBundle) {
        return wrapWithCompletableInThreadPool(() -> {
            List<ValueRange> rangesToUpdate = new ArrayList<>();
            rangesToUpdate.add(createLevelDeterminationValueRange(sheetName, reportBundle.getSchoolAccreditationLevel()));
            rangesToUpdate.addAll(createLevelsValueRanges(sheetName, reportBundle.getSchoolAccreditationLevel()));
            rangesToUpdate.addAll(createInfoValueRanges(sheetName, reportBundle.getHeader()));
            rangesToUpdate.addAll(createEvaluationScoreValueRanges(sheetName, reportBundle.getSummary(), EvaluationForm.SCHOOL_EVALUATION));
            rangesToUpdate.addAll(createEvaluationScoreValueRanges(sheetName, reportBundle.getSummary(), EvaluationForm.CLASSROOM_OBSERVATION));
            rangesToUpdate.addAll(createEvaluationRecommendationsValueRange(sheetName, reportBundle.getRecommendations(), EvaluationForm.SCHOOL_EVALUATION));
            rangesToUpdate.addAll(createEvaluationRecommendationsValueRange(sheetName, reportBundle.getRecommendations(), EvaluationForm.CLASSROOM_OBSERVATION));
            updateValues(spreadsheetId, rangesToUpdate);
        });
    }

    private List<ValueRange> createEvaluationRecommendationsValueRange(String sheetName,
                                                                       FlattenRecommendationsWrapper recommendationsWrapper,
                                                                       EvaluationForm evaluationForm) {
        String cellPrefix = getEvaluationFormPrefix(evaluationForm);
        List<? extends Standard> filteredStandards = recommendationsWrapper.getFlattenSurvey().getCategories()
                .stream()
                .filter(c -> c.getEvaluationForm() == evaluationForm)
                .flatMap(c -> c.getStandards().stream())
                .collect(Collectors.toList());
        List<Recommendation> filteredRecommendations = filterRecommendations(recommendationsWrapper, evaluationForm);
        return createRecommendationTableValueRanges(sheetName, filteredStandards, filteredRecommendations, cellPrefix);
    }

    private List<ValueRange> createRecommendationTableValueRanges(String sheetName,
                                                                  List<? extends Standard> standards,
                                                                  List<Recommendation> recommendations,
                                                                  String cellPrefix) {
        List<ValueRange> ranges = new ArrayList<>();
        for (int standardIndex = 0; standardIndex < standards.size(); standardIndex++) {
            final Standard standard = standards.get(standardIndex);
            for (int criteriaIndex = 0; criteriaIndex < standard.getCriterias().size(); criteriaIndex++) {
                final Criteria criteria = standard.getCriterias().get(criteriaIndex);
                Optional<Recommendation> existingRecommendation = recommendations.stream()
                        .filter(r -> (r instanceof CriteriaRecommendation) &&
                                ((CriteriaRecommendation) r).getObject().getSuffix().equals(criteria.getSuffix()))
                        .findFirst();

                ranges.add(createSingleCellValueRange(
                        sheetName,
                        getCellRange(FORMAT_CELL_RECOMMENDATION, cellPrefix, SUFFIX_POSITIVE, standardIndex + 1, criteriaIndex + 1),
                        existingRecommendation.isPresent() ? MARK_NOT_RECOMMENDATION : MARK_RECOMMENDATION
                ));
                ranges.add(createSingleCellValueRange(
                        sheetName,
                        getCellRange(FORMAT_CELL_RECOMMENDATION, cellPrefix, SUFFIX_NEGATIVE, standardIndex + 1, criteriaIndex + 1),
                        existingRecommendation.isPresent() ? MARK_RECOMMENDATION : MARK_NOT_RECOMMENDATION
                ));
            }
        }
        return ranges;
    }

    private List<Recommendation> filterRecommendations(FlattenRecommendationsWrapper recommendationsWrapper, EvaluationForm evaluationForm) {
        List<Recommendation> filteredRecommendations = new ArrayList<>();
        boolean isInCurrentRecommendationsBlock = false;
        for (Recommendation recommendation : recommendationsWrapper.getRecommendations()) {
            if (recommendation instanceof CategoryRecommendation) {
                isInCurrentRecommendationsBlock = ((CategoryRecommendation) recommendation).getObject().getEvaluationForm() == evaluationForm;
                continue;
            }

            if (isInCurrentRecommendationsBlock) {
                filteredRecommendations.add(recommendation);
            }
        }
        return filteredRecommendations;
    }

    private List<ValueRange> createLevelsValueRanges(String sheetName, SchoolAccreditationLevel level) {
        return Arrays.asList(
                createSingleCellValueRange(
                        sheetName,
                        getCellRange(CELL_SCHOOL_EVALUATION_OBTAINED_SCORE),
                        level.getForms().get(0).getObtainedScore()
                ),
                createSingleCellValueRange(
                        sheetName,
                        getCellRange(CELL_SCHOOL_EVALUATION_MULTIPLIER),
                        level.getForms().get(0).getMultiplier()
                ),
                createSingleCellValueRange(
                        sheetName,
                        getCellRange(CELL_SCHOOL_EVALUATION_FINAL_SCORE),
                        level.getForms().get(0).getFinalScore()
                ),
                createSingleCellValueRange(
                        sheetName,
                        getCellRange(CELL_CLASSROOM_OBSERVATION_OBTAINED_SCORE),
                        level.getForms().get(1).getObtainedScore()
                ),
                createSingleCellValueRange(
                        sheetName,
                        getCellRange(CELL_CLASSROOM_OBSERVATION_MULTIPLIER),
                        level.getForms().get(1).getMultiplier()
                ),
                createSingleCellValueRange(
                        sheetName,
                        getCellRange(CELL_CLASSROOM_OBSERVATION_FINAL_SCORE),
                        level.getForms().get(1).getFinalScore()
                ),
                createSingleCellValueRange(
                        sheetName,
                        getCellRange(CELL_TOTAL_OBTAINED_SCORE),
                        level.getTotalObtainedScore()
                ),
                createSingleCellValueRange(
                        sheetName,
                        getCellRange(CELL_TOTAL_FINAL_SCORE),
                        level.getTotalScore()
                )
        );
    }

    private ValueRange createLevelDeterminationValueRange(String sheetName, SchoolAccreditationLevel level) {
        return createSingleCellValueRange(
                sheetName,
                getCellRange(CELL_LEVEL_DETERMINATION),
                level.getReportLevel().getName().getString(appContext)
        );
    }

}
