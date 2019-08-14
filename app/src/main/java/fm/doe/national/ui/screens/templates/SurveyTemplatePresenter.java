package fm.doe.national.ui.screens.templates;

import android.content.ContentResolver;
import android.net.Uri;

import java.io.ByteArrayInputStream;

import fm.doe.national.app_support.MicronesiaApplication;
import fm.doe.national.core.data.data_source.DataSource;
import fm.doe.national.core.data.exceptions.WrongAppRegionException;
import fm.doe.national.core.data.serialization.SurveyParser;
import fm.doe.national.core.preferences.LocalSettings;
import fm.doe.national.core.ui.screens.base.BasePresenter;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public abstract class SurveyTemplatePresenter extends BasePresenter<SurveyTemplateView> {

    private static final String MIME_TYPE_XML = "text/xml";

    protected final DataSource dataSource;
    private final SurveyParser parser;
    private final LocalSettings localSettings = MicronesiaApplication.getInjection()
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
        String content = readExternalUriToString(contentResolver, uri);

        if (content != null) {
            addDisposable(
                    Single.fromCallable(() -> parser.parse(new ByteArrayInputStream(content.getBytes())))
                            .flatMapCompletable(survey -> {
                                if (survey.getAppRegion() == localSettings.getAppRegion()) {
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
}
