package fm.doe.national.ui.screens.settings.items;

import com.omega_r.libs.omegatypes.Image;
import com.omega_r.libs.omegatypes.Text;

import fm.doe.national.R;

public class OptionsItemFactory {

    public Item createAccountItem(Text displayName) {
        Item item = new Item(Text.from(R.string.label_account), Image.from(R.drawable.ic_user), Item.Type.ACCOUNT, Item.IconType.VALUE);
        item.setValue(displayName);
        return item;
    }

    public Item createContactItem(Text displayName) {
        Item item = new Item(Text.from(R.string.label_contact), null, Item.Type.CONTACT, Item.IconType.VALUE);
        item.setValue(displayName);
        return item;
    }

    public Item createContextItem(Text contextName) {
        Item item = new Item(Text.from(R.string.label_context), null, Item.Type.CONTEXT, Item.IconType.VALUE);
        item.setValue(contextName);
        return item;
    }

    public Item createExportFolderItem(Text currentFolderName) {
        Item item = new Item(Text.from(R.string.label_export_folder), Image.from(R.drawable.ic_folder), Item.Type.EXPORT_FOLDER, Item.IconType.VALUE);
        item.setValue(currentFolderName);
        return item;
    }

    public Item createImportSchoolsItem() {
        return new Item(Text.from(R.string.label_import_schools), Image.from(R.drawable.ic_unarchive), Item.Type.IMPORT_SCHOOLS, Item.IconType.NAV);
    }

    public Item createLogoItem(Image currentLogo) {
        Item item = new Item(Text.from(R.string.label_logo), null, Item.Type.LOGO, Item.IconType.NAV);
        item.setCustomImage(currentLogo);
        return item;
    }

    public Item createOpModeItem(Text currentMode) {
        Item item = new Item(Text.from(R.string.label_mode), null, Item.Type.OP_MODE, Item.IconType.VALUE);
        item.setValue(currentMode);
        return item;
    }

    public Item createNameItem(Text displayName) {
        Item item = new Item(Text.from(R.string.label_name), null, Item.Type.NAME, Item.IconType.VALUE);
        item.setValue(displayName);
        return item;
    }

    public Item createPasswordItem() {
        return new Item(Text.from(R.string.label_change_password), Image.from(R.drawable.ic_lock), Item.Type.PASSWORD, Item.IconType.NAV);
    }

    public Item createTemplatesItem() {
        return new Item(Text.from(R.string.label_templates), null, Item.Type.PASSWORD, Item.IconType.NAV);
    }
}
