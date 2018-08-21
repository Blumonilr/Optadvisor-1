package utf8.optadvisor.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import utf8.optadvisor.R;
import utf8.optadvisor.domain.ResponseMsg;
import utf8.optadvisor.util.ActivityJumper;
import utf8.optadvisor.util.NetUtil;

/**
 * 登录界面
 */
public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private RelativeLayout loginMain;
    private SharedPreferences sharedPreferences;
    private int[] background={R.drawable.pic_login_bg1,R.drawable.pic_login_bg2,R.drawable.pic_login_bg3,R.drawable.pic_login_bg4,R.drawable.pic_login_bg5,
            R.drawable.pic_login_bg6,R.drawable.pic_login_bg7,R.drawable.pic_login_bg8,R.drawable.pic_login_bg9,R.drawable.pic_login_bg10,R.drawable.pic_login_bg11};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferences=getSharedPreferences("userInfo",MODE_PRIVATE);
        if(sharedPreferences.getBoolean("isLogined",false)){
            ActivityJumper.rightEnterLeftExit(LoginActivity.this,LoginActivity.this,MainActivity.class);
            finish();
        }
        initLoginMain();
        initAllTextView();
        initLoginButton();
        initRegisterButton();
        initForgetPasswordButton();
    }

    /**
     * 初始化所有文本框
     */
    private void initAllTextView(){
        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        String savedUsername=sharedPreferences.getString("username","");
        String savedPassword=sharedPreferences.getString("password","");
        username.setText(savedUsername);
        password.setText(savedPassword);
    }

    /**
     * 设置背景
     */
    private void initLoginMain(){
        loginMain=findViewById(R.id.login_main);
        int bgIndex=sharedPreferences.getInt("bgIndex",0);
        loginMain.setBackgroundResource(background[bgIndex]);
        bgIndex=(bgIndex+1)%11;
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt("bgIndex",bgIndex);
        editor.apply();
    }

    /**
     * 设置输入框
     */
    private void initLoginButton(){
        Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String usenameText=username.getText().toString();
                String passwordText=password.getText().toString();
                Log.d("Login Input:","{"+usenameText+","+passwordText+"}");
                if(login(usenameText,passwordText)){
                    finish();
                }
            }
        });
    }

    /**
     * 设置注册按钮
     */
    private void initRegisterButton(){
        Button registerButton = findViewById(R.id.register);
        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityJumper.rightEnterLeftExit(LoginActivity.this,LoginActivity.this,RegisterActivity.class);
            }
        });
    }

    /**
     * 设置忘记密码按钮
     */
    private void initForgetPasswordButton(){
        Button forgetPasswordButton = findViewById(R.id.forgetPassword);
        forgetPasswordButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityJumper.rightEnterLeftExit(LoginActivity.this,LoginActivity.this,ForgetPasswordActivity.class);
            }
        });
    }

    /**
     * 登录
     * 未来要换成异步任务
     */
    private boolean login(String username,String password){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("username",username);
        editor.putString("password",password);
        editor.putBoolean("isLogined",true);
        editor.apply();
        Map<String,String> value=new HashMap<String,String>();
        value.put("username",username);
        value.put("password",password);
        NetUtil.INSTANCE.sendPostRequest("http://192.168.1.108:8088/login", value, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("Net","fail");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseMsg responseMsg=NetUtil.INSTANCE.parseJSONWithGSON(response);
                Log.d("Net Message", String.valueOf(responseMsg.getCode()));
            }
        });
        ActivityJumper.rightEnterLeftExit(LoginActivity.this,LoginActivity.this,MainActivity.class);
        return true;
    }
}

