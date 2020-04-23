package ru.nortti.filmssearch.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.nortti.filmssearch.network.models.MovieDetail
import ru.nortti.filmssearch.network.models.MovieResponce

interface ApiInterface {

    @GET("movie/top_rated")
    fun getTopRatedMovies(@Query("api_key") api_key: String, @Query("language") lang: String) : Call<MovieResponce>

    @GET("movie/top_rated")
    fun getTopRatedMoviesWithPagination(@Query("api_key") api_key: String, @Query("language") lang: String, @Query("page") page: Int) : Call<MovieResponce>

    @GET("movie/{id}")
    fun getMovieDetails(@Path("id") id: Int, @Query("api_key") api_key: String, @Query("language") lang: String) : Call<MovieDetail>
}