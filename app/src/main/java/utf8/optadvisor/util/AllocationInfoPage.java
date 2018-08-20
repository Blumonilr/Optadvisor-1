package utf8.optadvisor.util;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;
import utf8.optadvisor.R;

public class AllocationInfoPage extends LinearLayout {

    LinearLayout ll_buttons;
    ArrayList<OptionButton> buttons=new ArrayList<>();
    UserInfoMenuItem id;
    UserInfoMenuItem date;
    UserInfoMenuItem soldPrice;
    UserInfoMenuItem finalPrice;
    UserInfoMenuItem delta;
    UserInfoMenuItem gamma;
    UserInfoMenuItem theta;
    UserInfoMenuItem vega;
    UserInfoMenuItem rho;
    UserInfoMenuItem cost;
    UserInfoMenuItem guarantee;
    UserInfoMenuItem group_delta;
    UserInfoMenuItem group_gamma;
    UserInfoMenuItem group_theta;
    UserInfoMenuItem group_vega;
    UserInfoMenuItem group_rho;
    UserInfoMenuItem group_expectation;
    UserInfoMenuItem group_risk;
    UserInfoMenuItem property_expectation;
    UserInfoMenuItem property_risk;
    private LineChartView lineChart;
    int[] colors=new int[]{Color.parseColor("#BF0815"),Color.parseColor("#088B05")};
    String[] Xdate = {"5-23","5-22","6-22","5-23","5-22","2-22","5-22","4-22","9-22","10-22","11-22","12-22","1-22","6-22","5-23","5-22","2-22","5-22","4-22","9-22","10-22","11-22","12-22","4-22","9-22","10-22","11-22","zxc"};//X轴的标注
    int[] score= {74,22,18,79,20,74,20,74,42,90,74,42,90,50,42,90,33,10,74,22,18,79,20,74,22,18,79,20};//图表的数据
    int[] score2= {73,25,18,79,29,74,20,79,45,90,74,42,83,50,42,90,33,10,75,22,18,79,20,74,22,18,79,20};
    List<int[]> scores=new ArrayList<>();
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();
    private List<Line> lines = new ArrayList<Line>();

    public AllocationInfoPage(final Context context) {
        super(context);
        inflate(context, R.layout.linearlayout_allocation_info,this);

        ll_buttons=(LinearLayout)findViewById(R.id.allocation_ll_buttons);

        initMenuItem();

        OptionButton ob1=new OptionButton(context,"test01");
        ob1.setText("test01","2");
        ll_buttons.addView(ob1);
        buttons.add(ob1);


        OptionButton ob2=new OptionButton(context,"test02");
        ob2.setText("test02","3");
        ll_buttons.addView(ob2);
        buttons.add(ob2);

        for (OptionButton bt:buttons){
            bt.setClicked(buttons,id);
        }

        lineChart = (LineChartView)findViewById(R.id.allocation_line_chart);
        scores.add(score);
        scores.add(score2);
        getAxisXLables();//获取x轴的标注
        getAxisPoints();//获取坐标点
        initLineChart();//初始化

    }

    private void initMenuItem() {
        id=(UserInfoMenuItem)findViewById(R.id.allocation_item_id);
        id.setInfoTextLeft("交易代码");
        id.setInfoTextRight("");

        date=(UserInfoMenuItem)findViewById(R.id.allocation_item_date);
        date.setInfoTextLeft("到期时间");
        date.setInfoTextRight("");

        soldPrice=(UserInfoMenuItem)findViewById(R.id.allocation_item_soldprice);
        soldPrice.setInfoTextLeft("执行价格");
        soldPrice.setInfoTextRight("");

        finalPrice=(UserInfoMenuItem)findViewById(R.id.allocation_item_finalprice);
        finalPrice.setInfoTextLeft("成交价格");
        finalPrice.setInfoTextRight("");

        delta=(UserInfoMenuItem)findViewById(R.id.allocation_item_delta);
        delta.setInfoTextLeft("delta");
        id.setInfoTextRight("");

        gamma=(UserInfoMenuItem)findViewById(R.id.allocation_item_gamma);
        gamma.setInfoTextLeft("gamma");
        gamma.setInfoTextRight("");

        theta=(UserInfoMenuItem)findViewById(R.id.allocation_item_theta);
        theta.setInfoTextLeft("theta");
        theta.setInfoTextRight("");

        vega=(UserInfoMenuItem)findViewById(R.id.allocation_item_vega);
        vega.setInfoTextLeft("vega");
        vega.setInfoTextRight("");

        rho=(UserInfoMenuItem)findViewById(R.id.allocation_item_rho);
        rho.setInfoTextLeft("rho");
        rho.setInfoTextRight("");

        cost=(UserInfoMenuItem)findViewById(R.id.allocation_item_cost);
        cost.setInfoTextLeft("成本");
        cost.setInfoTextRight("");

        guarantee=(UserInfoMenuItem)findViewById(R.id.allocation_item_guarantee);
        guarantee.setInfoTextLeft("保证金");
        guarantee.setInfoTextRight("");

        group_delta=(UserInfoMenuItem)findViewById(R.id.allocation_item_group_delta);
        group_delta.setInfoTextLeft("delta");
        group_delta.setInfoTextRight("");

        group_gamma=(UserInfoMenuItem)findViewById(R.id.allocation_item_group_gamma);
        group_gamma.setInfoTextLeft("gamma");
        group_gamma.setInfoTextRight("");

        group_theta=(UserInfoMenuItem)findViewById(R.id.allocation_item_group_theta);
        group_theta.setInfoTextLeft("theta");
        group_theta.setInfoTextRight("");

        group_vega=(UserInfoMenuItem)findViewById(R.id.allocation_item_group_vega);
        group_vega.setInfoTextLeft("vega");
        group_vega.setInfoTextRight("");

        group_rho=(UserInfoMenuItem)findViewById(R.id.allocation_item_group_rho);
        group_rho.setInfoTextLeft("rho");
        group_rho.setInfoTextRight("");

        group_expectation=(UserInfoMenuItem)findViewById(R.id.allocation_item_group_expectation);
        group_expectation.setInfoTextLeft("组合期望收益率");
        group_expectation.setInfoTextRight("");

        group_risk=(UserInfoMenuItem)findViewById(R.id.allocation_item_group_risk);
        group_risk.setInfoTextLeft("组合风险率");
        group_risk.setInfoTextRight("");

        property_expectation=(UserInfoMenuItem)findViewById(R.id.allocation_item_property_expectation);
        property_expectation.setInfoTextLeft("资产期望收益率");
        property_expectation.setInfoTextRight("");

        property_risk=(UserInfoMenuItem)findViewById(R.id.allocation_item_property_risk);
        property_risk.setInfoTextLeft("资产风险率");
        property_risk.setInfoTextRight("");
    }

    /**
     * 初始化LineChart的一些设置
     */
    private void initLineChart(){

        LineChartData data = new LineChartData();
        data.setLines(lines);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(true);  //X轴下面坐标轴字体是斜的显示还是直的，true是斜的显示
//	    axisX.setTextColor(Color.WHITE);  //设置字体颜色
        axisX.setTextColor(Color.parseColor("#D6D6D9"));//灰色

//	    axisX.setName("未来几天的天气");  //表格名称
        axisX.setTextSize(11);//设置字体大小
        axisX.setMaxLabelChars(7); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisValues.length
        axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
//	    data.setAxisXTop(axisX);  //x 轴在顶部
        axisX.setHasLines(true); //x 轴分割线


        Axis axisY = new Axis();  //Y轴
        axisY.setName("");//y轴标注
        axisY.setTextSize(11);//设置字体大小
        data.setAxisYLeft(axisY);  //Y轴设置在左边
        //data.setAxisYRight(axisY);  //y轴设置在右边
        //设置行为属性，支持缩放、滑动以及平移
        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.HORIZONTAL);  //缩放类型，水平
        lineChart.setMaxZoom((float) 3);//缩放比例
        lineChart.setLineChartData(data);
        lineChart.setVisibility(View.VISIBLE);
        /**注：下面的7，10只是代表一个数字去类比而已
         * 见（http://forum.xda-developers.com/tools/programming/library-hellocharts-charting-library-t2904456/page2）;
         * 下面几句可以设置X轴数据的显示个数（x轴0-7个数据），当数据点个数小于（29）的时候，缩小到极致hellochart默认的是所有显示。当数据点个数大于（29）的时候，
         * 若不设置axisX.setMaxLabelChars(int count)这句话,则会自动适配X轴所能显示的尽量合适的数据个数。
         * 若设置axisX.setMaxLabelChars(int count)这句话,
         * 33个数据点测试，若 axisX.setMaxLabelChars(10);里面的10大于v.right= 7; 里面的7，则
         刚开始X轴显示7条数据，然后缩放的时候X轴的个数会保证大于7小于10
         若小于v.right= 7;中的7,反正我感觉是这两句都好像失效了的样子 - -!
         * 并且Y轴是根据数据的大小自动设置Y轴上限
         * 若这儿不设置 v.right= 7; 这句话，则图表刚开始就会尽可能的显示所有数据，交互性太差
         */
        Viewport v = new Viewport(lineChart.getMaximumViewport());
        v.left = 0;
        v.right= 7;
        lineChart.setCurrentViewport(v);
    }

    /**
     * X 轴的显示
     */
    private void getAxisXLables(){
        for (int i = 0; i < Xdate.length; i++) {
            mAxisXValues.add(new AxisValue(i).setLabel(Xdate[i]));
        }
    }
    /**
     * 图表的每个点的显示
     */
    private void getAxisPoints(){
        for (int i = 0; i < scores.size(); i++) {
            List<PointValue> mPointValues=new ArrayList<>();
            for (int j = 0; j < scores.get(i).length; j++)
                mPointValues.add(new PointValue(j, scores.get(i)[j]));
            Line line = new Line(mPointValues).setColor(colors[i]);  //折线的颜色
            line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.SQUARE）
            line.setCubic(false);//曲线是否平滑
            line.setFilled(false);//是否填充曲线的面积
            line.setHasLabels(true);//曲线的数据坐标是否加上备注
            line.setHasLines(true);//是否用直线显示。如果为false 则没有曲线只有点显示
            line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示
            lines.add(line);
        }

    }

}