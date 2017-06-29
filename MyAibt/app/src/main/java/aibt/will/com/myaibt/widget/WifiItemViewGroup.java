package aibt.will.com.myaibt.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

/**
 * @author 王晶
 * @date 17-6-29-上午11:08
 * @desc
 */
public class WifiItemViewGroup extends LinearLayout {


    private static final String TAG = "WifiItemViewGroup";
    public WifiItemViewGroup(Context context) {
        super(context);
        Log.i(TAG,"WifiItemViewGroup 1 params");
    }

    public WifiItemViewGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Log.i(TAG,"WifiItemViewGroup 2 params");
    }

    public WifiItemViewGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.i(TAG,"WifiItemViewGroup 3 params");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        /**
         * 获得此ViewGroup上级容器为其推荐的宽和高，以及计算模式
         */
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        Log.i(TAG,"widthMode is " + widthMode);
        Log.i(TAG,"sizeWidth is " + sizeWidth+", sizeHeight is "+sizeHeight);
//        sizeHeight = 200;
//        sizeWidth = 720;

        int width = 0;
        int height = 0;

        measureChildren(widthMeasureSpec, heightMeasureSpec);

        for(int i =0 ; i < getChildCount(); i++){
            View childView = getChildAt(i);
            MarginLayoutParams cParams = (MarginLayoutParams) childView.getLayoutParams();
            cParams.width = (sizeWidth - (cParams.leftMargin + cParams.rightMargin)*2)/2;
            childView.setLayoutParams(cParams);
        }
        if(getChildCount() > 0){
            View childView = getChildAt(0);

            int cWidth = childView.getMeasuredWidth();
            int cHeight = childView.getMeasuredHeight();
            MarginLayoutParams cParams = (MarginLayoutParams) childView.getLayoutParams();
            width = (cWidth + cParams.leftMargin + cParams.rightMargin) * 2;
            height = (cHeight + cParams.topMargin + cParams.bottomMargin) * 4;

        }

        Log.i(TAG,"width is "+width+", height is "+height);

        setMeasuredDimension((widthMode == MeasureSpec.EXACTLY) ? sizeWidth : width,
                (heightMode == MeasureSpec.EXACTLY) ? sizeHeight : height);


    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int cCount = getChildCount();
        int cWidth = 0;
        int cHeight = 0;
        MarginLayoutParams cParams = null;
        for(int i = 0; i < cCount; i++){
            View childView = getChildAt(i);
            cWidth = childView.getMeasuredWidth();
            cHeight = childView.getMeasuredHeight();
            cParams = (MarginLayoutParams) childView.getLayoutParams();

            int cl = 0, ct = 0, cr = 0, cb = 0;
            switch (i){
                case 0:
                    cl = cParams.leftMargin;
                    ct = cParams.topMargin;
                    break;
                case 1:
                    cl = cParams.leftMargin * 2 + cParams.rightMargin + cWidth;
                    ct = cParams.topMargin;
                    break;
                case 2:
                    cl = cParams.leftMargin;
                    ct = cParams.topMargin * 2 + cParams.bottomMargin + cHeight;
                    break;
                case 3:
                    cl = cParams.leftMargin * 2 + cParams.rightMargin + cWidth;
                    ct = cParams.topMargin * 2 + cParams.bottomMargin + cHeight;
                    break;
                case 4:
                    cl = cParams.leftMargin;
                    ct = cParams.topMargin * 3 + cParams.bottomMargin * 2 + cHeight * 2;
                    break;
                case 5:
                    cl = cParams.leftMargin * 2 + cParams.rightMargin + cWidth;
                    ct = cParams.topMargin * 3 + cParams.bottomMargin * 2 + cHeight * 2;
                    break;
                case 6:
                    cl = cParams.leftMargin;
                    ct = cParams.topMargin * 4 + cParams.bottomMargin * 3 + cHeight * 3;
                    break;
                case 7:
                    cl = cParams.leftMargin * 2 + cParams.rightMargin + cWidth;
                    ct = cParams.topMargin * 4 + cParams.bottomMargin * 3 + cHeight * 3;
                    break;
            }

            cr = cl + cWidth;
            cb = ct + cHeight;
            childView.layout(cl, ct, cr, cb);
        }
    }

}
