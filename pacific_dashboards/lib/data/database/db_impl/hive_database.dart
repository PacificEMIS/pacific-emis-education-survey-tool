import 'package:hive/hive.dart';
import 'package:hive_flutter/hive_flutter.dart';
import 'package:pacific_dashboards/data/database/database.dart';
import 'package:pacific_dashboards/data/database/db_impl/accreditations_dao_impl.dart';
import 'package:pacific_dashboards/data/database/db_impl/exams_dao_impl.dart';
import 'package:pacific_dashboards/data/database/db_impl/lookups_dao_impl.dart';
import 'package:pacific_dashboards/data/database/db_impl/schools_dao_impl.dart';
import 'package:pacific_dashboards/data/database/db_impl/strings_dao_impl.dart';
import 'package:pacific_dashboards/data/database/db_impl/teachers_dao_impl.dart';
import 'package:pacific_dashboards/data/database/model/accreditation/hive_district_accreditation.dart';
import 'package:pacific_dashboards/data/database/model/accreditation/hive_standard_accreditation.dart';
import 'package:pacific_dashboards/data/database/model/accreditation/hive_accreditation_chunk.dart';
import 'package:pacific_dashboards/data/database/model/exam/hive_exam.dart';
import 'package:pacific_dashboards/data/database/model/lookup/hive_lookup.dart';
import 'package:pacific_dashboards/data/database/model/lookup/hive_lookups.dart';
import 'package:pacific_dashboards/data/database/model/school/hive_school.dart';
import 'package:pacific_dashboards/data/database/model/teacher/hive_teacher.dart';

// typeIds {0, 1, 2, 3, 4, 5 ,6, 7}
class HiveDatabase extends Database {
  LookupsDao _lookupsDao;
  StringsDao _stringsDao;
  SchoolsDao _schoolsDao;
  TeachersDao _teachersDao;
  ExamsDao _examsDao;
  AccreditationsDao _accreditationsDao;

  Future<void> init() async {
    await Hive.initFlutter();
    Hive
      ..registerAdapter(HiveLookupsAdapter())
      ..registerAdapter(HiveLookupAdapter())
      ..registerAdapter(HiveSchoolAdapter())
      ..registerAdapter(HiveTeacherAdapter())
      ..registerAdapter(HiveExamAdapter())
      ..registerAdapter(HiveStandardAccreditationAdapter())
      ..registerAdapter(HiveDistrictAccreditationAdapter())
      ..registerAdapter(HiveAccreditationChunkAdapter());

    _lookupsDao = HiveLookupsDao();

    final stringDao = HiveStringsDao();
    await stringDao.init();
    _stringsDao = stringDao;

    _schoolsDao = HiveSchoolsDao();
    _teachersDao = HiveTeachersDao();
    _examsDao = HiveExamsDao();
    _accreditationsDao = HiveAccreditationsDao();
  }

  @override
  LookupsDao get lookups => _lookupsDao;

  @override
  StringsDao get strings => _stringsDao;

  @override
  SchoolsDao get schools => _schoolsDao;

  @override
  TeachersDao get teachers => _teachersDao;

  @override
  ExamsDao get exams => _examsDao;

  @override
  AccreditationsDao get accreditations => _accreditationsDao;
}
