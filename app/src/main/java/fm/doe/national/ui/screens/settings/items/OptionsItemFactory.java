package fm.doe.national.ui.screens.settings.items;

import com.omega_r.libs.omegatypes.Text;

import fm.doe.national.BuildConfig;
import fm.doe.national.R;

public class OptionsItemFactory {

    public Item createDebugStorageItem() {
        return new Item(Text.from(R.string.label_debug_storage), Item.Type.DEBUG_STORAGE, Item.IconType.NAV);
    }

    public Item createContactItem(Text displayName) {
        Item item = new Item(Text.from(R.string.label_contact), Item.Type.CONTACT, Item.IconType.VALUE);
        item.setTextValue(displayName);
        return item;
    }

    public Item createContextItem(Text contextName) {
        Item item = new Item(Text.from(R.string.label_context), Item.Type.CONTEXT, Item.IconType.VALUE);
        item.setTextValue(contextName);
        return item;
    }

    public Item createImportSchoolsItem() {
        return new Item(Text.from(R.string.label_import_schools), Item.Type.IMPORT_SCHOOLS, Item.IconType.RECEIVE);
    }

    public Item createLogoItem() {
        return new Item(Text.from(R.string.label_logo), Item.Type.LOGO, Item.IconType.NAV);
    }

    public Item createOpModeItem(Text currentMode) {
        Item item = new Item(Text.from(R.string.label_mode), Item.Type.OPERATING_MODE, Item.IconType.VALUE);
        item.setTextValue(currentMode);
        return item;
    }

    public Item createNameItem(Text displayName) {
        Item item = new Item(Text.from(R.string.label_name), Item.Type.NAME, Item.IconType.VALUE);
        item.setTextValue(displayName);
        return item;
    }

    public Item createPasswordItem() {
        return new Item(Text.from(R.string.label_change_password), Item.Type.PASSWORD, Item.IconType.NAV);
    }

    public Item createTemplatesItem() {
        return new Item(Text.from(R.string.label_templates), Item.Type.TEMPLATES, Item.IconType.NAV);
    }

    public Item createDebugBuildInfoItem() {
        return new Item(
                Text.from(R.string.label_format_debug_info, BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE),
                Item.Type.DEBUG_BUILD_INFO,
                Item.IconType.VALUE
        );
    }

    public Item createExportToExcelItem(boolean enabled) {
        Item item = new Item(
                Text.from(R.string.label_ability_to_export),
                Item.Type.EXPORT_TO_EXCEL,
                Item.IconType.BOOL
        );
        item.setBooleanValue(enabled);
        return item;
    }
}
