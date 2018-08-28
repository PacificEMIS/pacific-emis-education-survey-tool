package fm.doe.national.ui.screens.menu;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.omega_r.libs.omegatypes.Text;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.ui.screens.base.BaseClickableAdapter;
import fm.doe.national.ui.screens.base.BaseRecyclerViewHolder;

public class MenuAdapter extends BaseClickableAdapter<Text, MenuAdapter.MenuItemViewHolder> {

    @Override
    protected MenuItemViewHolder provideViewHolder(ViewGroup parent) {
        return new MenuItemViewHolder(parent);
    }

    class MenuItemViewHolder extends BaseRecyclerViewHolder<Text> implements View.OnClickListener {

        @BindView(R.id.textview_name)
        TextView typeTestTextView;

        MenuItemViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_menuitem);
        }

        @Override
        public void onClick(View v) {
            onItemClick(item);
        }

        @Override
        public void onBind() {
            typeTestTextView.setText(item.getString(getResources()));
            itemView.setOnClickListener(this);
        }

    }
}
