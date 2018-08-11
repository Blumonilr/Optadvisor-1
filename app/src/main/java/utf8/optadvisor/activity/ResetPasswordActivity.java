package utf8.optadvisor.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import utf8.optadvisor.R;

/**
 * 在知道旧密码的情况下重置密码
 */
public class ResetPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pwd);
        Button button=(Button) findViewById(R.id.resetFinish);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //检查信息是否合理并发送
            }
        });
    }
}
