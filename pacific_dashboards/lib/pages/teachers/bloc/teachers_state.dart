import 'package:equatable/equatable.dart';
import 'package:pacific_dashboards/pages/base/base_bloc.dart';
import 'package:pacific_dashboards/pages/teachers/teachers_page_data.dart';

abstract class TeachersState extends Equatable {
  const TeachersState();

  @override
  List<Object> get props => [];
}

class LoadingTeachersState extends TeachersState {}

class UpdatedTeachersState extends TeachersState {
  const UpdatedTeachersState(this.data);

  final TeachersPageData data;

  @override
  List<Object> get props => [data];
}

class ServerUnavailableState extends TeachersState implements ErrorState {}

class UnknownErrorState extends TeachersState implements ErrorState {}

class InitialTeachersState extends TeachersState {}