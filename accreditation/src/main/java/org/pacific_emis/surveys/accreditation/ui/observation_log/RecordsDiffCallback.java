package org.pacific_emis.surveys.accreditation.ui.observation_log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import org.pacific_emis.surveys.accreditation_core.data.model.mutable.MutableObservationLogRecord;

public class RecordsDiffCallback extends DiffUtil.ItemCallback<MutableObservationLogRecord> {

    @Override
    public boolean areItemsTheSame(@NonNull MutableObservationLogRecord oldItem, @NonNull MutableObservationLogRecord newItem) {
        return oldItem.getId() == newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull MutableObservationLogRecord oldItem, @NonNull MutableObservationLogRecord newItem) {
        return oldItem.equals(newItem);
    }

}
