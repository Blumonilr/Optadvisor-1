package utf8.optadvisor.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

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
import utf8.optadvisor.widget.DoubleSeekbar;

public class AllocationSettingSeekbar extends LinearLayout {

    private static final int INFO_SUCCESS = 0;//获取50etf成功的标识
    private static final int INFO_FAILURE = 1;//获取50etf失败的标识
    private static final int SIGMA_SUCCESS = 2;//获取50etf成功的标识
    private static final int SIGMA_FAILURE = 3;//获取50etf失败的标识

    TextView title;
    TextView min;
    TextView max;
    DoubleSeekbar seekBar;
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
                    max=(TextView)findViewById(R.id.allocation_sk_max);
                    min=(TextView)findViewById(R.id.allocation_sk_min);
                    if (isPrice&&isUp){
                        min.setText(ETF+"");
                        max.setText("4.000");
                    }
                    else if (isPrice&&!isUp){
                        min.setText("1.000");
                        max.setText(ETF+"");
                    }
                    break;
                case INFO_FAILURE:
                    System.out.println("1fail");
                    break;
                case SIGMA_SUCCESS:
                    String response2=(String)msg.obj;
                    String[] sigmas = response2.split("\n");
                    String temp = "";
                    for (String s : sigmas) {
                        if ((s.charAt(0)=='9'&&s.length() > 10)||(s.charAt(0)!='9'&&s.length()>11)&&!s.contains("N"))
                            temp = s;
                        else
                            break;
                    }
                    Log.d("资产配置",temp);
                    set50sigma(Double.parseDouble(temp.substring(temp.indexOf(",")+1,temp.lastIndexOf(","))));
                    max=(TextView)findViewById(R.id.allocation_sk_max);
                    min=(TextView)findViewById(R.id.allocation_sk_min);
                    if (!isPrice&&isUp){
                        min.setText(sigma+"");
                        max.setText(50.00+"");
                    }
                    else if (!isPrice&&!isUp){
                        min.setText("0.00");
                        max.setText(sigma+"");
                    }
                    break;
                case SIGMA_FAILURE:
                    System.out.println("sigmaFail");
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
        if(isPrice){
            title.setText("预测价格范围");
        }
        else
            title.setText("预测波动率范围");

        seekBar=(DoubleSeekbar)findViewById(R.id.allocation_sk_progress);

        seekBar.setOnSeekBarChangeListener(new DoubleSeekbar.OnSeekBarChangeListener(){


            @Override
            public void onProgressBefore() {

            }

            @Override
            public void onProgressChanged(DoubleSeekbar seekBar, int progressLow, int progressHigh) {

                if (progressHigh<progressLow){
                    int temp=progressHigh;
                    progressHigh=progressLow;
                    progressLow=temp;
                }

                if (isPrice&&isUp){
                    min.setText(df1.format(ETF+(4.0-ETF)*(progressLow/100.0)));
                    max.setText(df1.format(ETF+(4.0-ETF)*(progressHigh/100.0)));
                }
                else if (isPrice&&!isUp){
                    min.setText(df1.format(1+(ETF-1.0)*(progressLow/100.0)));
                    max.setText(df1.format(1+(ETF-1.0)*(progressHigh/100.0)));
                }
                else if (!isPrice&&isUp){
                    min.setText(df2.format(sigma + (50.0 - sigma) * (progressLow / 100.0)));
                    max.setText(df2.format(sigma + (50.0 - sigma) * (progressHigh / 100.0)));
                }
                else if (!isPrice&&!isUp){
                    min.setText(df2.format(sigma * (progressLow / 100.0)));
                    max.setText(df2.format(sigma * (progressHigh / 100.0)));
                }
            }

            @Override
            public void onProgressAfter() {

            }
        });

        initDialog();
    }


    private void get50ETF(){
        NetUtil.INSTANCE.sendGetRequest("http://hq.sinajs.cn/list=s_sh510050", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.obtainMessage(INFO_FAILURE).sendToTarget();
                dialog.setTitle("网络连接错误");
                dialog.setMessage("请稍后再试");
                dialogShow();
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
        return ETF;
    }

    public double getSigma() {
        return sigma;
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

    public double getMin(){
        return Double.parseDouble(min.getText().toString());
    }

    public double getMax(){
        return Double.parseDouble(max.getText().toString());
    }
}
