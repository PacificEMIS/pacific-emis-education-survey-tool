package fm.doe.national.remote_storage.data.export;

public class RmiSheetsExcelExporter/* extends SheetsExcelExporter*/ {

//    private static final Map<EvaluationForm, SummaryCellsInfo> MAP_EVALUATION_FORM_SUMMARY_CELLS_INFO;
//
//    static {
//        Map<EvaluationForm, SummaryCellsInfo> cellsInfoMap = new HashMap<>();
//        cellsInfoMap.put(
//                EvaluationForm.SCHOOL_EVALUATION,
//                new SummaryCellsInfo.Builder()
//                        .setColumnsOfStandardCells(Arrays.asList(
//                                Arrays.asList("B", "C", "D", "E"),
//                                Arrays.asList("F", "G", "H", "I"),
//                                Arrays.asList("J", "K", "L", "M"),
//                                Arrays.asList("N", "O", "P", "Q"),
//                                Arrays.asList("R", "S", "T", "U"),
//                                Arrays.asList("V", "W", "X", "Y")
//                        ))
//                        .setRowsOfSubCriteriaCells(12, 13, 14, 15)
//                        .setTotalByCriteriaRow(16)
//                        .build()
//        );
//        cellsInfoMap.put(
//                EvaluationForm.CLASSROOM_OBSERVATION,
//                new SummaryCellsInfo.Builder()
//                        .setColumnsOfStandardCells(Arrays.asList(
//                                Arrays.asList("Z", "AA", "AB", "AC"),
//                                Arrays.asList("AD", "AE", "AF", "AG")
//                        ))
//                        .setRowsOfSubCriteriaCells(12, 13, 14, 15)
//                        .setTotalByCriteriaRow(16)
//                        .build()
//        );
//        MAP_EVALUATION_FORM_SUMMARY_CELLS_INFO = cellsInfoMap;
//    }
//
//    public RmiSheetsExcelExporter(Sheets sheetsApi, Context appContext) {
//        super(sheetsApi, appContext);
//    }
//
//    @Override
//    protected SummaryCellsInfo getSummaryCellsInfo(EvaluationForm evaluationForm) {
//        return Objects.requireNonNull(MAP_EVALUATION_FORM_SUMMARY_CELLS_INFO.get(evaluationForm));
//    }
//
//    @Override
//    public Completable fillReportSheet(String spreadsheetId, String sheetName, ReportBundle reportBundle) {
//        return wrapWithCompletableInThreadPool(() -> {
//            List<ValueRange> rangesToUpdate = new ArrayList<>();
//            rangesToUpdate.add(createInfoValueRange(sheetName, reportBundle.getHeader()));
//            rangesToUpdate.addAll(createEvaluationScoreValueRanges(sheetName, reportBundle.getSummary(), EvaluationForm.SCHOOL_EVALUATION));
//            rangesToUpdate.addAll(createEvaluationScoreValueRanges(sheetName, reportBundle.getSummary(), EvaluationForm.CLASSROOM_OBSERVATION));
//            updateValues(spreadsheetId, rangesToUpdate);
//        });
//    }
}
