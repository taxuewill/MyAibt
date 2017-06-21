package aibt.will.com.myaibt.view;

import aibt.will.com.myaibt.vus.MainVu;

/**
 * @author 王晶
 * @date 17-6-20-上午9:21
 * @desc
 */
public class MvpActivity extends BasePresenterActivity<MainVu>{

    @Override
    protected void onBindVu() {
//        fm.beginTransaction()
//                .replace(vu.getContainerId(), IpsumListFragment.newInstance())
//                .commit();
    }

    @Override
    protected void afterResume() {
//        bus.registerSticky(this);
    }

    @Override
    protected void beforePause() {
//        bus.unregister(this);
    }

    @Override
    public boolean handleBackPressed() {
//        bus.removeAllStickyEvents();
        return false;
    }

    @Override
    protected Class<MainVu> getVuClass() {
        return MainVu.class;
    }

//    public void onEvent(ItemSelectedEvent event){
//        fm.beginTransaction()
//                .replace(vu.getContainerId(), IpsumDetailFragment.newInstance())
//                .addToBackStack(IpsumDetailFragment.class.getName())
//                .commit();
//    }
}
