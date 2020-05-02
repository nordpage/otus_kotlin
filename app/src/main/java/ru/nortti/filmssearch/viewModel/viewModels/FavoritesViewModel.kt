package ru.nortti.filmssearch.viewModel.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import ru.nortti.filmssearch.model.local.models.Favorite
import ru.nortti.filmssearch.viewModel.repositories.FavoritesRepository

class FavoritesViewModel(application: Application): AndroidViewModel(application) {

    private val favoritesRepository = FavoritesRepository.getInstance(application)

    val favorites : LiveData<List<Favorite>>
        get() = favoritesRepository.getAllFavorites()


    fun addToFavorites(favorite: Favorite) {
        favoritesRepository.insertFavorite(favorite)
    }

    fun removeFromFavorites(favorite: Favorite) {
        favoritesRepository.deleteFromFavorite(favorite)
    }

    fun getFavoriteById(id : Int) : Favorite {
       return favoritesRepository.getFavoriteById(id)
    }

    fun deleteById(id : Int) {
        favoritesRepository.deleteById(id)
    }
}