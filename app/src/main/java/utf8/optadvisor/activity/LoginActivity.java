package utf8.optadvisor.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import utf8.optadvisor.R;
import utf8.optadvisor.util.ActivityJumper;

import static android.Manifest.permission.READ_CONTACTS;

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
     * 未来要换成异步任务
     */
    private boolean login(String username,String password){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("username",username);
        editor.putString("password",password);
        editor.putBoolean("isLogined",true);
        editor.apply();
        ActivityJumper.rightEnterLeftExit(LoginActivity.this,LoginActivity.this,MainActivity.class);
        return true;
    }
}

