import 'dart:async';

import 'package:arch/arch.dart';
import 'package:dio/dio.dart';
import 'package:dio_flutter_transformer/dio_flutter_transformer.dart';
import 'package:flutter/services.dart';
import 'package:pacific_dashboards/configs/global_settings.dart';
import 'package:pacific_dashboards/data/data_source/remote/remote_data_source.dart';
import 'package:pacific_dashboards/data/data_source/remote/rest_client.dart';
import 'package:pacific_dashboards/models/accreditations/accreditation_chunk.dart';
import 'package:pacific_dashboards/models/emis.dart';
import 'package:pacific_dashboards/models/exam/exam.dart';
import 'package:pacific_dashboards/models/lookups/lookups.dart';
import 'package:pacific_dashboards/models/school/school.dart';
import 'package:pacific_dashboards/models/school_enroll/school_enroll.dart';
import 'package:pacific_dashboards/models/teacher/teacher.dart';
import 'package:pacific_dashboards/utils/exceptions.dart';

const _kFederalStatesOfMicronesiaUrl = "https://fedemis.doe.fm/api/";
const _kMarshalIslandsUrl = "http://data.pss.edu.mh/miemis/api/";
const _kKiribatiUrl = "https://data.moe.gov.ki/kemis/api/";

typedef _HandledApiCallable<T> = FutureOr<T> Function(RestClient);
typedef _ThrowableHandler = void Function(Object);

class RemoteDataSourceImpl implements RemoteDataSource {
  static const platform = const MethodChannel('com.pacific_emis.opendata/api');

  final GlobalSettings _settings;

  Dio _dio;
  RestClient _fedemisClient;
  RestClient _miemisClient;
  RestClient _kemisClient;

  RemoteDataSourceImpl(GlobalSettings settings) : _settings = settings {
    _dio = Dio(BaseOptions(
      connectTimeout: Duration(seconds: 10).inMilliseconds,
      receiveTimeout: Duration(minutes: 1).inMilliseconds,
      headers: {
        'Accept-Encoding': 'gzip, deflate',
      },
    ))
      ..interceptors.addAll([
        InterceptorsWrapper(
          onRequest: (options) async {
            final savedETag = await _settings.getEtag(options.url);
            if (savedETag != null && savedETag.isNotEmpty) {
              options.headers['If-None-Match'] = savedETag;
            }
            return options;
          },
          onResponse: (response) {
            if (response.statusCode == 304) {
              return DioError(
                request: response.request,
                response: response,
                type: DioErrorType.RESPONSE,
                error: NoNewDataRemoteException(url: response.requestUrl),
              );
            } else {
              final eTag = response.eTag;
              _settings.setEtag(response.requestUrl, eTag);
              return response;
            }
          },
          onError: (error) {
            print(error);
            return error;
          },
        ),
        LogInterceptor(
          requestBody: true,
          responseBody: true,
        ),
      ])
      ..transformer = FlutterTransformer();

    _fedemisClient = RestClient(_dio, baseUrl: _kFederalStatesOfMicronesiaUrl);
    _miemisClient = RestClient(_dio, baseUrl: _kMarshalIslandsUrl);
    _kemisClient = RestClient(_dio, baseUrl: _kKiribatiUrl);
  }

  void _handleErrors(DioError error) {
    final response = error.response;
    final code = response.statusCode;
    final url = response.requestUrl;
    switch (code) {
      case 401:
        throw UnauthorizedRemoteException(
          url: response.requestUrl,
          message: response.statusMessage,
          code: code,
        );
      case 304:
        throw NoNewDataRemoteException(url: url);
      default:
        throw UnknownRemoteException(url: url);
    }
  }

  Future<T> _withHandlers<T>(
    _HandledApiCallable<T> callable, {
    List<_ThrowableHandler> additionalHandlers,
  }) async {
    try {
      final emis = await _settings.currentEmis;
      RestClient client;
      switch (emis) {
        case Emis.miemis:
          client = _miemisClient;
          break;
        case Emis.fedemis:
          client = _fedemisClient;
          break;
        case Emis.kemis:
          client = _kemisClient;
          break;
      }
      return await callable.call(client);
    } catch (e) {
      if (additionalHandlers != null) {
        for (var handler in additionalHandlers) {
          handler.call(e);
        }
      }
      if (e is DioError) {
        _handleErrors(e);
      }
      rethrow;
    }
  }

  @override
  Future<String> fetchAccessToken() {
    // TODO: implement fetchAccessToken
    throw UnimplementedError();
  }

  @override
  Future<List<Exam>> fetchExams() {
    return _withHandlers((client) => client.getExams());
  }

  @override
  Future<List<SchoolEnroll>> fetchIndividualDistrictEnroll(
    String districtCode,
  ) {
    return _withHandlers(
        (client) => client.getIndividualDistrictEnroll(districtCode));
  }

  @override
  Future<List<SchoolEnroll>> fetchIndividualNationEnroll() {
    return _withHandlers((client) => client.getIndividualNationEnroll());
  }

  @override
  Future<List<SchoolEnroll>> fetchIndividualSchoolEnroll(String schoolId) {
    return _withHandlers(
        (client) => client.getIndividualSchoolEnroll(schoolId));
  }

  @override
  Future<Lookups> fetchLookupsModel() {
    return _withHandlers((client) => client.getLookups());
  }

  @override
  Future<AccreditationChunk> fetchSchoolAccreditationsChunk() async {
    final districtData = await _withHandlers(
        (client) => client.getSchoolAccreditationsByDistrict());
    final standardData = await _withHandlers(
        (client) => client.getSchoolAccreditationsByStandard());
    return AccreditationChunk(
      byDistrict: districtData,
      byStandard: standardData,
    );
  }

  @override
  Future<List<School>> fetchSchools() {
    return _withHandlers((client) => client.getSchools());
  }

  @override
  Future<List<Teacher>> fetchTeachers() {
    return _withHandlers((client) => client.getTeachers());
  }

}

extension Urls on Emis {
  String get baseUrl {
    switch (this) {
      case Emis.miemis:
        return _kMarshalIslandsUrl;
      case Emis.fedemis:
        return _kFederalStatesOfMicronesiaUrl;
      case Emis.kemis:
        return _kKiribatiUrl;
    }
    throw FallThroughError();
  }
}

extension RequestOptionsExt on RequestOptions {
  String get url => this.uri.toString();
}

extension ResponseExt on Response {
  String get eTag => this.headers.value('ETag');

  String get requestUrl => this.request.url;
}