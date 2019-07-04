package fm.doe.national.core.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import fm.doe.national.core.R;

public class IconButton extends FrameLayout {

    private ImageView imageView;
    private TextView textView;

    public IconButton(Context context) {
        this(context, null);
    }

    public IconButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IconButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public IconButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        inflate(context, R.layout.view_icon_button, this);
        imageView = findViewById(R.id.imageview);
        textView = findViewById(R.id.textview);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.IconButton, defStyleAttr, defStyleRes);
        textView.setText(a.getString(R.styleable.IconButton_text));
        textView.setTextColor(a.getColor(R.styleable.IconButton_textColor, getResources().getColor(R.color.white, null)));
        Drawable drawable = (a.getDrawable(R.styleable.IconButton_icon));

        if (drawable != null) {
            imageView.setImageDrawable(drawable);
        } else {
            imageView.setVisibility(View.GONE);
        }

        a.recycle();
    }
}
