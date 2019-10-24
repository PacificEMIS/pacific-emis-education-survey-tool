import 'package:flutter/material.dart';
import 'package:pacific_dashboards/src/ui/CategoryGridWidget.dart';
import 'package:pacific_dashboards/src/utils/GlobalSettings.dart';
import 'package:pacific_dashboards/src/utils/Localizations.dart';

class HomePage extends StatefulWidget {
  final GlobalSettings globalSettings;

  @override
  _HomePageState createState() => new _HomePageState();

  HomePage({
    Key key,
    this.globalSettings,
  }) : super(key: key);
}

class _HomePageState extends State<HomePage> {
  String _currentCountry;

  final String _kMarshallIslands = AppLocalizations.marshallIslands;
  final String _kFederatedStateOfMicronesia =
      AppLocalizations.federatedStateOfMicronesia;
  final String _kFederatedStateOfMicronesiaWithSplitter =
      AppLocalizations.federatedStateOfMicronesiaSplitted;

  @override
  void initState() {
    super.initState();
    _currentCountry = widget.globalSettings.currentCountry;
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      resizeToAvoidBottomPadding: true,
      body: new Container(
        color: Color.fromRGBO(26, 115, 232, 1),
        child: new ListView(children: <Widget>[
          Container(
            height: 80,
            alignment: Alignment.centerRight,
            child: _buildChooseCountry(context),
          ),
          Container(
              height: 160,
              width: 160,
              child: Image.asset("images/logos/$_currentCountry.png")),
          Container(
            height: 96,
            width: 266,
            alignment: Alignment.center,
            child: Center(
                child: Text(
                    (_currentCountry == _kFederatedStateOfMicronesia
                        ? _kFederatedStateOfMicronesiaWithSplitter
                        : _kMarshallIslands),
                    textAlign: TextAlign.center,
                    style: TextStyle(
                        fontStyle: FontStyle.normal,
                        fontWeight: FontWeight.w700,
                        fontSize: 24,
                        fontFamily: "NotoSans",
                        color: Colors.white))),
          ),
          Container(alignment: Alignment.center, child: CategoryGridWidget())
        ]),
      ),
    );
  }

  _buildChooseCountry(BuildContext context) {
    return FlatButton(
      color: Color.fromRGBO(26, 115, 232, 1),
      textColor: Colors.white,
      disabledColor: Colors.grey,
      disabledTextColor: Colors.black,
      padding: EdgeInsets.all(8.0),
      splashColor: Colors.lightBlue,
      onPressed: () {
        _showDialog(context);
      },
      child: Text(
        AppLocalizations.changeCountry,
        style: TextStyle(
            fontSize: 16.0,
            fontStyle: FontStyle.normal,
            fontFamily: "NotoSans-Regular"),
      ),
    );
  }

  void _showDialog(BuildContext context) async {
    await showDialog(
      context: context,
      builder: (BuildContext context) {
        return Container(
          height: 244,
          child: AlertDialog(
            shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.all(Radius.circular(8.0))),
            contentPadding: EdgeInsets.only(top: 10.0, right: 0),
            title: Text(
              AppLocalizations.changeCountry,
              style: TextStyle(
                  fontStyle: FontStyle.normal,
                  fontWeight: FontWeight.w700,
                  fontSize: 24,
                  fontFamily: "NotoSans"),
            ),
            content: Container(
              height: 200,
              width: 280,
              padding: const EdgeInsets.only(
                  top: 10.0, bottom: 10.0, left: 0.0, right: 0.0),
              child: Column(children: <Widget>[
                Expanded(
                  child: InkWell(
                    splashColor: Colors.blue.withAlpha(30),
                    onTap: () {
                      setState(() {
                        _onCountryChangeTap(_kFederatedStateOfMicronesia);
                      });
                    },
                    child: Row(
                      children: <Widget>[
                        Container(
                            padding: const EdgeInsets.only(left: 30.0),
                            child: Image.asset(
                              "images/logos/$_kFederatedStateOfMicronesia.png",
                              width: 40,
                              height: 40,
                            )),
                        Padding(
                          padding: EdgeInsets.only(left: 20.0),
                          child: Text(
                              "$_kFederatedStateOfMicronesiaWithSplitter",
                              style: TextStyle(fontFamily: "NotoSans")),
                        ),
                      ],
                    ),
                  ),
                ),
                Expanded(
                    child: InkWell(
                  splashColor: Colors.blue.withAlpha(30),
                  onTap: () {
                    setState(() {
                      _onCountryChangeTap(_kMarshallIslands);
                    });
                  },
                  child: Row(
                    children: [
                      Container(
                        padding: const EdgeInsets.only(left: 30.0),
                        child: Image.asset(
                          "images/logos/$_kMarshallIslands.png",
                          width: 40,
                          height: 40,
                        ),
                      ),
                      Padding(
                        padding: EdgeInsets.only(left: 20.0),
                        child: Text(_kMarshallIslands,
                            maxLines: 1,
                            style: TextStyle(fontFamily: "NotoSans")),
                      ),
                    ],
                  ),
                ))
              ]),
            ),
          ),
        );
      },
    );
  }

  _onCountryChangeTap(String country) {
    Navigator.of(context).pop();

    widget.globalSettings.currentCountry = country;

    setState(() {
      _currentCountry = country;
    });
  }
}