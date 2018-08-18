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

public class OptionButton extends LinearLayout {

    private Button bt;
    private String ID;

    public OptionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OptionButton(final Context context,String id) {
        super(context);
        inflate(context,R.layout.button_allocation_option,this);

        bt=(Button)findViewById(R.id.option_ll_bt);
        this.ID=id;
    }

    public Button getBt() {
        return bt;
    }

    public void setClicked(final ArrayList<OptionButton> buttons,final UserInfoMenuItem id){
        bt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                bt.setActivated(true);
                for (OptionButton ob:buttons){
                    if (!ob.getBt().equals(bt))
                        ob.getBt().setActivated(false);
                }

                id.setInfoTextRight(ID);
            }
        });
    }

    public void setText(String id, String num){
        bt.setText(id+"                         "+num);
    }



}
