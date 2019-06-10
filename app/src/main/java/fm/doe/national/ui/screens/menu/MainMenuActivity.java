package fm.doe.national.ui.screens.menu;

import android.content.Context;
import android.content.Intent;

import fm.doe.national.R;
import fm.doe.national.core.ui.screens.base.BaseActivity;

public class MainMenuActivity extends BaseActivity {

    public static Intent createIntent(Context parentContext) {
        return new Intent(parentContext, MainMenuActivity.class);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main_menu;
    }
}
