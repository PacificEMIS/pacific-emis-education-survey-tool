package fm.doe.national.ui.screens.settings.items;

import androidx.annotation.NonNull;

import com.omega_r.libs.omegatypes.Text;

import fm.doe.national.R;

public final class NameItem extends ValuableItem {

    public NameItem(@NonNull Text value) {
        super(Text.from(R.string.label_name), null, value);
    }

}
