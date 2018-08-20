package utf8.optadvisor.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

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
import utf8.optadvisor.util.HedgingMenuItem;


public class HedgingInfoDisplay extends ScrollView {

    private LineChartView lineChart;

    private HedgingMenuItem id ;
    private HedgingMenuItem name;
    private HedgingMenuItem purchase;
    private HedgingMenuItem wave;
    private HedgingMenuItem date;
    private HedgingMenuItem soldPrice;
    private HedgingMenuItem finalPrice;
    private HedgingMenuItem portion;
    private HedgingMenuItem delta;
    private HedgingMenuItem gamma;
    private HedgingMenuItem theta;
    private HedgingMenuItem vega;
    private HedgingMenuItem rho;
    private HedgingMenuItem maxLoss;

    int[] colors=new int[]{Color.parseColor("#BF0815"),Color.parseColor("#088B05")};
    String[] dates = {"5-23","5-22","6-22","5-23","5-22","2-22","5-22","4-22","9-22","10-22","11-22","12-22","1-22","6-22","5-23","5-22","2-22","5-22","4-22","9-22","10-22","11-22","12-22","4-22","9-22","10-22","11-22","zxc"};//X轴的标注
    int[] score= {74,22,18,79,20,74,20,74,42,90,74,42,90,50,42,90,33,10,74,22,18,79,20,74,22,18,79,20};//图表的数据
    int[] score2= {73,25,18,79,29,74,20,79,45,90,74,42,83,50,42,90,33,10,75,22,18,79,20,74,22,18,79,20};
    List<int[]> scores=new ArrayList<>();
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();
    private List<Line> lines = new ArrayList<Line>();

    public HedgingInfoDisplay(Context context) {
        super(context);
        inflate(context,R.layout.fragment_hedging_info_display,  this);
        TextView title=(TextView)findViewById(R.id.tv_table_title_left);
        title.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/msyh.ttc"));
        lineChart = (LineChartView)findViewById(R.id.line_chart);
        scores.add(score);
        scores.add(score2);

        id = findViewById(R.id.hedging_id);
        name =findViewById(R.id.hedging_name);
        purchase = findViewById(R.id.hedging_purchase);
        wave=findViewById(R.id.hedging_wave);
        date=findViewById(R.id.hedging_date);
        soldPrice=findViewById(R.id.hedging_soldprice);
        finalPrice=findViewById(R.id.hedging_finalprice);
        portion=findViewById(R.id.hedging_portion);
        delta=findViewById(R.id.hedging_delta);
        gamma=findViewById(R.id.hedging_gamma);
        theta=findViewById(R.id.hedging_theta);
        vega=findViewById(R.id.hedging_vega);
        rho=findViewById(R.id.hedging_rho);
        maxLoss=findViewById(R.id.hedging_maxloss);

        getAxisXLables();//获取x轴的标注
        getAxisPoints();//获取坐标点
        initLineChart();//初始化
        initMenu();
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
         * 尼玛搞的老子好辛苦！！！见（http://forum.xda-developers.com/tools/programming/library-hellocharts-charting-library-t2904456/page2）;
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
        for (int i = 0; i < dates.length; i++) {
            mAxisXValues.add(new AxisValue(i).setLabel(dates[i]));
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

    private void initMenu(){

        id.setMenuTextRight("null");
        id.setIconLeft(R.mipmap.ic_id);
        id.getMenuLeft().setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/msyhbd.ttc"));
        name.setMenuTextRight("null");
        name.getMenuLeft().setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/msyhbd.ttc"));
        name.setIconLeft(R.mipmap.ic_name);
        purchase.setMenuTextRight("null");
        purchase.getMenuLeft().setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/msyhbd.ttc"));
        purchase.setIconLeft(R.mipmap.ic_purchase);
        wave.setMenuTextRight("null");
        wave.getMenuLeft().setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/msyhbd.ttc"));
        wave.setIconLeft(R.mipmap.ic_wave);
        date.setMenuTextRight("null");
        date.getMenuLeft().setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/msyhbd.ttc"));
        date.setIconLeft(R.mipmap.ic_date);
        soldPrice.setMenuTextRight("null");
        soldPrice.getMenuLeft().setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/msyhbd.ttc"));
        soldPrice.setIconLeft(R.mipmap.ic_sold_price);
        finalPrice.setMenuTextRight("null");
        finalPrice.getMenuLeft().setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/msyhbd.ttc"));
        finalPrice.setIconLeft(R.mipmap.ic_final_price);
        portion.setMenuTextRight("null");
        portion.getMenuLeft().setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/msyhbd.ttc"));
        portion.setIconLeft(R.mipmap.ic_portion);
        delta.setMenuTextRight("null");
        delta.getMenuLeft().setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/msyhbd.ttc"));
        delta.setIconLeft(R.mipmap.ic_param);
        gamma.setMenuTextRight("null");
        gamma.getMenuLeft().setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/msyhbd.ttc"));
        gamma.setIconLeft(R.mipmap.ic_param);
        theta.setMenuTextRight("null");
        theta.getMenuLeft().setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/msyhbd.ttc"));
        theta.setIconLeft(R.mipmap.ic_param);
        vega.setMenuTextRight("null");
        vega.getMenuLeft().setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/msyhbd.ttc"));
        vega.setIconLeft(R.mipmap.ic_param);
        rho.setMenuTextRight("null");
        rho.getMenuLeft().setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/msyhbd.ttc"));
        rho.setIconLeft(R.mipmap.ic_param);
        maxLoss.setMenuTextRight("null");
        maxLoss.getMenuLeft().setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/msyhbd.ttc"));
        maxLoss.setIconLeft(R.mipmap.ic_loss);
    }
}
