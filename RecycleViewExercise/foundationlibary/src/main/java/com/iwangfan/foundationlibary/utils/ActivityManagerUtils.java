package com.iwangfan.foundationlibary.utils;

import android.app.Activity;

import java.util.Stack;

/**
 * @类描述：Activity 堆栈管理工具类
 * @创建人：Wangxiaopan
 * @创建时间：2017/6/16 0016 14:30
 * @修改人：
 * @修改时间：2017/6/16 0016 14:30
 * @修改备注：
 */
public class ActivityManagerUtils {

    private static Stack<Activity> activityStack;
    private static ActivityManagerUtils instance;

    private ActivityManagerUtils() {
    }

    public static ActivityManagerUtils getActivityManager() {
        if (instance == null) {
            instance = new ActivityManagerUtils();
        }
        return instance;
    }

    /**
     * 添加activity到堆栈
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 移除activity到堆栈
     *
     * @param activity
     */
    public void removeActivity(Activity activity) {
        if (activity != null && activityStack != null) {
            activityStack.remove(activity);
            activity = null;
        }
    }

    /**
     * 获取堆栈中第一个压入的Activity
     */
    public Activity getFirstActivity() {
        Activity activity = null;
        if (!activityStack.empty())
            activity = activityStack.firstElement();
        return activity;
    }

    /**
     * 获取堆栈中最后一个压入的activity
     */
    public Activity getLastActivity() {
        Activity activity = null;
        if (!activityStack.empty())
            activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishLastActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定activity
     *
     * @param activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null && activityStack != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 转跳至某个已在栈中的页面，并关闭其间中其他页面
     *
     * @param clz 页面类
     */
    public Activity toActivity(Class<?> clz) {
        if (clz != null) {
            while (!activityStack.isEmpty()) {
                if (activityStack.peek().getClass().getSimpleName().equals(clz.getSimpleName())) {
                    return activityStack.peek();
                    // break;
                } else {
                    activityStack.pop().finish();
                }
            }
        }
        return null;
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0; i < activityStack.size(); i++) {
            if (null != activityStack.get(i)) {
                Activity activity = activityStack.get(i);
                finishActivity(activity);
            }
        }
        activityStack.clear();
    }

    /**
     * 退出堆栈中所有activity（除了指定的一个activity）
     *
     * @param cls
     */
    public void finishAllActivityExceptOne(Class cls) {
        while (true) {
            Activity activity = getLastActivity();
            if (activity == null) {
                break;
            }
            if (activity.getClass().equals(cls)) {
                break;
            }
            finishActivity(activity);
        }
    }

    /**
     * 退出应用程序
     */
    public void appExit() {
        try {
            finishAllActivity();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        } catch (Exception e) {
        }
    }
}
