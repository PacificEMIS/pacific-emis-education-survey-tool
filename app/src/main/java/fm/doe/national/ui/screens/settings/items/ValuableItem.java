package fm.doe.national.ui.screens.settings.items;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.omega_r.libs.omegatypes.Image;
import com.omega_r.libs.omegatypes.Text;

public abstract class ValuableItem extends Item {

    @NonNull
    private Text value;

    public ValuableItem(Text title, @Nullable Image icon, @NonNull Text value) {
        super(title, icon);
        this.value = value;
    }

    @NonNull
    public Text getValue() {
        return value;
    }

    public void setValue(@NonNull Text value) {
        this.value = value;
    }
}
