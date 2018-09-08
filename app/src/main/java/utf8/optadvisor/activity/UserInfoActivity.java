package utf8.optadvisor.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import utf8.optadvisor.R;
import utf8.optadvisor.domain.entity.User;
import utf8.optadvisor.domain.response.ResponseMsg;
import utf8.optadvisor.util.NetUtil;
import utf8.optadvisor.widget.UserInfoMenuItem;

public class UserInfoActivity extends AppCompatActivity {

    private AlertDialog.Builder dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        Toolbar toolbar = (Toolbar) findViewById(R.id.user_info_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView userHead = (ImageView) findViewById(R.id.user_head);
        Glide.with(this).load(R.drawable.default_user_head).bitmapTransform(new CropCircleTransformation(this)).into(userHead);
        intiMenuItem();

        Button modify = (Button) findViewById(R.id.user_info_bt_modify);
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UserInfoActivity.this,ModifyActivity.class);
                startActivity(intent);
            }
        });
        initDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();
        intiMenuItem();
    }

    private void intiMenuItem() {


        NetUtil.INSTANCE.sendPostRequest(NetUtil.SERVER_BASE_ADDRESS + "/user/getInfo", UserInfoActivity.this, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                dialog.setTitle("网络连接错误");
                dialog.setMessage("登录时发生错误，请重试");
                dialogShow();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseMsg responseMsg = NetUtil.INSTANCE.parseJSONWithGSON(response);
                if (responseMsg.getData() != null) {
                    User user = new Gson().fromJson(responseMsg.getData().toString(), User.class);
                    UserInfoMenuItem name = (UserInfoMenuItem) findViewById(R.id.user_info_name);
                    UserInfoMenuItem account = (UserInfoMenuItem) findViewById(R.id.user_info_account);
                    UserInfoMenuItem gender = (UserInfoMenuItem) findViewById(R.id.user_info_gender);
                    UserInfoMenuItem age = (UserInfoMenuItem) findViewById(R.id.user_info_age);
                    UserInfoMenuItem birth = (UserInfoMenuItem) findViewById(R.id.user_info_birth);
                    UserInfoMenuItem phone = (UserInfoMenuItem) findViewById(R.id.user_info_phone);
                    UserInfoMenuItem email = (UserInfoMenuItem) findViewById(R.id.user_info_email);
                    name.setInfoTextRight(user.getName());
                    account.setInfoTextRight(user.getUsername());
                    gender.setInfoTextRight(user.getGender());
                    try {
                        age.setInfoTextRight("" + getAge(user.getBirthday()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    birth.setInfoTextRight(user.getBirthday());
                    phone.setInfoTextRight(user.getTelephone());
                    email.setInfoTextRight(user.getEmail());
                }
            }
        });

    }

    private static int getAge(String date) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date dateOfBirth = df.parse(date);
        int age = 0;
        Calendar born = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        if (dateOfBirth != null) {
            now.setTime(new Date());
            born.setTime(dateOfBirth);
            if (born.after(now)) {
                throw new IllegalArgumentException("年龄不能超过当前日期");
            }
            age = now.get(Calendar.YEAR) - born.get(Calendar.YEAR);
            int nowDayOfYear = now.get(Calendar.DAY_OF_YEAR);
            int bornDayOfYear = born.get(Calendar.DAY_OF_YEAR);
            System.out.println("nowDayOfYear:" + nowDayOfYear + " bornDayOfYear:" + bornDayOfYear);
            if (nowDayOfYear < bornDayOfYear) {
                age -= 1;
            }
        }
        return age;

    }

    private void initDialog(){
        dialog=new AlertDialog.Builder(UserInfoActivity.this);
        dialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
    }

    private void dialogShow(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.show();
            }
        });
    }
}
