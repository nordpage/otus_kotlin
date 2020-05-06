package ru.nortti.filmssearch

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import ru.nortti.filmssearch.api.ApiClient
import ru.nortti.filmssearch.api.ApiInteractor
import ru.nortti.filmssearch.api.ApiInterface
import ru.nortti.filmssearch.utils.LANGUAGE
import ru.nortti.filmssearch.utils.SharedPreference
import ru.nortti.filmssearch.utils.THEME
import timber.log.Timber
import timber.log.Timber.DebugTree


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

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        } else {
            Timber.plant()
        }

        super.onCreate()
    }

    fun initRetrofit(){
        val retrofit = ApiClient.getClient()
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