package utf8.optadvisor.util;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
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

    private double ETF;
    private double sigma;

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

        get50ETF();
    }

    public void setContent(boolean isPrice,boolean isUp){
        this.isPrice=isPrice;
        this.isUp=isUp;
        title.setText(isPrice?"预测价格范围":"预测波动率范围");
        max.setText(isPrice?(isUp?"2.457":"0.000"):isUp?"26.00":"0.00");
        if (isPrice&&!isUp)
            field.setText("价格范围 "+df1.format(0)+"~"+df1.format(ETF));
        if (isPrice&&isUp)
            field.setText("价格范围 "+df1.format(ETF)+"~"+df1.format(4.0));
        if (!isPrice&&!isUp)
            field.setText("波动率范围 "+df2.format(0)+"~"+df2.format(sigma));
        if (!isPrice&&isUp)
            field.setText("波动率范围 "+df2.format(sigma)+"~"+df2.format(50.0));
    }


    private void get50ETF(){
        OkHttpClient client=new OkHttpClient();
        String url="http://hq.sinajs.cn/list=s_sh510050";
        Request request2 = new Request.Builder()
                .url(url)
                .build();
        String response2= null;
        try {
            response2 = client.newCall(request2).execute().body().string();
            ETF=Double.parseDouble(response2.substring(response2.indexOf(",")+1,response2.indexOf(",")+6));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void get50Sigma(){
        OkHttpClient client=new OkHttpClient();
        String url="http://www.optbbs.com/d/csv/d/data.csv?v="+ Calendar.getInstance().getTimeInMillis();
        Request request2 = new Request.Builder()
                .url(url)
                .build();
        String response2= null;
        try {
            response2 = client.newCall(request2).execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] sigams=response2.split("\n");
        String temp="";
        for (String s:sigams){
            if (s.length()>9)
                temp=s;
        }
        this.sigma=Double.parseDouble(temp.substring(temp.indexOf(",")+1,temp.indexOf(" ")));
    }

    public double getETF() {
        return ETF;
    }

    public double getSigma() {
        return sigma;
    }
}
