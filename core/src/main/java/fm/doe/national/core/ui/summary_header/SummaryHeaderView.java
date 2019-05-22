package fm.doe.national.core.ui.summary_header;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import fm.doe.national.core.R;
import fm.doe.national.core.data.model.Level;
import fm.doe.national.core.data.model.ReportLevel;
import fm.doe.national.core.utils.DateUtils;

public class SummaryHeaderView extends LinearLayout {

    private final LegendAdapter legendAdapter = new LegendAdapter(getContext());

    TextView schoolIdTextView;
    TextView visitDateTextView;
    TextView schoolNameTextView;
    TextView principalNameTextView;
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
        bindViews();
        legendRecyclerView.setAdapter(legendAdapter);
    }

    private void bindViews() {
        schoolIdTextView = findViewById(R.id.textview_school_code);
        visitDateTextView = findViewById(R.id.textview_visit_date);
        schoolNameTextView = findViewById(R.id.textview_school_name);
        principalNameTextView = findViewById(R.id.textview_principal_name);
        legendRecyclerView = findViewById(R.id.recyclerview_levels);
    }

    public void setItem(Item item) {
        schoolIdTextView.setText(item.getSchoolId());
        schoolNameTextView.setText(item.getSchoolName());
        principalNameTextView.setText(item.getPrincipalName());
        visitDateTextView.setText(DateUtils.format(item.getDate()));
        try {
            legendAdapter.setItems(item.getLevels().stream().map(level -> (ReportLevel) level).collect(Collectors.toList()));
        } catch (ClassCastException cce) {
            // do nothing
        }
    }

    public static class Item {
        private String schoolId;
        private String schoolName;
        private Date date;
        private String principalName;
        private List<Level> levels;

        public static Item empty() {
            return new Item();
        }

        private Item() {
        }

        public Item(String schoolId, String schoolName, Date date, String principalName, List<Level> levels) {
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

        public List<Level> getLevels() {
            return levels;
        }

        public boolean isEmpty() {
            return schoolId == null;
        }
    }

}
