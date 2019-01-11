package fm.doe.national.ui.screens.base;

import android.os.Bundle;
import android.view.View;

import com.arellomobile.mvp.MvpAppCompatDialogFragmentX;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.ButterKnife;

public class BaseDialogFragment extends MvpAppCompatDialogFragmentX {
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }
}
