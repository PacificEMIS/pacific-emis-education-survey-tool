package org.pacific_emis.surveys.ui.screens.templates;

import android.content.ContentResolver;
import android.net.Uri;

import org.pacific_emis.surveys.app_support.MicronesiaApplication;
import org.pacific_emis.surveys.core.data.exceptions.WrongAppRegionException;
import org.pacific_emis.surveys.core.data.local_data_source.DataSource;
import org.pacific_emis.surveys.core.data.serialization.SurveyParser;
import org.pacific_emis.surveys.core.preferences.LocalSettings;
import org.pacific_emis.surveys.core.ui.screens.base.BasePresenter;

import java.io.ByteArrayInputStream;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public abstract class SurveyTemplatePresenter extends BasePresenter<SurveyTemplateView> {

    private static final String MIME_TYPE_XML = "text/xml";

    protected final DataSource dataSource;
    private final SurveyParser parser;
    private final LocalSettings localSettings = MicronesiaApplication.getInjection()
            .getCoreComponent()
            .getLocalSettings();

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
                                if (survey.getAppRegion() == localSettings.getCurrentAppRegion()) {
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
