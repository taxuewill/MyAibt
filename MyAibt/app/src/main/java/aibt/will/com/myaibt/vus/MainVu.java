package aibt.will.com.myaibt.vus;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import aibt.will.com.myaibt.R;

/**
 * @author 王晶
 * @date 17-6-20-上午9:12
 * @desc
 */
public class MainVu implements Vu {

    View view;
    LinearLayout containerView;
    @Override
    public void init(LayoutInflater inflater, ViewGroup container){
        view = inflater.inflate(R.layout.vu_main,container,false);
        containerView = (LinearLayout) view.findViewById(R.id.container);
    }

    @Override
    public View getView(){
        return view;
    }

    public int getContainerId(){
        return containerView.getId();
    }
}
