package utf8.optadvisor.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import utf8.optadvisor.R;
import utf8.optadvisor.domain.entity.User;
import utf8.optadvisor.domain.response.ResponseMsg;
import utf8.optadvisor.util.ModifyItem;
import utf8.optadvisor.util.NetUtil;

public class ModifyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);

        Toolbar toolbar = (Toolbar) findViewById(R.id.user_info_modify_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView userHead = (ImageView) findViewById(R.id.user_modify_head);
        Glide.with(this).load(R.drawable.default_user_head).bitmapTransform(new CropCircleTransformation(this)).into(userHead);
        intiMenuItem();


    }

    private void intiMenuItem() {
        final ModifyItem name =  (ModifyItem)findViewById(R.id.user_info_modify_name);
        final ModifyItem account = (ModifyItem) findViewById(R.id.user_info_modify_account);
        final ModifyItem gender = (ModifyItem) findViewById(R.id.user_info_modify_gender);
        final ModifyItem age = (ModifyItem) findViewById(R.id.user_info_modify_age);
        final ModifyItem birth = (ModifyItem) findViewById(R.id.user_info_modify_birth);
        final ModifyItem phone = (ModifyItem) findViewById(R.id.user_info_modify_phone);
        final ModifyItem email = (ModifyItem) findViewById(R.id.user_info_modify_email);
        final TextView intro = (TextView) findViewById(R.id.introduction);
        final EditText introduction = (EditText) findViewById(R.id.user_info_modify_introduction);


        NetUtil.INSTANCE.sendPostRequest(NetUtil.SERVER_BASE_ADDRESS + "/user/getInfo", "", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(ModifyActivity.this, "网络连接错误，请重试", Toast.LENGTH_SHORT);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseMsg responseMsg = NetUtil.INSTANCE.parseJSONWithGSON(response);
                User user = (User) responseMsg.getData();
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
                intro.setTextColor(Color.GRAY);
                introduction.setText("introduction");
            }
        });

        final Map<String,String> values=new HashMap<>();
        values.put("username",account.getInfoTextRight().toString());
        values.put("name",name.getInfoTextRight().toString());
        values.put("birthday",birth.getInfoTextRight().toString());
        values.put("telephone",phone.getInfoTextRight().toString());
        values.put("email",email.getInfoTextRight().toString());
        values.put("gender",gender.getInfoTextRight().toString());

        Button modify = (Button) findViewById(R.id.user_modify_bt_confirm);
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetUtil.INSTANCE.sendPostRequest(NetUtil.SERVER_BASE_ADDRESS + "/user/modifyInfo", values, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Toast.makeText(ModifyActivity.this, "网络连接错误，更新失败", Toast.LENGTH_SHORT);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Intent intent=new Intent(ModifyActivity.this,UserInfoActivity.class);
                        startActivity(intent);
                    }
                });
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
}
