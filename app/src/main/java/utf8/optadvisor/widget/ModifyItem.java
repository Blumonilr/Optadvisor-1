package utf8.optadvisor.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import utf8.optadvisor.R;

public class ModifyItem extends RelativeLayout {

    TextView leftText;
    EditText rightText;

    public ModifyItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ModifyItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        inflate(context, R.layout.layout_user_info_modify, this);
        leftText=findViewById(R.id.modify_left_text);
        rightText=findViewById(R.id.modify_right_text);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ModifyItemView);
        int rightTextColor = ta.getColor(R.styleable.ModifyItemView_modify_rightTextColor, Color.BLACK);
        int leftTextColor = ta.getColor(R.styleable.ModifyItemView_modify_leftTextColor, Color.GRAY);
        rightText.setTextColor(rightTextColor);
        leftText.setTextColor(leftTextColor);

        float textSize = ta.getDimension(R.styleable.ModifyItemView_modify_textSize, getResources().getDimension(R.dimen.default_text_size));
        leftText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        rightText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

        String text = ta.getString(R.styleable.ModifyItemView_modify_text);
        if (text != null) {
            leftText.setText(text);
            // menuTextRight.setText(text);
        }
        ta.recycle();

    }

    public void setInfoTextRight(String text) {
        rightText.setText(text);
    }

    public CharSequence getInfoTextRight() {
        return rightText.getText();
    }

    public void setInfoTextLeft(String text) {
        leftText.setText(text);
    }

    public CharSequence getInfoTextLeft() {
        return leftText.getText();
    }
}
