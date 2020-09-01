import 'package:flutter/material.dart';
import 'package:pacific_dashboards/res/colors.dart';
import 'package:pacific_dashboards/res/strings.dart';
import 'package:pacific_dashboards/shared_ui/bar_chart_data.dart';
import 'package:pacific_dashboards/shared_ui/chart_legend_item.dart';
import 'package:pacific_dashboards/shared_ui/mini_tab_layout.dart';
import 'package:charts_flutter/flutter.dart' as charts;
import 'package:pacific_dashboards/res/themes.dart';
import 'package:pacific_dashboards/utils/hex_color.dart';

import '../budget_data.dart';

class SpendngByDistrictComponent extends StatefulWidget {
  final List<DataSpendingByDistrict> data;

  const SpendngByDistrictComponent({
    Key key,
    @required this.data,
  })  : assert(data != null),
        super(key: key);

  @override
  _SpendngByDistrictComponentState createState() =>
      _SpendngByDistrictComponentState();
}

class _SpendngByDistrictComponentState
    extends State<SpendngByDistrictComponent> {
  @override
  Widget build(BuildContext context) {
    return Column(
      mainAxisSize: MainAxisSize.min,
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: <Widget>[
        MiniTabLayout(
          tabs: _DashboardTab.values,
          tabNameBuilder: (tab) {
            switch (tab) {
              case _DashboardTab.actualExpPerHead:
                return 'actualExpPerHead'.localized(context);
              case _DashboardTab.actualExpenditure:
                return 'actualExpenditure'.localized(context);
              case _DashboardTab.actualRecurrent:
                return 'actualRecurrentExpenditure'.localized(context);
              case _DashboardTab.budget:
                return 'budgetExpenditure'.localized(context);
              case _DashboardTab.budgetExpPerHead:
                return 'budgetExpPerHead'.localized(context);
              case _DashboardTab.budgetRecurrent:
                return 'budgetRecurrentExpenditure'.localized(context);
              case _DashboardTab.enrolment:
                return 'enrollment'.localized(context);
            }
            throw FallThroughError();
          },
          builder: (ctx, tab) {
            switch (tab) {
              case _DashboardTab.actualExpenditure:
                return _Chart(
                    data: widget.data,
                    groupingType: charts.BarGroupingType.stacked,
                    tab: tab);
              case _DashboardTab.budget:
                return _Chart(
                    data: widget.data,
                    groupingType: charts.BarGroupingType.stacked,
                    tab: tab);
              case _DashboardTab.actualRecurrent:
                return _Chart(
                    data: widget.data,
                    groupingType: charts.BarGroupingType.stacked,
                    tab: tab);
              case _DashboardTab.budgetRecurrent:
                return _Chart(
                    data: widget.data,
                    groupingType: charts.BarGroupingType.stacked,
                    tab: tab);
              case _DashboardTab.actualExpPerHead:
                return _Chart(
                    data: widget.data,
                    groupingType: charts.BarGroupingType.grouped,
                    tab: tab);
              case _DashboardTab.budgetExpPerHead:
                return _Chart(
                    data: widget.data,
                    groupingType: charts.BarGroupingType.grouped,
                    tab: tab);
              case _DashboardTab.enrolment:
                return _Chart(
                    data: widget.data,
                    groupingType: charts.BarGroupingType.stacked,
                    tab: tab);
            }
            throw FallThroughError();
          },
        ),
      ],
    );
  }
}

enum _DashboardTab {
  actualExpenditure,
  budget,
  actualRecurrent,
  budgetRecurrent,
  actualExpPerHead,
  budgetExpPerHead,
  enrolment
}

class _Chart extends StatelessWidget {
  final List<DataSpendingByDistrict> _data;
  final charts.BarGroupingType _groupingType;
  final _DashboardTab _tab;
  const _Chart(
      {Key key,
      @required List<DataSpendingByDistrict> data,
      @required charts.BarGroupingType groupingType,
      @required _DashboardTab tab})
      : assert(data != null),
        _data = data,
        _groupingType = groupingType,
        _tab = tab,
        super(key: key);

  @override
  Widget build(BuildContext context) {
    return Column(
      mainAxisSize: MainAxisSize.min,
      crossAxisAlignment: CrossAxisAlignment.stretch,
      children: <Widget>[
        AspectRatio(
          aspectRatio: 328 / 248,
          child: FutureBuilder(
            future: _series,
            builder: (context, snapshot) {
              if (!snapshot.hasData) {
                return Container();
              }

              return charts.BarChart(
                snapshot.data,
                animate: false,
                barGroupingType: _groupingType,
                primaryMeasureAxis: charts.NumericAxisSpec(
                  tickProviderSpec: charts.BasicNumericTickProviderSpec(
                    desiredMinTickCount: 7,
                    desiredMaxTickCount: 13,
                  ),
                  renderSpec: charts.GridlineRendererSpec(
                    labelStyle: chartAxisTextStyle,
                    lineStyle: chartAxisLineStyle,
                  ),
                ),
                domainAxis: charts.OrdinalAxisSpec(
                  renderSpec: charts.SmallTickRendererSpec(
                    labelStyle: chartAxisTextStyle,
                    lineStyle: chartAxisLineStyle,
                  ),
                ),
              );
            },
          ),
        ),
        Row(
            mainAxisSize: MainAxisSize.max,
            mainAxisAlignment: MainAxisAlignment.center,
            crossAxisAlignment: CrossAxisAlignment.center,
            children: getColumnTitles(_data)),
      ],
    );
  }

  List<Widget> getColumnTitles(List<DataSpendingByDistrict> data) {
    List<Widget> list = new List<Widget>();
    List<String> titles = new List<String>();
    for (var i = 0; i < data.length; i++) {
      titles.add(data[i].district);
    }
    titles = titles.toSet().toList();
    titles.forEach((it) {
      list.add(
        ChartLegendItem(color: HexColor.fromStringHash(it), value: it),
      );
    });
    return list;
  }

  Future<List<charts.Series<BarChartData, String>>> get _series {
    return Future.microtask(() {
      final data = List<BarChartData>();
      data.addAll(_data.map((it) {
        print(it);
        switch (_tab) {
          case _DashboardTab.actualExpPerHead:
            return BarChartData(
              it.year,
              it.edExpAPerHead,
              HexColor.fromStringHash(it.district),
            );
          case _DashboardTab.actualExpenditure:
            return BarChartData(
              it.year,
              it.edExpA,
              HexColor.fromStringHash(it.district),
            );
          case _DashboardTab.actualRecurrent:
            return BarChartData(
              it.year,
              it.edRecurrentExpA,
              HexColor.fromStringHash(it.district),
            );
          case _DashboardTab.budget:
            return BarChartData(
              it.year,
              it.edExpB,
              HexColor.fromStringHash(it.district),
            );
          case _DashboardTab.budgetExpPerHead:
            return BarChartData(
              it.year,
              it.edExpBPerHead,
              HexColor.fromStringHash(it.district),
            );
          case _DashboardTab.budgetRecurrent:
            return BarChartData(
              it.year,
              it.edRecurrentExpB,
              HexColor.fromStringHash(it.district),
            );
          case _DashboardTab.enrolment:
            return BarChartData(
              it.year,
              it.enrolment,
              HexColor.fromStringHash(it.district),
            );
        }
      }).toList());
      return [
        charts.Series(
          domainFn: (BarChartData chartData, _) => chartData.domain,
          measureFn: (BarChartData chartData, _) => chartData.measure,
          colorFn: (BarChartData chartData, _) => chartData.color.chartsColor,
          id: 'spending_Data',
          data: data,
        )
      ];
    });
  }
}