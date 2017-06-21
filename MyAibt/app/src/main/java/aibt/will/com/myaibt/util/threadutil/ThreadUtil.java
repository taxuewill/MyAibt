package aibt.will.com.myaibt.util.threadutil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author 王晶
 * @date 17-6-9-下午2:13
 * @desc
 */
public class ThreadUtil {

    public static ThreadUtil mInstance;

    private ExecutorService executorService;

    public static ThreadUtil getInstance(){
        if(mInstance == null){
            mInstance = new ThreadUtil();
        }

        return mInstance;
    }

    private ThreadUtil(){
        this.executorService = Executors.newFixedThreadPool(2);
    }

    public ExecutorService getThreadPool(){
        return executorService;
    }

}
