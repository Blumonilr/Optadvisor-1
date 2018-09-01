package utf8.optadvisor.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import utf8.optadvisor.R;
import utf8.optadvisor.domain.AllocationResponse;
import utf8.optadvisor.domain.response.ResponseMsg;
import utf8.optadvisor.fragment.AllocationSetting;

public class AllocationSettingPage extends LinearLayout {
    ImageButton ib11;
    ImageButton ib12;
    ImageButton ib13;
    ImageButton ib21;
    ImageButton ib22;
    ImageButton ib23;
    ImageButton ib31;
    ImageButton ib32;
    ImageButton ib33;
    Spinner time;
    LinearLayout linearLayout;
    EditText principle;
    EditText maxLoss;
    AllocationSettingSeekbar price;
    AllocationSettingSeekbar wave;
    private AlertDialog.Builder dialog;

    private char combination;
    private String date;
    private String m0;
    private String k;
    private String p1;
    private String p2;
    private String sigma1;
    private String sigma2;

    private AllocationSetting allocationSetting;
    private AllocationResponse responseAllocation;

    private static final int INFO_SUCCESS = 0;
    private static final int INFO_FAILURE = 1;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage (Message msg) {//此方法在ui线程运行
            switch (msg.what) {
                case INFO_SUCCESS:
                    String info = (String) msg.obj;
                    System.out.println(info);
                    AllocationSettingPage.this.responseAllocation=new Gson().fromJson(info,AllocationResponse.class);
                    allocationSetting.setView(responseAllocation);
                    break;
                case INFO_FAILURE:
                    System.out.println("1fail");
                    break;
            }
        }
    };

    public AllocationSettingPage(final Context context,final AllocationSetting allocationSetting) {
        super(context);
        inflate(context, R.layout.linearlayout_allocation_setting, this);
        this.allocationSetting=allocationSetting;

        initDialog();
        price=new AllocationSettingSeekbar(context,true,true,allocationSetting);
        wave=new AllocationSettingSeekbar(context,false,true,allocationSetting);

        ib11=(ImageButton)findViewById(R.id.allocation_ib_11);
        ib12=(ImageButton)findViewById(R.id.allocation_ib_12);
        ib13=(ImageButton)findViewById(R.id.allocation_ib_13);
        ib21=(ImageButton)findViewById(R.id.allocation_ib_21);
        ib22=(ImageButton)findViewById(R.id.allocation_ib_22);
        ib23=(ImageButton)findViewById(R.id.allocation_ib_23);
        ib31=(ImageButton)findViewById(R.id.allocation_ib_31);
        ib32=(ImageButton)findViewById(R.id.allocation_ib_32);
        ib33=(ImageButton)findViewById(R.id.allocation_ib_33);
        principle=(EditText)findViewById(R.id.allocation_et_principle);
        maxLoss=(EditText)findViewById(R.id.allocation_et_maxloss);
        linearLayout=(LinearLayout)findViewById(R.id.allocation_setting_ll);

        ib11.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ib11.setActivated(true);
                ib12.setActivated(false);
                ib13.setActivated(false);
                ib21.setActivated(false);
                ib22.setActivated(false);
                ib23.setActivated(false);
                ib31.setActivated(false);
                ib32.setActivated(false);
                ib33.setActivated(false);
                linearLayout.removeAllViews();
                price=new AllocationSettingSeekbar(context,true,true,allocationSetting);
                price.setContent(true,true);
                linearLayout.addView(price);
                wave=new AllocationSettingSeekbar(context,false,true,allocationSetting);
                wave.setContent(false,true);
                linearLayout.addView(wave);
                combination='A';
            }
        });

        ib12.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                ib11.setActivated(false);
                ib12.setActivated(true);
                ib13.setActivated(false);
                ib21.setActivated(false);
                ib22.setActivated(false);
                ib23.setActivated(false);
                ib31.setActivated(false);
                ib32.setActivated(false);
                ib33.setActivated(false);
                linearLayout.removeAllViews();
                wave=new AllocationSettingSeekbar(context,false,true,allocationSetting);
                wave.setContent(false,true);
                linearLayout.addView(wave);
                combination='B';
            }
        });

        ib13.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                ib11.setActivated(false);
                ib12.setActivated(false);
                ib13.setActivated(true);
                ib21.setActivated(false);
                ib22.setActivated(false);
                ib23.setActivated(false);
                ib31.setActivated(false);
                ib32.setActivated(false);
                ib33.setActivated(false);
                linearLayout.removeAllViews();
                price=new AllocationSettingSeekbar(context,true,false,allocationSetting);
                price.setContent(true,false);
                linearLayout.addView(price);
                wave=new AllocationSettingSeekbar(context,false,true,allocationSetting);
                wave.setContent(false,true);
                linearLayout.addView(wave);
                combination='C';
            }
        });

        ib21.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                ib11.setActivated(false);
                ib21.setActivated(true);
                ib13.setActivated(false);
                ib12.setActivated(false);
                ib22.setActivated(false);
                ib23.setActivated(false);
                ib31.setActivated(false);
                ib32.setActivated(false);
                ib33.setActivated(false);
                linearLayout.removeAllViews();
                price=new AllocationSettingSeekbar(context,true,true,allocationSetting);
                price.setContent(true,true);
                linearLayout.addView(price);
                combination='D';
            }
        });

        ib22.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                ib11.setActivated(false);
                ib13.setActivated(false);
                ib21.setActivated(false);
                ib12.setActivated(false);
                ib23.setActivated(false);
                ib31.setActivated(false);
                ib32.setActivated(false);
                ib33.setActivated(false);
                Toast.makeText(context,"此按钮不可选",Toast.LENGTH_SHORT).show();
            }
        });

        ib23.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                ib11.setActivated(false);
                ib23.setActivated(true);
                ib13.setActivated(false);
                ib21.setActivated(false);
                ib22.setActivated(false);
                ib12.setActivated(false);
                ib31.setActivated(false);
                ib32.setActivated(false);
                ib33.setActivated(false);
                linearLayout.removeAllViews();
                price=new AllocationSettingSeekbar(context,true,false,allocationSetting);
                price.setContent(true,false);
                linearLayout.addView(price);
                combination='E';
            }
        });

        ib31.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                ib11.setActivated(false);
                ib31.setActivated(true);
                ib13.setActivated(false);
                ib21.setActivated(false);
                ib22.setActivated(false);
                ib23.setActivated(false);
                ib12.setActivated(false);
                ib32.setActivated(false);
                ib33.setActivated(false);
                linearLayout.removeAllViews();
                price=new AllocationSettingSeekbar(context,true,true,allocationSetting);
                price.setContent(true,true);
                linearLayout.addView(price);
                wave=new AllocationSettingSeekbar(context,false,false,allocationSetting);
                wave.setContent(false,false);
                linearLayout.addView(wave);
                combination='F';
            }
        });

        ib32.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                ib11.setActivated(false);
                ib32.setActivated(true);
                ib13.setActivated(false);
                ib21.setActivated(false);
                ib22.setActivated(false);
                ib23.setActivated(false);
                ib31.setActivated(false);
                ib12.setActivated(false);
                ib33.setActivated(false);
                linearLayout.removeAllViews();
                wave=new AllocationSettingSeekbar(context,false,false,allocationSetting);
                wave.setContent(false,false);
                linearLayout.addView(wave);
                combination='G';
            }
        });

        ib33.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                ib11.setActivated(false);
                ib33.setActivated(true);
                ib13.setActivated(false);
                ib21.setActivated(false);
                ib22.setActivated(false);
                ib23.setActivated(false);
                ib31.setActivated(false);
                ib32.setActivated(false);
                ib12.setActivated(false);
                linearLayout.removeAllViews();
                price=new AllocationSettingSeekbar(context,true,false,allocationSetting);
                price.setContent(true,false);
                linearLayout.addView(price);
                wave=new AllocationSettingSeekbar(context,false,false,allocationSetting);
                wave.setContent(false,false);
                linearLayout.addView(wave);
                combination='H';
            }
        });

        time=(Spinner)findViewById(R.id.allocation_spr_validtime);
        time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        List<String> array=new ArrayList<>();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
        Calendar now=Calendar.getInstance();
        array.add(sdf.format(now.getTime()));
        now.add(Calendar.MONTH,1);
        array.add(sdf.format(now.getTime()));
        int i=0;
        while (i<2){
            now.add(Calendar.MONTH,1);
            if ((now.get(Calendar.MONTH)+1)%3==0) {
                array.add(sdf.format(now.getTime()));
                i++;
            }
        }
        SpinnerAdapter adapter=new ArrayAdapter<String>(context,android.R.layout.simple_dropdown_item_1line,array);
        time.setAdapter(adapter);

        Button bt=(Button)findViewById(R.id.allocation_setting_next);
        bt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Map<String,String> values=new HashMap<>();
                values.put("m0",getM0());
                values.put("k",getK());
                values.put("t",getDate());
                values.put("combination",getCombination()+"");
                values.put("p1",getP1());
                values.put("p2",getP2());
                values.put("sigma1",getSigma1());
                values.put("sigma2",getSigma2());

                NetUtil.INSTANCE.sendPostRequest(NetUtil.SERVER_BASE_ADDRESS + "/recommend/recommendPortfolio", values,getContext(), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        dialog.setTitle("网络连接错误");
                        dialog.setMessage("请稍后再试");
                        dialogShow();

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        ResponseMsg responseMsg = NetUtil.INSTANCE.parseJSONWithGSON(response);
                        System.out.println(responseMsg.getData().toString());
                        mHandler.obtainMessage(INFO_SUCCESS,responseMsg.getData().toString()).sendToTarget();
                    }
                });
            }

        });

    }

    public char getCombination() {
        return combination;
    }

    public String getDate() {
        return time.getSelectedItem().toString();
    }

    public String getM0() {
        return principle.getText().toString();
    }

    public String getK() {
        return maxLoss.getText().toString();
    }

    public String getP1() {
        switch (combination) {
            case 'A' :
                p1=price.getETF()+"";
                break;
            case 'B':
                p1=wave.getETF()+"";
                break;
            case 'C':
                p1="0";
                break;
            case 'D' :
                p1=price.getETF()+"";
                break;
            case 'E':
                p1="0";
                break;
            case 'F':
                p1=price.getETF()+"";
                break;
            case 'G' :
                p1=wave.getETF()+"";
                break;
            case 'H':
                p1="0";
                break;

        }

        return p1;
    }

    public String getP2() {
        switch (combination) {
            case 'A' :
                p2="4";
                break;
            case 'B':
                p2=wave.getETF()+"";
                break;
            case 'C':
                p2=price.getETF()+"";
                break;
            case 'D' :
                p2="4";
                break;
            case 'E':
                p2=price.getETF()+"";
                break;
            case 'F':
                p2="4";
                break;
            case 'G' :
                p2=wave.getETF()+"";
                break;
            case 'H':
                p2=price.getETF()+"";
                break;

        }
        return p2;
    }

    public String getSigma1() {
        switch (combination) {
            case 'A' :
                sigma1=wave.getSigma()+"";
                break;
            case 'B':
                sigma1=wave.getSigma()+"";
                break;
            case 'C':
                sigma1=wave.getSigma()+"";
                break;
            case 'D' :
                sigma1=price.getSigma()+"";
                break;
            case 'E':
                sigma1=price.getSigma()+"";;
                break;
            case 'F':
                sigma1="0";
                break;
            case 'G' :
                sigma1="0";
                break;
            case 'H':
                sigma1="0";
                break;

        }
        return sigma1;
    }

    public String getSigma2() {
        switch (combination) {
            case 'A' :
                sigma2="50";
                break;
            case 'B':
                sigma2="50";
                break;
            case 'C':
                sigma2="50";
                break;
            case 'D' :
                sigma2=price.getSigma()+"";
                break;
            case 'E':
                sigma2=price.getSigma()+"";;
                break;
            case 'F':
                sigma2=wave.getSigma()+"";
                break;
            case 'G' :
                sigma2=wave.getSigma()+"";
                break;
            case 'H':
                sigma2=wave.getSigma()+"";
                break;

        }
        return sigma2;
    }

    private void initDialog(){
        dialog=new AlertDialog.Builder(allocationSetting.getActivity());
        dialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
    }
    private void dialogShow(){
        allocationSetting.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.show();
            }
        });
    }
}
