package org.pacific_emis.surveys.report_core.ui.level_legend;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.stream.Collectors;

import org.pacific_emis.surveys.report_core.R;
import org.pacific_emis.surveys.report_core.domain.ReportLevel;
import org.pacific_emis.surveys.report_core.model.Level;

public class LevelLegendView extends LinearLayout {

    private final LegendAdapter legendAdapter = new LegendAdapter(getContext());
    private RecyclerView legendRecyclerView;

    public LevelLegendView(Context context) {
        this(context, null, 0);
    }

    public LevelLegendView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LevelLegendView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.view_level_legend, this);
        bindViews();
        legendRecyclerView.setAdapter(legendAdapter);
    }

    private void bindViews() {
        legendRecyclerView = findViewById(R.id.recyclerview_levels);
    }

    public void setItem(Item item) {
        if (item.getLevels() == null || item.getLevels().isEmpty()) {
            legendRecyclerView.setVisibility(View.GONE);
        } else {
            legendRecyclerView.setVisibility(View.VISIBLE);
            try {
                legendAdapter.setItems(item.getLevels().stream().map(level -> (ReportLevel) level).collect(Collectors.toList()));
            } catch (ClassCastException cce) {
                // do nothing
            }
        }
    }

    public static class Item {
        private String schoolId;
        private String schoolName;
        private String surveyTag;
        private String principalName;
        private List<Level> levels;

        public static Item empty() {
            return new Item();
        }

        private Item() {
        }

        public Item(String schoolId, String schoolName, String surveyTag, String principalName, List<Level> levels) {
            this.schoolId = schoolId;
            this.schoolName = schoolName;
            this.surveyTag = surveyTag;
            this.principalName = principalName;
            this.levels = levels;
        }

        public String getSchoolId() {
            return schoolId;
        }

        public String getSchoolName() {
            return schoolName;
        }

        public String getSurveyTag() {
            return surveyTag;
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
