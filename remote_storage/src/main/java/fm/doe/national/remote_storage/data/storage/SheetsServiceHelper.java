package fm.doe.national.remote_storage.data.storage;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.SheetProperties;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.util.Arrays;

import fm.doe.national.core.utils.DateUtils;
import fm.doe.national.report_core.ui.level_legend.LevelLegendView;
import io.reactivex.Completable;

public class SheetsServiceHelper extends TasksRxWrapper {

    private static final String VALUE_INPUT_OPTION_USER = "USER_ENTERED";

    private final Sheets sheetsApi;

    public SheetsServiceHelper(Sheets sheetsApi) {
        this.sheetsApi = sheetsApi;
    }

    public Completable updateSurveyHeader(String spreadsheetId, String tableName, LevelLegendView.Item header) {
        return wrapWithCompletableInThreadPool(() -> {
            String targetRange = "'" + tableName + "'!A3:B4";
            ValueRange existingValues = sheetsApi.spreadsheets()
                    .values()
                    .get(spreadsheetId, targetRange)
                    .execute();
            ValueRange updateValues = new ValueRange().setValues(Arrays.asList(
                    Arrays.asList(
                            existingValues.getValues().get(0).get(0) + " " + header.getSchoolId(),
                            existingValues.getValues().get(0).get(1) + " " + header.getSchoolName()
                    ),
                    Arrays.asList(
                            existingValues.getValues().get(1).get(0) + " " + DateUtils.formatNumericMonthYear(header.getDate()),
                            existingValues.getValues().get(1).get(1)
                    )
            ));
            sheetsApi.spreadsheets()
                    .values()
                    .update(spreadsheetId, targetRange, updateValues)
                    .setValueInputOption(VALUE_INPUT_OPTION_USER)
                    .execute();
        });
    }

    private static class Factory {

        static Sheet createSheet(String title) {
            Sheet sheet = new Sheet();
            SheetProperties properties = new SheetProperties();
            properties.setTitle(title);
            sheet.setProperties(properties);
            return sheet;
        }
    }
}
