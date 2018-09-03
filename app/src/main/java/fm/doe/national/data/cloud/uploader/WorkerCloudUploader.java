package fm.doe.national.data.cloud.uploader;

import android.util.LongSparseArray;

import java.util.UUID;

import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class WorkerCloudUploader implements CloudUploader {
    private final Constraints constraints = new Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build();

    private final LongSparseArray<UUID> currentWorkers = new LongSparseArray<>();

    @Override
    public void scheduleUploading(long passingId) {
        UUID foundedUUID = currentWorkers.get(passingId);
        if (foundedUUID != null) {
            WorkManager.getInstance().cancelWorkById(foundedUUID);
        }

        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(UploadWorker.class)
                .setConstraints(constraints)
                .setInputData(new Data.Builder()
                        .putLong(UploadWorker.DATA_PASSING_ID, passingId)
                        .build())
                .build();
        WorkManager.getInstance().enqueue(workRequest);
        currentWorkers.put(passingId, workRequest.getId());
    }
}
