package utf8.optadvisor;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ResetByEmail extends AppCompatActivity {
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
                //验证并发送信息
                time.start();
            }
        });
    }

    private void ConfirmCode(){
        sendCode_email=(Button) findViewById(R.id.sendCode_email);
        sendCode_email.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //发送验证码
                if(true){
                    //如果正确
                    Intent intent = new Intent(ResetByEmail.this,Forget_ResetPwd.class);
                    startActivity(intent);
                }
                else{
                    //如果错误
                }
            }
        });
    }
}
