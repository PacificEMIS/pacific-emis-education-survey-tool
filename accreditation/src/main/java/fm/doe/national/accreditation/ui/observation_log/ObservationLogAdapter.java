package fm.doe.national.accreditation.ui.observation_log;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView;
import com.omega_r.libs.omegarecyclerview.sticky_decoration.StickyAdapter;
import com.omega_r.libs.omegatypes.Size;
import com.omega_r.libs.omegatypes.Text;
import com.omega_r.libs.omegatypes.TextKt;
import com.omega_r.libs.omegatypes.TextStyle;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import fm.doe.national.accreditation.R;
import fm.doe.national.accreditation_core.data.model.mutable.MutableObservationLogRecord;
import fm.doe.national.core.ui.views.InputFieldLayout;
import fm.doe.national.core.utils.ViewUtils;

public class ObservationLogAdapter extends ListAdapter<MutableObservationLogRecord, ObservationLogAdapter.RecordViewHolder>
        implements StickyAdapter<ObservationLogAdapter.HeaderViewHolder> {

    @SuppressLint("ConstantLocale")
    private static final DateFormat DATE_HOURS_MINUTES_FORMAT = new SimpleDateFormat("hh:mm", Locale.US);
    private static final DateFormat DATE_AM_PM_FORMAT = new SimpleDateFormat(" a", Locale.US);

    @NonNull
    private Listener listener;

    public ObservationLogAdapter(@NonNull DiffUtil.ItemCallback<MutableObservationLogRecord> diffCallback, @NonNull Listener listener) {
        super(diffCallback);
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecordViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        holder.onBind(getItem(position));
    }

    @Override
    public long getStickyId(int position) {
        return 0; // just a simple header for all elements
    }

    @Override
    public HeaderViewHolder onCreateStickyViewHolder(ViewGroup parent) {
        return new HeaderViewHolder(parent);
    }

    @Override
    public void onBindStickyViewHolder(HeaderViewHolder viewHolder, int position) {
        // nothing
    }

    class RecordViewHolder extends OmegaRecyclerView.ViewHolder implements
            InputFieldLayout.OnDonePressedListener,
            View.OnClickListener {

        private final TextStyle timeNumbersTextStyle = TextStyle.size(Size.from(R.dimen.text_size_log_time_numbers));
        private final TextStyle timeMiddayTextStyle = TextStyle.size(Size.from(R.dimen.text_size_log_time_midday));
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

        private void onBind(MutableObservationLogRecord item) {
            final Date date = item.getDate();
            final Text timeText = Text.from(DATE_HOURS_MINUTES_FORMAT.format(date), timeNumbersTextStyle);
            final Text middayText = Text.from(DATE_AM_PM_FORMAT.format(date), timeMiddayTextStyle);
            TextKt.setText(timeTextView, timeText.plus(middayText), null);
            teacherInputFieldLayout.setStartingText(item.getTeacherActions());
            studentsInputFieldLayout.setStartingText(item.getStudentsActions());
        }

        @Override
        public void onClick(View v) {
            final int position = getAdapterPosition();
            if (position == RecyclerView.NO_POSITION) {
                return;
            }
            if (v.getId() == R.id.imagebutton_delete) {
                listener.onDeletePressed(position);
            } else if (v.getId() == R.id.textview_time) {
                listener.onTimePressed(position);
            }
        }

        @Override
        public void onDonePressed(View view, @Nullable String content) {
            ViewUtils.hideKeyboardAndClearFocus(view, itemView);
            final int position = getAdapterPosition();
            if (position == RecyclerView.NO_POSITION) {
                return;
            }
            if (view.getId() == R.id.inputfieldlayout_teacher_action) {
                listener.onTeacherActionChanged(position, content);
            } else if (view.getId() == R.id.inputfieldlayout_students_action) {
                listener.onStudentsActionChanged(position, content);
            }
        }

    }

    static class HeaderViewHolder extends OmegaRecyclerView.ViewHolder {
        public HeaderViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_observation_log_header);
        }
    }

    public interface Listener {
        void onTeacherActionChanged(int position, @Nullable String action);
        void onStudentsActionChanged(int position, @Nullable String action);
        void onTimePressed(int position);
        void onDeletePressed(int position);
    }
}
