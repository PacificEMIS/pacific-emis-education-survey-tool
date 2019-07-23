package fm.doe.national.remote_storage.data.export;

import android.content.Context;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;

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
import fm.doe.national.fcm_report.data.model.SchoolAccreditationLevel;
import fm.doe.national.remote_storage.R;
import fm.doe.national.remote_storage.data.model.ReportBundle;
import fm.doe.national.report_core.model.recommendations.CategoryRecommendation;
import fm.doe.national.report_core.model.recommendations.CriteriaRecommendation;
import fm.doe.national.report_core.model.recommendations.FlattenRecommendationsWrapper;
import fm.doe.national.report_core.model.recommendations.Recommendation;
import fm.doe.national.report_core.model.recommendations.StandardRecommendation;
import fm.doe.national.report_core.model.recommendations.SubCriteriaRecommendation;
import io.reactivex.Completable;

public class FcmSheetsExcelExporter extends SheetsExcelExporter implements ExcelExporter {

    private static final String RANGE_LEVELS = "B11:D13";
    private static final String MARK_RECOMMENDATION = "√";
    private static final int OFFSET_RECOMMENDATION = 4;
    private static final String COLUMN_LEVEL_DETERMINATION = "D";
    private static final int ROW_LEVEL_DETERMINATION = 14;

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
                        .setRowsOfSubCriteriaCells(21, 22, 23, 24)
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
                        .setRowsOfSubCriteriaCells(33, 34, 35, 36, 37)
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
                .setTableColumns("B", "C", "D", "E", "F", "G", "H", "I")
                .setTableRows(47, 48, 49, 50, 51, 52)
                .build()
        );
        cellsInfoMap.put(EvaluationForm.CLASSROOM_OBSERVATION, new RecommendationCellsInfo.Builder()
                .setTextStartColumn("K")
                .setTextStartRow(54)
                .setTableColumns("L", "M", "N", "O", "P", "Q", "R", "S")
                .setTableRows(47, 48, 49, 50, 51)
                .build()
        );
        MAP_RECOMMENDATION_CELLS_INFO = cellsInfoMap;
    }

    public FcmSheetsExcelExporter(Sheets sheetsApi, Context appContext) {
        super(sheetsApi, appContext);
    }

    @Override
    protected SummaryCellsInfo getSummaryCellsInfo(EvaluationForm evaluationForm) {
        return Objects.requireNonNull(MAP_EVALUATION_FORM_SUMMARY_CELLS_INFO.get(evaluationForm));
    }

    @Override
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

    private static class RecommendationCellsInfo {
        private String textStartColumn;
        private int textStartRow;
        private List<String> tableColumns;
        private List<Integer> tableRows;

        private RecommendationCellsInfo() {
            // private constructor
        }

        public static class Builder {
            private FcmSheetsExcelExporter.RecommendationCellsInfo cellsInfo = new FcmSheetsExcelExporter.RecommendationCellsInfo();

            public Builder setTextStartColumn(String textStartColumn) {
                cellsInfo.textStartColumn = textStartColumn;
                return this;
            }

            public Builder setTextStartRow(int textStartRow) {
                cellsInfo.textStartRow = textStartRow;
                return this;
            }

            public Builder setTableColumns(String... tableColumns) {
                cellsInfo.tableColumns = Arrays.asList(tableColumns);
                return this;
            }

            public Builder setTableRows(Integer... tableRows) {
                cellsInfo.tableRows = Arrays.asList(tableRows);
                return this;
            }

            public FcmSheetsExcelExporter.RecommendationCellsInfo build() {
                if (cellsInfo.textStartColumn == null || cellsInfo.tableColumns == null || cellsInfo.tableRows == null) {
                    throw new IllegalStateException();
                }
                return cellsInfo;
            }
        }

    }

}
