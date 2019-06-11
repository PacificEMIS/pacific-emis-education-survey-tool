package fm.doe.national.ui.screens.settings;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.omega_r.libs.omegarecyclerview.BaseListAdapter;
import com.omega_r.libs.omegatypes.Image;

import fm.doe.national.R;
import fm.doe.national.ui.screens.settings.items.Item;
import fm.doe.national.ui.screens.settings.items.ValuableItem;

public class SettingsAdapter extends BaseListAdapter<Item> {

    private static final int VIEW_TYPE_VALUE = 0;
    private static final int VIEW_TYPE_NAV = 1;

    public SettingsAdapter(@Nullable OnItemClickListener<Item> clickListener) {
        super(clickListener);
    }

    @Override
    public int getItemViewType(int position) {
        return (getItem(position) instanceof ValuableItem) ? VIEW_TYPE_VALUE : VIEW_TYPE_NAV;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_VALUE:
                return new ValueViewHolder(parent);
            case VIEW_TYPE_NAV:
                return new NavViewHolder(parent);
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
            actionIconImageView.setVisibility(View.GONE);
        }

        @Override
        protected void onBind(Item item) {
            super.onBind(item);
            ValuableItem valuableItem = (ValuableItem) item;
            valuableItem.getValue().applyTo(valueTextView);
        }
    }

    class NavViewHolder extends BaseViewHolder {

        NavViewHolder(ViewGroup parent) {
            super(parent);
            valueTextView.setVisibility(View.GONE);
        }

    }

    class BaseViewHolder extends ViewHolder {

        ImageView iconImageView = findViewById(R.id.imageview_icon);
        ImageView actionIconImageView = findViewById(R.id.imageview_action_icon);
        TextView valueTextView = findViewById(R.id.textview_value);
        TextView titleTextView = findViewById(R.id.textview_title);

        BaseViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_setting);
        }

        @Override
        protected void onBind(Item item) {
            item.getTitle().applyTo(titleTextView);

            Image iconImage = item.getIcon();

            if (iconImage != null) {
                iconImage.applyImage(iconImageView, 0);
            }
        }
    }
}
