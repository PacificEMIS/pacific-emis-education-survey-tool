package fm.doe.national.ui.screens.region;

import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.omega_r.libs.omegarecyclerview.BaseListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.doe.national.R;
import fm.doe.national.core.preferences.entities.AppRegion;

public class RegionsAdapter extends BaseListAdapter<AppRegion> {

    private int selectedPosition = 0; // first one by default

    public RegionsAdapter(@Nullable OnItemClickListener<AppRegion> clickListener) {
        super(clickListener);
    }

    @Override
    protected ViewHolder provideViewHolder(ViewGroup parent) {
        return new ItemViewHolder(parent);
    }

    private void updateSelectedPosition(int newPosition) {
        int oldPosition = selectedPosition;
        selectedPosition = newPosition;
        notifyItemChanged(oldPosition);
        notifyItemChanged(selectedPosition);
    }

    class ItemViewHolder extends ViewHolder {

        @BindView(R.id.textview)
        TextView textView;

        @BindView(R.id.radiobutton)
        RadioButton radioButton;

        ItemViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_region);
            ButterKnife.bind(this, itemView);
        }

        @Override
        protected void onBind(AppRegion item) {
            boolean isSelected = getAdapterPosition() == selectedPosition;
            itemView.setActivated(isSelected);
            radioButton.setChecked(isSelected);
            item.getName().applyTo(textView, null);
        }

        @Override
        public void onClick(View v) {
            super.onClick(v);
            updateSelectedPosition(getAdapterPosition());
        }
    }
}
