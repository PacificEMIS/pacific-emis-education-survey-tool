package fm.doe.national.ui.screens.settings.items;

import androidx.annotation.Nullable;

import com.omega_r.libs.omegatypes.Image;
import com.omega_r.libs.omegatypes.Text;

public final class Item {

    private final Text title;

    @Nullable
    private Image customImage;

    @Nullable
    private Text value;

    private final Type type;

    private final IconType iconType;

    public Item(Text title, Type type, IconType iconType) {
        this.title = title;
        this.type = type;
        this.iconType = iconType;
    }

    public Text getTitle() {
        return title;
    }

    @Nullable
    public Image getCustomImage() {
        return customImage;
    }

    @Nullable
    public Text getValue() {
        return value;
    }

    public Type getType() {
        return type;
    }

    public IconType getIconType() {
        return iconType;
    }

    public void setCustomImage(@Nullable Image customImage) {
        this.customImage = customImage;
    }

    public void setValue(@Nullable Text value) {
        this.value = value;
    }

    public enum Type {
        DEBUG_STORAGE, CONTACT, CONTEXT, EXPORT_FOLDER, IMPORT_SCHOOLS, LOGO, OPERATING_MODE, NAME, PASSWORD, TEMPLATES
    }

    public enum IconType {
        NAV, VALUE, RECEIVE
    }
}
