package fm.doe.national.ui.screens.menu;

import android.view.ViewGroup;
import android.widget.TextView;

import com.omega_r.libs.omegatypes.Text;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.ui.screens.base.BaseAdapter;

public class MenuListAdapter extends BaseAdapter<Text> {

    @Override
    protected MenuItemViewHolder provideViewHolder(ViewGroup parent) {
        return new MenuItemViewHolder(parent);
    }

    public class MenuItemViewHolder extends ViewHolder {

        @BindView(R.id.textview_name)
        TextView typeTestTextView;

        MenuItemViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_menuitem);
        }

        @Override
        public void onBind(Text item) {
            typeTestTextView.setText(item.getString(getResources()));
            itemView.setOnClickListener(this);
        }

    }
}
