package ru.nortti.filmssearch.api

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.nortti.filmssearch.model.remote.ErrorResponse
import ru.nortti.filmssearch.model.remote.MovieDetail
import ru.nortti.filmssearch.model.remote.MovieResponse

class ApiInteractor(private val service: ApiInterface) {
    fun getTopMovies(apiKey: String, language: String, callback: GetMoviesCallback) {
        service.getTopRatedMovies(apiKey,language).enqueue(object : Callback<MovieResponse> {
            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                callback.onError(t)
            }

            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.isSuccessful) {
                    callback.onSuccess(response.body()!!)
                } else {
                    val gson = Gson()
                    val type = object : TypeToken<ErrorResponse>() {}.type
                    var errorResponse: ErrorResponse? = gson.fromJson(response.errorBody()!!.charStream(), type)
                    callback.onCustomError(errorResponse!!)
                }
            }

        })
    }

    fun getTopMoviesPagination(apiKey: String, language: String, page: Int, callback: GetMoviesCallback){
        service.getTopRatedMoviesWithPagination(apiKey, language, page).enqueue(object : Callback<MovieResponse> {
            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                callback.onError(t)
            }

            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.isSuccessful) {
                    callback.onSuccess(response.body()!!)
                } else {
                    val gson = Gson()
                    val type = object : TypeToken<ErrorResponse>() {}.type
                    var errorResponse: ErrorResponse? = gson.fromJson(response.errorBody()!!.charStream(), type)
                    callback.onCustomError(errorResponse!!)
                }
            }

        })
    }

    fun getDetailedMovie(apiKey: String, language: String, id: Int, callback: GetMoviesDetails) {
        service.getMovieDetails(id, apiKey, language).enqueue(object : Callback<MovieDetail> {
            override fun onFailure(call: Call<MovieDetail>, t: Throwable) {
                callback.onError(t)
            }

            override fun onResponse(call: Call<MovieDetail>, response: Response<MovieDetail>) {
                if (response.isSuccessful) {
                    callback.onSuccess(response.body()!!)
                } else {
                    val gson = Gson()
                    val type = object : TypeToken<ErrorResponse>() {}.type
                    val errorResponse: ErrorResponse? = gson.fromJson(response.errorBody()!!.charStream(), type)
                    callback.onCustomError(errorResponse!!)
                }
            }
        })
    }

    interface GetMoviesCallback {
        fun onSuccess(movies: MovieResponse)
        fun onError(error: Throwable)
        fun onCustomError(error: ErrorResponse)
    }

    interface GetMoviesDetails {
        fun onSuccess(movies: MovieDetail)
        fun onError(error: Throwable)
        fun onCustomError(error: ErrorResponse)
    }
}