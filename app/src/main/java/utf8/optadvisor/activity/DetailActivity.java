package utf8.optadvisor.activity;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import utf8.optadvisor.R;
import utf8.optadvisor.util.ActivityJumper;

public class DetailActivity extends AppCompatActivity {

    private boolean rightExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        rightExit=false;
        Intent intent=getIntent();
        if(intent.getBooleanExtra("fromMyCombination",false)){
            rightExit=true;
        }
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
        textView.setText(content);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.activity_left_enter,R.anim.activity_right_exit);
    }
}
