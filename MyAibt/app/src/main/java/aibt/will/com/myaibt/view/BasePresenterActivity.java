package aibt.will.com.myaibt.view;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;

import aibt.will.com.myaibt.vus.Vu;

/**
 * @author 王晶
 * @date 17-6-20-上午9:17
 * @desc
 */
public abstract class BasePresenterActivity<V extends Vu> extends Activity {

    protected V vu;
//    protected EventBus bus;
    protected FragmentManager fm;


    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fm = getFragmentManager();
//        bus = EventBus.getDefault();
        try {
            vu = getVuClass().newInstance();
            vu.init(getLayoutInflater(), null);
            setContentView(vu.getView());
            onBindVu();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected final void onPause() {
        beforePause();
        super.onPause();
    }

    protected void beforePause(){}

    @Override
    protected final void onResume() {
        super.onResume();
        afterResume();
    }

    protected void afterResume(){}

    @Override
    protected final void onDestroy() {
        onDestroyVu();
        vu = null;
        super.onDestroy();
    }

    @Override
    public final void onBackPressed() {
        if(!handleBackPressed()) {
            super.onBackPressed();
        }
    }

    public boolean handleBackPressed(){
        return false;
    }

    protected abstract Class<V> getVuClass();

    protected void onBindVu(){};

    protected void onDestroyVu() {};
}
