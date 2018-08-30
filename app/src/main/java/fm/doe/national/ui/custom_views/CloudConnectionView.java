package fm.doe.national.ui.custom_views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.omega_r.libs.omegatypes.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.doe.national.R;

public class CloudConnectionView extends CardView implements View.OnClickListener {

    @BindView(R.id.textview_title)
    TextView nameTextView;

    @BindView(R.id.imageview_icon)
    ImageView iconImageView;

    @BindView(R.id.textview_connect)
    TextView connectTextView;

    @Nullable
    private OnConnectClickListener listener = null;

    public void setListener(@Nullable OnConnectClickListener listener) {
        this.listener = listener;
    }

    public void setCloudName(Text cloudName) {
        nameTextView.setText(cloudName.getString(getResources()));
    }

    public void setIconDrawableId(int iconDrawableId) {
        iconImageView.setImageResource(iconDrawableId);
    }

    public CloudConnectionView(Context context) {
        this(context, null);
    }

    public CloudConnectionView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
        connectTextView.setOnClickListener(this);
    }

    public CloudConnectionView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
        inflate(context, R.layout.view_cloud_connection, this);
        ButterKnife.bind(this);
    }

    @Override
    public void onClick(View v) {
        if (listener != null) listener.onConnectClick(this);
    }

    public interface OnConnectClickListener {
        void onConnectClick(View v);
    }
}
