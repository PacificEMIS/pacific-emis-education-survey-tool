package fm.doe.national.ui.screens.templates;

import android.content.ContentResolver;
import android.net.Uri;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import fm.doe.national.app_support.MicronesiaApplication;
import fm.doe.national.core.data.data_source.DataSource;
import fm.doe.national.core.data.exceptions.WrongAppRegionException;
import fm.doe.national.core.data.serialization.SurveyParser;
import fm.doe.national.core.preferences.GlobalPreferences;
import fm.doe.national.core.ui.screens.base.BasePresenter;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public abstract class SurveyTemplatePresenter extends BasePresenter<SurveyTemplateView> {

    private static final String MIME_TYPE_XML = "text/xml";

    protected final DataSource dataSource;
    private final SurveyParser parser;
    private final GlobalPreferences globalPreferences = MicronesiaApplication.getInjection()
            .getCoreComponent()
            .getGlobalPreferences();

    public SurveyTemplatePresenter(DataSource dataSource, SurveyParser parser) {
        this.dataSource = dataSource;
        this.parser = parser;
    }

    protected abstract void loadItems();

    protected void onLoadPressed() {
        getViewState().openExternalDocumentsPicker(MIME_TYPE_XML);
    }

    @Override
    public void onExternalDocumentPicked(ContentResolver contentResolver, Uri uri) {
        try {
            InputStream inputStream = contentResolver.openInputStream(uri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            inputStream.close();
            onExternalContentReceived(stringBuilder.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void onExternalContentReceived(String content) {
        addDisposable(
                Single.fromCallable(() -> parser.parse(new ByteArrayInputStream(content.getBytes())))
                        .flatMapCompletable(survey -> {
                            if (survey.getAppRegion() == globalPreferences.getAppRegion()) {
                                return dataSource.rewriteTemplateSurvey(survey);
                            } else {
                                throw new WrongAppRegionException();
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(d -> getViewState().showWaiting())
                        .doFinally(getViewState()::hideWaiting)
                        .subscribe(this::loadItems, this::handleError)
        );
    }
}
