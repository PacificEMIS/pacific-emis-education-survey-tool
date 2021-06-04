package org.pacific_emis.surveys.remote_storage.data.export;

import android.content.Context;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.util.List;

import org.pacific_emis.surveys.remote_storage.data.model.ReportBundle;

public class FsmSheetsExcelExporter extends SheetsExcelExporter {
    public FsmSheetsExcelExporter(Context appContext, Sheets sheetsApi) {
        super(appContext, sheetsApi);
    }

    @Override
    protected void updateRangesWithLevelsInfo(String sheetName, List<ValueRange> rangesToUpdate, ReportBundle reportBundle) {
        // nothing
    }
}
