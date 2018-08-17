package fm.doe.national;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.amitshekhar.DebugDB;
import com.crashlytics.android.Crashlytics;

import java.lang.reflect.Method;

import fm.doe.national.di.AppComponent;
import fm.doe.national.di.DaggerAppComponent;
import fm.doe.national.di.modules.ContextModule;
import io.fabric.sdk.android.Fabric;

public class MicronesiaApplication extends Application {

    private static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        appComponent = DaggerAppComponent.builder().contextModule(new ContextModule(this)).build();

        showDebugDBAddressLogToast(this);
    }

    public static void showDebugDBAddressLogToast(Context context) {
        if (BuildConfig.DEBUG) {
            try {
                Class<?> debugDB = Class.forName("com.amitshekhar.DebugDB");
                Method getAddressLog = debugDB.getMethod("getAddressLog");
                Object value = getAddressLog.invoke(null);
                Toast.makeText(context, (String) value, Toast.LENGTH_LONG).show();
            } catch (Exception ignore) {
            }
        }
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }

}
