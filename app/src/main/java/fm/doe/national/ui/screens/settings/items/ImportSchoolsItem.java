package fm.doe.national.ui.screens.settings.items;

import com.omega_r.libs.omegatypes.Image;
import com.omega_r.libs.omegatypes.Text;

import fm.doe.national.R;

public final class ImportSchoolsItem extends NavItem {

    public ImportSchoolsItem() {
        super(Text.from(R.string.label_import_schools), Image.from(R.drawable.ic_unarchive));
    }

}
