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
import fm.doe.national.fcm_report.data.model.SchoolAccreditationLevel;
import fm.doe.national.remote_storage.R;
import fm.doe.national.remote_storage.data.model.ReportWrapper;
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
    private static final String RANGE_INFO = "A3:B4";
    private static final String RANGE_LEVELS = "B11:D13";
    private final Sheets sheetsApi;
    private final Context appContext;

    private static final Map<EvaluationForm, SummaryCellsInfo> MAP_EVALUATION_FORM_SUMMARY_CELLS_INFO;

    static {
        Map<EvaluationForm, SummaryCellsInfo> cellsInfoMap = new HashMap<>();
        cellsInfoMap.put(EvaluationForm.SCHOOL_EVALUATION, new SummaryCellsInfo(
                Arrays.asList(
                        Arrays.asList("B", "C", "D", "E"),
                        Arrays.asList("F", "G", "H", "I"),
                        Arrays.asList("J", "K", "L", "M"),
                        Arrays.asList("N", "O", "P", "Q"),
                        Arrays.asList("R", "S", "T", "U"),
                        Arrays.asList("V", "W", "X", "Y")
                ),
                Arrays.asList(21, 22, 23, 24),
                25,
                26,
                27,
                "Z",
                25
        ));
        cellsInfoMap.put(EvaluationForm.CLASSROOM_OBSERVATION, new SummaryCellsInfo(
                Arrays.asList(
                        Arrays.asList("B", "C"),
                        Arrays.asList("D", "E", "F", "G"),
                        Arrays.asList("H", "I", "J", "K"),
                        Collections.singletonList("L"),
                        Collections.singletonList("M")
                ),
                Arrays.asList(33, 34, 35, 36, 37),
                38,
                39,
                40,
                "N",
                38
        ));
        MAP_EVALUATION_FORM_SUMMARY_CELLS_INFO = cellsInfoMap;
    }

    private static final Map<EvaluationForm, RecommendationCellsInfo> MAP_RECOMMENDATION_CELLS_INFO;

    static {
        Map<EvaluationForm, RecommendationCellsInfo> cellsInfoMap = new HashMap<>();
        cellsInfoMap.put(EvaluationForm.SCHOOL_EVALUATION, new RecommendationCellsInfo(
                "A",
                56
        ));
        cellsInfoMap.put(EvaluationForm.CLASSROOM_OBSERVATION, new RecommendationCellsInfo(
                "K",
                54
        ));
        MAP_RECOMMENDATION_CELLS_INFO = cellsInfoMap;
    }

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
            rangesToUpdate.addAll(createEvaluationScoreValueRanges(sheetName, reportWrapper.getSummary(), EvaluationForm.SCHOOL_EVALUATION));
            rangesToUpdate.addAll(createEvaluationScoreValueRanges(sheetName, reportWrapper.getSummary(), EvaluationForm.CLASSROOM_OBSERVATION));
            rangesToUpdate.addAll(createEvaluationRecommendationsValueRange(sheetName, reportWrapper.getRecommendations(), EvaluationForm.SCHOOL_EVALUATION));
            rangesToUpdate.addAll(createEvaluationRecommendationsValueRange(sheetName, reportWrapper.getRecommendations(), EvaluationForm.CLASSROOM_OBSERVATION));
            updateValues(spreadsheetId, rangesToUpdate);
        });
    }

    private List<ValueRange> createEvaluationRecommendationsValueRange(String sheetName,
                                                                       FlattenRecommendationsWrapper recommendationsWrapper,
                                                                       EvaluationForm evaluationForm) {
        RecommendationCellsInfo cellsInfo = Objects.requireNonNull(MAP_RECOMMENDATION_CELLS_INFO.get(evaluationForm));
        ArrayList<ValueRange> ranges = new ArrayList<>();

        List<? extends Standard> standards = recommendationsWrapper.getFlattenSurvey().getCategories()
                .stream()
                .filter(c -> c.getEvaluationForm() == evaluationForm)
                .flatMap(c -> c.getStandards().stream())
                .collect(Collectors.toList());

        List<Recommendation> filteredRecommendations = filterRecommendations(recommendationsWrapper, evaluationForm);
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

    private ValueRange createSingleCellValueRange(String sheetName, String row, int column, Object value) {
        return new ValueRange()
                .setRange(makeRange(sheetName, row + column))
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
                "D",
                14,
                level.getReportLevel().getName().getString(appContext)
        );
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


    private static class SummaryCellsInfo {
        private List<List<String>> columnsOfStandardCells;
        private List<Integer> rowsOfSubCriteriaCells;
        private int totalByCriteriaRow;
        private int totalByStandardRow;
        private int levelRow;
        private String totalByEvaluationColumn;
        private int totalByEvaluationRow;

        public SummaryCellsInfo(List<List<String>> columnsOfStandardCells,
                                List<Integer> rowsOfSubCriteriaCells,
                                int totalByCriteriaRow,
                                int totalByStandardRow,
                                int levelRow,
                                String totalByEvaluationColumn,
                                int totalByEvaluationRow) {
            this.columnsOfStandardCells = columnsOfStandardCells;
            this.rowsOfSubCriteriaCells = rowsOfSubCriteriaCells;
            this.totalByCriteriaRow = totalByCriteriaRow;
            this.totalByStandardRow = totalByStandardRow;
            this.levelRow = levelRow;
            this.totalByEvaluationColumn = totalByEvaluationColumn;
            this.totalByEvaluationRow = totalByEvaluationRow;
        }
    }

    private static class RecommendationCellsInfo {
        private String textStartColumn;
        private int textStartRow;

        public RecommendationCellsInfo(String textStartColumn, int textStartRow) {
            this.textStartColumn = textStartColumn;
            this.textStartRow = textStartRow;
        }
    }
}
