package utf8.optadvisor.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.w3c.dom.Text;

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
import utf8.optadvisor.activity.MainActivity;
import utf8.optadvisor.domain.AllocationResponse;
import utf8.optadvisor.domain.HedgingResponse;
import utf8.optadvisor.domain.response.ResponseMsg;
import utf8.optadvisor.util.AllocationSettingPage;
import utf8.optadvisor.util.NetUtil;

public class HedgingInfoSetting extends Fragment {

    private boolean isNull=true;

    private SeekBar seekBar;
    private EditText textView;
    Spinner date;
    private Calendar calendar; // 通过Calendar获取系统时间
    private int mYear;
    private int mMonth;
    private int mDay;
    private Button next;
    private LinearLayout ll;
    private EditText et1;
    private EditText et2;
    private TextView tvETF;
    private TextView tvSIGMA;

    private AlertDialog.Builder dialog;

    private static final int INFO_SUCCESS = 0;
    private static final int INFO_FAILURE = 1;
    private static final int MONTH_SUCCESS =2;
    private static final int MONTH_FAILURE=3;
    private static final int ETF_SUCCESS =4;//获取50etf成功的标识
    private static final int ETF_FAILURE = 5;
    private static final int SIGMA_SUCCESS =6;//获取50etf成功的标识
    private static final int SIGMA_FAILURE = 7;

    private String n0;
    private String a;
    private String sExp;
    private String monthList;

    private ProgressDialog progressDialog;

    private double etf;
    private double sigma;


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage (Message msg) {//此方法在ui线程运行
            switch (msg.what) {
                case INFO_SUCCESS:
                    String info = (String) msg.obj;
                    HedgingResponse responseOption=new Gson().fromJson(info,HedgingResponse.class);
                    ll.removeAllViews();
                    isNull=false;
                    ll.addView(new HedgingInfoDisplay(getContext(),responseOption,HedgingInfoSetting.this));
                    break;
                case INFO_FAILURE:
                    System.out.println("1fail");
                    break;
                case MONTH_SUCCESS:
                    String str = ((String) msg.obj).replace("\"","");
                    String month=str.substring(str.indexOf("contractMonth")+15,str.lastIndexOf("]"));
                    System.out.println(month);
                    monthList=month;
                    List<String> array=new ArrayList<>();
                    String[] monthlist=monthList.split(",");
                    for (String s:monthlist){
                        if (array.isEmpty()||!array.contains(s))
                            array.add(s);
                    }
                    if (isInFive()){
                        array.remove(0);
                    }
                    System.out.println(array);
                    date=(Spinner)getView().findViewById(R.id.hedging_spr_validtime);
                    SpinnerAdapter adapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_dropdown_item_1line,array);
                    date.setAdapter(adapter);
                    break;
                case MONTH_FAILURE:
                    System.out.println("2fail");
                    break;
                case ETF_SUCCESS:
                    String _etf = (String) msg.obj;
                    etf=Double.parseDouble(_etf.substring(_etf.indexOf(",") + 1, _etf.indexOf(",") + 6));
                    tvETF=(TextView)getView().findViewById(R.id.hedging_tv2);
                    tvETF.setText(etf+"");
                    break;
                case ETF_FAILURE:
                    System.out.println("3fail");
                    break;
                case SIGMA_SUCCESS:
                    String response2=(String)msg.obj;
                    String[] sigams = response2.split("\n");
                    String temp = "";
                    for (String s : sigams) {
                        if ((s.charAt(0)=='9'&&s.length() > 10)||(s.charAt(0)!='9'&&s.length()>11))
                            temp = s;
                        else
                            break;
                    }
                    sigma=Double.parseDouble(temp.substring(temp.indexOf(",")+1,temp.lastIndexOf(",")));
                    tvSIGMA=(TextView)getView().findViewById(R.id.hedging_tv4);
                    tvSIGMA.setText(""+sigma);
                    break;
                case SIGMA_FAILURE:
                    System.out.println("sigmaFail");
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_hedging_info_setting, container, false);

        intiETF();
        initSigma();

        initDialog();

        initProgessDialog();

        calendar = Calendar.getInstance();
        seekBar = (SeekBar) view.findViewById(R.id.progress);
        textView = (EditText) view.findViewById(R.id.hedging_et_sb);
        ll=(LinearLayout)view.findViewById(R.id.hedging_ll);
        et1=(EditText)view.findViewById(R.id.hedging_et_1);
        et2=(EditText)view.findViewById(R.id.hedging_et_2);

        textView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i== EditorInfo.IME_ACTION_DONE){
                    double n=Double.parseDouble(textView.getText().toString());
                    seekBar.setProgress((int)n);
                }
                return true;
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textView.setText(Double.toString(progress));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        getMonth();

        next=view.findViewById(R.id.hedging_bt_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();

                HedgingInfoSetting.this.sExp=et2.getText().toString();
                if (!TextUtils.isEmpty(et1.getText())&&!TextUtils.isEmpty(textView.getText())&&!TextUtils.isEmpty(et2.getText())) {
                    if (Integer.parseInt(et1.getText().toString()) <= 0) {
                        dialog.setTitle("持仓量填写不符合规则");
                        dialog.setMessage("请重新填写");
                        dialogShow();
                        return;
                    } else if (Double.parseDouble(et2.getText().toString()) <= 0||Double.parseDouble(et2.getText().toString())>etf) {
                        dialog.setTitle("预测最低价格填写不符合规则");
                        dialog.setMessage("请重新填写");
                        dialogShow();
                        return;
                    } else {
                        Map<String, String> values = new HashMap<>();
                        values.put("n0", et1.getText().toString());
                        n0=et1.getText().toString();
                        values.put("a", "" + (Double.parseDouble(textView.getText().toString())) / 100.0);
                        a="" + (Double.parseDouble(textView.getText().toString())) / 100.0;
                        values.put("s_exp", et2.getText().toString());
                        sExp=et2.getText().toString();
                        values.put("t", date.getSelectedItem().toString());


                        NetUtil.INSTANCE.sendPostRequest(NetUtil.SERVER_BASE_ADDRESS + "/recommend/hedging", values, getContext(), new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                dialog.setTitle("服务器连接错误");
                                dialog.setMessage("请稍后再试");
                                dialogShow();
                                progressDialog.dismiss();
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                progressDialog.dismiss();
                                ResponseMsg responseMsg = NetUtil.INSTANCE.parseJSONWithGSON(response);
                                if (responseMsg.getData() == null || responseMsg.getCode() == 1008) {
                                    dialog.setTitle("数据返回错误");
                                    dialog.setMessage("请重新点击");
                                    dialogShow();
                                } else {
                                    mHandler.obtainMessage(INFO_SUCCESS, responseMsg.getData().toString()).sendToTarget();
                                }
                            }
                        });
                    }
                }
                else {
                    dialog.setTitle("信息未填写完善");
                    dialog.setMessage("请重新填写");
                    dialogShow();
                }

            }
        });

        return view;
    }

    private void initSigma() {
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

    private void intiETF() {
        NetUtil.INSTANCE.sendGetRequest("http://hq.sinajs.cn/list=s_sh510050", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.obtainMessage(ETF_FAILURE).sendToTarget();
                dialog.setTitle("网络连接错误");
                dialog.setMessage("请稍后再试");
                dialogShow();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                mHandler.obtainMessage(ETF_SUCCESS,response.body().string()).sendToTarget();
            }
        });
    }

    private boolean isInFive() {
        Calendar now=Calendar.getInstance();
        int year=now.get(Calendar.YEAR);
        int month=now.get(Calendar.MONTH)+1;
        int day=now.get(Calendar.DAY_OF_MONTH);
        int weekDay=0;
        Calendar week=Calendar.getInstance();
        week.set(year,month-1,1);
        weekDay=week.get(Calendar.DAY_OF_WEEK);
        int theFourthWeek=weekDay<=3?24-weekDay:31-weekDay;
        return (day<=theFourthWeek-4)&&(day<=theFourthWeek+1);
    }

    private void getMonth(){
        NetUtil.INSTANCE.sendGetRequest("http://stock.finance.sina.com.cn/futures/api/openapi.php/StockOptionService.getStockName", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.obtainMessage(MONTH_FAILURE).sendToTarget();
                dialog.setTitle("网络连接错误");
                dialog.setMessage("请稍后再试");
                dialogShow();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                mHandler.obtainMessage(MONTH_SUCCESS,response.body().string()).sendToTarget();
            }
        });
    }


    /**
     * 设置进度条
     */
    private void initProgessDialog(){
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setTitle("请稍等");
        progressDialog.setMessage("请求发送中");
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

    public String getA(){return a;}

    public String getN0(){
        return n0;
    }


    public String getsExp() {
        return sExp;
    }

    public Handler getmHandler() {
        return mHandler;
    }

    public void refresh() {
        if (!isNull) {
            ll.removeAllViews();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            HedgingInfoSetting hedgingInfoSetting = new HedgingInfoSetting();
            transaction.add(R.id.build_portfolio_ll, hedgingInfoSetting);
            transaction.show(hedgingInfoSetting);
            transaction.commit();
        }
    }
}
