package utf8.optadvisor.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import utf8.optadvisor.R;

public class HedgingMenuItem extends RelativeLayout {
    ImageView iconLeft;
    TextView menuTextRight;
    TextView menuTextLeft;

    public HedgingMenuItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public HedgingMenuItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        inflate(context, R.layout.layout_menuitem, this);

        iconLeft = getView(R.id.icon_left);
        menuTextLeft = getView(R.id.menu_text_left);
        menuTextRight = getView(R.id.menu_text_right);


        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MenuItemView);
        int rightTextColor = ta.getColor(R.styleable.MenuItemView_rightTextColor, Color.GRAY);
        int leftTextColor = ta.getColor(R.styleable.MenuItemView_leftTextColor, Color.BLACK);
        menuTextRight.setTextColor(rightTextColor);
        menuTextLeft.setTextColor(leftTextColor);

        float textSize = ta.getDimension(R.styleable.MenuItemView_textSize, getResources().getDimension(R.dimen.default_text_size));
        menuTextLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        menuTextRight.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

        String text = ta.getString(R.styleable.MenuItemView_text);
        if (text != null) {
            menuTextLeft.setText(text);
//            menuTextRight.setText(text);
        }
        ta.recycle();

    }

    public void setMenuTextRight(String text) {
        menuTextRight.setText(text);
    }

    public CharSequence getMenuTextRight() {
        return menuTextRight.getText();
    }

    public void setMenuTextLeft(String text) {
        menuTextLeft.setText(text);
    }

    public CharSequence getMenuTextLeft() {
        return menuTextLeft.getText();
    }

    public <T extends View> T getView(int id) {
        return (T) findViewById(id);
    }

    public TextView getMenuLeft(){
        return menuTextLeft;
    }

    public void setIconLeft(int res) {
        this.iconLeft.setImageResource(res);
    }
}
