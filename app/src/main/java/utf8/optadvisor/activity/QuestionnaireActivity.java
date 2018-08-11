package utf8.optadvisor.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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

    class item{
        private String question;
        private String[] answers;
        private boolean isChoosed=false;
        private String choose;
        public item(String question,String[] answers){
            this.answers=answers;
            this.question=question;
        }
    }
}



