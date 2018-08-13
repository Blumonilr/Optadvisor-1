package utf8.optadvisor.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.security.PublicKey;
import java.util.ArrayList;

import utf8.optadvisor.R;

public class QuestionnaireActivity extends AppCompatActivity {


    @Override
    /**
     * 注册时填写的调查问卷
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);


    }

}



