package fm.doe.national.ui.screens.base;

import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.omega_r.libs.omegatypes.Text;

/**
 * Created by Alexander Chibirev on 8/10/2018.
 */

public class BaseFragment extends MvpAppCompatFragment implements BaseView {

    @Override
    public void showToast(Text text) {
        Toast.makeText(getContext(), text.getString(getResources()), Toast.LENGTH_SHORT).show();
    }

}
