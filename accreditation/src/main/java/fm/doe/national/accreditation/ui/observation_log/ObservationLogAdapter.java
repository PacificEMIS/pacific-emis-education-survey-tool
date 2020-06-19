package fm.doe.national.accreditation.ui.observation_log;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.omega_r.libs.omegarecyclerview.BaseListAdapter;
import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;
import com.omega_r.libs.omegarecyclerview.sticky_decoration.StickyAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import fm.doe.national.accreditation.R;
import fm.doe.national.core.ui.views.InputFieldLayout;

public class ObservationLogAdapter extends BaseListAdapter<RecordViewData>
        implements StickyAdapter<ObservationLogAdapter.HeaderViewHolder> {

    @SuppressLint("ConstantLocale")
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("hh:mm a", Locale.US);

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecordViewHolder(parent);
    }

    @Override
    public long getStickyId(int position) {
        return 0;
    }

    @Override
    public HeaderViewHolder onCreateStickyViewHolder(ViewGroup parent) {
        return new HeaderViewHolder(parent);
    }

    @Override
    public void onBindStickyViewHolder(HeaderViewHolder viewHolder, int position) {
        // nothing
    }

    class RecordViewHolder extends ViewHolder implements InputFieldLayout.OnDonePressedListener {

        private TextView timeTextView;
        private InputFieldLayout teacherInputFieldLayout;
        private InputFieldLayout studentsInputFieldLayout;
        private ImageButton deleteImageButton;

        public RecordViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_observation_log_record);
            bindViews();
            setupActions();
        }

        private void bindViews() {
            timeTextView = findViewById(R.id.textview_time);
            teacherInputFieldLayout = findViewById(R.id.inputfieldlayout_teacher_action);
            studentsInputFieldLayout = findViewById(R.id.inputfieldlayout_students_action);
            deleteImageButton = findViewById(R.id.imagebutton_delete);
        }

        private void setupActions() {
            deleteImageButton.setOnClickListener(this);
            timeTextView.setOnClickListener(this);
            teacherInputFieldLayout.setOnDonePressedListener(this);
            studentsInputFieldLayout.setOnDonePressedListener(this);
        }

        @Override
        protected void onBind(RecordViewData item) {
            timeTextView.setText(DATE_FORMAT.format(item.getDate()));
            teacherInputFieldLayout.setStartingText(item.getTeacherActions());
            studentsInputFieldLayout.setStartingText(item.getStudentsActions());
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.imagebutton_delete) {

            } else if (v.getId() == R.id.textview_time) {

            } else {
                super.onClick(v);
            }
        }

        @Override
        public void onDonePressed(View view, @Nullable String content) {
            if (view.getId() == R.id.inputfieldlayout_teacher_action) {

            } else if (view.getId() == R.id.inputfieldlayout_students_action) {

            }
        }
    }

    static class HeaderViewHolder extends OmegaRecyclerView.ViewHolder {
        public HeaderViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_observation_log_header);
        }
    }
}
