package utf8.optadvisor;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * 注册界面
 */
public class RegisterActivity extends AppCompatActivity {
    private RegisterInfo Info=new RegisterInfo();
    @Override
    /**
     * 注册界面
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }



        EditText edit_userName=(EditText) findViewById(R.id.edit_userName);
        EditText edit_password=(EditText) findViewById(R.id.edit_password);
        EditText edit_telephone=(EditText) findViewById(R.id.edit_telephone);
        EditText edit_name=(EditText) findViewById(R.id.edit_name);
        EditText edit_email=(EditText) findViewById(R.id.edit_email);
        final Spinner spinner_year=(Spinner) findViewById(R.id.spinner_year);
        final Spinner spinner_month=(Spinner) findViewById(R.id.spinner_month);
        final Spinner spinner_day=(Spinner) findViewById(R.id.spinner_day);
        final Spinner spinner_gender=(Spinner) findViewById(R.id.spinner_gender);
        /**获取填写的信息
         */
        spinner_year.setOnItemSelectedListener(new OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String year = (String) spinner_year.getSelectedItem();
                Info.setYear(year);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        spinner_month.setOnItemSelectedListener(new OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String month = (String) spinner_month.getSelectedItem();
                Info.setMonth(month);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        spinner_day.setOnItemSelectedListener(new OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String day = (String) spinner_day.getSelectedItem();
                Info.setDay(day);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        spinner_gender.setOnItemSelectedListener(new OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String gender = (String) spinner_gender.getSelectedItem();
                Info.setGender(gender);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

       Button button=(Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Info.isInfoOk()){
                    //向提交数据并跳转
                }
                else{
                    //显示错误
                }
            }
        });








    }
}
