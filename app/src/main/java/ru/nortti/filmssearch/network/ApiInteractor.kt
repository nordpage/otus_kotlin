package ru.nortti.filmssearch.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.nortti.filmssearch.network.models.MovieDetail
import ru.nortti.filmssearch.network.models.MovieResponce

class ApiInteractor(private val service: ApiInterface) {
    fun getTopMovies(apiKey: String, language: String, callback: GetMoviesCallback) {
        service.getTopRatedMovies(apiKey,language).enqueue(object : Callback<MovieResponce> {
            override fun onFailure(call: Call<MovieResponce>, t: Throwable) {
                callback.onError(String.format("Error: %s", t.message))
            }

            override fun onResponse(call: Call<MovieResponce>, response: Response<MovieResponce>) {
                if (response.isSuccessful) {
                    callback.onSuccess(response.body()!!)
                } else {
                    callback.onError(String.format("%s %s", response.code().toString(), response.errorBody().toString()))
                }
            }

        })
    }

    fun getTopMoviesPagination(apiKey: String, language: String, page: Int, callback: GetMoviesCallback){
        service.getTopRatedMoviesWithPagination(apiKey, language, page).enqueue(object : Callback<MovieResponce> {
            override fun onFailure(call: Call<MovieResponce>, t: Throwable) {
                callback.onError(String.format("Error: %s", t.message))
            }

            override fun onResponse(call: Call<MovieResponce>, response: Response<MovieResponce>) {
                if (response.isSuccessful) {
                    callback.onSuccess(response.body()!!)
                } else {
                    callback.onError(String.format("%s %s", response.code().toString(), response.errorBody().toString()))
                }
            }

        })
    }

    fun getDetailedMovie(apiKey: String, language: String, id: Int, callback: GetMoviesDetails) {
        service.getMovieDetails(id, apiKey, language).enqueue(object : Callback<MovieDetail> {
            override fun onFailure(call: Call<MovieDetail>, t: Throwable) {
                callback.onError(String.format("Error: %s", t.message))
            }

            override fun onResponse(call: Call<MovieDetail>, response: Response<MovieDetail>) {
                if (response.isSuccessful) {
                    callback.onSuccess(response.body()!!)
                } else {
                    callback.onError(String.format("%s %s", response.code().toString(), response.errorBody().toString()))
                }
            }
        })
    }

    interface GetMoviesCallback {
        fun onSuccess(movies: MovieResponce)
        fun onError(error: String)
    }

    interface GetMoviesDetails {
        fun onSuccess(movies: MovieDetail)
        fun onError(error: String)
    }
}