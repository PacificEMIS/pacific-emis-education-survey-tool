import 'package:pacific_dashboards/res/strings/strings.dart';

enum Emis { miemis, fedemis, kemis }

Emis emisFromString(String string) =>
    Emis.values.firstWhere((it) => it.toString() == string, orElse: () => null);

extension UI on Emis {

  String get name {
    switch (this) {
      case Emis.miemis:
        return AppLocalizations.marshallIslands;
      case Emis.fedemis:
        return AppLocalizations.federatedStateOfMicronesia;
      case Emis.kemis:
        return AppLocalizations.kiribati;
    }
    throw FallThroughError();
  }

  String get logo {
    switch (this) {
      case Emis.miemis:
        return "images/miemis.png";
      case Emis.fedemis:
        return "images/fedemis.png";
      case Emis.kemis:
        return "images/kiribati.png";
    }
    throw FallThroughError();
  }

}

extension Id on Emis {
  int get id {
    switch (this) {
      case Emis.miemis:
        return 0;
      case Emis.fedemis:
        return 1;
      case Emis.kemis:
        return 2;
    }
    throw FallThroughError();
  }
}