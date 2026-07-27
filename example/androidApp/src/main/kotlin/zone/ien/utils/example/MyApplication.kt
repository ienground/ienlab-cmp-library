package zone.ien.utils.example

import android.app.Application
import com.google.firebase.FirebaseApp
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext
import zone.ien.utils.example.di.initKoin

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this)
        }

        if (GlobalContext.getKoinApplicationOrNull() == null) {
            initKoin {
                androidContext(this@MyApplication.applicationContext)
            }
        }
    }

}
