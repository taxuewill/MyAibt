package aibt.will.com.myaibt.view;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import aibt.will.com.myaibt.R;
import aibt.will.com.myaibt.adapter.WFAdapter;
import aibt.will.com.myaibt.manager.FloatWindowManager;
import aibt.will.com.myaibt.widget.WifiFloatRootContainer;

/**
 * @author 王晶
 * @date 17-6-29-上午11:39
 * @desc
 */
public class WFItemActivity extends Activity{

    private static final String TAG = "WFItemActivity";

    MyViewPager mWFPager;
    WFAdapter mWFAdapter;
    Button btnFocus;
    int focusIndex = 0;
    Handler mHander = new Handler(Looper.getMainLooper());

    WifiFloatRootContainer floatRootContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wf_item_activity);
//        if(Build.VERSION.SDK_INT >= 23) {
//            if (!Settings.canDrawOverlays(this)) {
//                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
//                        Uri.parse("package:" + getPackageName()));
//                startActivityForResult(intent, 1234);
//            }
//        }
        init();
    }

    private void init(){
//        View popView = View.inflate(this,R.layout.wifi_pop,null);
//        mWFPager = (MyViewPager) popView.findViewById(R.id.wfPager);
//
//        mWFAdapter = new WFAdapter(this);
//        btnFocus = (Button) findViewById(R.id.btnFocus);
//
//        List<String> wifiNames = new ArrayList<>();
//        for(int i = 0; i < 20; i++){
//            wifiNames.add("wifi name is " + i);
//        }
//
//        mWFAdapter.setData(wifiNames,null);
//        mWFPager.setAdapter(mWFAdapter);
        if(floatRootContainer != null){
            FloatWindowManager.getInstance().removeFloatWindow(this,floatRootContainer);
            floatRootContainer = null;
        }

        floatRootContainer = new WifiFloatRootContainer(this);

        FloatWindowManager.getInstance().createFloatWindow(this,floatRootContainer, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

//        Thread t = new Thread(){
//            @Override
//            public void run(){
//                while(focusIndex < 8){
//                    try {
//                        sleep(500);
//                        mHander.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                ViewGroup viewGroup = (ViewGroup) mWFPager.findViewWithTag(mWFPager.getCurrentItem());
//                                if(focusIndex >= 8) {
//                                    focusIndex = 0;
//
//                                }
//                                boolean result = viewGroup.getChildAt(focusIndex).requestFocus();
//                                Log.i(TAG,"request focus result is "+result);
//
//                                View focusView = getWindow().getCurrentFocus();
//                                focusIndex++;
//                                Log.i(TAG,"focusView is "+focusView);
//                            }
//                        });
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        };
//        t.start();

    }


    @Override
    public boolean onKeyDown(int keyCode ,KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
            ViewGroup viewGroup = (ViewGroup) mWFPager.findViewWithTag(mWFPager.getCurrentItem());
            if(focusIndex >= 8) {
                focusIndex = 0;

            }
            boolean result = viewGroup.getChildAt(focusIndex).requestFocus();
            Log.i(TAG,"request focus result is "+result);

            View focusView = getWindow().getCurrentFocus();
            focusIndex++;
            Log.i(TAG,"focusView is "+focusView);
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(floatRootContainer != null){
            FloatWindowManager.getInstance().removeFloatWindow(this,floatRootContainer);
            floatRootContainer = null;
        }
    }

}
