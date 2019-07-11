package fm.doe.national.ui.screens.settings;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.omega_r.libs.omegarecyclerview.BaseListAdapter;

import fm.doe.national.R;
import fm.doe.national.core.data.exceptions.NotImplementedException;
import fm.doe.national.ui.screens.settings.items.Item;

public class SettingsAdapter extends BaseListAdapter<Item> {

    private static final int VIEW_TYPE_VALUE = 0;
    private static final int VIEW_TYPE_NAV = 1;
    private static final int VIEW_TYPE_RECEIVE = 2;

    public SettingsAdapter(@Nullable OnItemClickListener<Item> clickListener) {
        super(clickListener);
    }

    @Override
    public int getItemViewType(int position) {
        switch (getItem(position).getIconType()) {
            case NAV:
                return VIEW_TYPE_NAV;
            case VALUE:
                return VIEW_TYPE_VALUE;
            case RECEIVE:
                return VIEW_TYPE_RECEIVE;
            default:
                throw new NotImplementedException();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_VALUE:
                return new ValueViewHolder(parent);
            case VIEW_TYPE_NAV:
                return new NavViewHolder(parent);
            case VIEW_TYPE_RECEIVE:
                return new ReceiveViewHolder(parent);
        }
        throw new IllegalStateException();
    }

    @Override
    protected ViewHolder provideViewHolder(ViewGroup parent) {
        return null;
    }

    class ValueViewHolder extends BaseViewHolder {

        ValueViewHolder(ViewGroup parent) {
            super(parent);
            valueTextView.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onBind(Item item) {
            super.onBind(item);

            if (item.getValue() != null) {
                item.getValue().applyTo(valueTextView);
            }
        }
    }

    class NavViewHolder extends BaseViewHolder {

        private final Drawable iconDrawable = getResources().getDrawable(R.drawable.ic_navigate_next, getContext().getTheme());

        NavViewHolder(ViewGroup parent) {
            super(parent);
            actionIconImageView.setVisibility(View.VISIBLE);
            actionIconImageView.setImageDrawable(iconDrawable);
        }

    }

    class ReceiveViewHolder extends BaseViewHolder {

        private final Drawable iconDrawable = getResources().getDrawable(R.drawable.ic_download, getContext().getTheme());

        ReceiveViewHolder(ViewGroup parent) {
            super(parent);
            actionIconImageView.setVisibility(View.VISIBLE);
            actionIconImageView.setImageDrawable(iconDrawable);
        }

    }

    class BaseViewHolder extends ViewHolder {

        ImageView actionIconImageView = findViewById(R.id.imageview_action_icon);
        TextView valueTextView = findViewById(R.id.textview_value);
        TextView titleTextView = findViewById(R.id.textview_title);

        BaseViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_setting);
            valueTextView.setVisibility(View.GONE);
            actionIconImageView.setVisibility(View.GONE);
        }

        @Override
        protected void onBind(Item item) {
            item.getTitle().applyTo(titleTextView);
        }
    }
}
