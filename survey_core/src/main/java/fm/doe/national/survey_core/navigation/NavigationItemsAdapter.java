package fm.doe.national.survey_core.navigation;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.omega_r.libs.views.OmegaTextView;

import java.util.List;

import fm.doe.national.core.data.model.Progress;
import fm.doe.national.core.ui.screens.base.BaseAdapter;
import fm.doe.national.core.utils.ViewUtils;
import fm.doe.national.survey_core.R;
import fm.doe.national.survey_core.navigation.survey_navigator.ReportBuildableNavigationItem;

public class NavigationItemsAdapter extends BaseAdapter<NavigationItem> {

    private static final int VIEW_TYPE_SECTION = 0;
    private static final int VIEW_TYPE_QUESTION_GROUP = 1;

    private int selectedItemPosition = RecyclerView.NO_POSITION;
    private boolean isReportEnabled;

    public NavigationItemsAdapter(OnItemClickListener<NavigationItem> clickListener) {
        super(clickListener);
    }

    public void setSelectedItem(long itemId) {
        List<NavigationItem> items = getItems();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId() == itemId) {
                selectItemAtPosition(i);
                return;
            }
        }
    }

    private void selectItemAtPosition(int position) {
        int previouslySelectedPosition = selectedItemPosition;
        selectedItemPosition = position;
        notifyItemChanged(position);
        notifyItemChanged(previouslySelectedPosition);
    }

    public void notifyProgressChanged(long itemId, Progress progress) {
        List<NavigationItem> items = getItems();
        for (int i = 0; i < items.size(); i++) {
            NavigationItem item = items.get(i);
            if (item instanceof ProgressablePrefixedBuildableNavigationItem) {
                ProgressablePrefixedBuildableNavigationItem castedItem = (ProgressablePrefixedBuildableNavigationItem) item;
                if (castedItem.getId() == itemId) {
                    castedItem.setProgress(progress);
                    notifyItemChanged(i);
                    return;
                }
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        NavigationItem item = getItem(position);

        if (item instanceof BuildableNavigationItem) {
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

    public void setReportEnabled(boolean enabled) {
        isReportEnabled = enabled;
        notifyDataSetChanged();
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
            titleOmegaTextView.setText(item.getTitle());
            itemView.setActivated(isSelected());

            if (item instanceof ProgressablePrefixedBuildableNavigationItem) {
                ProgressablePrefixedBuildableNavigationItem navigationItem = (ProgressablePrefixedBuildableNavigationItem) item;
                titleOmegaTextView.setStartText(navigationItem.getTitlePrefix());
                progressTextView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                ViewUtils.rebindProgress(navigationItem.getProgress(), progressTextView, progressBar);
            } else {
                titleOmegaTextView.setStartText(null);
                progressTextView.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            }

            itemView.setEnabled(!(item instanceof ReportBuildableNavigationItem) || isReportEnabled);
        }

        private boolean isSelected() {
            return getAdapterPosition() == selectedItemPosition;
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
        public void onClick(View v) {
            // nothing - do not handle clicks on HeaderViewHolder
        }

        @Override
        protected void onBind(NavigationItem item) {
            item.getTitle().applyTo(titleTextView, null);
        }
    }
}
