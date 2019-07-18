package fm.doe.national.remote_storage.data.storage;

import android.content.Context;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesRequest;
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
import fm.doe.national.fcm_report.data.model.SchoolAccreditationLevel;
import fm.doe.national.remote_storage.R;
import fm.doe.national.remote_storage.data.model.ReportWrapper;
import fm.doe.national.report_core.model.SummaryViewData;
import fm.doe.national.report_core.ui.level_legend.LevelLegendView;
import io.reactivex.Completable;

public class SheetsServiceHelper extends TasksRxWrapper {

    private static final String VALUE_INPUT_OPTION_USER = "USER_ENTERED";
    private static final String SHEET_NAME_TEMPLATE = "template";

    private final Sheets sheetsApi;
    private final Context appContext;

    public SheetsServiceHelper(Sheets sheetsApi, Context appContext) {
        this.sheetsApi = sheetsApi;
        this.appContext = appContext;
    }

    public Completable fillReportSheet(String spreadsheetId, String sheetName, ReportWrapper reportWrapper) {
        return wrapWithCompletableInThreadPool(() -> {
            List<ValueRange> rangesToUpdate = new ArrayList<>();
            rangesToUpdate.add(createInfoValueRange(sheetName, reportWrapper.getHeader()));
            rangesToUpdate.add(createLevelsValueRange(sheetName, reportWrapper.getSchoolAccreditationLevel()));
            rangesToUpdate.add(createLevelDeterminationValueRange(sheetName, reportWrapper.getSchoolAccreditationLevel()));
            rangesToUpdate.addAll(createSchoolEvaluationScoresValueRanges(sheetName, reportWrapper.getSummary()));

            updateValues(spreadsheetId, rangesToUpdate);
        });
    }

    private List<ValueRange> createSchoolEvaluationScoresValueRanges(String sheetName, List<SummaryViewData> summary) {
        List<SummaryViewData> schoolEvaluationSummary = summary.stream()
                .filter(it -> it.getCategory().getEvaluationForm() == EvaluationForm.SCHOOL_EVALUATION)
                .collect(Collectors.toList());
        ArrayList<ValueRange> ranges = new ArrayList<>();
        final List<List<String>> columnsOfStandardCells = Arrays.asList(
                Arrays.asList("B", "C", "D", "E"),
                Arrays.asList("F", "G", "H", "I"),
                Arrays.asList("J", "K", "L", "M"),
                Arrays.asList("N", "O", "P", "Q"),
                Arrays.asList("R", "S", "T", "U"),
                Arrays.asList("V", "W", "X", "Y")
        );
        final List<Integer> rowsOfSubCriteriaCells = Arrays.asList(21, 22, 23, 24);
        final int totalByCriteriaRow = 25;
        final int totalByStandardRow = 26;
        final int levelRow = 27;

        for (int standardIndex = 0; standardIndex < schoolEvaluationSummary.size(); standardIndex++) {
            SummaryViewData data = schoolEvaluationSummary.get(standardIndex);

            ranges.add(
                    createSingleCellValueRange(
                            sheetName,
                            columnsOfStandardCells.get(standardIndex).get(0),
                            totalByStandardRow,
                            data.getTotalByStandard()
                    )
            );

            ranges.add(
                    createSingleCellValueRange(
                            sheetName,
                            columnsOfStandardCells.get(standardIndex).get(0),
                            levelRow,
                            data.getLevel().getName().getString(appContext)
                    )
            );

            for (int criteriaIndex = 0; criteriaIndex < data.getCriteriaSummaryViewDataList().size(); criteriaIndex++) {
                SummaryViewData.CriteriaSummaryViewData criteriaData = data.getCriteriaSummaryViewDataList().get(criteriaIndex);

                ranges.add(
                        createSingleCellValueRange(
                                sheetName,
                                columnsOfStandardCells.get(standardIndex).get(criteriaIndex),
                                totalByCriteriaRow,
                                criteriaData.getTotal()
                        )
                );

                for (int subCriteriaIndex = 0; subCriteriaIndex < criteriaData.getAnswerStates().length; subCriteriaIndex++) {
                    ranges.add(
                            createSingleCellValueRange(
                                    sheetName,
                                    columnsOfStandardCells.get(standardIndex).get(criteriaIndex),
                                    rowsOfSubCriteriaCells.get(subCriteriaIndex),
                                    criteriaData.getAnswerStates()[subCriteriaIndex] ? 1 : 0
                            )
                    );
                }
            }
        }

        return ranges;
    }

    private ValueRange createSingleCellValueRange(String sheetName, String row, int column, Object value) {
        return new ValueRange()
                .setRange(makeRange(sheetName, row + column))
                .setValues(Collections.singletonList(Collections.singletonList(value)));
    }

    private ValueRange createInfoValueRange(String sheetName, LevelLegendView.Item header) {
        return new ValueRange()
                .setRange(makeRange(sheetName, "A3:B4"))
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
                .setRange(makeRange(sheetName, "B11:D13"))
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
        return new ValueRange()
                .setRange(makeRange(sheetName, "D14"))
                .setValues(Collections.singletonList(
                        Collections.singletonList(
                                level.getReportLevel().getName().getString(appContext)
                        )
                ));
    }

    public Completable createSheetIfNeeded(String spreadsheetId, String sheetName) {
        return wrapWithCompletableInThreadPool(() -> {
            Sheet templateSheet = findTemplateSheet(spreadsheetId);
            Optional<Sheet> existingSheet = findSheet(spreadsheetId, sheetName);

            if (existingSheet.isPresent()) {
                return;
            }

            duplicateSheet(spreadsheetId, templateSheet.getProperties().getSheetId(), sheetName);
        });
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

}
