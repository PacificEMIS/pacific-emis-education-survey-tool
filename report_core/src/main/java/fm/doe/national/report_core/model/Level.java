package fm.doe.national.report_core.model;

import androidx.annotation.ColorRes;

import com.omega_r.libs.omegatypes.Text;

public interface Level {
    @ColorRes
    int getColorRes();

    Text getName();
}
