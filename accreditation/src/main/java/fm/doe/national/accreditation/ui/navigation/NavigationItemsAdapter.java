package fm.doe.national.accreditation.ui.navigation;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.omega_r.libs.views.OmegaTextView;

import fm.doe.national.accreditation.R;
import fm.doe.national.core.ui.screens.base.BaseAdapter;
import fm.doe.national.core.utils.ViewUtils;

public class NavigationItemsAdapter extends BaseAdapter<NavigationItem> {

    private static final int VIEW_TYPE_SECTION = 0;
    private static final int VIEW_TYPE_QUESTION_GROUP = 1;

    public NavigationItemsAdapter(OnItemClickListener<NavigationItem> clickListener) {
        super(clickListener);
    }

    @Override
    public int getItemViewType(int position) {
        NavigationItem item = getItem(position);

        if (item instanceof ProgressablePrefixedBuildableNavigationItem) {
            return VIEW_TYPE_QUESTION_GROUP;
        }

        return VIEW_TYPE_SECTION;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_QUESTION_GROUP:
                return new QuestionGroupViewHolder(parent);
            case VIEW_TYPE_SECTION:
                return new HeaderViewHolder(parent);
        }
        throw new IllegalStateException();
    }

    @Override
    protected ViewHolder provideViewHolder(ViewGroup parent) {
        // unused
        return null;
    }

    class QuestionGroupViewHolder extends ViewHolder {

        private TextView progressTextView;
        private ProgressBar progressBar;
        private OmegaTextView titleOmegaTextView;

        QuestionGroupViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_navigation_group);
            bindViews();
        }

        private void bindViews() {
            progressTextView = findViewById(R.id.textview_progress);
            progressBar = findViewById(R.id.progressbar);
            titleOmegaTextView = findViewById(R.id.omegatextview_title);
        }

        @Override
        protected void onBind(NavigationItem item) {
            ProgressablePrefixedBuildableNavigationItem navigationItem = (ProgressablePrefixedBuildableNavigationItem) item;
            ViewUtils.rebindProgress(navigationItem.getProgress(), progressTextView, progressBar);
            titleOmegaTextView.setStartText(navigationItem.getTitlePrefix());
            titleOmegaTextView.setText(navigationItem.getTitle());
        }
    }

    class HeaderViewHolder extends ViewHolder {

        private TextView titleTextView;

        HeaderViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_navigation_header);
            bindViews();
        }

        private void bindViews() {
            titleTextView = findViewById(R.id.textview_title);
        }

        @Override
        protected void onBind(NavigationItem item) {
            item.getTitle().applyTo(titleTextView, null);
        }

        @Override
        public void onClick(View v) {
            if (getItem() instanceof BuildableNavigationItem) {
                super.onClick(v);
            }
        }
    }
}
