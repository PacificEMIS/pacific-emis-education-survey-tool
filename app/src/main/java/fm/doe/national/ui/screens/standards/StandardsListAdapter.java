package fm.doe.national.ui.screens.standards;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import fm.doe.national.R;
import fm.doe.national.data.model.mutable.MutableStandard;
import fm.doe.national.domain.model.Progress;
import fm.doe.national.ui.screens.base.BaseAdapter;
import fm.doe.national.utils.ViewUtils;

public class StandardsListAdapter extends BaseAdapter<MutableStandard> {

    public StandardsListAdapter(OnItemClickListener<MutableStandard> clickListener) {
        super(clickListener);
    }

    @Override
    protected StandardViewHolder provideViewHolder(ViewGroup parent) {
        return new StandardViewHolder(parent);
    }

    protected class StandardViewHolder extends ViewHolder implements View.OnClickListener {

        @BindView(R.id.imageview_standard_icon)
        ImageView standardIconImageView;

        @BindView(R.id.textview_standard_name)
        TextView standardNameTextView;

        @BindView(R.id.textview_progress)
        TextView progressTextView;

        @BindView(R.id.progressbar)
        ProgressBar progressBar;

        StandardViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_standard);
        }

        @Override
        public void onBind(MutableStandard item) {
            Integer icon = ViewUtils.getStandardIconRes(item.getSuffix());
            if (icon != null) {
                standardIconImageView.setImageResource(icon);
                standardIconImageView.setActivated(true);
            }

            String standardPrefix = getString(R.string.format_standard, item.getSuffix());
            SpannableString spannableString = new SpannableString(standardPrefix + " " + item.getTitle());
            spannableString.setSpan(
                    new TypefaceSpan(getString(R.string.font_normal)),
                    0, standardPrefix.length(),
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            standardNameTextView.setText(spannableString);

            Progress categoryProgress = item.getProgress();
            ViewUtils.rebindProgress(
                    categoryProgress.total,
                    categoryProgress.completed,
                    getString(R.string.criteria_progress),
                    progressTextView, progressBar);

            itemView.setOnClickListener(this);
        }

    }
}