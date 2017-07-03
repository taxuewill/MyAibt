package aibt.will.com.myaibt.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import aibt.will.com.myaibt.MyApplication;
import aibt.will.com.myaibt.R;
import aibt.will.com.myaibt.adapter.WFAdapter;
import aibt.will.com.myaibt.manager.FloatWindowManager;
import aibt.will.com.myaibt.util.ViewPagerFocusHelper;
import aibt.will.com.myaibt.view.MyViewPager;

/**
 * @author 王晶
 * @date 17-6-30-上午9:31
 * @desc
 */
public class WifiFloatRootContainer extends FrameLayout{


    private static final String TAG = "WifiFloatRootContainer";

    MyViewPager myViewPager;
    WFAdapter mWFAdapter;
    int focusIndex = 1;
    Button btnClose;

    public WifiFloatRootContainer(Context context){
        this(context,null);
    }

    public WifiFloatRootContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.pop_window,this,true);
        myViewPager = (MyViewPager) findViewById(R.id.wfPager);
        btnClose = (Button) findViewById(R.id.btnClose);
        mWFAdapter = new WFAdapter(context);

        List<String> wifiNames = new ArrayList<>();
        for(int i = 0; i < 22; i++){
            wifiNames.add("wifi name is " + i);
        }

        mWFAdapter.setData(wifiNames,new WFAdapter.ItemLoadedListener(){
            @Override
            public void onItemLoaded(){
                myViewPager.postDelayed(new Runnable() {//延时一些,否则viewpager还没有加载完成,导致无法获取焦点
                    @Override
                    public void run() {
                        ViewPagerFocusHelper.initFocus(myViewPager,0);
                    }
                }, 50);
            }
        });
        myViewPager.setAdapter(mWFAdapter);
        btnClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                FloatWindowManager.getInstance().removeFloatWindow(MyApplication.getContext(),WifiFloatRootContainer.this);
            }
        });

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // Let the focused view and/or our descendants get the key first
        Log.i(TAG,"dispatchKeyEvent " + event);
        //return myViewPager.dispatchKeyEvent(event);
        if(event.getAction() == KeyEvent.ACTION_DOWN){
            switch (event.getKeyCode()){
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    ViewPagerFocusHelper.prevFoucus(myViewPager, mPageSnapListenerImpl);
                    break;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    ViewPagerFocusHelper.nextFoucus(myViewPager, mPageSnapListenerImpl);
                    break;
                case KeyEvent.ACTION_DOWN:


                    break;
            }
        }

        return true;


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i(TAG,"onKeyDown " + keyCode);
        return super.onKeyDown(keyCode,event);
//        return false;
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.d(TAG, "onAttachedToWindow");

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.d(TAG, "onDetachedFromWindow");
    }




    private ViewPagerFocusHelper.PageSnapListener mPageSnapListenerImpl = new ViewPagerFocusHelper.PageSnapListener() {
        @Override
        public void showPage(int index) {
            Log.i(TAG,"showPage index is "+index);
            if(index == -1){
                myViewPager.setCurrentItem(mWFAdapter.getCount() - 1, false);
            }else if(index == mWFAdapter.getCount()){
                myViewPager.setCurrentItem(0, false);
            }else{
                myViewPager.setCurrentItem(index, false);
            }
        }
    };




}
