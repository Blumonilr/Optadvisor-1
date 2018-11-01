package utf8.optadvisor.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.google.gson.Gson;

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
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initDialog();

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                backToLogin();
                break;
            default:
                break;
        }
        return true;
    }

    private void backToLogin(){
        finish();
        ActivityJumper.leftEnterRightExit(RegisterActivity.this,RegisterActivity.this,LoginActivity.class);
    }
    private void getInfo() {
        String username = edit_userName.getText().toString();
        String password = edit_password.getText().toString();
        String name = edit_name.getText().toString();
        String birthday = datePicker.getYear() + "-" + datePicker.getMonth() +"-" +datePicker.getDayOfMonth();
        String telephone = edit_telephone.getText().toString();
        String gender = null;
        if (radioGroup.getCheckedRadioButtonId() != -1) {
            rb = findViewById(radioGroup.getCheckedRadioButtonId());
            gender = rb.getText().toString();
        }
        String email = edit_email.getText().toString();
        if (username == null || password == null ||name == null || birthday ==null || telephone == null || gender == null) {
            dialog.setTitle("注册失败");
            dialog.setMessage("注册信息有空缺");
            dialogShow();
        }
        else {
            boolean testPhone = true;
            for (int i = 0; i < telephone.length(); i++) {
                if (!Character.isDigit(telephone.charAt(i))) {
                    testPhone = false;
                }
            }
            if (testPhone){
                Info.setUsername(username);
                Info.setBirthday(birthday);
                Info.setPassword(password);
                Info.setName(name);
                Info.setGender(gender);
                Info.setTelephone(telephone);
                Info.setEmail(email);
                Info.setW1(0);
                Info.setW2(0);


                Gson gson=new Gson();
                String value=gson.toJson(Info);
                NetUtil.INSTANCE.sendPostRequest(NetUtil.SERVER_BASE_ADDRESS + "/isUsernameUsed", value, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        System.out.println("注册界面网络错误");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        ResponseMsg responseMsg = NetUtil.INSTANCE.parseJSONWithGSON(response);
                        if (responseMsg.getData().equals("false")) {
                            Intent intent = new Intent(RegisterActivity.this, QuestionnaireActivity.class);
                            intent.putExtra("Info", Info);
                            startActivity(intent);
                            finish();
                        } else {
                            dialog.setTitle("注册失败");
                            dialog.setMessage("用户名重复");
                            dialogShow();
                        }

                    }
                });
            }else{
                dialog.setTitle("注册失败");
                dialog.setMessage("手机号码格式错误");
                dialogShow();
            }


        }
    }
    private void dialogShow(){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.show();
            }
        });
    }
    private void initDialog(){
        dialog=new AlertDialog.Builder(RegisterActivity.this);
        dialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
    }



}
