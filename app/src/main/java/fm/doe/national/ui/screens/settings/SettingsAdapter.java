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

public class SettingsAdapter extends BaseListAdapter<Item> {

    private static final int VIEW_TYPE_VALUE = 0;
    private static final int VIEW_TYPE_NAV = 1;
    private static final int VIEW_TYPE_LOGO = 2;

    public SettingsAdapter(@Nullable OnItemClickListener<Item> clickListener) {
        super(clickListener);
    }

    @Override
    public int getItemViewType(int position) {
        Item item = getItem(position);

        if (item.getType() == Item.Type.LOGO) {
            return VIEW_TYPE_LOGO;
        }

        switch (item.getIconType()) {
            case NAV:
                return VIEW_TYPE_NAV;
            case VALUE:
                return VIEW_TYPE_VALUE;
            default:
                throw new IllegalStateException();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_LOGO:
                return new LogoViewHolder(parent);
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

        NavViewHolder(ViewGroup parent) {
            super(parent);
            actionIconImageView.setVisibility(View.VISIBLE);
        }

    }

    class LogoViewHolder extends BaseViewHolder {

        LogoViewHolder(ViewGroup parent) {
            super(parent);
            imageView.setVisibility(View.VISIBLE);
            actionIconImageView.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onBind(Item item) {
            super.onBind(item);

            if (item.getCustomImage() != null) {
                item.getCustomImage().applyImage(imageView, 0);
            }
        }
    }

    class BaseViewHolder extends ViewHolder {

        ImageView iconImageView = findViewById(R.id.imageview_icon);
        ImageView imageView = findViewById(R.id.imageview_custom_image);
        ImageView actionIconImageView = findViewById(R.id.imageview_action_icon);
        TextView valueTextView = findViewById(R.id.textview_value);
        TextView titleTextView = findViewById(R.id.textview_title);

        BaseViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_setting);
            imageView.setVisibility(View.GONE);
            valueTextView.setVisibility(View.GONE);
            actionIconImageView.setVisibility(View.GONE);
        }

        @Override
        protected void onBind(Item item) {
            item.getTitle().applyTo(titleTextView);

            Image iconImage = item.getIcon();

            if (iconImage != null) {
                iconImageView.setVisibility(View.VISIBLE);
                iconImage.applyImage(iconImageView, 0);
            } else {
                iconImageView.setVisibility(View.GONE);
            }
        }
    }
}
