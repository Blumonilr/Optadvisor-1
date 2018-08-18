package utf8.optadvisor.util;

import android.widget.HorizontalScrollView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * 同向HorizontalScrollView联动
 */
public class SyncHorizontalScrollView extends HorizontalScrollView{
    private View mView;
    private View mAnother;
    private int mlength;
    public SyncHorizontalScrollView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub

    }
    public SyncHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub

    }

    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(mView!=null){
            mView.scrollTo(l, t);
        }
        if(mAnother!=null){
            mAnother.scrollTo(l,t);
        }
    }
    public void setScrollView(View view){
        mView = view;
    }
    public void setAnotherView(View anotherView,int length){
        mAnother=anotherView;
        mlength=length;
    }


}