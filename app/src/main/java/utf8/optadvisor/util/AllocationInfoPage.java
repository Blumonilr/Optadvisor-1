package utf8.optadvisor.util;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.DecimalFormat;
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
import utf8.optadvisor.domain.AllocationResponse;
import utf8.optadvisor.domain.entity.Option;
import utf8.optadvisor.fragment.AllocationSetting;
import utf8.optadvisor.fragment.MyCombination;
import utf8.optadvisor.widget.OptionButton;
import utf8.optadvisor.widget.UserInfoMenuItem;

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

    private AllocationResponse allocationResponse;
    private AllocationSetting allocationSetting;

    private LineChart lineChart1;
    private LineChart lineChart2;
    private LineChart lineChart3;
    int[] colors=new int[]{Color.parseColor("#BF0815"),Color.parseColor("#088B05")};

    public AllocationSetting getAllocationSetting() {
        return allocationSetting;
    }

    public AllocationInfoPage(final Context context, final AllocationResponse allocationResponse, final AllocationSetting allocationSetting) {
        super(context);
        inflate(context, R.layout.linearlayout_allocation_info,this);
        this.allocationSetting=allocationSetting;
        this.allocationResponse=allocationResponse;

        ll_buttons=(LinearLayout)findViewById(R.id.allocation_ll_buttons);


        for (Option option:allocationResponse.getOptions()){
            OptionButton ob=new OptionButton(context,option);
            ob.setText(option.getOptionCode(),option.getType()>0?"买入"+Math.abs(option.getType()):"卖出"+Math.abs(option.getType()));
            ll_buttons.addView(ob);
            buttons.add(ob);
        }

        initMenuItem();

        for (OptionButton bt:buttons){
            bt.setClicked(buttons,id,date,soldPrice,finalPrice,delta,gamma,theta,vega,rho);
        }


        lineChart1 = (LineChart)findViewById(R.id.allocation_line_chart1);
        lineChart2 = (LineChart)findViewById(R.id.allocation_line_chart2);
        lineChart3 = (LineChart)findViewById(R.id.allocation_line_chart3);
        initLineChart();//初始化

        Button add=(Button)findViewById(R.id.allocation_info_bt_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfirmDialog confirm=new ConfirmDialog(context,allocationResponse,AllocationInfoPage.this);
                confirm.show();
            }
        });

        Button back=(Button)findViewById(R.id.allocation_info_bt_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allocationSetting.getLL().removeAllViews();
                allocationSetting.getLL().addView(new AllocationSettingPage(context,allocationSetting));
            }
        });

    }

    private void initMenuItem() {
        DecimalFormat df1=new DecimalFormat("#0.0000");
        DecimalFormat df2=new DecimalFormat("#0.01");

        id=(UserInfoMenuItem)findViewById(R.id.allocation_item_id);
        id.setInfoTextLeft("交易代码");
        id.setInfoTextRight(allocationResponse.getOptions().get(0).getTradeCode());

        date=(UserInfoMenuItem)findViewById(R.id.allocation_item_date);
        date.setInfoTextLeft("到期时间");
        date.setInfoTextRight(allocationResponse.getOptions().get(0).getExpireTime());

        soldPrice=(UserInfoMenuItem)findViewById(R.id.allocation_item_soldprice);
        soldPrice.setInfoTextLeft("执行价格");
        soldPrice.setInfoTextRight(df1.format(allocationResponse.getOptions().get(0).getK()));

        finalPrice=(UserInfoMenuItem)findViewById(R.id.allocation_item_finalprice);
        finalPrice.setInfoTextLeft("成交价格");
        finalPrice.setInfoTextRight(allocationResponse.getOptions().get(0).getType()>0?df1.format(allocationResponse.getOptions().get(0).getPrice1())+"":df1.format(allocationResponse.getOptions().get(0).getPrice2())+"");

        delta=(UserInfoMenuItem)findViewById(R.id.allocation_item_delta);
        delta.setInfoTextLeft("delta");
        delta.setInfoTextRight(df1.format(allocationResponse.getOptions().get(0).getDelta())+"");

        gamma=(UserInfoMenuItem)findViewById(R.id.allocation_item_gamma);
        gamma.setInfoTextLeft("gamma");
        gamma.setInfoTextRight(df1.format(allocationResponse.getOptions().get(0).getGamma())+"");

        theta=(UserInfoMenuItem)findViewById(R.id.allocation_item_theta);
        theta.setInfoTextLeft("theta");
        theta.setInfoTextRight(df1.format(allocationResponse.getOptions().get(0).getTheta())+"");

        vega=(UserInfoMenuItem)findViewById(R.id.allocation_item_vega);
        vega.setInfoTextLeft("vega");
        vega.setInfoTextRight(df1.format(allocationResponse.getOptions().get(0).getVega())+"");

        rho=(UserInfoMenuItem)findViewById(R.id.allocation_item_rho);
        rho.setInfoTextLeft("rho");
        rho.setInfoTextRight(df1.format(allocationResponse.getOptions().get(0).getRho())+"");

        cost=(UserInfoMenuItem)findViewById(R.id.allocation_item_cost);
        cost.setInfoTextLeft("成本");
        cost.setInfoTextRight(df2.format(allocationResponse.getCost())+"");

        guarantee=(UserInfoMenuItem)findViewById(R.id.allocation_item_guarantee);
        guarantee.setInfoTextLeft("保证金");
        guarantee.setInfoTextRight(df2.format(allocationResponse.getBond())+"");

        group_delta=(UserInfoMenuItem)findViewById(R.id.allocation_item_group_delta);
        group_delta.setInfoTextLeft("delta");
        group_delta.setInfoTextRight(df1.format(allocationResponse.getZ_delta())+"");

        group_gamma=(UserInfoMenuItem)findViewById(R.id.allocation_item_group_gamma);
        group_gamma.setInfoTextLeft("gamma");
        group_gamma.setInfoTextRight(df1.format(allocationResponse.getZ_gamma())+"");

        group_theta=(UserInfoMenuItem)findViewById(R.id.allocation_item_group_theta);
        group_theta.setInfoTextLeft("theta");
        group_theta.setInfoTextRight(df1.format(allocationResponse.getZ_theta())+"");

        group_vega=(UserInfoMenuItem)findViewById(R.id.allocation_item_group_vega);
        group_vega.setInfoTextLeft("vega");
        group_vega.setInfoTextRight(df1.format(allocationResponse.getZ_vega())+"");

        group_rho=(UserInfoMenuItem)findViewById(R.id.allocation_item_group_rho);
        group_rho.setInfoTextLeft("rho");
        group_rho.setInfoTextRight(df1.format(allocationResponse.getZ_rho())+"");

        group_expectation=(UserInfoMenuItem)findViewById(R.id.allocation_item_group_expectation);
        group_expectation.setInfoTextLeft("组合期望收益率");
        group_expectation.setInfoTextRight(df1.format(allocationResponse.getEm())+"");

        group_risk=(UserInfoMenuItem)findViewById(R.id.allocation_item_group_risk);
        group_risk.setInfoTextLeft("组合风险率");
        group_risk.setInfoTextRight(df1.format(allocationResponse.getBeta())+"");

        property_expectation=(UserInfoMenuItem)findViewById(R.id.allocation_item_property_expectation);
        property_expectation.setInfoTextLeft("资产期望收益率");
        property_expectation.setInfoTextRight(df1.format(allocationResponse.getReturnOnAssets())+"");

        property_risk=(UserInfoMenuItem)findViewById(R.id.allocation_item_property_risk);
        property_risk.setInfoTextLeft("资产风险率");
        property_risk.setInfoTextRight(df1.format(allocationResponse.getBeta())+"");
    }

    /**
     * 初始化LineChart的一些设置
     */
    private void initLineChart(){
        ChartMarkerView markerView1 = new ChartMarkerView(this.getContext(), R.layout.marker_view);
        markerView1.setChartView(lineChart1);
        lineChart1.setMarker(markerView1);
        ChartMarkerView markerView2 = new ChartMarkerView(this.getContext(), R.layout.marker_view);
        markerView2.setChartView(lineChart2);
        lineChart2.setMarker(markerView2);
        ChartMarkerView markerView3 = new ChartMarkerView(this.getContext(), R.layout.marker_view);
        markerView3.setChartView(lineChart3);
        lineChart3.setMarker(markerView3);

        ArrayList<Entry> data1 = new ArrayList<>();
        ArrayList<Entry> data2=new ArrayList<>();
        ArrayList<Entry> data3=new ArrayList<>();
        for (int i = 0; i < allocationResponse.getAssertPrice2Profit()[0].length; i++) {
            data1.add(new Entry(Float.parseFloat(allocationResponse.getAssertPrice2Profit()[0][i]), Float.parseFloat(allocationResponse.getAssertPrice2Profit()[1][i])));
        }
        boolean cancel=true;
        double base=0;
        for (int i = 0; i <allocationResponse.getProfit2Probability()[0].length; i++) {
            float y=Float.parseFloat(allocationResponse.getProfit2Probability()[1][i]);
            if(cancel){
                if(y>0.1){
                    cancel=false;
                    base=Double.parseDouble(allocationResponse.getProfit2Probability()[0][i]);
                    ChartMarkerView marker= (ChartMarkerView) lineChart2.getMarkerView();
                    marker.setMode(1,base);
                    double dis=(Double.parseDouble(allocationResponse.getProfit2Probability()[0][i])-base)*1000000;
                    data2.add(new Entry((float) dis, y));
                }
            }else {
                double dis=(Double.parseDouble(allocationResponse.getProfit2Probability()[0][i])-base)*1000000;
                data2.add(new Entry((float) dis, y));
            }
        }
        for (int i = 0; i <allocationResponse.getHistoryProfit2Probability()[0].length; i++) {
            data3.add(new Entry(Float.parseFloat(allocationResponse.getHistoryProfit2Probability()[0][i]), Float.parseFloat(allocationResponse.getHistoryProfit2Probability()[1][i])));
        }
        LineDataSet set1= new LineDataSet(data1, "不同标的价格下组合收益");
        LineDataSet set2= new LineDataSet(data2,"组合收益在预期市场内的概率分布");
        LineDataSet set3=new LineDataSet(data3,"组合收益在历史市场内的概率分布");

        setChartDataSet(set1,0);
        setChartDataSet(set2,1);
        setChartDataSet(set3,2);

        //保存LineDataSet集合
        ArrayList<ILineDataSet> dataSets1= new ArrayList<>();
        dataSets1.add(set1);
        ArrayList<ILineDataSet> dataSets2= new ArrayList<>();
        dataSets2.add(set2);
        ArrayList<ILineDataSet> dataSets3= new ArrayList<>();
        dataSets3.add(set3);
        //创建LineData对象 属于LineChart折线图的数据集合
        LineData lineData1 = new LineData(dataSets1);
        LineData lineData2 = new LineData(dataSets2);
        LineData lineData3 = new LineData(dataSets3);
        // 添加到图表中
        lineChart1.setData(lineData1);
        lineChart2.setData(lineData2);
        lineChart3.setData(lineData3);

        lineChart1.setNoDataText("暂无数据显示");//没有数据时显示的文字
        lineChart1.setNoDataTextColor(Color.BLUE);//没有数据时显示文字的颜色
        lineChart1.setDrawGridBackground(false);//chart 绘图区后面的背景矩形将绘制
        lineChart1.setDrawBorders(false);//禁止绘制图表边框的线
        lineChart1.setTouchEnabled(true);
        lineChart1.setDragEnabled(true);
        lineChart1.setScaleXEnabled(true);// 缩放
        lineChart1.setScaleYEnabled(false);

        lineChart2.setNoDataText("暂无数据显示");//没有数据时显示的文字
        lineChart2.setNoDataTextColor(Color.BLUE);//没有数据时显示文字的颜色
        lineChart2.setDrawGridBackground(false);//chart 绘图区后面的背景矩形将绘制
        lineChart2.setDrawBorders(false);//禁止绘制图表边框的线
        lineChart2.setTouchEnabled(true);
        lineChart2.setDragEnabled(true);
        lineChart2.setScaleXEnabled(true);// 缩放
        lineChart2.setScaleYEnabled(false);

        lineChart3.setNoDataText("暂无数据显示");//没有数据时显示的文字
        lineChart3.setNoDataTextColor(Color.BLUE);//没有数据时显示文字的颜色
        lineChart3.setDrawGridBackground(false);//chart 绘图区后面的背景矩形将绘制
        lineChart3.setDrawBorders(false);//禁止绘制图表边框的线
        lineChart3.setTouchEnabled(true);
        lineChart3.setDragEnabled(true);
        lineChart3.setScaleXEnabled(true);// 缩放
        lineChart3.setScaleYEnabled(false);

        YAxis yAxis1=lineChart1.getAxisRight();
        yAxis1.setEnabled(false);
        YAxis yAxis2=lineChart2.getAxisRight();
        yAxis2.setEnabled(false);
        YAxis yAxis3=lineChart3.getAxisRight();
        yAxis3.setEnabled(false);

        XAxis xAxis1=lineChart1.getXAxis();
        xAxis1.setLabelCount(6,false);

        xAxis1.setAxisLineWidth(1f);
        xAxis1.setPosition(XAxis.XAxisPosition.BOTTOM);

        XAxis xAxis2=lineChart2.getXAxis();
        xAxis2.setEnabled(false);

        XAxis xAxis3=lineChart3.getXAxis();
        xAxis3.setLabelCount(6,false);

        xAxis3.setAxisLineWidth(1f);
        xAxis3.setPosition(XAxis.XAxisPosition.BOTTOM);


        //绘制图表
        lineChart1.invalidate();
        lineChart2.invalidate();
        lineChart3.invalidate();
        lineChart1.setVisibility(View.VISIBLE);
        lineChart2.setVisibility(View.VISIBLE);
        lineChart3.setVisibility(View.VISIBLE);
    }



    /**
     * 设置dataSet
     */
    private void setChartDataSet(LineDataSet lineDataSet,int type){
        if(type==0){
            lineDataSet.setColor(Color.RED);
            lineDataSet.setCircleColor(Color.RED);
            lineDataSet.setValueTextColor(Color.RED);
        }else if(type==1){
            lineDataSet.setColor(Color.BLUE);
            lineDataSet.setCircleColor(Color.BLUE);
            lineDataSet.setValueTextColor(Color.BLUE);
        }
        else {
            lineDataSet.setColor(Color.BLACK);
            lineDataSet.setCircleColor(Color.BLACK);
            lineDataSet.setValueTextColor(Color.BLACK);
        }
        lineDataSet.setLineWidth(1f);//设置线宽
        lineDataSet.setDrawCircles(false);
        lineDataSet.enableDashedHighlightLine(10f, 5f, 0f);//点击后的高亮线的显示样式
        lineDataSet.setHighlightLineWidth(0);//设置点击交点后显示高亮线宽
        lineDataSet.setHighlightEnabled(true);//是否禁用点击高亮线
        lineDataSet.setHighLightColor(Color.RED);//设置点击交点后显示交高亮线的颜色
        lineDataSet.setDrawFilled(false);//设置禁用范围背景填充
        lineDataSet.setDrawValues(false);//不显示值
    }


}
