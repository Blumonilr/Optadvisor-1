package utf8.optadvisor.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import utf8.optadvisor.R;
import utf8.optadvisor.util.ActivityJumper;

public class ForgetPasswordActivity extends AppCompatActivity {

    @Override
    /**
     * 选择方式重置密码
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        //设置标题栏
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    @Override
    public void onBackPressed() {
        backToLogin();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                backToLogin();
                break;
            default:
                break;
        }
        return true;
    }

    private void backToLogin(){
        finish();
        ActivityJumper.leftEnterRightExit(ForgetPasswordActivity.this,ForgetPasswordActivity.this,LoginActivity.class);
    }

}
