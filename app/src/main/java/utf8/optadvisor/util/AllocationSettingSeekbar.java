package utf8.optadvisor.util;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import utf8.optadvisor.R;

public class AllocationSettingSeekbar extends LinearLayout {
    TextView title;
    TextView max;
    TextView field;
    SeekBar seekBar;
    boolean isPrice;
    boolean isUp;
    DecimalFormat df1=new DecimalFormat("#0.000");
    DecimalFormat df2=new DecimalFormat("#0.01");

    public AllocationSettingSeekbar(Context context) {
        super(context);
        inflate(context, R.layout.seekbar_prediction,this);
        title=(TextView)findViewById(R.id.allocation_sk_title);
        max=(TextView)findViewById(R.id.allocation_sk_max);
        field=(TextView)findViewById(R.id.allocation_sk_field);
        seekBar=(SeekBar)findViewById(R.id.allocation_sk_progress);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (isPrice&&!isUp)
                    max.setText(df1.format(2.457*(progress/100.0))+"");
                if (isPrice&&isUp)
                    max.setText((df1.format(2.457+2.457*(progress/100.0)))+"");
                if (!isPrice&&!isUp)
                    max.setText(df2.format(26.0*(progress/100.0))+"");
                if (!isPrice&&isUp)
                    max.setText((df2.format(26.0+26*(progress/100.0)))+"");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void setContent(boolean isPrice,boolean isUp){
        this.isPrice=isPrice;
        this.isUp=isUp;
        title.setText(isPrice?"预测价格范围":"预测波动率范围");
        max.setText(isPrice?(isUp?"2.457":"0.000"):isUp?"26.00":"0.00");
        if (isPrice&&!isUp)
            field.setText("价格范围 "+df1.format(0)+"~"+df1.format(2.457));
        if (isPrice&&isUp)
            field.setText("价格范围 "+df1.format(2.457)+"~"+df1.format(2.457*2));
        if (!isPrice&&!isUp)
            field.setText("波动率范围 "+df2.format(0)+"~"+df2.format(26));
        if (!isPrice&&isUp)
            field.setText("波动率范围 "+df2.format(26)+"~"+df2.format(26*2));
    }

}
