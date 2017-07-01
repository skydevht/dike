package tech.skydev.dike

import android.app.Application
import timber.log.Timber

/**
 * Created by Hash Skyd on 3/27/2017.
 */

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        Injector.setup(this)

        //MobileAds.initialize(this, "pub-2731520567117800");
    }
}
