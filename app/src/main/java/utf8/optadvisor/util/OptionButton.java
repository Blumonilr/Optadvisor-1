package utf8.optadvisor.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import utf8.optadvisor.R;
import utf8.optadvisor.domain.entity.Option;

public class OptionButton extends LinearLayout {

    private Button bt;
    private Option option;

    public OptionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OptionButton(final Context context, Option option) {
        super(context);
        inflate(context,R.layout.button_allocation_option,this);

        bt=(Button)findViewById(R.id.option_ll_bt);
        this.option=option;
    }

    public Button getBt() {
        return bt;
    }

    public void setClicked(final ArrayList<OptionButton> buttons, final UserInfoMenuItem id, final UserInfoMenuItem date, final UserInfoMenuItem soldPrice, final UserInfoMenuItem finalPrice, final UserInfoMenuItem delta, final UserInfoMenuItem gamma, final UserInfoMenuItem theta, final UserInfoMenuItem vega, final UserInfoMenuItem rho){
        bt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                bt.setActivated(true);
                for (OptionButton ob:buttons){
                    if (!ob.getBt().equals(bt))
                        ob.getBt().setActivated(false);
                }

                id.setInfoTextRight(option.getTradeCode());
                date.setInfoTextRight(option.getExpireTime());
                soldPrice.setInfoTextRight(option.getK()+"");
                finalPrice.setInfoTextRight(option.getType()>0?option.getPrice1()+"":option.getPrice2()+"");
                delta.setInfoTextRight(option.getDelta()+"");
                gamma.setInfoTextRight(option.getGamma()+"");
                theta.setInfoTextRight(option.getTheta()+"");
                vega.setInfoTextRight(option.getVega()+"");
                rho.setInfoTextRight(option.getRho()+"");
            }
        });
    }

    public void setText(String id, String num){
        bt.setText(id+"                         "+num);
    }



}
