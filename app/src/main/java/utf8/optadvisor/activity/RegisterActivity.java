package utf8.optadvisor.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import utf8.optadvisor.R;
import utf8.optadvisor.domain.RegisterInfo;
import utf8.optadvisor.domain.response.ResponseMsg;
import utf8.optadvisor.util.ActivityJumper;

/**
 * 注册
 */
public class RegisterActivity extends AppCompatActivity {
    private RegisterInfo info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        info=new RegisterInfo();
        Toolbar toolbar = (Toolbar) findViewById(R.id.tbb);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        final EditText usernameInput= findViewById(R.id.edit_username);
        final EditText passwordInput=findViewById(R.id.edit_password);
        final EditText telephoneInput=findViewById(R.id.edit_telephone);
        final EditText nameInput=findViewById(R.id.edit_name);
        final EditText emailInput= findViewById(R.id.edit_email);
        final DatePicker birthdayInput=findViewById(R.id.edit_birthday);
        final RadioGroup genderInput=findViewById(R.id.edit_gender);


        Button button=findViewById(R.id.input_commit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usename=usernameInput.getText().toString();
                String password=passwordInput.getText().toString();
                String telephone=telephoneInput.getText().toString();
                if(TextUtils.isEmpty(usename)||TextUtils.isEmpty(password)){
                    AlertDialog.Builder dialog=new AlertDialog.Builder(RegisterActivity.this);
                    dialog.setTitle("格式错误");
                    dialog.setMessage("请检查用户名，密码，手机号码是否填写");
                    dialog.show();
                    return;
                }
                String birthday=birthdayInput.getYear()+"/"+birthdayInput.getMonth()+"/"+birthdayInput.getDayOfMonth();
                String name=nameInput.getText().toString();
                String email=emailInput.getText().toString();
                String gender="女";
                if(genderInput.getCheckedRadioButtonId()==0){
                    gender="男";
                }

                if(info.isInfoOk()){
                    //向提交数据并跳转
                }
                else{
                    //显示错误
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        backToLogin();
    }

    private void backToLogin(){
        finish();
        ActivityJumper.leftEnterRightExit(RegisterActivity.this,RegisterActivity.this,LoginActivity.class);
    }
}
