package utf8.optadvisor.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import utf8.optadvisor.R;
import utf8.optadvisor.domain.RegisterInfo;
import utf8.optadvisor.domain.response.ResponseMsg;
import utf8.optadvisor.util.ActivityJumper;
import utf8.optadvisor.util.NetUtil;


public class QuestionnaireActivity extends AppCompatActivity {
    private RadioGroup q1;
    private RadioButton a1;
    private RadioGroup q2;
    private RadioButton a2;
    private RadioGroup q3;
    private RadioButton a3;
    private RadioGroup q4;
    private RadioButton a4;
    private RadioGroup q5;
    private RadioButton a5;
    private RadioGroup q6;
    private RadioButton a6;
    private Button button;




    @Override
    /**
     * 注册时填写的调查问卷
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        button=findViewById(R.id.questionnaire_bt);
        q1=findViewById(R.id.q1);
        q2=findViewById(R.id.q2);
        q3=findViewById(R.id.q3);
        q4=findViewById(R.id.q4);
        q5=findViewById(R.id.q5);
        q6=findViewById(R.id.q6);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (q1.getCheckedRadioButtonId() == -1 ||
                        q2.getCheckedRadioButtonId() == -1 ||
                        q3.getCheckedRadioButtonId() == -1 ||
                        q4.getCheckedRadioButtonId() == -1 ||
                        q5.getCheckedRadioButtonId() == -1 ||
                        q6.getCheckedRadioButtonId() == -1
                        ) {
                    System.out.println("没做完");
                    //输出
                } else {
                    //q1
                    int score1=0;
                    a1 = findViewById(q1.getCheckedRadioButtonId());
                    switch (q1.indexOfChild(a1)) {
                        case 0:
                            score1 = 8;
                            break;
                        case 1:
                            score1 = 7;
                            break;
                        case 2:
                            score1 = 4;
                            break;
                        case 3:
                            score1 = 6;
                            break;
                        case 4:
                            score1 = 1;
                            break;
                    }
                    //q2
                    int score2=0;
                    a2 = findViewById(q2.getCheckedRadioButtonId());
                    switch (q2.indexOfChild(a2)) {
                        case 0:
                            score2 = 1;
                            break;
                        case 1:
                            score2 = 2;
                            break;
                        case 2:
                            score2 = 4;
                            break;
                        case 3:
                            score2 = 6;
                            break;
                        case 4:
                            score2 = 8;
                            break;
                    }
                    //q3
                    int score3=0;
                    a3 = findViewById(q3.getCheckedRadioButtonId());
                    switch (q3.indexOfChild(a3)) {
                        case 0:
                            score3 = 8;
                            break;
                        case 1:
                            score3 = 3;
                            break;
                        case 2:
                            score3 = 5;
                            break;
                        case 3:
                            score3 = 2;
                            break;
                    }
                    //q4
                    int score4=0;
                    a4 = findViewById(q4.getCheckedRadioButtonId());
                    switch (q4.indexOfChild(a4)) {
                        case 0:
                            score4 = 3;
                            break;
                        case 1:
                            score4 = 6;
                            break;
                        case 2:
                            score4 = 8;
                            break;
                    }
                    //q5
                    int score5=0;
                    a5 = findViewById(q5.getCheckedRadioButtonId());
                    switch (q5.indexOfChild(a5)) {
                        case 0:
                            score5 = 8;
                            break;
                        case 1:
                            score5 = 6;
                            break;
                        case 2:
                            score5 = 4;
                            break;
                        case 3:
                            score5 = 2;
                            break;
                    }

                    //q6
                    int score6=0;
                    a6 = findViewById(q6.getCheckedRadioButtonId());
                    switch (q6.indexOfChild(a6)) {
                        case 0:
                            score6 = 1;
                            break;
                        case 1:
                            score6 = 2;
                            break;
                        case 2:
                            score6 = 4;
                            break;
                        case 3:
                            score6 = 6;
                            break;
                        case 4:
                            score6 = 10;
                            break;
                    }
                    //结算
                    int sum=score1+score2+score3+score4+score5+score6;

                    int w1=0;
                    int w2=0;
                    if(sum<=10){
                        w1=10;
                        w2=90;
                    }else if(sum>10&&sum<=20){
                        w1=30;
                        w2=70;
                    }else if(sum>20&&sum<=30){
                        w1=50;
                        w2=50;
                    }else if(sum>30&&sum<=40){
                        w1=70;
                        w2=30;
                    }else if(sum>40){
                        w1=90;
                        w2=10;
                    }
                    Intent intent = getIntent();
                    RegisterInfo info=(RegisterInfo) intent.getSerializableExtra("Info");
                    info.setW1(w1);
                    info.setW2(w2);
                   /* final Map<String, String> value = new HashMap<String, String>();
                    value.put("username", info.getUsername());
                    value.put("password", info.getPassword());
                    value.put("name", info.getName());
                    value.put("birthday", info.getBirthday());
                    value.put("telephone", info.getTelephone());
                    value.put("email", info.getEmail());
                    value.put("gender", info.getGender());
                    value.put("avatarPath", "");
                    value.put("w1", w1);
                    value.put("w2", w2);*/
                    System.out.println(w1+"   "+w2);
                    Gson gson=new Gson();
                    String value=gson.toJson(info);
                    NetUtil.INSTANCE.sendPostRequest(NetUtil.SERVER_BASE_ADDRESS+"/signUp", value, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            System.out.print("问卷网络错误");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            ResponseMsg responseMsg=NetUtil.INSTANCE.parseJSONWithGSON(response);
                            if(responseMsg.getCode()==0){
                                System.out.println("注册成功");
                                finish();
                            }else{
                                System.out.print("注册失败");
                            }

                        }
                    });

                }
            }
        });




    }

}



