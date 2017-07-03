package aibt.will.com.myaibt.util;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author 王晶
 * @date 17-7-3-下午4:09
 * @desc
 */
public class ViewPagerFocusHelper {

    private static final String TAG = "ViewPagerFocusHelper";

    public interface PageSnapListener {
        void showPage(int index);
    }

    /**
     * 点击确定
     *
     * @param viewPager
     */
    public static void clickFocus(ViewPager viewPager) {
        View currentPageView = viewPager.findViewWithTag(viewPager.getCurrentItem());

        Log.d(TAG, "clickFocus:" + currentPageView);
        if (currentPageView != null && currentPageView instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) currentPageView;
            View focusChild = parent.getFocusedChild();
            Log.d(TAG, "parent:" + parent + ", focusChild:" + focusChild + ", childCnt:" + parent.getChildCount());
            if (focusChild != null) {
                // Simulate a click on the widget
                focusChild.callOnClick();
            }
        }
    }

    /**
     * 下一个focus
     *
     * @param viewPager
     * @param listener
     */
    public static void nextFoucus(ViewPager viewPager, PageSnapListener listener) {
        View currentPageView = viewPager.findViewWithTag(viewPager.getCurrentItem());
        if (currentPageView != null && currentPageView instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) currentPageView;
            Log.d(TAG, "parent:" + parent + ", childCnt:" + parent.getChildCount());
            int focusChildIndex = parent.indexOfChild(parent.getFocusedChild());
            if (focusChildIndex < parent.getChildCount() - 1) {
                View child = parent.getChildAt(focusChildIndex + 1);
                Log.d(TAG, "focus view:" + child + ", childCnt:" + parent.getChildCount());
                if (child != null) {
                    child.requestFocus();
                }
            } else {
                Log.d(TAG, "next page");
                if (listener != null) {
                    listener.showPage(viewPager.getCurrentItem() + 1);
                }
                currentPageView = viewPager.findViewWithTag(viewPager.getCurrentItem());
                if (currentPageView != null && currentPageView instanceof ViewGroup) {
                    parent = (ViewGroup) currentPageView;
                    Log.d(TAG, "parent:" + parent + ", childCnt:" + parent.getChildCount());
                    View child = parent.getChildAt(0);
                    Log.d(TAG, "focus view:" + child + ", childCnt:" + parent.getChildCount());
                    if (child != null) {
                        child.requestFocus();
                    }
                }
            }
        }
    }

    /**
     * 上一个focus
     *
     * @param viewPager
     * @param listener
     */
    public static void prevFoucus(ViewPager viewPager, PageSnapListener listener) {
        View currentPageView = viewPager.findViewWithTag(viewPager.getCurrentItem());
        if (currentPageView != null && currentPageView instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) currentPageView;
            Log.d(TAG, "parent:" + parent + ", childCnt:" + parent.getChildCount());
            int focusChildIndex = parent.indexOfChild(parent.getFocusedChild());
            if (focusChildIndex > 0) {
                View child = parent.getChildAt(focusChildIndex - 1);
                Log.d(TAG, "focus view:" + child);
                if (child != null) {
                    child.requestFocus();
                }
            } else {
                Log.d(TAG, "pre page");
                if (listener != null) {
                    listener.showPage(viewPager.getCurrentItem() - 1);
                }
                currentPageView = viewPager.findViewWithTag(viewPager.getCurrentItem());
                if (currentPageView != null && currentPageView instanceof ViewGroup) {
                    parent = (ViewGroup) currentPageView;
                    Log.d(TAG, "parent:" + parent + ", childCnt:" + parent.getChildCount());
                    View child = parent.getChildAt(parent.getChildCount() - 1);
                    Log.d(TAG, "focus view:" + child + ", childCnt:" + parent.getChildCount());
                    if (child != null) {
                        child.requestFocus();
                    }
                }
            }
        }
    }

    /**
     * 初始化focus焦点位置
     *
     * @param viewPager
     */
    public static void initFocus(ViewPager viewPager) {
        initFocus(viewPager, 0);
    }
    /**
     * 初始化focus焦点位置
     *
     * @param viewPager
     * @param position
     */
    public static void initFocus(ViewPager viewPager, int position) {
        View currentPageView = viewPager.findViewWithTag(position);
        if (currentPageView != null && currentPageView instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) currentPageView;
            Log.d(TAG, "init focus page:" + viewPager.getCurrentItem() + ", parent:" + parent + ", childCnt:" + parent.getChildCount());
            if (parent.getChildCount() > 0) {
                boolean result = parent.getChildAt(0).requestFocus();
                Log.i(TAG,"init focus result is "+result);
            } else {
                Log.d(TAG, "init focus failed. no child");
            }
        } else {
            Log.d(TAG, "init focus failed");
        }
    }

    public static int getCurrentPageFocusIndex(ViewPager viewPager) {
        int focusChildIndex = -1;
        View currentPageView = viewPager.findViewWithTag(viewPager.getCurrentItem());
        if (currentPageView != null && currentPageView instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) currentPageView;
            focusChildIndex = parent.indexOfChild(parent.getFocusedChild());
            Log.d(TAG, "getCurrentPageFocusIndex parent:" + parent + ", childCnt:" + parent.getChildCount() + ", focusChildIndex:" + focusChildIndex);
        }
        return focusChildIndex;
    }

    public static void setCurrentPageFocus(ViewPager viewPager, int focusChildIndex) {
        View currentPageView = viewPager.findViewWithTag(viewPager.getCurrentItem());
        if (currentPageView != null && currentPageView instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) currentPageView;
            if (focusChildIndex > 0) {
                View child = parent.getChildAt(focusChildIndex);
                Log.d(TAG, "setCurrentPageFocus focus view:" + child + ", focusChildIndex:" + focusChildIndex);
                if (child != null) {
                    child.requestFocus();
                }
            }
        }
    }
}
