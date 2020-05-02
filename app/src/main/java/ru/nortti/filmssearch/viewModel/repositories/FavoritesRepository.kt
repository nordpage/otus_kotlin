package ru.nortti.filmssearch.viewModel.repositories

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.nortti.filmssearch.model.local.RoomDB
import ru.nortti.filmssearch.model.local.models.Favorite
import ru.nortti.filmssearch.model.remote.MovieResponse

class FavoritesRepository private constructor(application: Application) {

    private val favoritesDao = RoomDB.getDatabase(application).getFavoritesDao()

    fun getAllFavorites() : LiveData<List<Favorite>> {
        return favoritesDao.getAllFavoritesLiveData()
    }

    fun insertFavorite(favorite: Favorite) {
        favoritesDao.insertFavorite(favorite)
    }

    fun deleteFromFavorite(favorite: Favorite) {
        favoritesDao.deleteFavorite(favorite)
    }

    fun getFavoriteById(id : Int) : Favorite {
      return favoritesDao.getFavoriteById(id)
    }

    fun deleteById(id : Int) {
        favoritesDao.deleteById(id)
    }


    companion object {
        private var INSTANCE : FavoritesRepository? = null

        fun getInstance(application: Application): FavoritesRepository = INSTANCE ?: kotlin.run {
            INSTANCE = FavoritesRepository(application)
            INSTANCE!!
        }
    }

}