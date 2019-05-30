package fm.doe.national.accreditation.ui.providers;

import android.content.Context;
import android.content.Intent;

import fm.doe.national.accreditation.ui.survey.SurveyActivity;

public class AccreditationSurveyProviderImpl implements AccreditationSurveyProvider {

    @Override
    public Intent provideAccreditationIntent(Context context) {
        return new Intent(context, SurveyActivity.class);
    }

}
