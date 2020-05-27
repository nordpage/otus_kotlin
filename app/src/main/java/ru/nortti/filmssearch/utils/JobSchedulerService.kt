package ru.nortti.filmssearch.utils

import android.app.job.JobParameters
import android.app.job.JobService

class JobSchedulerService : JobService() {
    override fun onStopJob(params: JobParameters?): Boolean {
        return false
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        return false
    }
}