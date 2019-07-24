package fm.doe.national.remote_storage.data.export;

import android.content.Context;

import com.google.api.services.sheets.v4.Sheets;

import fm.doe.national.remote_storage.data.model.ReportBundle;
import io.reactivex.Completable;

public class RmiSheetsExcelExporter extends SheetsExcelExporter {

    public RmiSheetsExcelExporter(Sheets sheetsApi, Context appContext) {
        super(sheetsApi, appContext);
    }

    @Override
    public Completable fillReportSheet(String fileId, String sheetName, ReportBundle reportBundle) {
        // TODO: not implemented
        return Completable.complete();
    }
}
