import 'dart:convert';

import 'package:built_collection/built_collection.dart';
import 'package:built_value/built_value.dart';
import 'package:built_value/serializer.dart';
import 'package:pacific_dashboards/models/lookups/lookup.dart';
import 'package:pacific_dashboards/models/serialized/serializers.dart';

part 'lookups.g.dart';

abstract class Lookups implements Built<Lookups, LookupsBuilder> {
  Lookups._();

  factory Lookups([updates(LookupsBuilder b)]) = _$Lookups;

  static Lookups empty() => Lookups((b) {});

  @BuiltValueField(wireName: 'authorityGovt')
  BuiltList<Lookup> get authorityGovt;

  @BuiltValueField(wireName: 'schoolTypes')
  BuiltList<Lookup> get schoolTypes;

  @BuiltValueField(wireName: 'districts')
  BuiltList<Lookup> get districts;

  @BuiltValueField(wireName: 'authorities')
  BuiltList<Lookup> get authorities;

  @BuiltValueField(wireName: 'levels')
  BuiltList<Lookup> get levels;

  @BuiltValueField(wireName: 'accreditationTerms')
  BuiltList<Lookup> get accreditationTerms;

  @BuiltValueField(wireName: 'educationLevels')
  BuiltList<Lookup> get educationLevels;

  String toJson() {
    return json.encode(serializers.serializeWith(Lookups.serializer, this));
  }

  static Lookups fromJson(String jsonString) {
    return serializers.deserializeWith(
        Lookups.serializer, json.decode(jsonString));
  }

  static Serializer<Lookups> get serializer => _$lookupsSerializer;

  bool isEmpty() =>
      authorityGovt.isEmpty &&
      schoolTypes.isEmpty &&
      districts.isEmpty &&
      authorities.isEmpty &&
      levels.isEmpty &&
      accreditationTerms.isEmpty;
}

extension Lookuped on String {
  String from(Iterable<Lookup> lookup) {
    return lookup
            .firstWhere((it) => it.code == this, orElse: () => null)
            ?.name ??
        this;
  }

  String educationLevelFrom(Lookups lookups) {
    final educationLevelCode = lookups.levels
        .firstWhere((it) => it.code == this, orElse: () => null)
        ?.l;

    if (educationLevelCode == null) {
      return this;
    }

    return educationLevelCode.from(lookups.educationLevels);
  }
}
