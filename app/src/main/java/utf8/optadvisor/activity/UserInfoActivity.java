package utf8.optadvisor.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import utf8.optadvisor.R;
import utf8.optadvisor.util.UserInfoMenuItem;

public class UserInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        Toolbar toolbar=(Toolbar) findViewById(R.id.user_info_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView userHead=(ImageView)findViewById(R.id.user_head);
        Glide.with(this).load(R.drawable.default_user_head).bitmapTransform(new CropCircleTransformation(this)).into(userHead);
        intiMenuItem();
    }

    private void intiMenuItem() {
        UserInfoMenuItem name=(UserInfoMenuItem)findViewById(R.id.user_info_name);
        UserInfoMenuItem account=(UserInfoMenuItem)findViewById(R.id.user_info_account);
        UserInfoMenuItem gender=(UserInfoMenuItem)findViewById(R.id.user_info_gender);
        UserInfoMenuItem age=(UserInfoMenuItem)findViewById(R.id.user_info_age);
        UserInfoMenuItem birth=(UserInfoMenuItem)findViewById(R.id.user_info_birth);
        TextView intro=(TextView)findViewById(R.id.introduction);
        TextView introduction=(TextView) findViewById(R.id.user_info_introduction);

        name.setInfoTextRight("最后的韵律");
        account.setInfoTextRight("12345678");
        gender.setInfoTextRight("男");
        age.setInfoTextRight("20");
        birth.setInfoTextRight("1998-01-01");
        intro.setTextColor(Color.GRAY);
        introduction.setText("懒。");
    }
}
