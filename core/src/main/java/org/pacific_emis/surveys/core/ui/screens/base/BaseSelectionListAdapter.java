package org.pacific_emis.surveys.core.ui.screens.base;

import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.omega_r.libs.omegarecyclerview.BaseListAdapter;

public abstract class BaseSelectionListAdapter<T> extends BaseListAdapter<T> {

    protected int selectedPosition = RecyclerView.NO_POSITION;

    public BaseSelectionListAdapter(@Nullable OnItemClickListener<T> clickListener) {
        super(clickListener);
    }

    protected abstract class SelectionViewHolder extends ViewHolder {

        protected RadioButton radioButton;

        public SelectionViewHolder(ViewGroup parent, int res) {
            super(parent, res);
        }

        @CallSuper
        @Override
        protected void onBind(T item) {
            itemView.setActivated(isSelected());
            radioButton.setChecked(isSelected());
        }

        private boolean isSelected() {
            return selectedPosition == getAdapterPosition();
        }

        @Override
        public void onClick(View v) {
            super.onClick(v);

            if (selectedPosition != getAdapterPosition()) {
                int oldSelectedPosition = selectedPosition;
                selectedPosition = getAdapterPosition();
                notifyItemChanged(oldSelectedPosition);
                notifyItemChanged(selectedPosition);
            }
        }
    }
}
