package org.pacific_emis.surveys.remote_storage.data.export;

import android.content.Context;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.util.List;

import org.pacific_emis.surveys.remote_storage.data.model.ReportBundle;
import org.pacific_emis.surveys.report_core.model.Level;
import org.pacific_emis.surveys.rmi_report.model.SchoolAccreditationTallyLevel;

public class RmiSheetsExcelExporter extends SheetsExcelExporter {

    private static final String CELL_TALLY_1 = "$tally1";
    private static final String CELL_TALLY_2 = "$tally2";
    private static final String CELL_TALLY_3 = "$tally3";
    private static final String CELL_TALLY_4 = "$tally4";
    private static final String CELL_TALLY_SCORE = "$tallyScore";
    private static final String CELL_TALLY_LEVEL = "$tallyLevel";

    public RmiSheetsExcelExporter(Context appContext, Sheets sheetsApi) {
        super(appContext, sheetsApi);
    }

    @Override
    protected void updateRangesWithLevelsInfo(String sheetName, List<ValueRange> rangesToUpdate, ReportBundle reportBundle) {
        SchoolAccreditationTallyLevel tally = reportBundle.getSchoolAccreditationTallyLevel();
        rangesToUpdate.add(createSingleCellValueRange(
                sheetName,
                getCellRange(CELL_TALLY_1),
                tally.getCountOfOnes()
        ));
        rangesToUpdate.add(createSingleCellValueRange(
                sheetName,
                getCellRange(CELL_TALLY_2),
                tally.getCountOfTwos()
        ));
        rangesToUpdate.add(createSingleCellValueRange(
                sheetName,
                getCellRange(CELL_TALLY_3),
                tally.getCountOfThrees()
        ));
        rangesToUpdate.add(createSingleCellValueRange(
                sheetName,
                getCellRange(CELL_TALLY_4),
                tally.getCountOfFours()
        ));
        rangesToUpdate.add(createSingleCellValueRange(
                sheetName,
                getCellRange(CELL_TALLY_SCORE),
                tally.getTallyScore()
        ));
        Level tallyLevel = tally.getLevel();
        if (tallyLevel != null) {
            rangesToUpdate.add(createSingleCellValueRange(
                    sheetName,
                    getCellRange(CELL_TALLY_LEVEL),
                    tallyLevel.getName().getString(appContext)
            ));
        }
    }
}
