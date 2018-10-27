package utf8.optadvisor.activity;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import utf8.optadvisor.R;
import utf8.optadvisor.util.ActivityJumper;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent=getIntent();

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolBar);
        CollapsingToolbarLayout collapsingToolbar=(CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        ImageView background=(ImageView)findViewById(R.id.background);
        TextView textView=(TextView)findViewById(R.id.content_text);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        collapsingToolbar.setTitle("Test");
        String content="";
        for(int i=0;i<100;i++){
            content+="test";
        }
        if(intent.getBooleanExtra("fromMyCombination",false)){
            content=intent.getStringExtra("optionDetail");
            textView.setTextSize(18);
            textView.setLineSpacing(8,1);
            collapsingToolbar.setTitle(intent.getStringExtra("optionName"));
        }
        textView.setText(content);
    }

    /**
     * 重写Home键实现滑动切换界面
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                exit();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        exit();
    }
    /**
     * 滑动退出
     */
    private void exit(){
        finish();
        overridePendingTransition(R.anim.activity_left_enter,R.anim.activity_right_exit);
    }
}
