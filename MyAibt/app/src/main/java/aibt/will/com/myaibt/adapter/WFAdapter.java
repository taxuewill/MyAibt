package aibt.will.com.myaibt.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aibt.will.com.myaibt.MyApplication;
import aibt.will.com.myaibt.R;
import aibt.will.com.myaibt.widget.WifiItemViewGroup;

/**
 * @author 王晶
 * @date 17-6-29-下午2:29
 * @desc
 */
public class WFAdapter extends PagerAdapter{


    private static final String TAG = "WFAdapter";


    Context mContext;
    List<String> mScanResults;
    private int currentPageSize = 0;
    private int currentPageItemNum = 0;
    private static final int PAGE_SIZE = 8;
    private int currentPageIndex = 0;
    boolean mViewLoaded = false;
    private int itemsNum = 0;
    private ItemLoadedListener mItemLoadedListener ;
    private MyViewHolder currentHolder;


    private Map<Integer,MyViewHolder> holders = new HashMap<Integer, MyViewHolder>();

    public static class MyViewHolder{
        public WifiItemViewGroup view;
    }

    public WFAdapter(Context context){
        mContext = context;
    }
    @Override
    public int getCount() {
        return currentPageSize;
    }



    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Log.i(TAG,"instantiateItem" + position);
        currentHolder = holders.get(position);
        int currentPageItem = mScanResults.size() -position * 8;
        if(currentPageItem > 8){
            currentPageItem = 8;
        }
        WifiItemViewGroup view;
        if(currentHolder == null){
            view = (WifiItemViewGroup) View.inflate(MyApplication.getContext(),R.layout.wf_pager,null);
            currentHolder = new MyViewHolder();
            holders.put(position,currentHolder);
            currentHolder.view = view;

            for(int i = 0 ; i < currentPageItem; i++){
                View itemView = View.inflate(MyApplication.getContext(),R.layout.wifi_item_layout,null);
                TextView wifiItemIndex = (TextView) itemView.findViewById(R.id.wfNum);
                wifiItemIndex.setText(String.valueOf(i%8 + 1));
                itemView.setFocusable(true);    //!!!
                view.addView(itemView);
                Log.i(TAG,"instantiateItem "+i);
            }

        }else{
            view = currentHolder.view;
            int childNum = view.getChildCount();
            for(int i = childNum; i < currentPageItem; i++){
                View itemView = View.inflate(MyApplication.getContext(),R.layout.wifi_item_layout,null);
                TextView wifiItemIndex = (TextView) itemView.findViewById(R.id.wfNum);
                wifiItemIndex.setText(String.valueOf(i%8 + 1));
                itemView.setFocusable(true);    //!!!
                view.addView(itemView);
            }
            //make wifi item invisible before feed data
            for(int i = 0 ; i < view.getChildCount();i++){
                view.getChildAt(i).setVisibility(View.INVISIBLE);
            }
        }

        //feed wifi name
        for(int i = 0; i< currentPageItem;i++){


                View itemView = view.getChildAt(i);
                TextView wifiItemName = (TextView) itemView.findViewById(R.id.wfName);
                wifiItemName.setText(mScanResults.get(position * 8 + i));
                itemView.setVisibility(View.VISIBLE);

                Log.i(TAG,"feed wifi name " + i);
        }
        container.addView(view);
        view.setTag(position);
        if (mViewLoaded == false) {
            mViewLoaded = true;
            if (mItemLoadedListener != null) {
                mItemLoadedListener.onItemLoaded();
            }
        }
        return view;
    }

    public void setData(List<String> scanResults,ItemLoadedListener itemLoadedListener){
        Log.i(TAG,"setData");
        mViewLoaded = false;
        mItemLoadedListener = itemLoadedListener;
        mScanResults = scanResults;

        itemsNum = scanResults.size();
        currentPageSize = scanResults.size() / PAGE_SIZE;
        if(scanResults.size() % PAGE_SIZE > 0){
            currentPageSize++;
        }
        notifyDataSetChanged();

    }



    public int getCurrentPageIndex() {
        return currentPageIndex;
    }

    public void setCurrentPageIndex(int currentPageIndex) {
        this.currentPageIndex = currentPageIndex;
        currentPageItemNum = mScanResults.size() - PAGE_SIZE * currentPageIndex;
        if(currentPageItemNum > 8){
            currentPageItemNum = 8;
        }
        Log.i(TAG,"currentPageItemNum " +currentPageItemNum);
    }


    public int getItemsNum(){
        return itemsNum;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)     {
        container.removeView(holders.get(position).view);//删除页卡
    }

    public int getCurrentPageItemNum() {

        return currentPageItemNum;
    }



    @Override
    public int getItemPosition(Object object)   {
        View view = (View) object;
        Log.i(TAG,"currentPageIndex is " + currentPageIndex + ", view.getTag : " +view.getTag());
        if(currentPageIndex ==  (Integer) view.getTag()){
            return POSITION_NONE;
        }else{
            return super.getItemPosition(object);
        }
    }


    public interface ItemLoadedListener {
        void onItemLoaded();
    }





}
