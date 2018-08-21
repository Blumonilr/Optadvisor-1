package utf8.optadvisor.activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import utf8.optadvisor.R;
import utf8.optadvisor.domain.response.ResponseMsg;
import utf8.optadvisor.util.ActivityJumper;
import utf8.optadvisor.util.NetUtil;
import utf8.optadvisor.util.TimeCounter;

/**
 * 忘记密码界面
 */
public class ForgetPasswordActivity extends AppCompatActivity {

    private Button send;
    private Button confirm;
    private TextView username;
    private TextView verifyCode;
    private AlertDialog.Builder dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        initToolBar();
        initDialog();
        initAllTextView();
        initSendButton();
        initConfirmButton();
    }

    /**
     * 设置所有文本框
     */
    private void initAllTextView(){
        username=findViewById(R.id.username_input);
        verifyCode=findViewById(R.id.verify_input);
    }

    /**
     * 设置确认按钮
     */
    private void initConfirmButton(){
        confirm=findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String usernameText=username.getText().toString();
                String code=verifyCode.getText().toString();
                if(TextUtils.isEmpty(usernameText)||TextUtils.isEmpty(code)){
                    dialog.setTitle("格式错误");
                    dialog.setMessage("输入不能为空");
                    dialogShow();
                }else {
                    Map<String,String> value=new HashMap<>();
                    value.put("verifyCode",code);
                    NetUtil.INSTANCE.sendPostRequest(NetUtil.SERVER_BASE_ADDRESS + "/checkVerifyCode", value, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            dialog.setTitle("网络连接错误");
                            dialog.setMessage("请稍后再试");
                            dialogShow();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            ResponseMsg responseMsg=NetUtil.INSTANCE.parseJSONWithGSON(response);
                            String title="验证失败";
                            String content="";
                            switch (responseMsg.getCode()){
                                case 0:
                                    Intent intent=new Intent(ForgetPasswordActivity.this,ForgetResetPwdActivity.class);
                                    intent.putExtra("username",usernameText);
                                    ActivityJumper.rightEnterLeftExit(intent,ForgetPasswordActivity.this,ForgetPasswordActivity.this);
                                    break;
                                case 1001:
                                    content="账号不存在";
                                    break;
                                case 1013:
                                case 1014:
                                    content="验证码无效";
                                    break;
                                default:
                                    content="未知的错误，请重试";
                            }
                            if(content.length()>0){
                                dialogShow();
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * 设置弹窗
     */
    private void initDialog(){
        dialog=new AlertDialog.Builder(ForgetPasswordActivity.this);
        dialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
    }


    /**
     * 重写back键实现滑动
     */
    @Override
    public void onBackPressed() {
        backToLogin();
    }

    /**
     * 重写Home键实现滑动切换界面
     */
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
                String usernameText=username.getText().toString();
                if(TextUtils.isEmpty(usernameText)){
                    dialog.setTitle("格式错误");
                    dialog.setMessage("输入不能为空");
                    dialogShow();
                }else {
                    TimeCounter timeCounter=new TimeCounter(60*1000,1000,send);
                    timeCounter.start();

                    Map<String,String> value=new HashMap<String,String>();
                    value.put("username",usernameText);
                    NetUtil.INSTANCE.sendPostRequest(NetUtil.SERVER_BASE_ADDRESS + "/sendVerifyCode", value, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            dialog.setTitle("网络连接错误");
                            dialog.setMessage("请稍后再试");
                            dialogShow();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            ResponseMsg responseMsg=NetUtil.INSTANCE.parseJSONWithGSON(response);
                            if(responseMsg.getCode()!=0){
                                dialog.setTitle("格式错误");
                                dialog.setMessage("发送失败，请检查账号");
                                dialogShow();
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * 弹出弹窗
     */
    private void dialogShow(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.show();
            }
        });
    }


}
