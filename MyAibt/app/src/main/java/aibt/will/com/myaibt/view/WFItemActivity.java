package aibt.will.com.myaibt.view;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import aibt.will.com.myaibt.R;
import aibt.will.com.myaibt.adapter.WFAdapter;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wf_item_activity);
        init();
    }

    private void init(){
        mWFPager = (MyViewPager) findViewById(R.id.wfPager);
        mWFAdapter = new WFAdapter(this);
        btnFocus = (Button) findViewById(R.id.btnFocus);

        List<String> wifiNames = new ArrayList<>();
        for(int i = 0; i < 20; i++){
            wifiNames.add("wifi name is " + i);
        }

        mWFAdapter.setData(wifiNames,null);
        mWFPager.setAdapter(mWFAdapter);

        btnFocus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });




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

}
