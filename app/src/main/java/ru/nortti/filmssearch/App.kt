package ru.nortti.filmssearch

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import ru.nortti.filmssearch.Constants.LANGUAGE
import ru.nortti.filmssearch.Constants.THEME
import ru.nortti.filmssearch.network.ApiClient
import ru.nortti.filmssearch.network.ApiInteractor
import ru.nortti.filmssearch.network.ApiInterface

class App : Application() {

    companion object {
        lateinit var prefs: SharedPreference
        lateinit var INSTANCE: App
        lateinit var service: ApiInterface
        lateinit var appInteractor: ApiInteractor

        @JvmStatic
        fun getInstance() : App {
            return INSTANCE
        }


    }
    override fun onCreate() {
        prefs = SharedPreference(applicationContext)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        if  (prefs.getValueString(LANGUAGE) == null) prefs.save(LANGUAGE, "ru")
        if  (prefs.getValueString(THEME) == null) prefs.save(THEME, "DAY")

        INSTANCE = this
        initRetrofit()
        initInteractor()
        super.onCreate()
    }

    fun initRetrofit(){
        var retrofit = ApiClient.getClient()
        service = retrofit.create(ApiInterface::class.java)
    }
    fun initInteractor() {
        appInteractor = ApiInteractor(service)
    }
    fun getService() : ApiInterface {
        return service
    }

    fun getInteractor() : ApiInteractor {
        return appInteractor
    }

}