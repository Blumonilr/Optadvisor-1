package utf8.optadvisor.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import utf8.optadvisor.R;
import utf8.optadvisor.util.ActivityJumper;
import utf8.optadvisor.util.TimeCounter;

/**
 * 忘记密码界面
 */
public class ForgetPasswordActivity extends AppCompatActivity {

    private Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        initToolBar();
        initSendButton();
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

    /**
     * 滑动退出效果（back键+标题栏退出）
     */
    private void backToLogin(){
        finish();
        ActivityJumper.leftEnterRightExit(ForgetPasswordActivity.this,ForgetPasswordActivity.this,LoginActivity.class);
    }

    /**
     * 设置标题栏
     */
    private void initToolBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * 设置发送按钮
     */
    private void initSendButton(){
        send=findViewById(R.id.send_button);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send();
                TimeCounter timeCounter=new TimeCounter(60*1000,1000,send);
                timeCounter.start();
            }
        });
    }

    private void send(){
        Log.d("ForgetPassword","sending...");
    }
}
