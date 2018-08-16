package fm.doe.national.ui.screens.search.base;

import android.app.Activity;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.annotation.LayoutRes;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.omega_r.libs.omegatypes.Text;
import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;

import java.util.List;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.ui.screens.base.BaseActivity;

/**
 * Created by Alexander Chibirev on 8/16/2018.
 */

public abstract class BaseSearchActivity extends BaseActivity implements SearchBox.SearchListener, BaseSearchView {

    @BindView(R.id.searchbox)
    SearchBox searchBox;

    @BindView(R.id.textview_not_found_message)
    TextView mMessageTextView;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        searchBox.setVisibility(View.GONE);
        searchBox.setSearchListener(this);
        searchBox.enableVoiceRecognition(this);
        searchBox.setAnimateDrawerLogo(false);
        searchBox.setHint(getString(R.string.write_school));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SearchBox.VOICE_RECOGNITION_CODE && resultCode == Activity.RESULT_OK) {
            List<String> matches = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            searchBox.populateEditText(matches.get(0));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                if (!searchBox.getSearchOpen()) {
                    onActionSearchClicked();
                }
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void showNotFoundObject(Text message) {
        mMessageTextView.setText(message.getString(getResources()));
        mMessageTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNotFoundObject() {
        mMessageTextView.setVisibility(View.GONE);
    }

    @Override
    public void onSearchOpened() {
    }

    @Override
    public void onSearchCleared() {
    }

    @Override
    public void onSearchClosed() {
    }

    @Override
    public void onSearchTermChanged(String term) {
    }

    @Override
    public void onSearch(String result) {
    }

    @Override
    public void onResultClick(SearchResult result) {
    }

    @Override
    public void closeSearch() {
        searchBox.hideCircularly(this);
        searchBox.clearText();
        hideNotFoundObject();
    }

    @Override
    public void openSearch() {
        searchBox.revealFromMenuItem(R.id.action_search, this);
    }

    protected abstract void onActionSearchClicked();
}
