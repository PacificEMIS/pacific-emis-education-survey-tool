package fm.doe.national.ui.custom_views.summary_header;

import android.graphics.PorterDuff;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.omega_r.libs.omegarecyclerview.BaseListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.doe.national.R;
import fm.doe.national.app_support.utils.IntRange;
import fm.doe.national.ui.screens.report.ReportLevel;

public class LegendAdapter extends BaseListAdapter<ReportLevel> {

    @Override
    protected ViewHolder provideViewHolder(ViewGroup parent) {
        return new ItemViewHolder(parent);
    }

    class ItemViewHolder extends ViewHolder {

        @BindView(R.id.imageview_icon)
        ImageView iconImageView;

        @BindView(R.id.textview_name)
        TextView nameTextView;

        @BindView(R.id.textview_description)
        TextView desctiptionTextView;

        public ItemViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_level_legend);
            ButterKnife.bind(this, itemView);
        }

        @Override
        protected void onBind(ReportLevel item) {
            iconImageView.setColorFilter(ContextCompat.getColor(getContext(), item.getColorRes()), PorterDuff.Mode.SRC_IN);
            nameTextView.setText(buildLevelName(item));
            desctiptionTextView.setText(buildLevelDescription(item));
        }

        private String buildLevelName(ReportLevel item) {
            StringBuilder stringBuilder = new StringBuilder()
                    .append(item.getName().getString(getContext()))
                    .append(" (");
            IntRange range = item.getRange();

            if (range.getEnd() < 100) {
                stringBuilder
                        .append(range.getStart())
                        .append("-")
                        .append(range.getEnd())
                        .append("%");
            } else {
                stringBuilder
                        .append(getString(R.string.above))
                        .append(" ")
                        .append(range.getStart() - 1)
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
