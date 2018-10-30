package utf8.optadvisor.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import utf8.optadvisor.R;
import utf8.optadvisor.domain.entity.Option;
import utf8.optadvisor.util.NetUtil;

public class MoreInfoActivity extends AppCompatActivity {
    private TextView optionCode;
    private TextView theoriticValue;
    private TextView valueState;
    private TextView innerValue;
    private TextView timeValue;
    private TextView dealAmount;
    private TextView optionVolatility;
    private TextView optionDelta;
    private TextView optionGamma;
    private TextView optionTheta;
    private TextView optionVega;
    private TextView optionMaxPrice;
    private TextView optionMinPrice;
    private TextView m_sale1;
    private TextView m_sale1_2;
    private TextView m_sale2;
    private TextView m_sale2_2;
    private TextView m_sale3;
    private TextView m_sale3_2;
    private TextView m_sale4;
    private TextView m_sale4_2;
    private TextView m_sale5;
    private TextView m_sale5_2;
    private TextView m_buy1;
    private TextView m_buy1_2;
    private TextView m_buy2;
    private TextView m_buy2_2;
    private TextView m_buy3;
    private TextView m_buy3_2;
    private TextView m_buy4;
    private TextView m_buy4_2;
    private TextView m_buy5;
    private TextView m_buy5_2;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);
        optionCode= findViewById(R.id.more1);
        theoriticValue= findViewById(R.id.more2);
        valueState= findViewById(R.id.more3);
        innerValue= findViewById(R.id.more4);
        timeValue= findViewById(R.id.more5);
        dealAmount= findViewById(R.id.more6);
        optionVolatility= findViewById(R.id.more7);
        optionDelta= findViewById(R.id.more8);
        optionGamma= findViewById(R.id.more9);
        optionTheta= findViewById(R.id.more10);
        optionVega= findViewById(R.id.more11);
        optionMaxPrice= findViewById(R.id.more12);
        optionMinPrice= findViewById(R.id.more13);
        m_buy1=findViewById(R.id.m_buy1);
        m_buy1_2=findViewById(R.id.m_buy1_2);
        m_buy2=findViewById(R.id.m_buy2);
        m_buy2_2=findViewById(R.id.m_buy2_2);
        m_buy3=findViewById(R.id.m_buy3);
        m_buy3_2=findViewById(R.id.m_buy3_2);
        m_buy4=findViewById(R.id.m_buy4);
        m_buy4_2=findViewById(R.id.m_buy4_2);
        m_buy5=findViewById(R.id.m_buy5);
        m_buy5_2=findViewById(R.id.m_buy5_2);
        m_sale1=findViewById(R.id.m_sale1);
        m_sale1_2=findViewById(R.id.m_sale1_2);
        m_sale2=findViewById(R.id.m_sale2);
        m_sale2_2=findViewById(R.id.m_sale2_2);
        m_sale3=findViewById(R.id.m_sale3);
        m_sale3_2=findViewById(R.id.m_sale3_2);
        m_sale4=findViewById(R.id.m_sale4);
        m_sale4_2=findViewById(R.id.m_sale4_2);
        m_sale5=findViewById(R.id.m_sale5);
        m_sale5_2=findViewById(R.id.m_sale5_2);
        initToolbar();
        Intent intent=getIntent();
        final int type=intent.getIntExtra("type",1);
        String num=intent.getStringExtra("num");

/*        NetUtil.INSTANCE.sendGetRequest("http://hq.sinajs.cn/list=CON SO" + num.substring(7), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String soValue=response.body().string();
            }
        });*/
        NetUtil.INSTANCE.sendGetRequest("http://hq.sinajs.cn/list=CON_SO_"+num.substring(7) ,new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String optionDetail=response.body().string();
                currentOptionChange(optionDetail,type);

            }
        });
        NetUtil.INSTANCE.sendGetRequest("http://hq.sinajs.cn/list="+num, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            //

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
            String s=response.body().string();
            drawPan(s);

            }
        });

    }

    private void currentOptionChange(final String optionDetail, final int cp){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(optionDetail!=null&&optionDetail.contains("\"")) {
                    int index = optionDetail.indexOf("\"");
                    String str1 = optionDetail.substring(index + 1).replace(",,,,", ",");
                    String[] result = str1.split(",");
                    if(result.length==14) {
                        optionCode.setText(String.format("%s", result[9]));
                        dealAmount.setText(String.format("%s", result[1]));
                        optionDelta.setText(String.format("%s", result[2]));
                        optionGamma.setText(String.format("%s", result[3]));
                        optionTheta.setText(String.format("%s", result[4]));
                        optionVega.setText(String.format("%s", result[5]));
                        optionVolatility.setText(String.format("%s", result[6]));
                        optionMaxPrice.setText(String.format("%s", result[7]));
                        optionMinPrice.setText(String.format("%s", result[8]));

                        final double latestPrice=Double.parseDouble(result[11]);
                        final double usePrice=Double.parseDouble(result[10]);

                        theoriticValue.setText(String.format("%s", result[12]));
                        NetUtil.INSTANCE.sendGetRequest("http://hq.sinajs.cn/list=s_sh510050,sh510050", new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String etfValue = response.body().string();
                                try {
                                    setOtherOptionValue(latestPrice, usePrice, etfValue, cp);
                                }catch (NumberFormatException e){
                                    /*dialog.setTitle("网络错误");
                                    dialog.setMessage("期权数据计算可能存在误差");
                                    dialogShow();*/
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    private void setOtherOptionValue(final double latestPrice, final double usePrice, final String etfValue, final int cp)throws NumberFormatException{
            runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int index=etfValue.indexOf("50ETF,");
                String temp1=etfValue.substring(index+"50ETF,".length());
                index=temp1.indexOf(",");
                double priceMark=Double.parseDouble(temp1.substring(0,index))-usePrice;
                double innerPrice;
                if(cp==1){
                    if(priceMark>0){
                        valueState.setText("实值");
                        innerPrice=priceMark;
                    }
                    else{
                        if(priceMark==0){
                            valueState.setText("平值");
                        }else {
                            valueState.setText("虚值");
                        }
                        innerPrice=0;
                    }
                    double timeValueText=((latestPrice-innerPrice)<=0)?0:latestPrice-innerPrice;
                    innerValue.setText(String.format("%.4f", innerPrice));
                    timeValue.setText(String.format("%.4f", timeValueText));
                }else if(cp==-1){
                    if(priceMark<0){
                        valueState.setText("实值");
                        innerPrice=-priceMark;
                    }
                    else{
                        if(priceMark==0){
                            valueState.setText("平值");
                        }else {
                            valueState.setText("虚值");
                        }
                        innerPrice=0;
                    }
                    double timeValueText=latestPrice-innerPrice;
                    innerValue.setText(String.format("%.4f", innerPrice));
                    timeValue.setText(String.format("%.4f", timeValueText));
                }
            }

        });
    }
    private void drawPan(String optionInfo){
        String[] s=optionInfo.split(",");
        m_sale5_2.setText(s[12]);
        m_sale5.setText(s[13]);
        m_sale4_2.setText(s[14]);
        m_sale4.setText(s[15]);
        m_sale3_2.setText(s[16]);
        m_sale3.setText(s[17]);
        m_sale2_2.setText(s[18]);
        m_sale2.setText(s[19]);
        m_sale1_2.setText(s[20]);
        m_sale1.setText(s[21]);
        m_buy1_2.setText(s[22]);
        m_buy1.setText(s[23]);
        m_buy2_2.setText(s[24]);
        m_buy2.setText(s[25]);
        m_buy3_2.setText(s[26]);
        m_buy3.setText(s[27]);
        m_buy4_2.setText(s[28]);
        m_buy4.setText(s[29]);
        m_buy5_2.setText(s[30]);
        m_buy5.setText(s[31]);

    }
    private void initToolbar(){
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    /**
     * 重写Home键实现滑动切换界面
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                exit();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        exit();
    }
    /**
     * 滑动退出
     */
    private void exit(){
        finish();
        overridePendingTransition(R.anim.activity_left_enter,R.anim.activity_right_exit);
    }

}
