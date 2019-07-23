package fm.doe.national.remote_storage.data.export;

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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import fm.doe.national.accreditation_core.data.model.EvaluationForm;
import fm.doe.national.core.utils.DateUtils;
import fm.doe.national.core.utils.TextUtil;
import fm.doe.national.remote_storage.R;
import fm.doe.national.remote_storage.data.model.ReportBundle;
import fm.doe.national.remote_storage.data.storage.TasksRxWrapper;
import fm.doe.national.report_core.model.SummaryViewData;
import fm.doe.national.report_core.ui.level_legend.LevelLegendView;
import io.reactivex.Completable;

public abstract class SheetsExcelExporter extends TasksRxWrapper implements ExcelExporter {

    private static final String VALUE_INPUT_OPTION_USER = "USER_ENTERED";
    private static final String RANGE_INFO = "A3:B4";
    private static final ScoresSummaryCellsInfo CELLS_INFO_SCORES_SUMMARY = new ScoresSummaryCellsInfo.Builder()
            .setNamingRange("A1:C")
            .setSchoolIdColumnNumber(0)
            .setSchoolNameColumnNumber(1)
            .setDateColumnNumber(2)
            .setFirstNumericColumnNumber(3)
            .build();

    private final Sheets sheetsApi;
    protected final Context appContext;

    public SheetsExcelExporter(Sheets sheetsApi, Context appContext) {
        this.sheetsApi = sheetsApi;
        this.appContext = appContext;
    }

    @Override
    public Completable updateSummarySheet(String spreadsheetId, String sheetName, ReportBundle reportBundle) {
        return wrapWithCompletableInThreadPool(() -> {
            String schoolId = reportBundle.getHeader().getSchoolId();
            String dateAsString = DateUtils.formatNumericMonthYear(reportBundle.getHeader().getDate());
            int row = findSummaryRowToUpdate(spreadsheetId, schoolId, dateAsString, sheetName);
            List<ValueRange> ranges = new ArrayList<>();

            ranges.add(createSingleCellValueRange(
                    sheetName,
                    TextUtil.convertIntToCharsIcons(CELLS_INFO_SCORES_SUMMARY.schoolIdColumnNumber),
                    row,
                    schoolId
            ));

            ranges.add(createSingleCellValueRange(
                    sheetName,
                    TextUtil.convertIntToCharsIcons(CELLS_INFO_SCORES_SUMMARY.schoolNameColumnNumber),
                    row,
                    reportBundle.getHeader().getSchoolName()
            ));

            ranges.add(createSingleCellValueRange(
                    sheetName,
                    TextUtil.convertIntToCharsIcons(CELLS_INFO_SCORES_SUMMARY.dateColumnNumber),
                    row,
                    dateAsString
            ));

            int column = CELLS_INFO_SCORES_SUMMARY.firstNumericColumnNumber;
            for (SummaryViewData summaryViewData : reportBundle.getSummary()) {
                for (SummaryViewData.CriteriaSummaryViewData criteriaSummaryViewData : summaryViewData.getCriteriaSummaryViewDataList()) {
                    ranges.add(createSingleCellValueRange(
                            sheetName,
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

    private int findSummaryRowToUpdate(String spreadsheetId, String schoolId, String dateAsString, String sheetName) throws IOException {
        ValueRange existingValues = sheetsApi.spreadsheets()
                .values()
                .get(spreadsheetId, makeRange(sheetName, CELLS_INFO_SCORES_SUMMARY.namingRange))
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

    protected List<ValueRange> createEvaluationScoreValueRanges(String sheetName,
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
        SummaryCellsInfo cellsInfo = getSummaryCellsInfo(evaluationForm);
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

    protected abstract SummaryCellsInfo getSummaryCellsInfo(EvaluationForm evaluationForm);

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

    protected ValueRange createSingleCellValueRange(String sheetName, String column, int row, Object value) {
        return new ValueRange()
                .setRange(makeRange(sheetName, column + row))
                .setValues(Collections.singletonList(Collections.singletonList(value)));
    }

    protected ValueRange createInfoValueRange(String sheetName, LevelLegendView.Item header) {
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

    @Override
    public Completable recreateSheet(String spreadsheetId, String sheetName, String templateSheetName) {
        return wrapWithCompletableInThreadPool(() -> {
            Sheet templateSheet = findTemplateSheet(spreadsheetId, templateSheetName);
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

    private Sheet findTemplateSheet(String spreadsheetId, String templateSheetName) throws IOException {
        return findSheet(spreadsheetId, templateSheetName).get();
    }

    private Optional<Sheet> findSheet(String spreadsheetId, String sheetName) throws IOException {
        Spreadsheet spreadsheet = sheetsApi.spreadsheets().get(spreadsheetId).execute();
        return spreadsheet.getSheets()
                .stream()
                .filter(sheet -> sheet.getProperties().getTitle().equals(sheetName))
                .findFirst();
    }

    protected void updateValues(String spreadsheetId, List<ValueRange> values) throws IOException {
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

    protected String makeRange(String sheetName, String a1Range) {
        return "'" + sheetName + "'!" + a1Range;
    }

    protected static class SummaryCellsInfo {
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

            public Builder setRowsOfSubCriteriaCells(Integer... rowsOfSubCriteriaCells) {
                cellsInfo.rowsOfSubCriteriaCells = Arrays.asList(rowsOfSubCriteriaCells);
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

    protected static class ScoresSummaryCellsInfo {
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
