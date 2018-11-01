package utf8.optadvisor.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import utf8.optadvisor.R;
import utf8.optadvisor.domain.AllocationResponse;
import utf8.optadvisor.domain.entity.User;
import utf8.optadvisor.domain.response.ResponseMsg;
import utf8.optadvisor.util.AllocationSettingPage;
import utf8.optadvisor.widget.ModifyItem;
import utf8.optadvisor.util.NetUtil;
import utf8.optadvisor.widget.UserInfoMenuItem;

public class ModifyActivity extends AppCompatActivity {

    private ModifyItem name ;
    private UserInfoMenuItem account;
    private ModifyItem gender;
    private TextView birthText;
    private TextView birth;
    private Button birthSelect;
    private ModifyItem phone;
    private ModifyItem email;

    private AlertDialog.Builder dialog;

    private static final int INFO_SUCCESS = 0;
    private static final int INFO_FAILURE = 1;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage (Message msg) {//此方法在ui线程运行
            switch (msg.what) {
                case INFO_SUCCESS:
                    String info = (String) msg.obj;
                    System.out.println(info);
                    User user = new Gson().fromJson(info,User.class);
                    name.setInfoTextRight(user.getName());
                    account.setInfoTextRight(user.getUsername());
                    gender.setInfoTextRight(user.getGender());
                    birth.setText(user.getBirthday());
                    phone.setInfoTextRight(user.getTelephone());
                    email.setInfoTextRight(user.getEmail());
                    break;
                case INFO_FAILURE:
                    System.out.println("1fail");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);

        Toolbar toolbar = (Toolbar) findViewById(R.id.user_info_modify_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView userHead = (ImageView) findViewById(R.id.user_modify_head);
        Glide.with(this).load(R.mipmap.home).bitmapTransform(new CropCircleTransformation(this)).into(userHead);
        intiMenuItem();
        initDialog();

    }

    private void initDialog(){
        dialog=new AlertDialog.Builder(ModifyActivity.this);
        dialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
    }

    private void intiMenuItem() {
        name =  (ModifyItem)findViewById(R.id.user_info_modify_name);
        account = (UserInfoMenuItem) findViewById(R.id.user_info_modify_account);
        gender = (ModifyItem) findViewById(R.id.user_info_modify_gender);
        birthText=(TextView)findViewById(R.id.modify_birth_left);
        birth = (TextView) findViewById(R.id.modify_birth_right);
        birth.setTextColor(Color.BLACK);
        phone = (ModifyItem) findViewById(R.id.user_info_modify_phone);
        email = (ModifyItem) findViewById(R.id.user_info_modify_email);


        NetUtil.INSTANCE.sendPostRequest(NetUtil.SERVER_BASE_ADDRESS + "/user/getInfo",ModifyActivity.this, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                dialog.setTitle("修改失败");
                dialog.setMessage("请检查网络连接");
                dialogShow();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseMsg responseMsg = NetUtil.INSTANCE.parseJSONWithGSON(response);
                mHandler.obtainMessage(INFO_SUCCESS,responseMsg.getData().toString()).sendToTarget();
            }
        });



        Button modify = (Button) findViewById(R.id.user_modify_bt_confirm);
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,String> values=new HashMap<>();
                values.put("username",account.getInfoTextRight().toString());
                values.put("name",name.getInfoTextRight().toString());
                values.put("birthday",birth.getText().toString());
                values.put("telephone",phone.getInfoTextRight().toString());
                values.put("email",email.getInfoTextRight().toString());
                values.put("gender",gender.getInfoTextRight().toString());
                NetUtil.INSTANCE.sendPostRequest(NetUtil.SERVER_BASE_ADDRESS + "/user/modifyInfo", values,ModifyActivity.this, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        dialog.setTitle("修改失败");
                        dialog.setMessage("请检查网络连接");
                        dialogShow();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Intent intent=new Intent(ModifyActivity.this,UserInfoActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });

        Button password=(Button)findViewById(R.id.user_modify_bt_password);
        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ModifyActivity.this,ForgetResetPwdActivity.class);
                startActivity(intent);
            }
        });

        birthSelect=(Button)findViewById(R.id.modify_birth_bt);
        birthSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // TODO Auto-generated method stub
                        birth.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();

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
                return -1;
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

    private void dialogShow(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                dialog.show();
            }
        });
    }
}
