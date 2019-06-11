package fm.doe.national.ui.screens.settings.items;

import androidx.annotation.Nullable;

import com.omega_r.libs.omegatypes.Image;
import com.omega_r.libs.omegatypes.Text;

public abstract class Item {

    private final Text title;

    @Nullable
    private final Image icon;

    public Item(Text title, @Nullable Image icon) {
        this.title = title;
        this.icon = icon;
    }

    public Text getTitle() {
        return title;
    }


    @Nullable
    public Image getIcon() {
        return icon;
    }
}
