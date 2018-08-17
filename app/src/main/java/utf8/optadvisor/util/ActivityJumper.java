package utf8.optadvisor.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import utf8.optadvisor.R;
import utf8.optadvisor.activity.ForgetPasswordActivity;
import utf8.optadvisor.activity.LoginActivity;

/**
 * 界面跳转控制器
 */
public class ActivityJumper {

    /**
     * 左进右出
     * @param context 当前活动布局
     * @param toExit 当前活动对象
     * @param cls 转入活动的类
     */
    public static void leftEnterRightExit(Context context,Activity toExit, Class<?> cls){
        Intent intent = new Intent(context,cls);
        context.startActivity(intent);
        toExit.overridePendingTransition(R.anim.activity_left_enter,R.anim.activity_right_exit);
    }

    /**
     * 右进左出
     * @param context 当前活动布局
     * @param toExit 当前活动对象
     * @param cls 转入活动的类
     */
    public static void rightEnterLeftExit(Context context,Activity toExit, Class<?> cls){
        Intent intent = new Intent(context,cls);
        context.startActivity(intent);
        toExit.overridePendingTransition(R.anim.activity_right_enter,R.anim.activity_left_exit);
    }
}
