package fm.doe.national.ui.custom_views.summary_header;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.omega_r.libs.omegarecyclerview.BaseListAdapter;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.doe.national.R;
import fm.doe.national.ui.screens.report.ReportLevel;

public class LegendAdapter extends BaseListAdapter<ReportLevel> {

    private static final Map<ReportLevel, ColorStateList> COLOR_STATE_LIST_MAP = new HashMap<>();

    public LegendAdapter(Context context) {
        for (ReportLevel value : ReportLevel.values()) {
            COLOR_STATE_LIST_MAP.put(value, ColorStateList.valueOf( ContextCompat.getColor(context, value.getColorRes())));
        }
    }

    @Override
    protected ViewHolder provideViewHolder(ViewGroup parent) {
        return new ItemViewHolder(parent);
    }

    class ItemViewHolder extends ViewHolder {

        @BindView(R.id.textview_name)
        TextView nameTextView;

        @BindView(R.id.textview_description)
        TextView descriptionTextView;

        public ItemViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_level_legend);
            ButterKnife.bind(this, itemView);
        }

        @Override
        protected void onBind(ReportLevel item) {
            nameTextView.setCompoundDrawableTintList(COLOR_STATE_LIST_MAP.get(item));
            nameTextView.setText(buildLevelName(item));
            descriptionTextView.setText(buildLevelDescription(item));
        }

        private String buildLevelName(ReportLevel item) {
            StringBuilder stringBuilder = new StringBuilder()
                    .append(item.getName().getString(getContext()))
                    .append(" (");

            if (item.getMaxValue() < 100) {
                stringBuilder
                        .append(item.getMinValue())
                        .append("-")
                        .append(item.getMaxValue())
                        .append("%");
            } else {
                stringBuilder
                        .append(getString(R.string.above))
                        .append(" ")
                        .append(item.getMinValue() - 1)
                        .append("%");
            }

            stringBuilder.append(")");
            return stringBuilder.toString();
        }

        private String buildLevelDescription(ReportLevel item) {
            return item.getMeaning().getString(getContext()) + " (" + item.getAwards().getString(getContext()) + ")";
        }
    }
}
