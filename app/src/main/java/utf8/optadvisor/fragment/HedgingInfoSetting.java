package utf8.optadvisor.fragment;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import utf8.optadvisor.R;
import utf8.optadvisor.domain.HedgingResponse;
import utf8.optadvisor.domain.response.ResponseMsg;
import utf8.optadvisor.util.NetUtil;

public class HedgingInfoSetting extends Fragment {

    private SeekBar seekBar;
    private TextView textView;
    private Button datePicker;
    private TextView dateView;
    private Calendar calendar; // 通过Calendar获取系统时间
    private int mYear;
    private int mMonth;
    private int mDay;
    private Button next;
    private LinearLayout ll;
    private EditText et1;
    private EditText et2;

    private AlertDialog.Builder dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_hedging_info_setting, container, false);

        initDialog();
        
        calendar = Calendar.getInstance();
        seekBar = (SeekBar) view.findViewById(R.id.progress);
        textView = (TextView) view.findViewById(R.id.hedging_tv_sb);
        datePicker = (Button) view.findViewById(R.id.hedging_datePicker);
        dateView = (TextView) view.findViewById(R.id.hedging_tv_dateview);
        ll=(LinearLayout)view.findViewById(R.id.hedging_ll);
        et1=(EditText)view.findViewById(R.id.hedging_et_1);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textView.setText(Integer.toString(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int month, int day) {
                                // TODO Auto-generated method stub
                                mYear = year;
                                mMonth = month;
                                mDay = day;
                                // 更新EditText控件日期 小于10加0
                                dateView.setText(new StringBuilder()
                                        .append(mYear)
                                        .append("-")
                                        .append((mMonth + 1) < 10 ? "0"
                                                + (mMonth + 1) : (mMonth + 1))
                                        .append("-")
                                        .append((mDay < 10) ? "0" + mDay : mDay));
                            }
                        }, calendar.get(Calendar.YEAR), calendar
                        .get(Calendar.MONTH), calendar
                        .get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        next=view.findViewById(R.id.hedging_bt_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,String> values=new HashMap<>();
                values.put("n0",et1.getText().toString());
                values.put("a",""+(Double.parseDouble(textView.getText().toString())/100.0));
                values.put("s_exp",et2.getText().toString());
                values.put("t",dateView.getText().toString());


                NetUtil.INSTANCE.sendPostRequest(NetUtil.SERVER_BASE_ADDRESS + "/recommend/hedging", values,getContext(), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        dialog.setTitle("网络连接错误");
                        dialog.setMessage("请稍后再试");
                        dialogShow();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        ResponseMsg responseMsg = NetUtil.INSTANCE.parseJSONWithGSON(response);
                        HedgingResponse responseOption=new Gson().fromJson(responseMsg.getData().toString(),HedgingResponse.class);
                        ll.removeAllViews();
                        ll.addView(new HedgingInfoDisplay(getContext(),responseOption));
                    }
                });

            }
        });

        return view;
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
