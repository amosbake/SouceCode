package yanhao.com.soucecode;

import android.util.Log;

import java.util.Locale;

/**
 * Created by amosb on 2015/8/1.
 * Log 工具类
 */
public class LogUtils {
    private static boolean LOGV =  true ;
    private static boolean LOGD =  true ;
    private static boolean LOGI =  true ;
    private static boolean LOGW =  true ;
    private static boolean LOGE =  true ;

    public static void v(String tag, String mess) {
        if (LOGV) { Log.v(getTag(), buildMessage(mess)); }
    }
    public static void d(String tag, String mess) {
        if (LOGD) { Log.d(getTag(), buildMessage(mess)); }
    }
    public static void i(String tag, String mess) {
        if (LOGI) { Log.i(getTag(), buildMessage(mess)); }
    }
    public static void w(String tag, String mess) {
        if (LOGW) { Log.w(getTag(), buildMessage(mess)); }
    }
    public static void e(String tag, String mess) {
        if (LOGE) { Log.e(getTag(), buildMessage(mess)); }
    }

    /**
     * 自动获取Tag
     */
    private static String getTag() {
        StackTraceElement[] trace =  new Throwable().fillInStackTrace()
                .getStackTrace();
        String callingClass =  "" ;
        for ( int i =  2 ; i <trace.length; i++) {
            Class  clazz = trace[i].getClass();
            if (!clazz.equals(LogUtils. class )) {
                callingClass = trace[i].getClassName();
                callingClass = callingClass.substring(callingClass
                        .lastIndexOf( '.' ) +  1 );
                break ;
            }
        }
        return callingClass;
    }

    /**
     * 获取方法名 行号
     * @param msg 运行信息
     */
    private static String buildMessage(String msg) {
        StackTraceElement[] trace =  new Throwable().fillInStackTrace()
                .getStackTrace();
        String caller =  "" ;
        for ( int i =  2 ; i <trace.length; i++) {
            Class  clazz = trace[i].getClass();
            if (!clazz.equals(LogUtils. class )) {
                caller = trace[i].getMethodName();
                break ;
            }
        }
        return String.format(Locale.US,  "[%d] %s: %s" , Thread.currentThread()
                .getId(), caller, msg);
    }
}
