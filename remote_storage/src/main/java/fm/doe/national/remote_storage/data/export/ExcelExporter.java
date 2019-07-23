package fm.doe.national.remote_storage.data.export;

import fm.doe.national.remote_storage.data.model.ReportBundle;
import io.reactivex.Completable;

public interface ExcelExporter {

    Completable recreateSheet(String fileId, String sheetName, String templateSheetName);

    Completable fillReportSheet(String fileId, String sheetName, ReportBundle reportBundle);

    Completable updateSummarySheet(String fileId, String sheetName, ReportBundle reportBundle);

    // every chain method must call this after any work
    Completable recycle();

}
