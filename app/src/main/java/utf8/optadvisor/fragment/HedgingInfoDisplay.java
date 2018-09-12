package utf8.optadvisor.fragment;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

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
import utf8.optadvisor.activity.MainActivity;
import utf8.optadvisor.domain.HedgingResponse;
import utf8.optadvisor.util.AddDialog;
import utf8.optadvisor.util.AllocationInfoPage;
import utf8.optadvisor.util.ChartMarkerView;
import utf8.optadvisor.util.ConfirmDialog;
import utf8.optadvisor.util.HedgingMenuItem;
import utf8.optadvisor.util.PortfolioXFormatter;


public class HedgingInfoDisplay extends ScrollView {

    private LineChart lineChart;

    private HedgingMenuItem id ;
    private HedgingMenuItem name;
    private HedgingMenuItem purchase;
    private HedgingMenuItem wave;
    private HedgingMenuItem date;
    private HedgingMenuItem soldPrice;
    private HedgingMenuItem finalPrice;
    private HedgingMenuItem delta;
    private HedgingMenuItem gamma;
    private HedgingMenuItem theta;
    private HedgingMenuItem vega;
    private HedgingMenuItem rho;
    private HedgingMenuItem maxLoss;
    private HedgingResponse response;

    private int[] colors=new int[]{Color.parseColor("#BF0815"),Color.parseColor("#088B05"),Color.parseColor("#4876FF")};

    private Button add;
    private Button back;
    private HedgingInfoSetting mainActivity;

    public HedgingInfoDisplay(final Context context, final HedgingResponse hedging, final HedgingInfoSetting mainActivity) {
        super(context);
        inflate(context,R.layout.fragment_hedging_info_display,  this);
        this.mainActivity=mainActivity;
        this.response=hedging;
        TextView title=(TextView)findViewById(R.id.tv_table_title_left);
        title.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/msyh.ttc"));

        id = findViewById(R.id.hedging_id);
        name =findViewById(R.id.hedging_name);
        purchase = findViewById(R.id.hedging_purchase);
        wave=findViewById(R.id.hedging_wave);
        date=findViewById(R.id.hedging_date);
        soldPrice=findViewById(R.id.hedging_soldprice);
        finalPrice=findViewById(R.id.hedging_finalprice);
        delta=findViewById(R.id.hedging_delta);
        gamma=findViewById(R.id.hedging_gamma);
        theta=findViewById(R.id.hedging_theta);
        vega=findViewById(R.id.hedging_vega);
        rho=findViewById(R.id.hedging_rho);
        maxLoss=findViewById(R.id.hedging_maxloss);

        add=findViewById(R.id.hedging_info_bt_add);
        add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                AddDialog add=new AddDialog(context,hedging,mainActivity);
                add.show();
            }
        });

        back=findViewById(R.id.hedging_info_bt_back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.refresh();
            }
        });

        initLineChart();//初始化
        initMenu();
    }

    /**
     * 初始化LineChart的一些设置
     */
    private void initLineChart(){

        lineChart = findViewById(R.id.line_chart);
        Description description = new Description();
        description.setText("组合表现");
        description.setTextColor(getResources().getColor(R.color.colorButtnDark, null));
        description.setTextSize(18);

        lineChart.setDescription(description);//设置图表描述信息
        lineChart.setNoDataText("暂无数据显示");//没有数据时显示的文字
        lineChart.setNoDataTextColor(Color.BLUE);//没有数据时显示文字的颜色
        lineChart.setDrawGridBackground(false);//chart 绘图区后面的背景矩形将绘制
        lineChart.setDrawBorders(false);//禁止绘制图表边框的线
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleXEnabled(true);// 缩放
        lineChart.setScaleYEnabled(false);

        XAxis xAxis=lineChart.getXAxis();
//        xAxis.setValueFormatter(new PortfolioXFormatter());
        xAxis.setLabelCount(6,false);
        xAxis.setGranularity(1f);
        xAxis.setAxisLineWidth(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis yAxis=lineChart.getAxisRight();
        yAxis.setEnabled(false);

        refreshChartData();
    }

    private void refreshChartData() {
        ArrayList<Entry> data1 = new ArrayList<>();
        ArrayList<Entry> data2=new ArrayList<>();
        ArrayList<Entry> data3=new ArrayList<>();
        for (int i = 0; i < response.getGraph()[0].length; i++) {
//            String date=response.getGraph()[0][i].replace("-",".");
            data1.add(new Entry(handleDate(response.getGraph()[0][i]), Float.parseFloat(response.getGraph()[1][i])));
            data2.add(new Entry(handleDate(response.getGraph()[0][i]), Float.parseFloat(response.getGraph()[2][i])));
        }
        LineDataSet set1= new LineDataSet(data1, "不持有时损失");
        LineDataSet set2= new LineDataSet(data2,"持有时损失");
        setChartDataSet(set1,0);
        setChartDataSet(set2,1);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        dataSets.add(set2);

        LineData data = new LineData(dataSets);

        lineChart.setData(data);


        lineChart.setVisibleXRangeMaximum(8f);

        ChartMarkerView markerView = new ChartMarkerView(HedgingInfoDisplay.this.getContext(), R.layout.marker_view);
        markerView.setChartView(lineChart);
        lineChart.setMarker(markerView);//设置交互小图标

        lineChart.invalidate();
    }


    private void initMenu(){

        id.setMenuTextRight(response.getOption().getOptionCode());
        id.setIconLeft(R.mipmap.ic_id);
        id.getMenuLeft().setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/msyhbd.ttc"));
        name.setMenuTextRight(response.getOption().getName());
        name.getMenuLeft().setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/msyhbd.ttc"));
        name.setIconLeft(R.mipmap.ic_name);
        if (response.getOption().getType()<0)
            purchase.setMenuTextRight("卖出");
        else
            purchase.setMenuTextRight("买入");
        purchase.getMenuLeft().setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/msyhbd.ttc"));
        purchase.setIconLeft(R.mipmap.ic_purchase);
        if (response.getOption().getCp()<0)
            wave.setMenuTextRight("看跌");
        else
            wave.setMenuTextRight("看涨");
        wave.getMenuLeft().setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/msyhbd.ttc"));
        wave.setIconLeft(R.mipmap.ic_wave);
        date.setMenuTextRight(response.getOption().getExpireTime());
        date.getMenuLeft().setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/msyhbd.ttc"));
        date.setIconLeft(R.mipmap.ic_date);
        soldPrice.setMenuTextRight(response.getOption().getK()+"");
        soldPrice.getMenuLeft().setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/msyhbd.ttc"));
        soldPrice.setIconLeft(R.mipmap.ic_sold_price);
        finalPrice.setMenuTextRight(response.getOption().getType()>0?response.getOption().getPrice1()+"":response.getOption().getPrice2()+"");
        finalPrice.getMenuLeft().setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/msyhbd.ttc"));
        finalPrice.setIconLeft(R.mipmap.ic_final_price);
        delta.setMenuTextRight(response.getOption().getDelta()+"");
        delta.getMenuLeft().setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/msyhbd.ttc"));
        delta.setIconLeft(R.mipmap.ic_param);
        gamma.setMenuTextRight(response.getOption().getGamma()+"");
        gamma.getMenuLeft().setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/msyhbd.ttc"));
        gamma.setIconLeft(R.mipmap.ic_param);
        theta.setMenuTextRight(response.getOption().getTheta()+"");
        theta.getMenuLeft().setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/msyhbd.ttc"));
        theta.setIconLeft(R.mipmap.ic_param);
        vega.setMenuTextRight(response.getOption().getVega()+"");
        vega.getMenuLeft().setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/msyhbd.ttc"));
        vega.setIconLeft(R.mipmap.ic_param);
        rho.setMenuTextRight(response.getOption().getRho()+"");
        rho.getMenuLeft().setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/msyhbd.ttc"));
        rho.setIconLeft(R.mipmap.ic_param);
        maxLoss.setMenuTextRight(response.getIk()+"");
        maxLoss.getMenuLeft().setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/msyhbd.ttc"));
        maxLoss.setIconLeft(R.mipmap.ic_loss);
    }

    private float handleDate(String str){
        if(TextUtils.isEmpty(str)) return 0;
        int index=str.indexOf("-");
        String year=str.substring(0,index);
        String month=str.substring(index+1);

        return (Float.parseFloat(year)-PortfolioXFormatter.baseYear)*12+Float.parseFloat(month)-PortfolioXFormatter.baseMonth;
    }

    /**
     * 设置dataSet
     */
    private void setChartDataSet(LineDataSet lineDataSet,int type){
        if(type==0){
            lineDataSet.setColor(colors[0]);
            lineDataSet.setCircleColor(colors[0]);
            lineDataSet.setValueTextColor(colors[0]);
        }else if(type==1){
            lineDataSet.setColor(colors[1]);
            lineDataSet.setCircleColor(colors[1]);
            lineDataSet.setValueTextColor(colors[1]);
        }
        else {
            lineDataSet.setColor(colors[2]);
            lineDataSet.setCircleColor(colors[2]);
            lineDataSet.setValueTextColor(colors[2]);
        }
        lineDataSet.setLineWidth(1f);//设置线宽
        lineDataSet.setCircleRadius(3f);//设置焦点圆心的大小
        lineDataSet.enableDashedHighlightLine(10f, 5f, 0f);//点击后的高亮线的显示样式
        lineDataSet.setHighlightLineWidth(0);//设置点击交点后显示高亮线宽
        lineDataSet.setHighlightEnabled(true);//是否禁用点击高亮线
        lineDataSet.setHighLightColor(colors[0]);//设置点击交点后显示交高亮线的颜色
        lineDataSet.setDrawValues(false);
        //lineDataSet.setValueTextSize(11f);//设置显示值的文字大小
        lineDataSet.setDrawFilled(false);//设置禁用范围背景填充

    }
}
