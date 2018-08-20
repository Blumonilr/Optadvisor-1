package utf8.optadvisor.fragment;

import android.app.DatePickerDialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Calendar;

import utf8.optadvisor.R;

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

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_hedging_info_setting, container, false);

        calendar = Calendar.getInstance();
        seekBar = (SeekBar) view.findViewById(R.id.progress);
        textView = (TextView) view.findViewById(R.id.hedging_tv_sb);
        datePicker = (Button) view.findViewById(R.id.hedging_datePicker);
        dateView = (TextView) view.findViewById(R.id.hedging_tv_dateview);
        ll=(LinearLayout)view.findViewById(R.id.hedging_ll);

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
                ll.removeAllViews();
                ll.addView(new HedgingInfoDisplay(getContext()));
            }
        });

        return view;
    }
}
