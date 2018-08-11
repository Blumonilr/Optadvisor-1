package utf8.optadvisor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Questionnaire extends AppCompatActivity {

    @Override
    /**
     * 注册时填写的调查问卷
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);
    }
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
