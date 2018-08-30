package utf8.optadvisor.util;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import utf8.optadvisor.R;

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

    private char combination;
    private String date;
    private String m0;
    private String k;
    private String p1;
    private String p2;
    private String sigma1;
    private String sigma2;


    public AllocationSettingPage(final Context context) {
        super(context);
        inflate(context, R.layout.linearlayout_allocation_setting, this);
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
                AllocationSettingSeekbar price=new AllocationSettingSeekbar(context);
                price.setContent(true,true);
                linearLayout.addView(price);
                p1=price.getETF()+"";
                p2="4";
                AllocationSettingSeekbar wave=new AllocationSettingSeekbar(context);
                wave.setContent(false,true);
                linearLayout.addView(wave);
                sigma1=wave.getSigma()+"";
                sigma2="50";
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
                AllocationSettingSeekbar wave=new AllocationSettingSeekbar(context);
                wave.setContent(false,true);
                linearLayout.addView(wave);
                p1=wave.getETF()+"";
                p2=wave.getETF()+"";
                sigma1=wave.getSigma()+"";
                sigma2="50";
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
                AllocationSettingSeekbar price=new AllocationSettingSeekbar(context);
                price.setContent(true,false);
                linearLayout.addView(price);
                p1="0";
                p2=price.getETF()+"";
                AllocationSettingSeekbar wave=new AllocationSettingSeekbar(context);
                wave.setContent(false,true);
                linearLayout.addView(wave);
                sigma1=wave.getSigma()+"";
                sigma2="50";
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
                AllocationSettingSeekbar price=new AllocationSettingSeekbar(context);
                price.setContent(true,true);
                linearLayout.addView(price);
                p1=price.getETF()+"";
                p2="4";
                sigma1=""+price.getSigma();
                sigma2=""+price.getSigma();
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
                AllocationSettingSeekbar price=new AllocationSettingSeekbar(context);
                price.setContent(true,false);
                linearLayout.addView(price);
                p1="0";
                p2=price.getETF()+"";
                sigma1=""+price.getSigma();
                sigma2=""+price.getSigma();
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
                AllocationSettingSeekbar price=new AllocationSettingSeekbar(context);
                price.setContent(true,true);
                linearLayout.addView(price);
                p1=price.getETF()+"";
                p2="4";
                AllocationSettingSeekbar wave=new AllocationSettingSeekbar(context);
                wave.setContent(false,false);
                linearLayout.addView(wave);
                sigma1="0";
                sigma2=""+wave.getSigma();
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
                AllocationSettingSeekbar wave=new AllocationSettingSeekbar(context);
                wave.setContent(false,false);
                linearLayout.addView(wave);
                p1=""+wave.getETF();
                p2=""+wave.getETF();
                sigma1="0";
                sigma2=""+wave.getSigma();
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
                AllocationSettingSeekbar price=new AllocationSettingSeekbar(context);
                price.setContent(true,false);
                linearLayout.addView(price);
                p1="0";
                p2=price.getETF()+"";
                AllocationSettingSeekbar wave=new AllocationSettingSeekbar(context);
                wave.setContent(false,false);
                linearLayout.addView(wave);
                sigma1="0";
                sigma2=wave.getSigma()+"";
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
        return p1;
    }

    public String getP2() {
        return p2;
    }

    public String getSigma1() {
        return sigma1;
    }

    public String getSigma2() {
        return sigma2;
    }
}
