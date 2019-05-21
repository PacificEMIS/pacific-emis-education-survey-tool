package fm.doe.national.ui.screens.survey;

import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;
import com.omega_r.libs.omegarecyclerview.sticky_decoration.StickyAdapter;
import com.omega_r.libs.views.OmegaTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.doe.national.R;
import fm.doe.national.app_support.utils.ViewUtils;
import fm.doe.national.ui.screens.base.BaseAdapter;
import fm.doe.national.ui.screens.survey.navigation.NavigationItem;
import fm.doe.national.ui.screens.survey.navigation.ProgressableNavigationItem;

public class NavigationItemsAdapter extends BaseAdapter<NavigationItem>
        implements StickyAdapter<NavigationItemsAdapter.HeaderViewHolder> {

    private static final int VIEW_TYPE_PROGRESSABLE = 0;
    private static final int VIEW_TYPE_SINGLE_LINE = 1;

    public NavigationItemsAdapter(OnItemClickListener<NavigationItem> clickListener) {
        super(clickListener);
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position) instanceof ProgressableNavigationItem ? VIEW_TYPE_PROGRESSABLE : VIEW_TYPE_SINGLE_LINE;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_PROGRESSABLE:
                return new ProgressableViewHolder(parent);
            case VIEW_TYPE_SINGLE_LINE:
                return new SingleLineViewHolder(parent);
        }
        throw new IllegalStateException();
    }

    @Override
    protected ViewHolder provideViewHolder(ViewGroup parent) {
        // unused
        return null;
    }

    @Override
    public long getStickyId(int position) {
        return position == RecyclerView.NO_POSITION ? 0 : getItem(position).getHeaderId();
    }

    @Override
    public HeaderViewHolder onCreateStickyViewHolder(ViewGroup parent) {
        return new HeaderViewHolder(parent);
    }

    @Override
    public void onBindStickyViewHolder(HeaderViewHolder viewHolder, int position) {
        viewHolder.bind(getItem(position));
    }

    class ProgressableViewHolder extends ViewHolder {

        @BindView(R.id.textview_progress)
        TextView progressTextView;

        @BindView(R.id.progressbar)
        ProgressBar progressBar;

        @BindView(R.id.omegatextview_title)
        OmegaTextView titleOmegaTextView;

        ProgressableViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_navigation_with_progress);
        }

        @Override
        protected void onBind(NavigationItem item) {
            ProgressableNavigationItem navigationItem = (ProgressableNavigationItem) item;
            ViewUtils.rebindProgress(navigationItem.getProgress(), progressTextView, progressBar);
            titleOmegaTextView.setStartText(navigationItem.getNamePrefix());
            titleOmegaTextView.setText(navigationItem.getName());
        }
    }

    class SingleLineViewHolder extends ViewHolder {

        @BindView(R.id.textview_title)
        TextView titleTextView;

        SingleLineViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_navigation_without_progress);
        }

        @Override
        protected void onBind(NavigationItem item) {
            item.getName().applyTo(titleTextView, null);
        }
    }

    class HeaderViewHolder extends OmegaRecyclerView.ViewHolder {

        @BindView(R.id.textview_title)
        TextView titleTextView;

        HeaderViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_navigation_header);
            ButterKnife.bind(this, itemView);
        }

        void bind(NavigationItem item) {
            item.getHeader().applyTo(titleTextView, null);
        }
    }
}
