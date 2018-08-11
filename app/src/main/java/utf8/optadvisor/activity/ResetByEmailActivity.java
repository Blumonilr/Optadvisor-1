package utf8.optadvisor.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import utf8.optadvisor.R;
import utf8.optadvisor.util.TimeCounter;

/**
 * 通过邮箱重置密码
 */
public class ResetByEmailActivity extends AppCompatActivity {
    private Button sendEmail;
    private Button sendCode_email;
    private TimeCounter time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_by_email);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        ConfirmEmail();
        ConfirmCode();

    }
    private void ConfirmEmail(){
        sendEmail=(Button) findViewById(R.id.sendEmail);
        time= new TimeCounter(60000,1000,sendEmail);
        sendEmail.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                try {
                    Gson gson = new Gson();
                    EditText emailText = (EditText) findViewById(R.id.email);
                    String email = emailText.getText().toString();
                    String jsonEmail = gson.toJson(email);
                    OkHttpClient client = new OkHttpClient();
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    RequestBody requestBody = RequestBody.create(JSON, jsonEmail);
                    Request request = new Request.Builder()
                            .url("http://111.111.11")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String toGson = response.body().string();//返回值
                    //验证并发送信息
                }catch(Exception e){
                    e.printStackTrace();
                }
                time.start();
            }
        });
    }

    private void ConfirmCode(){
        sendCode_email=(Button) findViewById(R.id.sendCode_email);
        sendCode_email.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                try {
                    Gson gson = new Gson();
                    EditText code_emailText = (EditText) findViewById(R.id.code_email);
                    String code = code_emailText.getText().toString();
                    String jsonCode = gson.toJson(code);
                    OkHttpClient client = new OkHttpClient();
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    RequestBody requestBody = RequestBody.create(JSON, jsonCode);
                    Request request = new Request.Builder()
                            .url("http://111.111.11")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String toGson = response.body().string();//返回值
                    //验证并发送信息
                }catch(Exception e){
                    e.printStackTrace();
                }

                //发送验证码
                if(true){
                    //如果正确
                    Intent intent = new Intent(ResetByEmailActivity.this,ForgetResetPwdActivity.class);
                    startActivity(intent);
                }
                else{
                    //如果错误
                }
            }
        });
    }
}
