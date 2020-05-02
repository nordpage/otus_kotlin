package ru.nortti.filmssearch.model.local.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.nortti.filmssearch.model.local.models.Favorite

@Dao
interface FavoritesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorite(favorite: Favorite)

    @Delete
    fun deleteFavorite(favorite: Favorite)

    @Query("DELETE FROM favorites WHERE id = :id")
    fun deleteById(id : Int)

    @Query("SELECT * FROM favorites")
    fun getAllFavoritesLiveData(): LiveData<List<Favorite>>

    @Query("SELECT * FROM favorites WHERE id = :id")
    fun getFavoriteById(id: Int) : Favorite
}