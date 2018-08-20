package utf8.optadvisor.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

/**
 * 重置密码界面
 */
public class ForgetResetPwdActivity extends AppCompatActivity {

    private AlertDialog.Builder dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_reset_pwd);
        dialog=new AlertDialog.Builder(ForgetResetPwdActivity.this);
        final EditText oldPassword=findViewById(R.id.old_password_textview);
        final EditText input1=findViewById(R.id.reset_first_textview);
        final EditText input2=findViewById(R.id.reset_again_textview);
        final Button confirm=findViewById(R.id.reset_button);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String pwd1=input1.getText().toString();
                String pwd2=input2.getText().toString();
                if(!pwd1.equals(pwd2)){
                    dialog.setTitle("格式错误");
                    dialog.setMessage("两次密码不一致");
                    dialog.show();
                }else{
                    Map<String,String> value=new HashMap<String,String>();
                    value.put("oldPassword",oldPassword.getText().toString());
                    value.put("newPassword",pwd1);
                    NetUtil.INSTANCE.sendPostRequest(NetUtil.SERVER_BASE_ADDRESS + "/user/resetPassword", value, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            dialog.setTitle("网络连接错误");
                            dialog.setMessage("请稍后再试");
                            dialogShow();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            ResponseMsg responseMsg=NetUtil.INSTANCE.parseJSONWithGSON(response);
                            String title="重置失败";
                            String content="";
                            switch (responseMsg.getCode()){
                                case 0:
                                    SharedPreferences sharedPreferences=getSharedPreferences("userInfo",MODE_PRIVATE);
                                    SharedPreferences.Editor editor=sharedPreferences.edit();
                                    editor.putString("password",pwd1);
                                    editor.apply();
                                    ActivityJumper.leftEnterRightExit(ForgetResetPwdActivity.this,ForgetResetPwdActivity.this,LoginActivity.class);
                                    break;
                                case 1001:
                                    content="用户名错误";
                                    break;
                                case 1002:
                                    content="密码错误";
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
