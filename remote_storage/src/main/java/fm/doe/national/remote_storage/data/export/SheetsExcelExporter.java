package fm.doe.national.remote_storage.data.export;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

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
import java.util.Optional;
import java.util.stream.Collectors;

import fm.doe.national.accreditation_core.data.model.EvaluationForm;
import fm.doe.national.core.utils.TextUtil;
import fm.doe.national.remote_storage.R;
import fm.doe.national.remote_storage.data.model.ReportBundle;
import fm.doe.national.remote_storage.data.storage.TasksRxWrapper;
import fm.doe.national.report_core.model.SummaryViewData;
import fm.doe.national.report_core.ui.level_legend.LevelLegendView;
import io.reactivex.Completable;
import io.reactivex.Single;

@SuppressLint("DefaultLocale")
public class SheetsExcelExporter extends TasksRxWrapper {

    public static final String MIME_TYPE_MS_EXCEL = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    public static final String MIME_TYPE_GOOGLE_SHEETS = "application/vnd.google-apps.spreadsheet";

    private static final String TAG = SheetsExcelExporter.class.getName();
    private static final String PREFIX_UPDATABLE_CELL = "$";
    private static final String FORMAT_CELL_SUMMARY_SUB_CRITERIA_TOTAL = "$%sSummary-%d.%d.%d";
    private static final String CELL_SCHOOL_ID = "$schNo";
    private static final String CELL_SCHOOL_NAME = "$schName";
    private static final String CELL_DATE = "$date";
    private static final String CELL_PRINCIPAL = "$principal";

    private static final String VALUE_INPUT_OPTION_USER = "USER_ENTERED";
    private final Sheets sheetsApi;
    protected final Context appContext;
    protected final Map<String, CellInfo> updatableCells = new HashMap<>();
    private static final ScoresSummaryCellsInfo CELLS_INFO_SCORES_SUMMARY = new ScoresSummaryCellsInfo.Builder()
            .setNamingRange("A1:C")
            .setSchoolIdColumnNumber(0)
            .setSchoolNameColumnNumber(1)
            .setDateColumnNumber(2)
            .setFirstNumericColumnNumber(3)
            .build();


    public SheetsExcelExporter(Context appContext, Sheets sheetsApi) {
        this.sheetsApi = sheetsApi;
        this.appContext = appContext;
    }

    public Completable fillReportSheet(String spreadsheetId, String sheetName, ReportBundle reportBundle) {
        return wrapWithCompletableInThreadPool(() -> {
            List<ValueRange> rangesToUpdate = new ArrayList<>();
            rangesToUpdate.addAll(createInfoValueRanges(sheetName, reportBundle.getHeader()));
            rangesToUpdate.addAll(createEvaluationScoreValueRanges(sheetName, reportBundle.getSummary(), EvaluationForm.SCHOOL_EVALUATION));
            rangesToUpdate.addAll(createEvaluationScoreValueRanges(sheetName, reportBundle.getSummary(), EvaluationForm.CLASSROOM_OBSERVATION));
            updateValues(spreadsheetId, rangesToUpdate);
        });
    }

    public Single<String> recreateSheet(String spreadsheetId, String sheetName, String templateSheetName) {
        return wrapWithSingleInThreadPool(() -> {
            Sheet templateSheet = findTemplateSheet(spreadsheetId, templateSheetName);
            Optional<Sheet> existingSheet = findSheet(spreadsheetId, sheetName);

            if (existingSheet.isPresent()) {
                deleteSheet(spreadsheetId, existingSheet.get().getProperties().getSheetId());
            }

            duplicateSheet(spreadsheetId, templateSheet.getProperties().getSheetId(), sheetName);
            updateCellsInfo(spreadsheetId, sheetName);
            String spreadsheetUrl = sheetsApi.spreadsheets().get(spreadsheetId).execute().getSpreadsheetUrl();
            return spreadsheetUrl == null ? "" : spreadsheetUrl;
        }, "");
    }

    private void updateCellsInfo(String spreadsheetId, String sheetName) throws IOException {
        updatableCells.clear();
        List<List<Object>> values = sheetsApi.spreadsheets().values().get(spreadsheetId, sheetName).execute().getValues();
        for (int rowIndex = 0; rowIndex < values.size(); rowIndex++) {
            List<Object> row = values.get(rowIndex);
            for (int columnIndex = 0; columnIndex < row.size(); columnIndex++) {
                String value = (String) row.get(columnIndex);

                if (value.startsWith(PREFIX_UPDATABLE_CELL)) {
                    updatableCells.put(value, new CellInfo(rowIndex, columnIndex));
                }
            }
        }
    }

    public Completable updateSummarySheet(String spreadsheetId, String sheetName, ReportBundle reportBundle) {
        return wrapWithCompletableInThreadPool(() -> {
            String schoolId = reportBundle.getHeader().getSchoolId();
            String surveyTag = reportBundle.getHeader().getSurveyTag();
            int row = findSummaryRowToUpdate(spreadsheetId, schoolId, surveyTag, sheetName);
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
                    surveyTag
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
        List<ValueRange> values = new ArrayList<>();

        Map<String, List<SummaryViewData>> dataByCategories = summary.stream()
                .filter(it -> it.getCategory().getEvaluationForm() == evaluationForm)
                .collect(Collectors.groupingBy(it -> it.getCategory().getTitle()));

        List<List<SummaryViewData>> groupedSummary = new ArrayList<>(dataByCategories.values());

        for (int i = 0; i < groupedSummary.size(); i++) {
            values.addAll(
                    createSummaryValueRange(
                            sheetName,
                            groupedSummary.get(i),
                            evaluationForm,
                            i
                    )
            );
        }

        return values;
    }

    protected String getEvaluationFormPrefix(EvaluationForm evaluationForm) {
        switch (evaluationForm) {
            case SCHOOL_EVALUATION:
                return "se";
            case CLASSROOM_OBSERVATION:
                return "co";
            default:
                return "";
        }
    }

    private List<ValueRange> createSummaryValueRange(String sheetName,
                                                     List<SummaryViewData> summary,
                                                     EvaluationForm evaluationForm,
                                                     int categoryIndex) {
        List<ValueRange> ranges = new ArrayList<>();
        final String cellPrefix = getEvaluationFormPrefix(evaluationForm) + (categoryIndex + 1);

        for (int standardIndex = 0; standardIndex < summary.size(); standardIndex++) {
            SummaryViewData data = summary.get(standardIndex);
            ranges.addAll(createCriteriasValueRanges(sheetName, data, cellPrefix, standardIndex + 1));
        }

        return ranges;
    }

    protected String getCellRange(String cellKey) {
        CellInfo cellInfo = updatableCells.get(cellKey);
        if (cellInfo == null) {
            Log.w(TAG, "getCellRange: cell not found(" + cellKey + ")");
            return "";
        }

        return cellInfo.getRange();
    }

    protected String getCellRange(String format, Object... args) {
        return getCellRange(String.format(format, args));
    }

    private List<ValueRange> createCriteriasValueRanges(String sheetName,
                                                        SummaryViewData data,
                                                        String cellPrefix,
                                                        int standardIndex) {
        List<ValueRange> ranges = new ArrayList<>();

        for (int criteriaIndex = 0; criteriaIndex < data.getCriteriaSummaryViewDataList().size(); criteriaIndex++) {
            SummaryViewData.CriteriaSummaryViewData criteriaData = data.getCriteriaSummaryViewDataList().get(criteriaIndex);
            ranges.addAll(createSubCriteriasValueRanges(sheetName, criteriaData, cellPrefix, standardIndex, criteriaIndex + 1));
        }

        return ranges;
    }

    private List<ValueRange> createSubCriteriasValueRanges(String sheetName,
                                                           SummaryViewData.CriteriaSummaryViewData criteriaData,
                                                           String cellPrefix,
                                                           int standardIndex,
                                                           int criteriaIndex) {
        List<ValueRange> ranges = new ArrayList<>();

        for (int subCriteriaIndex = 0; subCriteriaIndex < criteriaData.getAnswerStates().length; subCriteriaIndex++) {
            ranges.add(
                    createSingleCellValueRange(
                            sheetName,
                            getCellRange(
                                    FORMAT_CELL_SUMMARY_SUB_CRITERIA_TOTAL,
                                    cellPrefix,
                                    standardIndex,
                                    criteriaIndex,
                                    subCriteriaIndex + 1
                            ),
                            criteriaData.getAnswerStates()[subCriteriaIndex] ? 1 : 0
                    )
            );
        }

        return ranges;
    }

    protected ValueRange createSingleCellValueRange(String sheetName, String column, int row, Object value) {
        return createSingleCellValueRange(sheetName, column + row, value);
    }

    protected ValueRange createSingleCellValueRange(String sheetName, String a1Range, Object value) {
        return new ValueRange()
                .setRange(makeRange(sheetName, a1Range))
                .setValues(Collections.singletonList(Collections.singletonList(value)));
    }

    protected List<ValueRange> createInfoValueRanges(String sheetName, LevelLegendView.Item header) {
        return Arrays.asList(
                createSingleCellValueRange(
                        sheetName,
                        getCellRange(CELL_SCHOOL_ID),
                        appContext.getString(R.string.label_school_code) + " " + header.getSchoolId()
                ),
                createSingleCellValueRange(
                        sheetName,
                        getCellRange(CELL_SCHOOL_NAME),
                        appContext.getString(R.string.label_school_name) + " " + header.getSchoolName()
                ),
                createSingleCellValueRange(
                        sheetName,
                        getCellRange(CELL_DATE),
                        appContext.getString(R.string.label_date_of_accreditation) + " " + header.getSurveyTag()
                ),
                createSingleCellValueRange(
                        sheetName,
                        getCellRange(CELL_PRINCIPAL),
                        appContext.getString(R.string.label_principal_name)
                )
        );
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

    protected static class CellInfo {

        private int row;
        private int column;

        public CellInfo(int row, int column) {
            this.row = row;
            this.column = column;
        }

        public String getRange() {
            return TextUtil.convertIntToCharsIcons(column) + (row + 1);
        }

    }
}
