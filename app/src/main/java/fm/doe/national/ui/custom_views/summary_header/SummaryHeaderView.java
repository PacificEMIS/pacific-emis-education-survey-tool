package fm.doe.national.ui.custom_views.summary_header;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.doe.national.R;
import fm.doe.national.app_support.utils.DateUtils;
import fm.doe.national.ui.screens.report.ReportLevel;

public class SummaryHeaderView extends LinearLayout {

    private final LegendAdapter legendAdapter = new LegendAdapter();

    @BindView(R.id.textview_school_code)
    TextView schoolIdTextView;

    @BindView(R.id.textview_visit_date_code)
    TextView visitDateTextView;

    @BindView(R.id.textview_school_name)
    TextView schoolNameTextView;

    @BindView(R.id.textview_principal_name)
    TextView principalNameTextView;

    @BindView(R.id.recyclerview_levels)
    RecyclerView legendRecyclerView;

    public SummaryHeaderView(Context context) {
        this(context, null, 0);
    }

    public SummaryHeaderView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SummaryHeaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inflate(context, R.layout.view_summary_header, this);
        ButterKnife.bind(this);

        legendRecyclerView.setAdapter(legendAdapter);
    }

    public void setItem(Item item) {
        schoolIdTextView.setText(item.getSchoolId());
        schoolNameTextView.setText(item.getSchoolName());
        principalNameTextView.setText(item.getPrincipalName());
        visitDateTextView.setText(DateUtils.format(item.getDate()));
        legendAdapter.setItems(item.getLevels());
    }

    public static class Item {
        private String schoolId;
        private String schoolName;
        private Date date;
        private String principalName;
        private List<ReportLevel> levels;

        public static Item empty() {
            return new Item();
        }

        private Item() {
        }

        public Item(String schoolId, String schoolName, Date date, String principalName, List<ReportLevel> levels) {
            this.schoolId = schoolId;
            this.schoolName = schoolName;
            this.date = date;
            this.principalName = principalName;
            this.levels = levels;
        }

        public String getSchoolId() {
            return schoolId;
        }

        public String getSchoolName() {
            return schoolName;
        }

        public Date getDate() {
            return date;
        }

        public String getPrincipalName() {
            return principalName;
        }

        public List<ReportLevel> getLevels() {
            return levels;
        }

        public boolean isEmpty() {
            return schoolId == null;
        }
    }

}
