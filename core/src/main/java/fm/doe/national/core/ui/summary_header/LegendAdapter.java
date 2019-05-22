package fm.doe.national.core.ui.summary_header;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.ViewGroup;
import android.widget.TextView;

import com.omega_r.libs.omegarecyclerview.BaseListAdapter;

import java.util.HashMap;
import java.util.Map;

import fm.doe.national.core.R;
import fm.doe.national.core.data.model.ReportLevel;

public class LegendAdapter extends BaseListAdapter<ReportLevel> {

    private static final Map<ReportLevel, ColorStateList> sColorStateListMap = new HashMap<>();

    public LegendAdapter(Context context) {
        for (ReportLevel value : ReportLevel.values()) {
            sColorStateListMap.put(value, ColorStateList.valueOf(context.getColor(value.getColorRes())));
        }
    }

    @Override
    protected ViewHolder provideViewHolder(ViewGroup parent) {
        return new ItemViewHolder(parent);
    }

    class ItemViewHolder extends ViewHolder {

        TextView nameTextView;
        TextView descriptionTextView;

        public ItemViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_level_legend);
            bindViews();
        }

        private void bindViews() {
            nameTextView = findViewById(R.id.textview_name);
            descriptionTextView = findViewById(R.id.textview_description);
        }

        @Override
        protected void onBind(ReportLevel item) {
            nameTextView.setCompoundDrawableTintList(sColorStateListMap.get(item));
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
