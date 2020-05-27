package ru.nortti.filmssearch.model.local.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.nortti.filmssearch.model.local.models.Pending

@Dao
interface PendingDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPending(pending: Pending)

    @Delete
    fun deletePending(pending: Pending)

    @Query("DELETE FROM pending WHERE id = :id")
    fun deleteById(id : Int)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updatePending(pending: Pending)

    @Query("SELECT * FROM pending")
    fun getAllPendingLiveData() : LiveData<List<Pending>>

    @Query("SELECT * FROM pending WHERE id = :id")
    fun getPendingByIdLiveData(id: Int) : LiveData<Pending>
}