package aibt.will.com.myaibt.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.LinearLayout;

/**
 * @author 王晶
 * @date 17-7-3-下午3:20
 * @desc
 */
public class MyLinearLayout extends LinearLayout{

    private static final String TAG = "MyLinearLayout";
    public MyLinearLayout(Context context) {
        super(context);
        Log.i(TAG,"MyLinearLayout 1 params");
    }

    public MyLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Log.i(TAG,"MyLinearLayout 2 params");
    }

    public MyLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.i(TAG,"MyLinearLayout 3 params");
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event){

        Log.i(TAG,"dispatchKeyEvent" + event.getKeyCode());
        return super.dispatchKeyEvent(event);

    }


    @Override
    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        Log.i(TAG,"requestFocus: "+direction);
        return super.requestFocus(direction, previouslyFocusedRect);
    }
}
