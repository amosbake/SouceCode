package yanhao.com.soucecode;

import android.app.Activity;

import java.util.Stack;

/**
 * 应用程序Activity管理类：用于Activity管理和应用程序退出
 * Author: yanhao(amosbake@gmail.com)
 * Date : 2015-07-30
 * Time: 18:06
 */
public class ActivityStack {
    private static Stack<Activity> sActivityStack;
    private static final String TAG = "ActivityStack";
    private static final ActivityStack instance = new ActivityStack();
    private ActivityStack() {
    }

    public static ActivityStack getInstance() {
        return instance;
    }

    /**
     * 获取当前Activity栈中元素个数
     */
    public int getCount() {
        if (sActivityStack == null) {
            return 0;
        }
        return sActivityStack.size();
    }

    /**
     * 添加Activity到栈
     */
    public void addActivity(Activity activity) {
        if (sActivityStack == null) {
            sActivityStack = new Stack<>();
        }
        sActivityStack.add(activity);
    }

    /**
     * 弹出并结束一个Activity
     */
    public void popActivity(){
        Activity activity=sActivityStack.lastElement();
        if (activity!=null){
            activity.finish();
            activity=null;
        }
    }

    public void popActivity(Activity activity){
        if (activity!=null){
            activity.finish();
            sActivityStack.remove(activity);
            activity=null;
        }
    }

    public Activity currentActivity(){
        Activity activity=sActivityStack.lastElement();
        return activity;
    }

    /**
     * 将actovity压入栈
     */
    public void pushActivity(Activity activity){
        if (sActivityStack==null){
            sActivityStack=new Stack<>();
        }
        sActivityStack.add(activity);
    }

    public void popAllActivityExceptOne(Class cls){
        while (true){
            Activity activity=currentActivity();
            if (activity==null){
                break;
            }
            if (activity.getClass().equals(cls)){
                continue;
            }
            popActivity(activity);
        }
    }

    public void finishAllActivity(){
        for (int i = 0, size = sActivityStack.size(); i < size; i++) {
            if (null != sActivityStack.get(i)) {
                ( sActivityStack.get(i)).finish();
            }
        }
        sActivityStack.clear();
    }
}
