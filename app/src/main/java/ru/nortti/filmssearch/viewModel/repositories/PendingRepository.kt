package ru.nortti.filmssearch.viewModel.repositories

import android.app.Application
import androidx.lifecycle.LiveData
import ru.nortti.filmssearch.model.local.RoomDB
import ru.nortti.filmssearch.model.local.models.Pending

class PendingRepository private constructor(application: Application){

    private val pendingDao = RoomDB.getDatabase(application).getPendingDao()

    fun getAllPendingLiveData() : LiveData<List<Pending>> {
        return pendingDao.getAllPendingLiveData()
    }

    fun insertPending(pending: Pending) {
        pendingDao.insertPending(pending)
    }

    fun deleteFromPending(pending: Pending) {
        pendingDao.deletePending(pending)
    }

    fun deleeteFromPendingById(id: Int){
        pendingDao.deleteById(id)
    }

    fun updatePendingData(pending: Pending) {
        pendingDao.updatePending(pending)
    }

    fun getPendingById(id : Int) {
        pendingDao.getPendingByIdLiveData(id)
    }

    companion object {
        private var INSTANCE : PendingRepository? = null

        fun getInstance(application: Application) : PendingRepository = INSTANCE ?: kotlin.run {
            INSTANCE = PendingRepository(application)
            INSTANCE!!
        }
    }

}