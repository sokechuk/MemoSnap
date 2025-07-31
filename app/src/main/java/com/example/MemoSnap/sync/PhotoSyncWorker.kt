package com.example.MemoSnap.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.MemoSnap.data.MemoSnapDatabase
import com.example.MemoSnap.data.Photo
import kotlinx.coroutines.delay

class PhotoSyncWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val db = MemoSnapDatabase.getDatabase(applicationContext)
        val photoDao = db.photoDao()
        val unsynced = photoDao.getUnsynced()

        for (photo in unsynced) {
            val success = uploadToServer(photo)
            if (success) {
                photoDao.update(photo.copy(isSynced = true))
            }
        }

        return Result.success()
    }

    private suspend fun uploadToServer(photo: Photo): Boolean {
        delay(500) // Simulate upload delay
        return true // Simulate success
    }
}
