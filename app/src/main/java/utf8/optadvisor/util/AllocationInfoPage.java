package utf8.optadvisor.util;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
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
import utf8.optadvisor.domain.AllocationResponse;
import utf8.optadvisor.domain.entity.Option;
import utf8.optadvisor.fragment.AllocationSetting;
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

    private LineChart lineChart;
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


        lineChart = (LineChart)findViewById(R.id.allocation_line_chart);
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
        id=(UserInfoMenuItem)findViewById(R.id.allocation_item_id);
        id.setInfoTextLeft("交易代码");
        id.setInfoTextRight(allocationResponse.getOptions().get(0).getTradeCode());

        date=(UserInfoMenuItem)findViewById(R.id.allocation_item_date);
        date.setInfoTextLeft("到期时间");
        date.setInfoTextRight(allocationResponse.getOptions().get(0).getExpireTime());

        soldPrice=(UserInfoMenuItem)findViewById(R.id.allocation_item_soldprice);
        soldPrice.setInfoTextLeft("执行价格");
        soldPrice.setInfoTextRight(allocationResponse.getOptions().get(0).getK()+"");

        finalPrice=(UserInfoMenuItem)findViewById(R.id.allocation_item_finalprice);
        finalPrice.setInfoTextLeft("成交价格");
        finalPrice.setInfoTextRight(allocationResponse.getOptions().get(0).getType()>0?allocationResponse.getOptions().get(0).getPrice1()+"":allocationResponse.getOptions().get(0).getPrice2()+"");

        delta=(UserInfoMenuItem)findViewById(R.id.allocation_item_delta);
        delta.setInfoTextLeft("delta");
        delta.setInfoTextRight(allocationResponse.getOptions().get(0).getDelta()+"");

        gamma=(UserInfoMenuItem)findViewById(R.id.allocation_item_gamma);
        gamma.setInfoTextLeft("gamma");
        gamma.setInfoTextRight(allocationResponse.getOptions().get(0).getGamma()+"");

        theta=(UserInfoMenuItem)findViewById(R.id.allocation_item_theta);
        theta.setInfoTextLeft("theta");
        theta.setInfoTextRight(allocationResponse.getOptions().get(0).getTheta()+"");

        vega=(UserInfoMenuItem)findViewById(R.id.allocation_item_vega);
        vega.setInfoTextLeft("vega");
        vega.setInfoTextRight(allocationResponse.getOptions().get(0).getVega()+"");

        rho=(UserInfoMenuItem)findViewById(R.id.allocation_item_rho);
        rho.setInfoTextLeft("rho");
        rho.setInfoTextRight(allocationResponse.getOptions().get(0).getRho()+"");

        cost=(UserInfoMenuItem)findViewById(R.id.allocation_item_cost);
        cost.setInfoTextLeft("成本");
        cost.setInfoTextRight(allocationResponse.getCost()+"");

        guarantee=(UserInfoMenuItem)findViewById(R.id.allocation_item_guarantee);
        guarantee.setInfoTextLeft("保证金");
        guarantee.setInfoTextRight(allocationResponse.getBond()+"");

        group_delta=(UserInfoMenuItem)findViewById(R.id.allocation_item_group_delta);
        group_delta.setInfoTextLeft("delta");
        group_delta.setInfoTextRight(allocationResponse.getZ_delta()+"");

        group_gamma=(UserInfoMenuItem)findViewById(R.id.allocation_item_group_gamma);
        group_gamma.setInfoTextLeft("gamma");
        group_gamma.setInfoTextRight(allocationResponse.getZ_gamma()+"");

        group_theta=(UserInfoMenuItem)findViewById(R.id.allocation_item_group_theta);
        group_theta.setInfoTextLeft("theta");
        group_theta.setInfoTextRight(allocationResponse.getZ_theta()+"");

        group_vega=(UserInfoMenuItem)findViewById(R.id.allocation_item_group_vega);
        group_vega.setInfoTextLeft("vega");
        group_vega.setInfoTextRight(allocationResponse.getZ_vega()+"");

        group_rho=(UserInfoMenuItem)findViewById(R.id.allocation_item_group_rho);
        group_rho.setInfoTextLeft("rho");
        group_rho.setInfoTextRight(allocationResponse.getZ_rho()+"");

        group_expectation=(UserInfoMenuItem)findViewById(R.id.allocation_item_group_expectation);
        group_expectation.setInfoTextLeft("组合期望收益率");
        group_expectation.setInfoTextRight(allocationResponse.getEm()+"");

        group_risk=(UserInfoMenuItem)findViewById(R.id.allocation_item_group_risk);
        group_risk.setInfoTextLeft("组合风险率");
        group_risk.setInfoTextRight(allocationResponse.getBeta()+"");

        property_expectation=(UserInfoMenuItem)findViewById(R.id.allocation_item_property_expectation);
        property_expectation.setInfoTextLeft("资产期望收益率");
        property_expectation.setInfoTextRight(allocationResponse.getReturnOnAssets()+"");

        property_risk=(UserInfoMenuItem)findViewById(R.id.allocation_item_property_risk);
        property_risk.setInfoTextLeft("资产风险率");
        property_risk.setInfoTextRight(allocationResponse.getBeta()+"");
    }

    /**
     * 初始化LineChart的一些设置
     */
    private void initLineChart(){

        ArrayList<Entry> backTestData = new ArrayList<>();
        ArrayList<Entry> profitData=new ArrayList<>();
        for (int i = 0; i <allocationResponse.getGraph().get(0).size(); i++) {
            backTestData.add(new Entry(handleDate(allocationResponse.getGraph().get(0).get(i)), Float.parseFloat(allocationResponse.getGraph().get(1).get(i))));
            profitData.add(new Entry(handleDate(allocationResponse.getGraph().get(0).get(i)), Float.parseFloat(allocationResponse.getGraph().get(2).get(i))));
        }
        LineDataSet set1= new LineDataSet(backTestData, "回测收益曲线");
        LineDataSet set2= new LineDataSet(profitData,"资产收益曲线");

        setChartDataSet(set1,0);
        setChartDataSet(set2,1);

        //保存LineDataSet集合
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        dataSets.add(set2);
        //创建LineData对象 属于LineChart折线图的数据集合
        LineData data = new LineData(dataSets);
        // 添加到图表中
        lineChart.setData(data);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new PortfolioXFormatter());

        lineChart.setVisibleXRangeMaximum(8f);
        //绘制图表
        lineChart.invalidate();
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
        lineDataSet.setCircleRadius(3f);//设置焦点圆心的大小
        lineDataSet.enableDashedHighlightLine(10f, 5f, 0f);//点击后的高亮线的显示样式
        lineDataSet.setHighlightLineWidth(2f);//设置点击交点后显示高亮线宽
        lineDataSet.setHighlightEnabled(true);//是否禁用点击高亮线
        lineDataSet.setHighLightColor(Color.RED);//设置点击交点后显示交高亮线的颜色
        lineDataSet.setValueTextSize(11f);//设置显示值的文字大小
        lineDataSet.setDrawFilled(false);//设置禁用范围背景填充
    }


    /**
     * 把graph的日期转换成相对序号
     */
    private float handleDate(String str){
        if(TextUtils.isEmpty(str)) return 0;
        int index=str.indexOf("-");
        String year=str.substring(0,index);
        String month=str.substring(index+1);

        return (Float.parseFloat(year)-PortfolioXFormatter.baseYear)*12+Float.parseFloat(month)-PortfolioXFormatter.baseMonth;
    }

}
