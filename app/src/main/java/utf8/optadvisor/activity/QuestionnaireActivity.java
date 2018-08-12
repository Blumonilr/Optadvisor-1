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
    private LinearLayout itemLayout;
    private View questionText;
    private View answerText;
    private LayoutInflater xInflater;
    private item[] items=new item[6];



    @Override
    /**
     * 注册时填写的调查问卷
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);
        xInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initData();


    }
    private void initData(){
        String[] ans1={"工资、劳务报酬",
                "生产经营所得","利息、股息、转让证券等金融性资产收入",
                "出租、出售房地产等非金融性资产收入",
                "无固定经济来源或个人或家庭人均收入低于当地城乡居民最低生活保障标准"};
        String que1="1.您的主要收入来源是:";
        items[0]=new item(que1,ans1);
        String[]ans2={"70%以上","50%-70% ","30%-50%","10%-30%","10%以下"};
        String que2="2.最近您家庭预计进行证券或者金融衍生品投资的资金占家庭现有总资产（不含自主、自用房产及汽车等固定资产）的比例是：";
        items[1]=new item(que2,ans2);
        String[] ans3={"没有", "有，住房抵押贷款等长期定额债务", "有，信用卡欠款、消费信贷等短期信用债务", "有，亲朋之间借款"};
        String que3="3.您是否有尚未清偿的数额较大的债务，如有，其性质是：";
        items[2]=new item(que3,ans3);
        String[] ans4={"一般，尝试过衍生品交易但经验有限，需要进一步指导",
        "丰富，参与过权证、期货或创业板等高风险产品的交易，倾向于自己做出投资决策",
        "非常丰富，能够运用期权的多种策略，有数年进行期权交易的经验"};
        String que4="4.您的投资经验可以被概括为：";
        items[3]=new item(que4,ans4);
        String[] ans5={ "太高了","偏高","偏低"};
        String que5="5.有一位投资者一个月内做了15笔交易（同一品种买卖各一次算一笔），您认为这样的交易频率：";
        items[4]=new item(que5,ans5);
        String[] ans6={"①","①②","①②③","①②③④","①②③④⑤"};
        String que6="6.您过去的投资行为中重点投资于哪些种类的投资品种：\n" +
                "①低风险产品或服务中的一项或多项。包括但不限于债券质押式报价回购业务、债券质押式逆回购业务、本金保障型收益凭证、低风险金融产品、低风险投资顾问服务产品等\n" +
                "②中低风险产品或服务中的一项或多项。包括但不限于中低风险金融产品、中低风险投资顾问服务产品、网下申购服务等\n" +
                "③中风险产品或服务中的一项或多项。包括但不限于股票交易、创业板股票交易、优先股交易、股票期权备兑开仓业务、股票期权保护性认沽开仓业务、贵金属现货实盘合约业务、中风险金融产品、中风险投资顾问服务产品等\n" +
                "④中高风险产品或服务中的一项或多项。包括但不限于退市整理期股票交易、港股通股票(包括沪港通下的港股通和深港通下的港股通)交易、股票质押式回购(融入方)、约定购回式证券交易(融入方)、风险警示股票交易、融资融券业务、转融通证券出借业务、个股期权买入开仓业务、股票期权保证金卖出开仓业务、权证、分级基金子份额买入及基础份额分拆、全国中小企业股份转让系统(挂牌公司股票、两网退市股票、优先股)、优先股转让、债券市场合格投资者权限开通、暂停_上市提供转让服务债券合格投资者开通、债券基金类产品质押式回购交易(正回购)、贵金属现货延期交易业务、中高风险金融产品、中高风险投资顾问服务产品等\n" +
                "⑤高风险产品或服务中的一项或多项。包括但不限于场外衍生产品及相关服务、复杂或高风险金融产品、高风险投资顾问服务产品等\n";
        items[5]=new item(que6,ans6);
        itemLayout=(LinearLayout) findViewById(R.id.items);
        for(int i=0;i<=5;i++){
            questionText=xInflater.inflate(R.layout.items,null);
            TextView question_text=(TextView)questionText.findViewById(R.id.question_text);
            question_text.setText(items[i].getQuestion());
            itemLayout.addView(questionText);
            System.out.println(items[i].getQuestion());
            LinearLayout add_layout=(LinearLayout)questionText.findViewById(R.id.answers);
            CheckBox[] boxes=new CheckBox[items[i].getAnswers().length];
            for(int j=0;j<items[i].getAnswers().length;j++){
                answerText=xInflater.inflate(R.layout.answers,null);
                CheckBox checkBox=(CheckBox) answerText.findViewById(R.id.answer_text);
                boxes[j]=checkBox;
                checkBox.setText(items[i].getAnswers()[j]);
                add_layout.addView(answerText);
            }
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

        public String getChoose() {
            return choose;
        }


        public String getQuestion() {
            return question;
        }

        public String[] getAnswers() {
            return answers;
        }
    }
}



