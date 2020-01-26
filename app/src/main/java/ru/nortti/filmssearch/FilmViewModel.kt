package ru.nortti.filmssearch

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FilmViewModel : ViewModel(){
    var filmList : MutableLiveData<List<Film>> = MutableLiveData()

    init {
        filmList.value = FilmData.getFilms()
    }

    fun getListFilms() = filmList
}