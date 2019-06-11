package fm.doe.national.ui.screens.settings.items;

import androidx.annotation.Nullable;

import com.omega_r.libs.omegatypes.Text;

import fm.doe.national.R;

public final class LogoItem extends NavItem {

    @Nullable
    private String imagePath;

    public LogoItem(@Nullable String imagePath) {
        super(Text.from(R.string.label_logo), null);
        this.imagePath = imagePath;
    }

    @Nullable
    public String getImagePath() {
        return imagePath;
    }

}
