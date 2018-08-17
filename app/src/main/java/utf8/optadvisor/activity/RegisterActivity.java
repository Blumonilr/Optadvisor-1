package utf8.optadvisor.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import utf8.optadvisor.R;
import utf8.optadvisor.domain.RegisterInfo;
import utf8.optadvisor.util.ActivityJumper;

/**
 * 注册界面
 */
public class RegisterActivity extends AppCompatActivity {
    private RegisterInfo Info=new RegisterInfo();
    @Override
    /**
     * 注册界面
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tbb);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        EditText edit_userName=(EditText) findViewById(R.id.edit_userName);
        EditText edit_password=(EditText) findViewById(R.id.edit_password);
        EditText edit_telephone=(EditText) findViewById(R.id.edit_telephone);
        EditText edit_name=(EditText) findViewById(R.id.edit_name);
        EditText edit_email=(EditText) findViewById(R.id.edit_email);

        /**获取填写的信息
         */

        Button button=(Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Info.isInfoOk()){
                    //向提交数据并跳转
                }
                else{
                    //显示错误
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        backToLogin();
    }

    private void backToLogin(){
        finish();
        ActivityJumper.leftEnterRightExit(RegisterActivity.this,RegisterActivity.this,LoginActivity.class);
    }
}
