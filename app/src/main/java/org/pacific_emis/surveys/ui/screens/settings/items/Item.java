package org.pacific_emis.surveys.ui.screens.settings.items;

import androidx.annotation.Nullable;

import com.omega_r.libs.omegatypes.Text;
import com.omega_r.libs.omegatypes.image.Image;

public final class Item {

    private final Text title;

    @Nullable
    private Image customImage;

    @Nullable
    private Text textValue;

    private boolean booleanValue;

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
    public Text getTextValue() {
        return textValue;
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

    public void setTextValue(@Nullable Text value) {
        this.textValue = value;
    }

    public boolean getBooleanValue() {
        return booleanValue;
    }

    public void setBooleanValue(boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    public enum Type {
        DEBUG_STORAGE,
        CONTACT,
        CONTEXT,
        EXPORT_FOLDER,
        IMPORT_SCHOOLS,
        LOAD_SCHOOLS,
        LOGO,
        OPERATING_MODE,
        NAME,
        PASSWORD,
        TEMPLATES,
        REMOTE_SETTIGNS,
        DEBUG_BUILD_INFO,
        EXPORT_TO_EXCEL,
        LOAD_PROD_CERTIFICATE
    }

    public enum IconType {
        NAV, VALUE, RECEIVE, BOOLEAN
    }
}
