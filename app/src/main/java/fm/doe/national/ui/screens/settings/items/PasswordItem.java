package fm.doe.national.ui.screens.settings.items;

import com.omega_r.libs.omegatypes.Image;
import com.omega_r.libs.omegatypes.Text;

import fm.doe.national.R;

public final class PasswordItem extends NavItem {

    public PasswordItem() {
        super(Text.from(R.string.label_change_password), Image.from(R.drawable.ic_lock));
    }

}
