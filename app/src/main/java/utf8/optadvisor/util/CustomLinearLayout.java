package utf8.optadvisor.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.widget.LinearLayout;

public class CustomLinearLayout extends LinearLayout {
    public CustomLinearLayout(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public CustomLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public CustomLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    //请求其Parents不对Touch进行拦截
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN)
        {
            ViewParent p = getParent();
            if (p != null)
                p.requestDisallowInterceptTouchEvent(true);
        }

        return false;
    }

    //给LinearLayout加入Touch事件，使得所有Layout区域的Touch操作均不受拦截
    //参考：http://blog.csdn.net/android_tutor/article/details/7193090
    public boolean onTouchEvent(MotionEvent ev)
    {
        return true;
    }



}
