package fm.doe.national.ui.screens.settings.items;

import androidx.annotation.NonNull;

import com.omega_r.libs.omegatypes.Image;
import com.omega_r.libs.omegatypes.Text;

import fm.doe.national.R;

public final class ExportFolderItem extends ValuableItem {

    public ExportFolderItem(@NonNull Text value) {
        super(Text.from(R.string.label_export_folder), Image.from(R.drawable.ic_folder), value);
    }

}
