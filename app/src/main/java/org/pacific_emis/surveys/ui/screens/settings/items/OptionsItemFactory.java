package org.pacific_emis.surveys.ui.screens.settings.items;

import com.omega_r.libs.omegatypes.Text;

import org.pacific_emis.surveys.BuildConfig;
import org.pacific_emis.surveys.R;

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

    public Item createLoadSchoolsItem(Text contextName) {
        return new Item(
                Text.from(R.string.label_load_schools).plus(" ").plus(contextName).plus(" API"),
                Item.Type.LOAD_SCHOOLS,
                Item.IconType.RECEIVE
        );
    }

    public Item createLoadTeachersItem(Text contextName) {
        return new Item(
                Text.from(R.string.label_load_teachers).plus(" ").plus(contextName).plus(" API"),
                Item.Type.LOAD_TEACHERS,
                Item.IconType.RECEIVE
        );
    }

    public Item createLoadSubjectsItem(Text contextName) {
        return new Item(
                Text.from(R.string.label_load_subjects).plus(" ").plus(contextName).plus(" API"),
                Item.Type.LOAD_SUBJECTS,
                Item.IconType.RECEIVE
        );
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

    public Item createForceFetchRemoteSettingsItem() {
        return new Item(Text.from(R.string.label_force_remote_settings), Item.Type.REMOTE_SETTIGNS, Item.IconType.NAV);
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
                Item.IconType.BOOLEAN
        );
        item.setBooleanValue(enabled);
        return item;
    }

    public Item createLoadProdCertificateItem() {
        return new Item(Text.from(R.string.label_load_prod_certificate), Item.Type.LOAD_PROD_CERTIFICATE, Item.IconType.RECEIVE);
    }
}
