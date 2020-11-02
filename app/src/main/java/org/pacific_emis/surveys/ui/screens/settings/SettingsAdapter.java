package org.pacific_emis.surveys.ui.screens.settings;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.omega_r.libs.omegarecyclerview.BaseListAdapter;

import org.pacific_emis.surveys.R;
import org.pacific_emis.surveys.core.data.exceptions.NotImplementedException;
import org.pacific_emis.surveys.ui.screens.settings.items.Item;

public class SettingsAdapter extends BaseListAdapter<Item> {

    private static final int VIEW_TYPE_VALUE = 0;
    private static final int VIEW_TYPE_NAV = 1;
    private static final int VIEW_TYPE_RECEIVE = 2;
    private static final int VIEW_TYPE_BOOLEAN = 3;

    private final OnBooleanValueChangedListener onBooleanValueChangedListener;

    public SettingsAdapter(OnItemClickListener<Item> clickListener, OnBooleanValueChangedListener onBooleanValueChangedListener) {
        super(clickListener);
        this.onBooleanValueChangedListener = onBooleanValueChangedListener;
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
            case BOOLEAN:
                return VIEW_TYPE_BOOLEAN;
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
            case VIEW_TYPE_BOOLEAN:
                return new BooleanViewHolder(parent);
            default:
                throw new IllegalStateException();
        }
    }

    class BooleanViewHolder extends ViewHolder implements CompoundButton.OnCheckedChangeListener {

        private TextView titleTextView = findViewById(R.id.textview_title);
        private Switch valueSwitch = findViewById(R.id.switch_value);

        public BooleanViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_setting_bool);
            itemView.setOnClickListener(null);
        }

        @Override
        protected void onBind(Item item) {
            item.getTitle().applyTo(titleTextView);
            valueSwitch.setOnCheckedChangeListener(null);
            valueSwitch.setChecked(item.getBooleanValue());
            valueSwitch.setOnCheckedChangeListener(this);
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Item item = getItem();
            item.setBooleanValue(isChecked);
            onBooleanValueChangedListener.onBooleanValueChanged(item, isChecked);
        }
    }

    class ValueViewHolder extends BaseViewHolder {

        ValueViewHolder(ViewGroup parent) {
            super(parent);
            valueTextView.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onBind(Item item) {
            super.onBind(item);

            if (item.getTextValue() != null) {
                item.getTextValue().applyTo(valueTextView);
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

    public interface OnBooleanValueChangedListener {
        void onBooleanValueChanged(Item item, boolean value);
    }
}
