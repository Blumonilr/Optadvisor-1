package utf8.optadvisor.activity;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import utf8.optadvisor.R;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolBar);
        CollapsingToolbarLayout collapsingToolbar=(CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        ImageView background=(ImageView)findViewById(R.id.background);
        LinearLayout content=(LinearLayout)findViewById(R.id.content);
        TextView textView=(TextView)findViewById(R.id.content_text);
        ImageView imageView=(ImageView)findViewById(R.id.content_image);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Glide.with(content.getContext()).load(R.drawable.pic_logo).override(this.getWindowManager().getDefaultDisplay().getWidth(),this.getWindowManager().getDefaultDisplay().getHeight()/8).into(imageView);
        collapsingToolbar.setExpandedTitleGravity(Gravity.CENTER);
        collapsingToolbar.setTitle("Test");
        String context="";
        for(int i=0;i<100;i++){
            context+="test";
        }
        textView.setText(context);

    }


}
