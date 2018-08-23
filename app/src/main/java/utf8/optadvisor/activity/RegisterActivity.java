package utf8.optadvisor.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import utf8.optadvisor.R;
import utf8.optadvisor.domain.RegisterInfo;
import utf8.optadvisor.domain.response.ResponseMsg;
import utf8.optadvisor.util.ActivityJumper;
import utf8.optadvisor.util.NetUtil;
import utf8.optadvisor.util.SyncHorizontalScrollView;

/**
 * 注册界面
 */
public class RegisterActivity extends AppCompatActivity {
    private RegisterInfo Info=new RegisterInfo();
    private EditText edit_userName;
    private EditText edit_password;
    private EditText edit_telephone;
    private EditText edit_name;
    private EditText edit_email;
    private DatePicker datePicker;
    private RadioGroup radioGroup;
    private RadioButton rb;
    private AlertDialog.Builder dialog;
    @Override
    /**
     * 注册界面
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tbb);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        edit_userName=(EditText) findViewById(R.id.edit_userName);
        edit_password=(EditText) findViewById(R.id.edit_password);
        edit_telephone=(EditText) findViewById(R.id.edit_telephone);
        edit_name=(EditText) findViewById(R.id.edit_name);
        edit_email=(EditText) findViewById(R.id.edit_email);
        datePicker=findViewById(R.id.date_picker);
        radioGroup=findViewById(R.id.gender_group);



        /**获取填写的信息
         */

        Button button=(Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInfo();
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
    private void getInfo() {
        String username = edit_userName.getText().toString();
        String password = edit_password.getText().toString();
        String name = edit_name.getText().toString();
        String birthday = datePicker.getYear() + "/" + datePicker.getMonth() +"/" +datePicker.getDayOfMonth();
        String telephone = edit_telephone.getText().toString();
        String gender = null;
        if (radioGroup.getCheckedRadioButtonId() != -1) {
            rb = findViewById(radioGroup.getCheckedRadioButtonId());
            gender = rb.getText().toString();
        }
        String email = edit_email.getText().toString();
        if (username == null || password == null ||name == null || birthday ==null || telephone == null || gender == null) {
            System.out.println("注册信息有空缺");
        }
        else{
            Info.setUsername(username);
            Info.setBirthday(birthday);
            Info.setPassword(password);
            Info.setName(name);
            Info.setGender(gender);
            Info.setTelephone(telephone);
            Info.setEmail(email);


            final Map<String, String> value = new HashMap<String, String>();
            value.put("username", username);
            value.put("password", password);
            value.put("name", name);
            value.put("birthday", birthday);
            value.put("telephone", telephone);
            value.put("email", email);
            value.put("gender", gender);
            value.put("avatarPath", "");
            value.put("w1", "");
            value.put("w2", "");
            NetUtil.INSTANCE.sendPostRequest(NetUtil.SERVER_BASE_ADDRESS+"/isUsernameUsed", value, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println("注册界面网络错误");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    ResponseMsg responseMsg = NetUtil.INSTANCE.parseJSONWithGSON(response);
                    if (responseMsg.getCode() == 0) {
                        Intent intent = new Intent(RegisterActivity.this, QuestionnaireActivity.class);
                        intent.putExtra("Info", Info);
                        startActivity(intent);
                        finish();
                    } else {
                        System.out.println("重名了");
                    }

                }
            });


        }
    }



}
