# pacific-emis-education-survey-tool

Pacific EMIS Education Survey Tool is the android app used for survey data collection integrated with the Pacific EMIS

# Modules description

App is splitted to several so-called feature-modules. They are connected in `app:Injection` class.

Short explanation of currently existing modules:

## Whole app:

### app

- injection configuration
- glide configuration
- screens which not vary by EMIS and survey types

### core 

- shared interfaces and classes
- base classes
- shared views
- shared logic

## Survey feature:

### survey_core

- base classes for survey feature
- shared UI
- base UI classes

### survey

- Survey activity
- SurveyType related DI presenter provider

`SurveyType`-related implementations:

### accreditation_core (School Accreditation)

- SurveyInteractor implementation
- data sources

### accreditation (School Accreditation)

- UI
- Navigator

### wash_core (WASH)

- SurveyInteractor implementation
- data sources

### wash (WASH)

- UI
- Navigator

### data_source_injector

- data source provider DI

## Report feature:

### report_core

- base interactor logic
- report entities
- base and shared UI

### report

- EMIS-dependent DI
- UI

### fsm_report (FedEMIS)

- interactor implementation
- additional entities and logic

### rmi_report (MIEMIS)

- interactor implementation
- additional entities and logic

### remote_settings

- `RemoteSettings` implementation based on Firebase Remote Config

### remote_storage

- everything related to Google Auth and Google Drive

### offline_sync

- everything related to Bluetooth local syncronization