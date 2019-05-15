package fm.doe.national.ui.screens.base;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.omegar.mvp.MvpAppCompatDialogFragment;

import butterknife.ButterKnife;

public class BaseDialogFragment extends MvpAppCompatDialogFragment {
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }
}
