package fm.doe.national.ui.screens.base;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public abstract class BaseAdapter<T>
        extends OmegaRecyclerView.Adapter<BaseAdapter<T>.ViewHolder> {

    private List<T> items = new ArrayList<>();

    @Nullable
    private OnItemClickListener<T> listener = null;

    public void setItems(@NonNull List<T> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    protected T getItem(int position) {
        return items.get(position);
    }

    protected List<T> getItems() {
        return items;
    }

    public void setListener(@Nullable OnItemClickListener<T> listener) {
        this.listener = listener;
    }

    protected void onItemClick(T item) {
        if (listener != null) {
            listener.onItemClick(item);
        }
    }

    @NonNull
    @Override
    public BaseAdapter<T>.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return provideViewHolder(parent);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(@NonNull BaseAdapter<T>.ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    protected abstract BaseAdapter<T>.ViewHolder provideViewHolder(ViewGroup parent);

    public abstract class ViewHolder extends OmegaRecyclerView.ViewHolder implements View.OnClickListener  {

        private T item;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

        public ViewHolder(ViewGroup parent, int res) {
            super(parent, res);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void bind(T item) {
            this.item = item;
            onBind(item);
        }

        protected abstract void onBind(T item);

        @NonNull
        protected T getItem() {
           return item;
        }

        @Override
        public void onClick(View v) {
            onItemClick(item);
        }
    }

    public interface OnItemClickListener<T> {
        void onItemClick(T item);
    }
}
