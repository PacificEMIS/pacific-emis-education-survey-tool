package fm.doe.national.remote_storage.data.storage;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.SheetProperties;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.SpreadsheetProperties;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.util.Arrays;

import io.reactivex.Completable;
import io.reactivex.Single;

public class SheetsServiceHelper extends TasksRxWrapper {

    private static final String VALUE_INPUT_OPTION_USER = "USER_ENTERED";

    private final Sheets sheetsApi;

    public SheetsServiceHelper(Sheets sheetsApi) {
        this.sheetsApi = sheetsApi;
    }

    public Single<Spreadsheet> getSpreadsheet(String spreadsheetId) {
        return wrapWithSingleInThreadPool(() -> {
                    ValueRange response = sheetsApi.spreadsheets().values().get(spreadsheetId, "'SCHNO-MM-YYYY'!B11:D13").execute();
                    return Factory.createSpreadsheet("NONE");
                },
                Factory.createSpreadsheet("NONE")
        );
    }

    public Completable updateSurveyInfo(String spreadsheetId, String tableName, String schoolCode, String schoolName, String date) {
        return wrapWithCompletableInThreadPool(() -> {
            String targetRange = "'" + tableName + "'!A3:B4";
            ValueRange existingValues = sheetsApi.spreadsheets()
                    .values()
                    .get(spreadsheetId, targetRange)
                    .execute();
            ValueRange updateValues = new ValueRange().setValues(Arrays.asList(
                    Arrays.asList(
                            existingValues.getValues().get(0).get(0) + " " + schoolCode,
                            existingValues.getValues().get(0).get(1) + " " + schoolName
                    ),
                    Arrays.asList(
                            existingValues.getValues().get(1).get(0) + " " + date,
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

        static Spreadsheet createSpreadsheet(String title) {
            Spreadsheet spreadsheet = new Spreadsheet();
            SpreadsheetProperties properties = new SpreadsheetProperties();
            properties.setTitle(title);
            spreadsheet.setProperties(properties);
            return spreadsheet;
        }

        static Sheet createSheet(String title) {
            Sheet sheet = new Sheet();
            SheetProperties properties = new SheetProperties();
            properties.setTitle(title);
            sheet.setProperties(properties);
            return sheet;
        }
    }
}
