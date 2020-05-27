package ru.nortti.filmssearch.viewModel.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import ru.nortti.filmssearch.model.local.models.Pending
import ru.nortti.filmssearch.viewModel.repositories.PendingRepository

class PendingViewModel(application: Application): AndroidViewModel(application) {

    private val pendingRepository = PendingRepository.getInstance(application)

    val pendingList : LiveData<List<Pending>>
        get() = pendingRepository.getAllPendingLiveData()

    fun addToPendings(pending: Pending) {
        pendingRepository.insertPending(pending)
    }

    fun removeFromPendings(pending: Pending) {
        pendingRepository.deleteFromPending(pending)
    }

    fun removeFromPendingById(id: Int) {
        pendingRepository.deleeteFromPendingById(id);
    }

    fun updatePending(pending: Pending) {
        pendingRepository.updatePendingData(pending)
    }

    fun getPendingById(id: Int){
        pendingRepository.getPendingById(id)
    }

}