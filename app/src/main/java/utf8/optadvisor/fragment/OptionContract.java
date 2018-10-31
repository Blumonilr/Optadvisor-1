package utf8.optadvisor.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import utf8.optadvisor.R;
import utf8.optadvisor.util.CenterAdapter;
import utf8.optadvisor.util.LeftAdapter;
import utf8.optadvisor.util.NetUtil;
import utf8.optadvisor.util.RightAdapter;
import utf8.optadvisor.activity.MoreInfoActivity;

import utf8.optadvisor.util.SyncHorizontalScrollView;


public class OptionContract extends Fragment {
    private List<String> spinner_items=new ArrayList<>();
    private List<String[]> right_data=new ArrayList<>();
    private List<String[]> left_data=new ArrayList<>();
    private List<String> center_data=new ArrayList<>();
    ArrayAdapter<String> spinner_adapter;
    LeftAdapter leftAdapter=new LeftAdapter(left_data);
    RightAdapter rightAdapter=new RightAdapter(right_data);
    CenterAdapter centerAdapter=new CenterAdapter(center_data);
    private RecyclerView left_view;
    private Spinner spinner;
    private RecyclerView right_view;
    private ProgressBar progressBar;
    private RecyclerView center_view;
    private LinearLayout var;

    private List<String[]> mlist;

    private List<String[]> leftInfo;
    private List<String[]> rightInfo;
    private List<String> centerInfo;
    private final int SUCCESS=0;
    private final int FAILURE=1;
    private final int MONTHS_GET=2;
    private final int MONTHS_FAIL=3;
    private AlertDialog.Builder dialog;
    private View view;
    private Handler mHandler = new Handler() {
        public void handleMessage (Message msg) {//此方法在ui线程运行
            switch(msg.what) {
                case SUCCESS:
                    if(left_data.size()>=1) {
                        left_data.clear();
                    }
                    for(String[] item:((AllInfo)msg.obj).getLeftInfo()) {
                        left_data.add(item);
                    }
                    if(right_data.size()>=1) {
                        right_data.clear();
                    }
                    for(String[] item:((AllInfo)msg.obj).getRightInfo()) {
                        right_data.add(item);
                    }
                    if(center_data.size()>=1) {
                        center_data.clear();
                    }
                    for(String item:((AllInfo)msg.obj).getCenterInfo()) {
                        center_data.add(item);
                    }
                    leftAdapter.notifyDataSetChanged();
                    rightAdapter.notifyDataSetChanged();
                    centerAdapter.notifyDataSetChanged();
                    break;
                case FAILURE:
                    break;
                case MONTHS_FAIL:
                    break;
                case MONTHS_GET:
                    for(int i=1;i<((String[])msg.obj).length;i++) {
                        spinner_items.add(((String[])msg.obj)[i]);
                    }
                    spinner_adapter.notifyDataSetChanged();
                    mlist=initOptionInfo(spinner_items.get(0));
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_option_cantract, container, false);
        spinner_adapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,spinner_items);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        progressBar=view.findViewById(R.id.option_progress_bar);
        initDialog();
        initMonths();
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
        spinner=view.findViewById(R.id.choose_month);
        spinner.setAdapter(spinner_adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mlist=initOptionInfo(spinner_items.get(position));
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



        //填充RecyclerView;
        left_view=(RecyclerView) view.findViewById(R.id.call_option_recycler_view);
        right_view=(RecyclerView) view.findViewById(R.id.put_option_recycler_view);
        center_view=(RecyclerView) view.findViewById(R.id.strike_price_recycler_view);
        LinearLayoutManager leftLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        LinearLayoutManager rightLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        LinearLayoutManager centerLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        left_view.setLayoutManager(leftLayoutManager);
        right_view.setLayoutManager(rightLayoutManager);
        center_view.setLayoutManager(centerLayoutManager);
        left_view.setAdapter(leftAdapter);
        right_view.setAdapter(rightAdapter);
        center_view.setAdapter(centerAdapter);

        //详情跳转
        leftAdapter.setOnItemClickListener(new LeftAdapter.OnItemClickListener(){
        @Override
        public void onItemClick(View view) {
            int position = left_view.getChildAdapterPosition(view);
            Intent intent=new Intent(view.getContext(),MoreInfoActivity.class);
            intent.putExtra("type",1);
            intent.putExtra("num",mlist.get(0)[position]);
            startActivity(intent);
            }
        });
         rightAdapter.setOnItemClickListener(new RightAdapter.OnItemClickListener(){
        @Override
        public void onItemClick(View view) {
            int position = right_view.getChildAdapterPosition(view);
            Intent intent=new Intent(view.getContext(),MoreInfoActivity.class);
            intent.putExtra("type",-1);
            intent.putExtra("num",mlist.get(1)[position]);
            startActivity(intent);
            }
        });

        //实现垂直滚动同步
        syncScroll();

        //实现水平滚动同步
        SyncHorizontalScrollView left1=(SyncHorizontalScrollView) view.findViewById(R.id.left_horizontal_up);
        SyncHorizontalScrollView left2=(SyncHorizontalScrollView) view.findViewById(R.id.left_horizontal);
        final SyncHorizontalScrollView right1=(SyncHorizontalScrollView) view.findViewById(R.id.right_horizontal_up);
        final SyncHorizontalScrollView right2=(SyncHorizontalScrollView) view.findViewById(R.id.right_horizontal);
        left1.setScrollView(left2);
        right1.setScrollView(right2);
        left2.setScrollView(left1);
        right2.setScrollView(right1);
        var=(LinearLayout) view.findViewById(R.id.var) ;
        right2.setAnotherView(left2,var.getWidth());
        left2.setAnotherView(right2,var.getWidth());


        return view;
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

    /**
     * 初始化数据
     */
    private List initOptionInfo(final String month){
       final List<String[]> a=new ArrayList();
        new Thread(new Runnable(){
            @Override
            public void run(){
                try{

                    OkHttpClient client=new OkHttpClient();

                    //获取各月期权代号
                    List<String[]> list_up=new ArrayList<>();
                    List<String[]> list_down=new ArrayList<>();

                    Request request2=new Request.Builder()
                            .url("http://hq.sinajs.cn/list=OP_UP_510050"+month.substring(2,4)+month.substring(5)+
                                    ",OP_DOWN_510050"+month.substring(2,4)+month.substring(5))
                            .build();
                    String response2=client.newCall(request2).execute().body().string();
                    list_up.add(response2.substring(response2.indexOf("=")+2,response2.indexOf(";")-2).split(","));
                    list_down.add(response2.substring(response2.lastIndexOf("=")+2,response2.lastIndexOf(";")-2).split(","));
                    a.add(list_up.get(0));
                    a.add(list_down.get(0));


                    //获取某月每个期权的数据
                    List<String[]> left=new ArrayList<>();
                    List<String[]> right=new ArrayList<>();
                        for (String item : list_up.get(0)) {
                            Request request_up = new Request.Builder()
                                    .url("http://hq.sinajs.cn/list=" + item)
                                    .build();
                            String response_up = client.newCall(request_up).execute().body().string();
                            left.add(response_up.substring(response_up.indexOf("=") + 2, response_up.length() - 2).split(","));
                        }
                        for (String item : list_down.get(0)) {
                            Request request_down = new Request.Builder()
                                    .url("http://hq.sinajs.cn/list=" + item)
                                    .build();
                            String response_down = client.newCall(request_down).execute().body().string();
                            right.add(response_down.substring(response_down.indexOf("=") + 2, response_down.length() - 2).split(","));
                        }
                   List<String> center=new ArrayList<>();
                    for(int k=0;k<left.size();k++){
                        center.add(left.get(k)[7]);
                    }
                    AllInfo allInfo=new AllInfo(left,right,center);
                    mHandler.obtainMessage(SUCCESS,allInfo).sendToTarget();
                }catch (Exception e){
                    mHandler.obtainMessage(FAILURE).sendToTarget();
                    dialog.setTitle("网络连接错误");
                    dialog.setMessage("请稍后再试");
                    dialogShow();
                    e.printStackTrace();
                }
            }
        }).start();

        return a;
    }
    class AllInfo{
        private List<String[]> leftInfo;
        private List<String[]> rightInfo;
        private List<String> centerInfo;
        public AllInfo(List<String[]> leftInfo, List<String[]> rightInfo,List<String> centerInfo){
            this.leftInfo=leftInfo;
            this.rightInfo=rightInfo;
            this.centerInfo=centerInfo;
        }

        public List<String> getCenterInfo() {
            return centerInfo;
        }

        public List<String[]> getLeftInfo() {
            return leftInfo;
        }

        public List<String[]> getRightInfo() {
            return rightInfo;
        }
    }


    /**
     *使得RecyclerView联动滚动（垂直方向）
     */
    private void syncScroll() {
        left_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView,int dx,int dy) {
                if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
                    // note: scrollBy() not trigger OnScrollListener
                    // This is a known issue. It is caused by the fact that RecyclerView does not know how LayoutManager will handle the scroll or if it will handle it at all.
                    right_view.scrollBy(dx,dy);
                    center_view.scrollBy(dx,dy);
                }
            }
        });

        right_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
                    left_view.scrollBy(dx,dy);
                    center_view.scrollBy(dx,dy);
                }
            }
        });
        center_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
                    left_view.scrollBy(dx, dy);
                    right_view.scrollBy(dx,dy);
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
}

