package aibt.will.com.myaibt.vus;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author 王晶
 * @date 17-6-20-上午9:11
 * @desc
 */
public interface Vu {

    void init(LayoutInflater inflater, ViewGroup container);
    View getView();
}
