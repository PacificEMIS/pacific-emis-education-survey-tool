package fm.doe.national.ui.screens.start;

import android.os.Bundle;

import fm.doe.national.ui.screens.base.BaseActivity;
import fm.doe.national.ui.screens.main.MainActivity;

/**
 * Created by Alexander Chibirev on 8/9/2018.
 */

public class StartActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO added correct logic
        startActivity(MainActivity.createIntent(this));
    }

}
