package utf8.optadvisor.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import utf8.optadvisor.R;
import utf8.optadvisor.activity.MainActivity;
import utf8.optadvisor.domain.response.ResponseMsg;
import utf8.optadvisor.util.ControllerAdapter;
import utf8.optadvisor.util.LeftAdapter;
import utf8.optadvisor.util.NetUtil;
import utf8.optadvisor.util.CustomOption;
import utf8.optadvisor.util.SyncHorizontalScrollView;


public class DIY extends Fragment {
    private View view;
    private List<String[]> list=new ArrayList<>();
    private String expireTime;
    private Boolean call_or_put=true;
    private List<String> spinner_items=new ArrayList<>();
    private List<String[]> left_data=new ArrayList<>();
    private List<Integer> controller_data=new ArrayList<>();
    private ArrayAdapter<String> spinner_adapter;
    private LeftAdapter leftAdapter=new LeftAdapter(left_data);
    private ControllerAdapter controllerAdapter=new ControllerAdapter(controller_data);
    private Button fb;
    private LinearLayoutManager optionsLayoutManager;
    private LinearLayoutManager controllersLayoutManager;
    private RecyclerView options_view;
    private RecyclerView controllers_view;
    private Spinner spinner;
    private final int SUCCESS=0;
    private final int FAILURE=1;
    private final int MONTHS_GET=2;
    private final int MONTHS_FAIL=3;
    private final int GET_EXPIRETIME=4;
    private final int SEND_DATA=5;
    private Button bt1;
    private List<CustomOption> temp1;
    private int[] temp_map1;
    private int[] temp_map2;
    private List<CustomOption> temp2;
    private MyCombination myCombination;
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;
    private AlertDialog.Builder dialog;
    private Button bt2;
    private Handler mHandler = new Handler() {
        public void handleMessage (Message msg) {//此方法在ui线程运行
            switch(msg.what) {
                case SUCCESS:
                    if(left_data.size()>=1) {
                        left_data.clear();
                    }
                    for(String[] item:((List<String[]>)msg.obj)) {
                        left_data.add(item);
                    }
                    if(controller_data.size()>=1){
                        controller_data.clear();
                    }
                    if(call_or_put==true) {
                        for (int i = 0; i < ((List<String[]>) msg.obj).size(); i++) {
                            if(temp_map1!=null){
                                controller_data.add(temp_map1[i]);
                            }else{
                                controller_data.add(0);
                            }
                        }
                    }else{
                        for (int i = 0; i < ((List<String[]>) msg.obj).size(); i++) {
                            if(temp_map2!=null){
                                controller_data.add(temp_map2[i]);
                            }else{
                                controller_data.add(0);
                            }
                        }
                    }
                    leftAdapter.notifyDataSetChanged();
                    controllerAdapter.notifyDataSetChanged();

                 break;
                case FAILURE:
                    break;
                case MONTHS_FAIL:
                    break;
                case MONTHS_GET:
                    for(int i=1;i<((String[])msg.obj).length;i++) {
                        spinner_items.add(((String[])msg.obj)[i]);
                    }
                    expireTime=((String[])msg.obj)[1];
                    spinner_adapter.notifyDataSetChanged();
                    initOptionInfo(spinner_items.get(0),call_or_put);
                 break;
                case GET_EXPIRETIME:
                    String et=(String)msg.obj;
                    sendDIY(et);
                    break;
                case SEND_DATA:
                    String value=(String) msg.obj;
                    value="{\"options\""+value.substring(value.indexOf("optionList")+11,value.length()-2)+",\"name\": \"portfolioName\",\n" +
                            "\t\"type\": 2,\n" +
                            "\t\"trackingStatus\": false"+"}";
                    System.out.println("DIY传数据库"+value);
                    NetUtil.INSTANCE.sendPostRequest(NetUtil.SERVER_BASE_ADDRESS + "/portfolio",value, getContext(), new okhttp3.Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                }
                            });
                            dialog.setTitle("网络连接错误");
                            dialog.setMessage("请稍后再试");
                            dialogShow();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(temp1!=null) {
                                        temp1.clear();
                                    }
                                    if(temp2!=null) {
                                        temp2.clear();
                                    }
                                   temp_map1=null;
                                   temp_map2=null;
                                   initMonths();
                                   progressDialog.dismiss();
                                }
                            });
                        }
                    });



            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_diy, container, false);
        spinner_adapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,spinner_items);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        progressBar=view.findViewById(R.id.diy_progress_bar);
        initDialog();
        initMonths();
        //进度条时间
        final CountDownTimer timer = new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                progressBar.setVisibility(View.GONE);
            }
        };
        timer.start();
        //选时间
        spinner=view.findViewById(R.id.choose_month2);
        spinner.setAdapter(spinner_adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                expireTime=spinner_items.get(position);
                initOptionInfo(spinner_items.get(position),call_or_put);
                if(progressBar.getVisibility()== View.GONE){
                    progressBar.setVisibility(View.VISIBLE);
                    timer.start();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //

            }
        });
        //切换看涨看跌
        bt1=view.findViewById(R.id.diy_call);
        bt2=view.findViewById(R.id.diy_put);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(call_or_put!=true){
                    saveTemp2();
                    call_or_put=true;
                    initOptionInfo(spinner.getSelectedItem().toString(),call_or_put);
                    if(progressBar.getVisibility()== View.GONE){
                        progressBar.setVisibility(View.VISIBLE);
                        timer.start();
                    }
                }
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(call_or_put!=false){
                    saveTemp1();
                    call_or_put=false;
                    initOptionInfo(spinner.getSelectedItem().toString(),call_or_put);
                    if(progressBar.getVisibility()== View.GONE){
                        progressBar.setVisibility(View.VISIBLE);
                        timer.start();
                    }
                }
            }
        });
        //初始化RecyclerView数据
        options_view=view.findViewById(R.id.diy_options);
        controllers_view=view.findViewById(R.id.quantity_controller);
        optionsLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        controllersLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        options_view.setLayoutManager(optionsLayoutManager);
        controllers_view.setLayoutManager(controllersLayoutManager);
        options_view.setAdapter(leftAdapter);
        controllers_view.setAdapter(controllerAdapter);

        //垂直同步
        syncScroll();

        //水平同步
        SyncHorizontalScrollView left1=(SyncHorizontalScrollView) view.findViewById(R.id.scroll1);
        SyncHorizontalScrollView left2=(SyncHorizontalScrollView) view.findViewById(R.id.scroll2);
        left1.setScrollView(left2);
        left2.setScrollView(left1);

        //提交按钮
        fb=view.findViewById(R.id.diy_finish);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            OkHttpClient client=new OkHttpClient();
                            Request request=new Request.Builder()
                                    .url("http://stock.finance.sina.com.cn/futures/api/openapi.php/StockOptionService.getRemainderDay?date="+expireTime)
                                    .build();
                            Response response=client.newCall(request).execute();
                            String s=response.body().string();
                            s=s.substring(s.indexOf("expireDay")+12,s.indexOf("expireDay")+22);
                            mHandler.obtainMessage(GET_EXPIRETIME,s).sendToTarget();
                            Looper.prepare();
                            progressDialog=new ProgressDialog(getContext());
                            progressDialog.setTitle("请稍等");
                            progressDialog.setMessage("Loading...");
                            progressDialog.show();
                            progressDialog.setCancelable(true);
                            Looper.loop();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }).start();



            }
        });



        return view;
    }
    private void sendDIY(String et){
        int cp=1;
        if(call_or_put==false){
            cp=-1;
        }
        int[] temp;
        if(call_or_put==false) {
            temp = controllerAdapter.getV2();
        }
        else {
            temp=controllerAdapter.getV1();
        }
        List<CustomOption> option_list=new ArrayList<>();
        for(int k=0;k<temp.length;k++){
            if(temp[k]!=0) {
                String code = list.get(0)[k];
                option_list.add(new CustomOption(et, temp[k], cp, code));
            }
        }
        if(call_or_put==true){
            if(temp2!=null) {
                for (int j = 0; j < temp2.size(); j++) {
                    option_list.add(new CustomOption(et, temp2.get(j).getType(), temp2.get(j).getCp(), temp2.get(j).getOptionCode()));
                }
            }
        }else{
            if(temp1!=null) {
                for (int j = 0; j < temp1.size(); j++) {
                    option_list.add(new CustomOption(et, temp1.get(j).getType(), temp1.get(j).getCp(), temp1.get(j).getOptionCode()));
                }
            }
        }
        Gson gson=new Gson();
        String value=gson.toJson(option_list);
        value="{\"options\": "+value+"}";
        System.out.println("DIY json"+value);
        NetUtil.INSTANCE.sendPostRequest(NetUtil.SERVER_BASE_ADDRESS + "/recommend/customPortfolio", value, getContext(), new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                    }
                });
                dialog.setTitle("网络连接错误");
                dialog.setMessage("请稍后再试");
                dialogShow();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
               String value=response.body().string();
               if(value.contains("1008")){
                   getActivity().runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           progressDialog.dismiss();
                       }
                   });
                   dialog.setTitle("网络连接错误");
                   dialog.setMessage("请重试");
                   dialogShow();
               }else {
                   mHandler.obtainMessage(SEND_DATA, value).sendToTarget();
               }
            }
        });
    }



    private void initMonths(){
        NetUtil.INSTANCE.sendGetRequest("http://stock.finance.sina.com.cn/futures/api/openapi.php/StockOptionService.getStockName", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.obtainMessage(MONTHS_FAIL).sendToTarget();
                dialog.setTitle("网络连接错误");
                dialog.setMessage("请稍后再试");
                dialogShow();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string=response.body().string();
                String[] months=string.substring(string.indexOf("contractMonth")+17,string.lastIndexOf("]")-1).split("\",\"");
                mHandler.obtainMessage(MONTHS_GET,months).sendToTarget();
            }
        });

    }

    private void initOptionInfo(final String month, final boolean call_or_put){
        new Thread(new Runnable(){
            @Override
            public void run(){
                try{
                    OkHttpClient client=new OkHttpClient();
                    //获取各月期权代号
                    list =new ArrayList<>();
                    String url=call_or_put?"http://hq.sinajs.cn/list=OP_UP_510050" + month.substring(2, 4) + month.substring(5):"http://hq.sinajs.cn/list=OP_DOWN_510050" + month.substring(2, 4) + month.substring(5);
                    Request request2 = new Request.Builder()
                            .url(url)
                            .build();
                    String response2=client.newCall(request2).execute().body().string();
                    list.add(response2.substring(response2.indexOf("=")+2,response2.indexOf(";")-2).split(","));


                    //获取某月每个期权的数据
                    List<String[]> left=new ArrayList<>();
                    for (String item : list.get(0)) {
                        Request request_up = new Request.Builder()
                                .url("http://hq.sinajs.cn/list=" + item)
                                .build();
                        String response_up = client.newCall(request_up).execute().body().string();
                        left.add(response_up.substring(response_up.indexOf("=") + 2, response_up.length() - 2).split(","));
                    }

                    mHandler.obtainMessage(SUCCESS,left).sendToTarget();
                }catch (Exception e){
                    mHandler.obtainMessage(FAILURE).sendToTarget();
                    e.printStackTrace();
                }
            }
        }).start();
    }



    private void syncScroll() {
        options_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView,int dx,int dy) {
                if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
                    // note: scrollBy() not trigger OnScrollListener
                    // This is a known issue. It is caused by the fact that RecyclerView does not know how LayoutManager will handle the scroll or if it will handle it at all.
                    controllers_view.scrollBy(dx,dy);
                }
            }
        });

        controllers_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
                    options_view.scrollBy(dx,dy);
                }
            }
        });
    }
    private void initDialog(){
        dialog=new AlertDialog.Builder(getActivity());
        dialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
    }
    private void dialogShow(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.show();
            }
        });
    }
    private void saveTemp2(){
        temp_map2=controllerAdapter.getAmount_map();
        int cp=1;
        if(call_or_put==false){
            cp=-1;
        }
        temp2=new ArrayList<>();
        for(int i=0;i<controllerAdapter.getItemCount();i++){
            if(temp_map2[i]!=0) {
                String code = list.get(0)[i];
                temp2.add(new CustomOption(temp_map2[i], cp, code));
            }
        }
    }
    private void saveTemp1(){
        temp_map1=controllerAdapter.getAmount_map();
        int cp=1;
        if(call_or_put==false){
            cp=-1;
        }
        temp1=new ArrayList<>();
        for(int i=0;i<controllerAdapter.getItemCount();i++){
            if(temp_map1[i]!=0) {
                String code = list.get(0)[i];
                temp1.add(new CustomOption(temp_map1[i], cp, code));
            }
        }

    }
}
