package fm.doe.national.ui.screens.settings.items;

import androidx.annotation.Nullable;

import com.omega_r.libs.omegatypes.Image;
import com.omega_r.libs.omegatypes.Text;

public abstract class NavItem extends Item {
    public NavItem(Text title, @Nullable Image icon) {
        super(title, icon);
    }
}
