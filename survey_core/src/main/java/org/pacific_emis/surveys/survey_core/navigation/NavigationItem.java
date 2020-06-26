package org.pacific_emis.surveys.survey_core.navigation;

import com.omega_r.libs.omegatypes.Text;

public abstract class NavigationItem {

    public static final long NO_ID = -1;

    private Text title;
    private long id;

    public NavigationItem(Text title, long id) {
        this.title = title;
        this.id = id;
    }

    public Text getTitle() {
        return title;
    }

    public long getId() {
        return id;
    }
}
