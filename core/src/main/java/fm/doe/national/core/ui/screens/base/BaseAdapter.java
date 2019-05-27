package fm.doe.national.core.ui.screens.base;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public abstract class BaseAdapter<T>
        extends OmegaRecyclerView.Adapter<BaseAdapter<T>.ViewHolder> {

    private List<T> items = new ArrayList<>();

    @Nullable
    private OnItemClickListener<T> clickListener = null;

    @Nullable
    private OnItemLongClickListener<T> longClickListener = null;

    public BaseAdapter(OnItemClickListener<T> clickListener, OnItemLongClickListener<T> longClickListener) {
        this(clickListener);
        this.longClickListener = longClickListener;
    }

    public BaseAdapter(OnItemClickListener<T> clickListener) {
        this.clickListener = clickListener;
    }

    public BaseAdapter() {
    }

    public void setClickListener(@Nullable OnItemClickListener<T> clickListener) {
        this.clickListener = clickListener;
        notifyDataSetChanged();
    }

    public void setLongClickListener(@Nullable OnItemLongClickListener<T> longClickListener) {
        this.longClickListener = longClickListener;
        notifyDataSetChanged();
    }

    public void setItems(@NonNull List<T> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void removeItem(@NonNull T item) {
        if (getItemCount() == 0) {
            return;
        }

        int index = items.indexOf(item);
        if (index != RecyclerView.NO_POSITION) {
            items.remove(index);
            notifyItemRemoved(index);
        }
    }

    protected T getItem(int position) {
        return items.get(position);
    }

    protected List<T> getItems() {
        return items;
    }

    protected void onItemClick(T item) {
        if (clickListener != null) {
            clickListener.onItemClick(item);
        }
    }

    protected void onLongItemClick(T item) {
        if (longClickListener != null) {
            longClickListener.onItemLongClick(item);
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

    public abstract class ViewHolder extends OmegaRecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener  {

        private T item;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

        public ViewHolder(ViewGroup parent, int res) {
            super(parent, res);
            ButterKnife.bind(this, itemView);
        }

        public void bind(T item) {
            this.item = item;

            if (clickListener != null) {
                itemView.setOnClickListener(this);
            }
            if (longClickListener != null) {
                itemView.setOnLongClickListener(this);
            }

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

        @Override
        public boolean onLongClick(View v) {
            onLongItemClick(item);
            return true;
        }
    }

    public interface OnItemClickListener<T> {
        void onItemClick(T item);
    }

    public interface OnItemLongClickListener<T> {
        void onItemLongClick(T item);
    }
}
