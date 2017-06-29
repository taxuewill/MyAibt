package aibt.will.com.myaibt;

import android.app.Application;
import android.content.Context;

/**
 * @author 王晶
 * @date 17-6-29-下午2:48
 * @desc
 */
public class MyApplication  extends Application{

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext() {
        if (mContext == null) {
            throw new RuntimeException("Unknown Error");
        }
        return mContext;
    }

    public static MyApplication getApplication() {
        if (mContext == null) {
            throw new RuntimeException("Unknown Error");
        }
        return (MyApplication) mContext;
    }

}
