package utf8.optadvisor.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import utf8.optadvisor.R;
import utf8.optadvisor.ResetByEmail;
import utf8.optadvisor.ResetByPhone;

public class ForgetPassword extends AppCompatActivity {

    @Override
    /**
     * 选择方式重置密码
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foget_password);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        Button way1Button=(Button) findViewById(R.id.way1);
        Button way2Button=(Button) findViewById(R.id.way2);
        way1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgetPassword.this, ResetByEmail.class);
                startActivity(intent);
            }
        });
        way2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgetPassword.this, ResetByPhone.class);
                startActivity(intent);
            }
        });
    }
}
