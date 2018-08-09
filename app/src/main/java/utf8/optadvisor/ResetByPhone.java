package utf8.optadvisor;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;


public class ResetByPhone extends AppCompatActivity {
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
        sendMessage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //验证并发送信息
                time.start();
            }
            });
    }

    private void ConfirmCode(){
        sendCode_phone=(Button) findViewById(R.id.sendCode_phone);
        sendCode_phone.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //发送验证码
               if(true){
                   //如果正确
                   Intent intent = new Intent(ResetByPhone.this, Forget_ResetPwd.class);
                   startActivity(intent);
               }
               else{
                   //如果错误
               }
            }
        });
    }
}

