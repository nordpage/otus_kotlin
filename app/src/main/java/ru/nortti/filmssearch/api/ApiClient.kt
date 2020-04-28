package ru.nortti.filmssearch.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.nortti.filmssearch.utils.BASE_URL
import java.util.concurrent.TimeUnit

class ApiClient {


    companion object {
        var logging = HttpLoggingInterceptor()
        var retrofit: Retrofit? = null

        @JvmStatic
        fun getClient(): Retrofit {
            logging.level = HttpLoggingInterceptor.Level.BODY
            val httpClient = OkHttpClient.Builder()
            httpClient.connectTimeout(300, TimeUnit.SECONDS)
            httpClient.readTimeout(80, TimeUnit.SECONDS)
            httpClient.writeTimeout(90, TimeUnit.SECONDS)
            httpClient.addInterceptor(logging)
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build()
            }
            return retrofit!!
        }
    }
}