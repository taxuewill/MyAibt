package aibt.will.com.myaibt.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * @author 王晶
 * @date 17-6-29-下午5:17
 * @desc
 */
public class MyViewPager extends ViewPager {

    private static final String TAG = "MyViewPager";
    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    int focusIndex = 0;





}
