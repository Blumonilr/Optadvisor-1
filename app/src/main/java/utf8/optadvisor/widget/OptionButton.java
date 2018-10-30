package utf8.optadvisor.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.text.DecimalFormat;
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

        final DecimalFormat df1=new DecimalFormat("#0.0000");
        DecimalFormat df2=new DecimalFormat("#0.01");

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
                soldPrice.setInfoTextRight(df1.format(option.getK())+"");
                finalPrice.setInfoTextRight(option.getType()>0?df1.format(option.getPrice1())+"":df1.format(option.getPrice2())+"");
                delta.setInfoTextRight(df1.format(option.getDelta())+"");
                gamma.setInfoTextRight(df1.format(option.getGamma())+"");
                theta.setInfoTextRight(df1.format(option.getTheta())+"");
                vega.setInfoTextRight(df1.format(option.getVega())+"");
                rho.setInfoTextRight(df1.format(option.getRho())+"");
            }
        });
    }

    public void setText(String id, String num){
        bt.setText(id+"                         "+num);
    }



}
