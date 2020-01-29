import 'package:built_collection/built_collection.dart';
import 'package:pacific_dashboards/models/emis.dart';
import 'package:pacific_dashboards/models/exam/exam.dart';
import 'package:pacific_dashboards/models/lookups/lookups.dart';
import 'package:pacific_dashboards/models/school/school.dart';
import 'package:pacific_dashboards/models/teacher/teacher.dart';

abstract class Database {
  LookupsDao get lookups;
  StringsDao get strings;
  SchoolsDao get schools;
  TeachersDao get teachers;
  ExamsDao get exams;
}

abstract class LookupsDao {
  Future<void> save(Lookups lookups, Emis emis);
  Future<Lookups> get(Emis emis);
}

abstract class StringsDao {
  Future<void> save(String key, String string);
  Future<String> getByKey(String key, {String defaultValue});
}

abstract class SchoolsDao {
  Future<void> save(BuiltList<School> schools, Emis emis);
  Future<BuiltList<School>> get(Emis emis);
}

abstract class TeachersDao {
  Future<void> save(BuiltList<Teacher> teachers, Emis emis);
  Future<BuiltList<Teacher>> get(Emis emis);
}

abstract class ExamsDao {
  Future<void> save(BuiltList<Exam> exams, Emis emis);
  Future<BuiltList<Exam>> get(Emis emis);
}