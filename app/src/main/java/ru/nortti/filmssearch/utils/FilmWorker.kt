package ru.nortti.filmssearch.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.NonNull
import androidx.core.app.NotificationCompat
import androidx.work.*
import ru.nortti.filmssearch.view.activities.MainActivity
import java.util.*
import java.util.concurrent.TimeUnit


class FilmWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    companion object {
        fun scheduleReminder(
            duration: Long,
            data: Data?,
            tag: String?
        ) {
            val notificationWork =
                OneTimeWorkRequest.Builder(FilmWorker::class.java)
                    .setInitialDelay(duration, TimeUnit.MILLISECONDS)
                    .addTag(tag!!)
                    .setInputData(data!!).build()
            val instance = WorkManager.getInstance()
            instance.enqueue(notificationWork)
        }

        fun cancelReminder(tag: String?) {
            val instance = WorkManager.getInstance()
            instance.cancelAllWorkByTag(tag!!)
        }
    }

    @NonNull
    override fun doWork(): Result {
        val title = inputData.getString(EXTRA_TITLE)
        val text = inputData.getString(EXTRA_TEXT)
        val id = inputData.getInt(EXTRA_ID, 0)
        sendNotification(title!!, text!!, id)
        return Result.success()
    }
    private fun sendNotification(
        title: String,
        text: String,
        id: Int
    ) {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.putExtra(EXTRA_ID, id)
        val pendingIntent =
            PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel("default", "Default", NotificationManager.IMPORTANCE_DEFAULT)
            Objects.requireNonNull(notificationManager).createNotificationChannel(channel)
        }
        val notification: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, "default")
            .setContentTitle(title)
            .setStyle(NotificationCompat.BigTextStyle().bigText(text))
            .setContentIntent(pendingIntent)
            .setSmallIcon(ru.nortti.filmssearch.R.drawable.ic_film_notification)
            .setAutoCancel(true)
        Objects.requireNonNull(notificationManager).notify(id, notification.build())
    }



}