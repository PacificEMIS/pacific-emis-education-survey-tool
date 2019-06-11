package fm.doe.national.ui.screens.settings.items;

import androidx.annotation.NonNull;

import com.omega_r.libs.omegatypes.Text;

import fm.doe.national.R;

public final class ContextItem extends ValuableItem {

    public ContextItem(@NonNull Text value) {
        super(Text.from(R.string.label_context), null, value);
    }

}
