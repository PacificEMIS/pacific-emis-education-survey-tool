package fm.doe.national.ui.screens.settings.items;

import androidx.annotation.NonNull;

import com.omega_r.libs.omegatypes.Image;
import com.omega_r.libs.omegatypes.Text;

import fm.doe.national.R;

public final class AccountItem extends ValuableItem {

    public AccountItem(@NonNull Text value) {
        super(Text.from(R.string.label_account), Image.from(R.drawable.ic_user), value);
    }

}
