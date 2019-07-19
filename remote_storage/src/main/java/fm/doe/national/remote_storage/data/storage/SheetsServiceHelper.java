package fm.doe.national.remote_storage.data.storage;

import android.content.Context;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesRequest;
import com.google.api.services.sheets.v4.model.DeleteSheetRequest;
import com.google.api.services.sheets.v4.model.DuplicateSheetRequest;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import fm.doe.national.accreditation_core.data.model.Criteria;
import fm.doe.national.accreditation_core.data.model.EvaluationForm;
import fm.doe.national.accreditation_core.data.model.Standard;
import fm.doe.national.accreditation_core.data.model.SubCriteria;
import fm.doe.national.core.utils.DateUtils;
import fm.doe.national.core.utils.TextUtil;
import fm.doe.national.fcm_report.data.model.SchoolAccreditationLevel;
import fm.doe.national.remote_storage.R;
import fm.doe.national.remote_storage.data.model.ReportBundle;
import fm.doe.national.report_core.model.SummaryViewData;
import fm.doe.national.report_core.model.recommendations.CategoryRecommendation;
import fm.doe.national.report_core.model.recommendations.CriteriaRecommendation;
import fm.doe.national.report_core.model.recommendations.FlattenRecommendationsWrapper;
import fm.doe.national.report_core.model.recommendations.Recommendation;
import fm.doe.national.report_core.model.recommendations.StandardRecommendation;
import fm.doe.national.report_core.model.recommendations.SubCriteriaRecommendation;
import fm.doe.national.report_core.ui.level_legend.LevelLegendView;
import io.reactivex.Completable;

public class SheetsServiceHelper extends TasksRxWrapper {

    private static final String VALUE_INPUT_OPTION_USER = "USER_ENTERED";
    private static final String SHEET_NAME_TEMPLATE = "template";
    private static final String SHEET_NAME_SUMMARY = "Standard Scores Summary";
    private static final String RANGE_INFO = "A3:B4";
    private static final String RANGE_LEVELS = "B11:D13";
    private static final String MARK_RECOMMENDATION = "√";
    private static final int OFFSET_RECOMMENDATION = 4;
    private static final String COLUMN_LEVEL_DETERMINATION = "D";
    private static final int ROW_LEVEL_DETERMINATION = 14;
    private static final ScoresSummaryCellsInfo CELLS_INFO_SCORES_SUMMARY = new ScoresSummaryCellsInfo.Builder()
            .setNamingRange("A1:C")
            .setSchoolIdColumnNumber(0)
            .setSchoolNameColumnNumber(1)
            .setDateColumnNumber(2)
            .setFirstNumericColumnNumber(3)
            .build();

    private static final Map<EvaluationForm, SummaryCellsInfo> MAP_EVALUATION_FORM_SUMMARY_CELLS_INFO;

    static {
        Map<EvaluationForm, SummaryCellsInfo> cellsInfoMap = new HashMap<>();
        cellsInfoMap.put(
                EvaluationForm.SCHOOL_EVALUATION,
                new SummaryCellsInfo.Builder()
                        .setColumnsOfStandardCells(Arrays.asList(
                                Arrays.asList("B", "C", "D", "E"),
                                Arrays.asList("F", "G", "H", "I"),
                                Arrays.asList("J", "K", "L", "M"),
                                Arrays.asList("N", "O", "P", "Q"),
                                Arrays.asList("R", "S", "T", "U"),
                                Arrays.asList("V", "W", "X", "Y")
                        ))
                        .setRowsOfSubCriteriaCells(Arrays.asList(21, 22, 23, 24))
                        .setTotalByCriteriaRow(25)
                        .setTotalByStandardRow(26)
                        .setLevelRow(27)
                        .setTotalByEvaluationColumn("Z")
                        .setTotalByEvaluationRow(25)
                        .build()
        );
        cellsInfoMap.put(
                EvaluationForm.CLASSROOM_OBSERVATION,
                new SummaryCellsInfo.Builder()
                        .setColumnsOfStandardCells(Arrays.asList(
                                Arrays.asList("B", "C"),
                                Arrays.asList("D", "E", "F", "G"),
                                Arrays.asList("H", "I", "J", "K"),
                                Collections.singletonList("L"),
                                Collections.singletonList("M")
                        ))
                        .setRowsOfSubCriteriaCells(Arrays.asList(33, 34, 35, 36, 37))
                        .setTotalByCriteriaRow(38)
                        .setTotalByStandardRow(39)
                        .setLevelRow(40)
                        .setTotalByEvaluationColumn("N")
                        .setTotalByEvaluationRow(38)
                        .build()
        );
        MAP_EVALUATION_FORM_SUMMARY_CELLS_INFO = cellsInfoMap;
    }

    private static final Map<EvaluationForm, RecommendationCellsInfo> MAP_RECOMMENDATION_CELLS_INFO;

    static {
        Map<EvaluationForm, RecommendationCellsInfo> cellsInfoMap = new HashMap<>();
        cellsInfoMap.put(EvaluationForm.SCHOOL_EVALUATION, new RecommendationCellsInfo.Builder()
                .setTextStartColumn("A")
                .setTextStartRow(56)
                .setTableColumns(Arrays.asList("B", "C", "D", "E", "F", "G", "H", "I"))
                .setTableRows(Arrays.asList(47, 48, 49, 50, 51, 52))
                .build()
        );
        cellsInfoMap.put(EvaluationForm.CLASSROOM_OBSERVATION, new RecommendationCellsInfo.Builder()
                .setTextStartColumn("K")
                .setTextStartRow(54)
                .setTableColumns(Arrays.asList("L", "M", "N", "O", "P", "Q", "R", "S"))
                .setTableRows(Arrays.asList(47, 48, 49, 50, 51))
                .build()
        );
        MAP_RECOMMENDATION_CELLS_INFO = cellsInfoMap;
    }

    private final Sheets sheetsApi;
    private final Context appContext;

    public SheetsServiceHelper(Sheets sheetsApi, Context appContext) {
        this.sheetsApi = sheetsApi;
        this.appContext = appContext;
    }

    public Completable updateSummarySheet(String spreadsheetId, ReportBundle reportBundle) {
        return wrapWithCompletableInThreadPool(() -> {
            String schoolId = reportBundle.getHeader().getSchoolId();
            String dateAsString = DateUtils.formatNumericMonthYear(reportBundle.getHeader().getDate());
            int row = findSummaryRowToUpdate(spreadsheetId, schoolId, dateAsString);
            List<ValueRange> ranges = new ArrayList<>();

            ranges.add(createSingleCellValueRange(
                    SHEET_NAME_SUMMARY,
                    TextUtil.convertIntToCharsIcons(CELLS_INFO_SCORES_SUMMARY.schoolIdColumnNumber),
                    row,
                    schoolId
            ));

            ranges.add(createSingleCellValueRange(
                    SHEET_NAME_SUMMARY,
                    TextUtil.convertIntToCharsIcons(CELLS_INFO_SCORES_SUMMARY.schoolNameColumnNumber),
                    row,
                    reportBundle.getHeader().getSchoolName()
            ));

            ranges.add(createSingleCellValueRange(
                    SHEET_NAME_SUMMARY,
                    TextUtil.convertIntToCharsIcons(CELLS_INFO_SCORES_SUMMARY.dateColumnNumber),
                    row,
                    dateAsString
            ));

            int column = CELLS_INFO_SCORES_SUMMARY.firstNumericColumnNumber;
            for (SummaryViewData summaryViewData : reportBundle.getSummary()) {
                for (SummaryViewData.CriteriaSummaryViewData criteriaSummaryViewData : summaryViewData.getCriteriaSummaryViewDataList()) {
                    ranges.add(createSingleCellValueRange(
                            SHEET_NAME_SUMMARY,
                            TextUtil.convertIntToCharsIcons(column),
                            row,
                            criteriaSummaryViewData.getTotal()
                    ));
                    column++;
                }
            }

            updateValues(spreadsheetId, ranges);
        });
    }

    private int findSummaryRowToUpdate(String spreadsheetId, String schoolId, String dateAsString) throws IOException {
        ValueRange existingValues = sheetsApi.spreadsheets()
                .values()
                .get(spreadsheetId, makeRange(SHEET_NAME_SUMMARY, CELLS_INFO_SCORES_SUMMARY.namingRange))
                .execute();
        String targetUniqueId = schoolId + dateAsString;
        int rowToUpdate = 1;

        for (int rowNumber = 0; rowNumber < existingValues.getValues().size(); rowNumber++) {
            List<Object> row = existingValues.getValues().get(rowNumber);
            if (row.size() >= CELLS_INFO_SCORES_SUMMARY.dateColumnNumber) {
                String rowUniqueId = (String) row.get(CELLS_INFO_SCORES_SUMMARY.schoolIdColumnNumber) +
                        (String) row.get(CELLS_INFO_SCORES_SUMMARY.dateColumnNumber);

                if (targetUniqueId.equals(rowUniqueId)) {
                    break;
                }
            }
            rowToUpdate++;
        }

        return rowToUpdate;
    }

    public Completable fillReportSheet(String spreadsheetId, String sheetName, ReportBundle reportBundle) {
        return wrapWithCompletableInThreadPool(() -> {
            List<ValueRange> rangesToUpdate = new ArrayList<>();
            rangesToUpdate.add(createInfoValueRange(sheetName, reportBundle.getHeader()));
            rangesToUpdate.add(createLevelsValueRange(sheetName, reportBundle.getSchoolAccreditationLevel()));
            rangesToUpdate.add(createLevelDeterminationValueRange(sheetName, reportBundle.getSchoolAccreditationLevel()));
            rangesToUpdate.addAll(createEvaluationScoreValueRanges(sheetName, reportBundle.getSummary(), EvaluationForm.SCHOOL_EVALUATION));
            rangesToUpdate.addAll(createEvaluationScoreValueRanges(sheetName, reportBundle.getSummary(), EvaluationForm.CLASSROOM_OBSERVATION));
            rangesToUpdate.addAll(createEvaluationRecommendationsValueRange(sheetName, reportBundle.getRecommendations(), EvaluationForm.SCHOOL_EVALUATION));
            rangesToUpdate.addAll(createEvaluationRecommendationsValueRange(sheetName, reportBundle.getRecommendations(), EvaluationForm.CLASSROOM_OBSERVATION));
            updateValues(spreadsheetId, rangesToUpdate);
        });
    }

    private List<ValueRange> createEvaluationRecommendationsValueRange(String sheetName,
                                                                       FlattenRecommendationsWrapper recommendationsWrapper,
                                                                       EvaluationForm evaluationForm) {
        RecommendationCellsInfo cellsInfo = Objects.requireNonNull(MAP_RECOMMENDATION_CELLS_INFO.get(evaluationForm));
        ArrayList<ValueRange> ranges = new ArrayList<>();
        List<? extends Standard> filteredStandards = recommendationsWrapper.getFlattenSurvey().getCategories()
                .stream()
                .filter(c -> c.getEvaluationForm() == evaluationForm)
                .flatMap(c -> c.getStandards().stream())
                .collect(Collectors.toList());
        List<Recommendation> filteredRecommendations = filterRecommendations(recommendationsWrapper, evaluationForm);
        ranges.addAll(createRecommendationTableValueRanges(sheetName, cellsInfo, filteredStandards, filteredRecommendations));
        ranges.addAll(createRecommendationTextValueRanges(sheetName, cellsInfo, filteredRecommendations));
        return ranges;
    }

    private List<ValueRange> createRecommendationTableValueRanges(String sheetName,
                                                                  RecommendationCellsInfo cellsInfo,
                                                                  List<? extends Standard> standards,
                                                                  List<Recommendation> recommendations) {
        ArrayList<ValueRange> ranges = new ArrayList<>();
        for (int standardIndex = 0; standardIndex < standards.size(); standardIndex++) {
            final Standard standard = standards.get(standardIndex);
            for (int criteriaIndex = 0; criteriaIndex < standard.getCriterias().size(); criteriaIndex++) {
                final Criteria criteria = standard.getCriterias().get(criteriaIndex);
                Optional<Recommendation> existingRecommendation = recommendations.stream()
                        .filter(r -> (r instanceof CriteriaRecommendation) &&
                                ((CriteriaRecommendation) r).getObject().getSuffix().equals(criteria.getSuffix()))
                        .findFirst();

                if (existingRecommendation.isPresent()) {
                    ranges.add(createSingleCellValueRange(
                            sheetName,
                            cellsInfo.tableColumns.get(criteriaIndex + OFFSET_RECOMMENDATION),
                            cellsInfo.tableRows.get(standardIndex),
                            MARK_RECOMMENDATION
                    ));
                } else {
                    ranges.add(createSingleCellValueRange(
                            sheetName,
                            cellsInfo.tableColumns.get(criteriaIndex),
                            cellsInfo.tableRows.get(standardIndex),
                            MARK_RECOMMENDATION
                    ));
                }
            }
        }
        return ranges;
    }

    private List<ValueRange> createRecommendationTextValueRanges(String sheetName,
                                                                 RecommendationCellsInfo cellsInfo,
                                                                 List<Recommendation> filteredRecommendations) {
        ArrayList<ValueRange> ranges = new ArrayList<>();
        int currentTextRow = cellsInfo.textStartRow;
        for (Recommendation recommendation : filteredRecommendations) {

            if (recommendation instanceof StandardRecommendation) {
                Standard standard = ((StandardRecommendation) recommendation).getObject();
                ranges.add(createSingleCellValueRange(
                        sheetName,
                        cellsInfo.textStartColumn,
                        currentTextRow,
                        appContext.getString(R.string.format_export_standard, standard.getSuffix(), standard.getTitle())
                ));
                currentTextRow++;
                continue;
            }

            if (recommendation instanceof CriteriaRecommendation) {
                Criteria criteria = ((CriteriaRecommendation) recommendation).getObject();
                ranges.add(createSingleCellValueRange(
                        sheetName,
                        cellsInfo.textStartColumn,
                        currentTextRow,
                        appContext.getString(R.string.format_export_criteria, criteria.getSuffix(), criteria.getTitle())
                ));
                currentTextRow++;
                continue;
            }

            if (recommendation instanceof SubCriteriaRecommendation) {
                SubCriteria subCriteria = ((SubCriteriaRecommendation) recommendation).getObject();
                ranges.add(createSingleCellValueRange(
                        sheetName,
                        cellsInfo.textStartColumn,
                        currentTextRow,
                        appContext.getString(R.string.format_export_sub_criteria, subCriteria.getSuffix(), subCriteria.getTitle())
                ));
                currentTextRow++;
            }
        }
        return ranges;
    }

    private List<Recommendation> filterRecommendations(FlattenRecommendationsWrapper recommendationsWrapper, EvaluationForm evaluationForm) {
        ArrayList<Recommendation> filteredRecommendations = new ArrayList<>();
        boolean isInCurrentRecommendationsBlock = false;
        for (Recommendation recommendation : recommendationsWrapper.getRecommendations()) {
            if (recommendation instanceof CategoryRecommendation) {
                isInCurrentRecommendationsBlock = ((CategoryRecommendation) recommendation).getObject().getEvaluationForm() == evaluationForm;
                continue;
            }

            if (isInCurrentRecommendationsBlock) {
                filteredRecommendations.add(recommendation);
            }
        }
        return filteredRecommendations;
    }

    private List<ValueRange> createEvaluationScoreValueRanges(String sheetName,
                                                              List<SummaryViewData> summary,
                                                              EvaluationForm evaluationForm) {
        return createSummaryValueRange(
                sheetName,
                summary.stream()
                        .filter(it -> it.getCategory().getEvaluationForm() == evaluationForm)
                        .collect(Collectors.toList()),
                evaluationForm
        );
    }

    private List<ValueRange> createSummaryValueRange(String sheetName,
                                                     List<SummaryViewData> summary,
                                                     EvaluationForm evaluationForm) {
        SummaryCellsInfo cellsInfo = Objects.requireNonNull(MAP_EVALUATION_FORM_SUMMARY_CELLS_INFO.get(evaluationForm));
        ArrayList<ValueRange> ranges = new ArrayList<>();
        int totalByEvaluation = 0;

        for (int standardIndex = 0; standardIndex < summary.size(); standardIndex++) {
            SummaryViewData data = summary.get(standardIndex);
            totalByEvaluation += data.getTotalByStandard();

            ranges.add(
                    createSingleCellValueRange(
                            sheetName,
                            cellsInfo.columnsOfStandardCells.get(standardIndex).get(0),
                            cellsInfo.totalByStandardRow,
                            data.getTotalByStandard()
                    )
            );

            ranges.add(
                    createSingleCellValueRange(
                            sheetName,
                            cellsInfo.columnsOfStandardCells.get(standardIndex).get(0),
                            cellsInfo.levelRow,
                            data.getLevel().getName().getString(appContext)
                    )
            );

            ranges.addAll(createCriteriasValueRanges(sheetName, cellsInfo, cellsInfo.columnsOfStandardCells.get(standardIndex), data));
        }

        ranges.add(
                createSingleCellValueRange(
                        sheetName,
                        cellsInfo.totalByEvaluationColumn,
                        cellsInfo.totalByEvaluationRow,
                        totalByEvaluation
                )
        );

        return ranges;
    }

    private List<ValueRange> createCriteriasValueRanges(String sheetName,
                                                        SummaryCellsInfo cellsInfo,
                                                        List<String> criteriaColumns,
                                                        SummaryViewData data) {
        ArrayList<ValueRange> ranges = new ArrayList<>();

        for (int criteriaIndex = 0; criteriaIndex < data.getCriteriaSummaryViewDataList().size(); criteriaIndex++) {
            SummaryViewData.CriteriaSummaryViewData criteriaData = data.getCriteriaSummaryViewDataList().get(criteriaIndex);

            ranges.add(
                    createSingleCellValueRange(
                            sheetName,
                            criteriaColumns.get(criteriaIndex),
                            cellsInfo.totalByCriteriaRow,
                            criteriaData.getTotal()
                    )
            );

            ranges.addAll(createSubCriteriasValueRanges(sheetName, cellsInfo, criteriaColumns.get(criteriaIndex), criteriaData));
        }

        return ranges;
    }

    private List<ValueRange> createSubCriteriasValueRanges(String sheetName,
                                                           SummaryCellsInfo cellsInfo,
                                                           String column,
                                                           SummaryViewData.CriteriaSummaryViewData criteriaData) {
        ArrayList<ValueRange> ranges = new ArrayList<>();

        for (int subCriteriaIndex = 0; subCriteriaIndex < criteriaData.getAnswerStates().length; subCriteriaIndex++) {
            ranges.add(
                    createSingleCellValueRange(
                            sheetName,
                            column,
                            cellsInfo.rowsOfSubCriteriaCells.get(subCriteriaIndex),
                            criteriaData.getAnswerStates()[subCriteriaIndex] ? 1 : 0
                    )
            );
        }

        return ranges;
    }

    private ValueRange createSingleCellValueRange(String sheetName, String column, int row, Object value) {
        return new ValueRange()
                .setRange(makeRange(sheetName, column + row))
                .setValues(Collections.singletonList(Collections.singletonList(value)));
    }

    private ValueRange createInfoValueRange(String sheetName, LevelLegendView.Item header) {
        return new ValueRange()
                .setRange(makeRange(sheetName, RANGE_INFO))
                .setValues(Arrays.asList(
                        Arrays.asList(
                                appContext.getString(R.string.label_school_code) + " " + header.getSchoolId(),
                                appContext.getString(R.string.label_school_name) + " " + header.getSchoolName()
                        ),
                        Arrays.asList(
                                appContext.getString(R.string.label_date_of_accreditation) + " " +
                                        DateUtils.formatUi(header.getDate()),
                                appContext.getString(R.string.label_principal_name)
                        )
                ));
    }

    private ValueRange createLevelsValueRange(String sheetName, SchoolAccreditationLevel level) {
        return new ValueRange()
                .setRange(makeRange(sheetName, RANGE_LEVELS))
                .setValues(Arrays.asList(
                        Arrays.asList(
                                level.getForms().get(0).getObtainedScore(),
                                level.getForms().get(0).getMultiplier(),
                                level.getForms().get(0).getFinalScore()
                        ),
                        Arrays.asList(
                                level.getForms().get(1).getObtainedScore(),
                                level.getForms().get(1).getMultiplier(),
                                level.getForms().get(1).getFinalScore()
                        ),
                        Arrays.asList(
                                level.getTotalObtainedScore(),
                                "",
                                level.getTotalScore()
                        )
                ));
    }

    private ValueRange createLevelDeterminationValueRange(String sheetName, SchoolAccreditationLevel level) {
        return createSingleCellValueRange(
                sheetName,
                COLUMN_LEVEL_DETERMINATION,
                ROW_LEVEL_DETERMINATION,
                level.getReportLevel().getName().getString(appContext)
        );
    }

    public Completable recreateSheet(String spreadsheetId, String sheetName) {
        return wrapWithCompletableInThreadPool(() -> {
            Sheet templateSheet = findTemplateSheet(spreadsheetId);
            Optional<Sheet> existingSheet = findSheet(spreadsheetId, sheetName);

            if (existingSheet.isPresent()) {
                deleteSheet(spreadsheetId, existingSheet.get().getProperties().getSheetId());
            }

            duplicateSheet(spreadsheetId, templateSheet.getProperties().getSheetId(), sheetName);
        });
    }

    private void deleteSheet(String spreadsheetId, Integer sheetId) throws IOException {
        DeleteSheetRequest requestBody = new DeleteSheetRequest()
                .setSheetId(sheetId);
        BatchUpdateSpreadsheetRequest batchUpdateSpreadsheetRequest = new BatchUpdateSpreadsheetRequest()
                .setRequests(Collections.singletonList(new Request().setDeleteSheet(requestBody)));
        sheetsApi.spreadsheets()
                .batchUpdate(spreadsheetId, batchUpdateSpreadsheetRequest)
                .execute();
    }

    private void duplicateSheet(String spreadsheetId, Integer sourceSheetId, String newSheetName) throws IOException {
        DuplicateSheetRequest requestBody = new DuplicateSheetRequest()
                .setNewSheetName(newSheetName)
                .setSourceSheetId(sourceSheetId);
        BatchUpdateSpreadsheetRequest batchUpdateSpreadsheetRequest = new BatchUpdateSpreadsheetRequest()
                .setRequests(Collections.singletonList(new Request().setDuplicateSheet(requestBody)));
        sheetsApi.spreadsheets()
                .batchUpdate(spreadsheetId, batchUpdateSpreadsheetRequest)
                .execute();
    }

    private Sheet findTemplateSheet(String spreadsheetId) throws IOException {
        return findSheet(spreadsheetId, SHEET_NAME_TEMPLATE).get();
    }

    private Optional<Sheet> findSheet(String spreadsheetId, String sheetName) throws IOException {
        Spreadsheet spreadsheet = sheetsApi.spreadsheets().get(spreadsheetId).execute();
        return spreadsheet.getSheets()
                .stream()
                .filter(sheet -> sheet.getProperties().getTitle().equals(sheetName))
                .findFirst();
    }

    private void updateValues(String spreadsheetId, List<ValueRange> values) throws IOException {
        sheetsApi.spreadsheets()
                .values()
                .batchUpdate(
                        spreadsheetId,
                        new BatchUpdateValuesRequest()
                                .setData(values)
                                .setValueInputOption(VALUE_INPUT_OPTION_USER)
                )
                .execute();
    }

    private String makeRange(String sheetName, String a1Range) {
        return "'" + sheetName + "'!" + a1Range;
    }


    private static class SummaryCellsInfo {
        private List<List<String>> columnsOfStandardCells;
        private List<Integer> rowsOfSubCriteriaCells;
        private int totalByCriteriaRow;
        private int totalByStandardRow;
        private int levelRow;
        private String totalByEvaluationColumn;
        private int totalByEvaluationRow;

        private SummaryCellsInfo() {
            // private constructor
        }

        public static class Builder {
            private SummaryCellsInfo cellsInfo = new SummaryCellsInfo();

            public Builder setColumnsOfStandardCells(List<List<String>> columnsOfStandardCells) {
                cellsInfo.columnsOfStandardCells = columnsOfStandardCells;
                return this;
            }

            public Builder setRowsOfSubCriteriaCells(List<Integer> rowsOfSubCriteriaCells) {
                cellsInfo.rowsOfSubCriteriaCells = rowsOfSubCriteriaCells;
                return this;
            }

            public Builder setTotalByCriteriaRow(int totalByCriteriaRow) {
                cellsInfo.totalByCriteriaRow = totalByCriteriaRow;
                return this;
            }

            public Builder setTotalByStandardRow(int totalByStandardRow) {
                cellsInfo.totalByStandardRow = totalByStandardRow;
                return this;
            }

            public Builder setLevelRow(int levelRow) {
                cellsInfo.levelRow = levelRow;
                return this;
            }

            public Builder setTotalByEvaluationColumn(String totalByEvaluationColumn) {
                cellsInfo.totalByEvaluationColumn = totalByEvaluationColumn;
                return this;
            }

            public Builder setTotalByEvaluationRow(int totalByEvaluationRow) {
                cellsInfo.totalByEvaluationRow = totalByEvaluationRow;
                return this;
            }

            public SummaryCellsInfo build() {
                if (cellsInfo.columnsOfStandardCells == null ||
                        cellsInfo.rowsOfSubCriteriaCells == null ||
                        cellsInfo.totalByEvaluationColumn == null) {
                    throw new IllegalStateException();
                }
                return cellsInfo;
            }
        }
    }

    private static class RecommendationCellsInfo {
        private String textStartColumn;
        private int textStartRow;
        private List<String> tableColumns;
        private List<Integer> tableRows;

        private RecommendationCellsInfo() {
            // private constructor
        }

        public static class Builder {
            private RecommendationCellsInfo cellsInfo = new RecommendationCellsInfo();

            public Builder setTextStartColumn(String textStartColumn) {
                cellsInfo.textStartColumn = textStartColumn;
                return this;
            }

            public Builder setTextStartRow(int textStartRow) {
                cellsInfo.textStartRow = textStartRow;
                return this;
            }

            public Builder setTableColumns(List<String> tableColumns) {
                cellsInfo.tableColumns = tableColumns;
                return this;
            }

            public Builder setTableRows(List<Integer> tableRows) {
                cellsInfo.tableRows = tableRows;
                return this;
            }

            public RecommendationCellsInfo build() {
                if (cellsInfo.textStartColumn == null || cellsInfo.tableColumns == null || cellsInfo.tableRows == null) {
                    throw new IllegalStateException();
                }
                return cellsInfo;
            }
        }

    }

    private static class ScoresSummaryCellsInfo {
        private String namingRange;
        private int schoolIdColumnNumber;
        private int schoolNameColumnNumber;
        private int dateColumnNumber;
        private int firstNumericColumnNumber;

        private ScoresSummaryCellsInfo() {
            // private constructor
        }

        public static class Builder {
            private ScoresSummaryCellsInfo cellsInfo = new ScoresSummaryCellsInfo();

            public Builder setNamingRange(String namingRange) {
                cellsInfo.namingRange = namingRange;
                return this;
            }

            public Builder setSchoolIdColumnNumber(int schoolIdColumnNumber) {
                cellsInfo.schoolIdColumnNumber = schoolIdColumnNumber;
                return this;
            }

            public Builder setSchoolNameColumnNumber(int schoolNameColumnNumber) {
                cellsInfo.schoolNameColumnNumber = schoolNameColumnNumber;
                return this;
            }

            public Builder setDateColumnNumber(int dateColumnNumber) {
                cellsInfo.dateColumnNumber = dateColumnNumber;
                return this;
            }

            public Builder setFirstNumericColumnNumber(int firstNumericColumnNumber) {
                cellsInfo.firstNumericColumnNumber = firstNumericColumnNumber;
                return this;
            }

            public ScoresSummaryCellsInfo build() {
                if (cellsInfo.namingRange == null) {
                    throw new IllegalStateException();
                }
                return cellsInfo;
            }
        }
    }
}
