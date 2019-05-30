package fm.doe.national.accreditation.ui.navigation;

import com.omega_r.libs.omegatypes.Text;

public abstract class NavigationItem {

    private Text title;

    public NavigationItem(Text title) {
        this.title = title;
    }

    public Text getTitle() {
        return title;
    }
}
