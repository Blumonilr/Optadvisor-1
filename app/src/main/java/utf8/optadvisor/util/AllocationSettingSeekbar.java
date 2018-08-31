package utf8.optadvisor.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
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
import utf8.optadvisor.fragment.AllocationSetting;

public class AllocationSettingSeekbar extends LinearLayout {

    private static final int INFO_SUCCESS = 0;//获取50etf成功的标识
    private static final int INFO_FAILURE = 1;//获取50etf失败的标识
    private static final int SIGMA_SUCCESS = 2;//获取50etf成功的标识
    private static final int SIGMA_FAILURE = 3;//获取50etf失败的标识

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

    private AlertDialog.Builder dialog;

    private AllocationSetting setting;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage (Message msg) {//此方法在ui线程运行
            switch (msg.what) {
                case INFO_SUCCESS:
                    String info = (String) msg.obj;
                    set50ETF(Double.parseDouble(info.substring(info.indexOf(",") + 1, info.indexOf(",") + 6)));
                    break;
                case INFO_FAILURE:
                    System.out.println("1fail");
                    break;
                case SIGMA_SUCCESS:
                    String response2=(String)msg.obj;
                    String[] sigams = response2.split("\n");
                    String temp = "";
                    for (String s : sigams) {
                        if (s.length() > 9)
                            temp = s;
                        else
                            break;
                    }
                    set50sigma(Double.parseDouble(temp.substring(temp.indexOf(",")+1,temp.indexOf(" "))));
                    break;
                case SIGMA_FAILURE:
                    System.out.println("2fail");
            }
        }
    };

    public AllocationSettingSeekbar(Context context, boolean Price, boolean Up, AllocationSetting setting) {
        super(context);
        inflate(context, R.layout.seekbar_prediction,this);

        this.isPrice=Price;
        this.isUp=Up;
        this.setting=setting;

        get50ETF();
        get50Sigma();

        title=(TextView)findViewById(R.id.allocation_sk_title);
        max=(TextView)findViewById(R.id.allocation_sk_max);
        field=(TextView)findViewById(R.id.allocation_sk_field);
        seekBar=(SeekBar)findViewById(R.id.allocation_sk_progress);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (isPrice&&!isUp) {
                    max.setText(df1.format(ETF * (progress / 100.0)) + "");
                    setContent(isPrice,isUp);
                }
                if (isPrice&&isUp) {
                    max.setText((df1.format(ETF + (4.0 - ETF) * (progress / 100.0))) + "");
                    setContent(isPrice,isUp);
                }
                if (!isPrice&&!isUp) {
                    max.setText(df2.format(sigma * (progress / 100.0)) + "");
                    setContent(isPrice, isUp);
                }
                if (!isPrice&&isUp) {
                    max.setText((df2.format(sigma + (50.0 - sigma) * (progress / 100.0))) + "");
                    setContent(isPrice, isUp);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        initDialog();
    }

    private void setContent(boolean isPrice,boolean isUp){
        System.out.println(isPrice);
        title.setText(isPrice?"预测价格范围":"预测波动率范围");
        max.setText(isPrice?(isUp?ETF+"":"0.000"):isUp?sigma+"":"0.00");
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
        NetUtil.INSTANCE.sendGetRequest("http://hq.sinajs.cn/list=s_sh510050", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.obtainMessage(INFO_FAILURE).sendToTarget();
                Toast.makeText(AllocationSettingSeekbar.this.getContext(),"网络连接错误",Toast.LENGTH_SHORT);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                mHandler.obtainMessage(INFO_SUCCESS,response.body().string()).sendToTarget();
            }
        });
    }

    private void get50Sigma(){
        NetUtil.INSTANCE.sendGetRequest("http://www.optbbs.com/d/csv/d/data.csv?v=" + Calendar.getInstance().getTimeInMillis(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.obtainMessage(SIGMA_FAILURE).sendToTarget();
                dialog.setTitle("网络连接错误");
                dialog.setMessage("请稍后再试");
                dialogShow();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                mHandler.obtainMessage(SIGMA_SUCCESS,response.body().string()).sendToTarget();
            }
        });
    }

    public double getETF() {
        return Double.parseDouble(max.getText().toString());
    }

    public double getSigma() {
        return Double.parseDouble(max.getText().toString());
    }

    private void set50ETF(double a50ETF) {
        this.ETF = a50ETF;
    }

    private void set50sigma(double a50sigma) {
        this.sigma = a50sigma;
    }

    private void initDialog(){
        dialog=new AlertDialog.Builder(setting.getActivity());
        dialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
    }
    private void dialogShow(){
        setting.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.show();
            }
        });
    }
}
