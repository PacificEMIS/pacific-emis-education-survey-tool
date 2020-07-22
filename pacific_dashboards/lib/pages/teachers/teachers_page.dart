import 'package:arch/arch.dart';
import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:pacific_dashboards/models/filter/filter.dart';
import 'package:pacific_dashboards/pages/filter/filter_page.dart';
import 'package:pacific_dashboards/pages/teachers/teachers_page_data.dart';
import 'package:pacific_dashboards/pages/teachers/teachers_view_model.dart';
import 'package:pacific_dashboards/res/strings/strings.dart';
import 'package:pacific_dashboards/shared_ui/chart_factory.dart';
import 'package:pacific_dashboards/shared_ui/chart_with_table.dart';
import 'package:pacific_dashboards/shared_ui/multi_table.dart';
import 'package:pacific_dashboards/shared_ui/page_note_widget.dart';
import 'package:pacific_dashboards/shared_ui/platform_app_bar.dart';
import 'package:pacific_dashboards/shared_ui/platform_progress_indicator.dart';
import 'package:pacific_dashboards/view_model_factory.dart';

class TeachersPage extends MvvmStatefulWidget {
  static const String kRoute = '/Teachers';

  TeachersPage({
    Key key,
  }) : super(
          key: key,
          viewModelBuilder: (ctx) =>
              ViewModelFactory.instance.createTeachersViewModel(ctx),
        );

  @override
  State<StatefulWidget> createState() {
    return TeachersPageState();
  }
}

class TeachersPageState extends MvvmState<TeachersViewModel, TeachersPage> {
  @override
  Widget buildWidget(BuildContext context) {
    return Scaffold(
      appBar: PlatformAppBar(
        title: Text(AppLocalizations.schools),
        actions: <Widget>[
          StreamBuilder<List<Filter>>(
            stream: viewModel.filtersStream,
            builder: (ctx, snapshot) {
              return Visibility(
                visible: snapshot.hasData,
                child: IconButton(
                  icon: SvgPicture.asset('images/filter.svg'),
                  onPressed: () {
                    _openFilters(snapshot.data);
                  },
                ),
              );
            },
          )
        ],
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: <Widget>[
            PageNoteWidget(noteStream: viewModel.noteStream),
            StreamBuilder<TeachersPageData>(
              stream: viewModel.dataStream,
              builder: (ctx, snapshot) {
                if (!snapshot.hasData) {
                  return Center(
                    child: PlatformProgressIndicator(),
                  );
                } else {
                  return Column(
                    children: <Widget>[
                      ChartWithTable(
                        key: ObjectKey(snapshot.data.teachersByAuthority),
                        title: AppLocalizations.teachersByAuthority,
                        data: snapshot.data.teachersByAuthority,
                        chartType: ChartType.pie,
                        tableKeyName: AppLocalizations.authority,
                        tableValueName: AppLocalizations.teachers,
                      ),
                      ChartWithTable(
                        key: ObjectKey(snapshot.data.teachersByPrivacy),
                        title: AppLocalizations.teachersEnrollmentGovtNonGovt,
                        data: snapshot.data.teachersByPrivacy,
                        chartType: ChartType.pie,
                        tableKeyName: AppLocalizations.publicPrivate,
                        tableValueName: AppLocalizations.teachers,
                      ),
                      ChartWithTable(
                        key: ObjectKey(snapshot.data.teachersByDistrict),
                        title: AppLocalizations.teachersByState,
                        data: snapshot.data.teachersByDistrict,
                        chartType: ChartType.bar,
                        tableKeyName: AppLocalizations.state,
                        tableValueName: AppLocalizations.teachers,
                      ),
                      MultiTable(
                        key: ObjectKey(
                            snapshot.data.teachersBySchoolLevelStateAndGender),
                        title:
                            AppLocalizations.teacherBySchoolTypeStateAndGender,
                        firstColumnName: AppLocalizations.schoolLevels,
                        data: snapshot.data.teachersBySchoolLevelStateAndGender,
                        keySortFunc: (lv, rv) => lv.compareTo(rv),
                      ),
                    ],
                  );
                }
              },
            ),
          ],
        ),
      ),
    );
  }

  void _openFilters(List<Filter> filters) {
    Navigator.push<List<Filter>>(
      context,
      MaterialPageRoute(builder: (context) {
        return FilterPage(
          filters: filters,
        );
      }),
    ).then((filters) => _applyFilters(context, filters));
  }

  void _applyFilters(BuildContext context, List<Filter> filters) {
    if (filters == null) {
      return;
    }
    viewModel.onFiltersChanged(filters);
  }
}