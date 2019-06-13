package fm.doe.national.ui.screens.settings.items;

import androidx.annotation.Nullable;

import com.omega_r.libs.omegatypes.Image;
import com.omega_r.libs.omegatypes.Text;

public final class Item {

    private final Text title;

    @Nullable
    private final Image icon;

    @Nullable
    private Image customImage;

    @Nullable
    private Text value;

    private final Type type;

    private final IconType iconType;

    public Item(Text title, @Nullable Image icon, Type type, IconType iconType) {
        this.title = title;
        this.icon = icon;
        this.type = type;
        this.iconType = iconType;
    }

    public Text getTitle() {
        return title;
    }

    @Nullable
    public Image getIcon() {
        return icon;
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
        ACCOUNT, CONTACT, CONTEXT, EXPORT_FOLDER, IMPORT_SCHOOLS, LOGO, OP_MODE, NAME, PASSWORD, TEMPLATES
    }

    public enum IconType {
        NAV, VALUE
    }
}
