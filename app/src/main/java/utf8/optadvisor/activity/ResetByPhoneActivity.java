package utf8.optadvisor.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import utf8.optadvisor.Forget_ResetPwd;
import utf8.optadvisor.R;
import utf8.optadvisor.TimeCounter;

/**
 * 通过手机重置密码
 */
public class ResetByPhoneActivity extends AppCompatActivity {
    private Button sendMessage;
    private Button sendCode_phone;
    private TimeCounter time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_by_phone);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        ConfirmPhone();
        ConfirmCode();

    }
    private void ConfirmPhone(){
        sendMessage=(Button) findViewById(R.id.sendPhone);
        time= new TimeCounter(60000,1000,sendMessage);
        sendMessage.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                try {
                    Gson gson = new Gson();
                    EditText phoneText = (EditText) findViewById(R.id.phone);
                    String phone = phoneText.getText().toString();
                    String jsonPhone = gson.toJson(phone);
                    OkHttpClient client = new OkHttpClient();
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    RequestBody requestBody = RequestBody.create(JSON, jsonPhone);
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

                //验证并发送信息
                time.start();
            }
            });
    }

    private void ConfirmCode(){
        sendCode_phone=(Button) findViewById(R.id.sendCode_phone);
        sendCode_phone.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                try {
                    Gson gson = new Gson();
                    EditText code_phoneText = (EditText) findViewById(R.id.code_phone);
                    String code = code_phoneText.getText().toString();
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
                   Intent intent = new Intent(ResetByPhoneActivity.this, Forget_ResetPwd.class);
                   startActivity(intent);
               }
               else{
                   //如果错误
               }
            }
        });
    }
}

