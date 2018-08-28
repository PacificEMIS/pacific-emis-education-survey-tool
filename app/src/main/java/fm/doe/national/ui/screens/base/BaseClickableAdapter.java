package fm.doe.national.ui.screens.base;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseClickableAdapter<T, VH extends BaseRecyclerViewHolder<T>> extends RecyclerView.Adapter<VH> {

    private List<T> items = new ArrayList<>();

    @Nullable
    private OnRecyclerItemClickListener<T> listener = null;

    public void setItems(@NonNull List<T> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void setListener(@Nullable OnRecyclerItemClickListener<T> listener) {
        this.listener = listener;
    }

    public void onItemClick(T item) {
        if (listener != null) {
            listener.onRecyclerItemClick(item);
        }
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return provideViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    protected abstract VH provideViewHolder(ViewGroup parent);

    public interface OnRecyclerItemClickListener<T> {
        void onRecyclerItemClick(T item);
    }
}
