package utf8.optadvisor.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import utf8.optadvisor.R;
import utf8.optadvisor.activity.DetailActivity;
import utf8.optadvisor.domain.entity.Portfolio;
import utf8.optadvisor.util.ActivityJumper;
import utf8.optadvisor.util.ExpandableAdapter;

/**
 * 对我的组合的管理
 */
public class MyCombination extends Fragment implements View.OnClickListener {

    private List<String> groupArray;
    private List<List<String>> childArray;
    private View view;
    private LineChart lineChart;
    private ExpandableListView expandableListView;
    private Button[] buttons;
    private ExpandableAdapter expandableAdapter;
    private Portfolio currentPortfolio;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_combination, container, false);

        initExpandList();
        initLineChart();
        initButtons();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.combination_menu, menu);
    }

    /**
     * 设置所有按钮
     */
    private void initButtons(){
        Button allocationButton=view.findViewById(R.id.allocation_button);
        Button hedgeButton=view.findViewById(R.id.hedge_button);
        Button diyButton=view.findViewById(R.id.diy_button);
        buttons= new Button[]{allocationButton, hedgeButton, diyButton};
        allocationButton.setTextColor(view.getResources().getColor(R.color.colorButton,null));
        allocationButton.setOnClickListener(this);
        hedgeButton.setOnClickListener(this);
        diyButton.setOnClickListener(this);
    }

    /**
     * 设置扩展文本框
     */
    private void initExpandList() {
        groupArray = new ArrayList<String>();
        childArray = new ArrayList<List<String>>();
        groupArray.add("组合信息");

        List<String> tempArray = new ArrayList<String>();
        tempArray.add("期权1");
        tempArray.add("期权2");
        tempArray.add("期权3");

        childArray.add(tempArray);

        expandableListView =view.findViewById(R.id.option_list);
        expandableAdapter=new ExpandableAdapter(getContext(), groupArray, childArray);
        expandableListView.setAdapter(expandableAdapter);
        expandableListView.expandGroup(0);
        setListViewHeight(expandableListView,4);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                Log.d("main", "i:" + i + " i1:" + i1 + " l:" + l);
                Intent intent=new Intent(getContext(), DetailActivity.class);
                intent.putExtra("fromMyCombination",true);
                ActivityJumper.rightEnterLeftExit(intent, Objects.requireNonNull(getContext()), Objects.requireNonNull(getActivity()));
                return false;
            }
        });
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int i) {
                setListViewHeight(expandableListView,childArray.get(0).size()+1);
            }
        });
        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int i) {
                setListViewHeight(expandableListView,1);
            }
        });

    }

    /**
     * 动态设置expandableList高度
     */
    private void setListViewHeight(ExpandableListView listView,int count) {
        ListAdapter listAdapter = listView.getAdapter();
        int totalHeight = 0;
        View headItem=listAdapter.getView(0,null,listView);
        headItem.measure(0,0);
        for (int i = 0; i < count; i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (count - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    private void initLineChart() {
        lineChart = (LineChart) view.findViewById(R.id.my_line_chart);
        Description description = new Description();
        description.setText("组合表现");
        description.setTextColor(getResources().getColor(R.color.colorButtnDark, null));
        description.setTextSize(20);
        lineChart.setDescription(description);//设置图表描述信息
        lineChart.setNoDataText("没有数据熬");//没有数据时显示的文字
        lineChart.setNoDataTextColor(Color.BLUE);//没有数据时显示文字的颜色
        lineChart.setDrawGridBackground(false);//chart 绘图区后面的背景矩形将绘制
        lineChart.setDrawBorders(false);//禁止绘制图表边框的线
        //lineChart.setBorderColor(); //设置 chart 边框线的颜色。
        //lineChart.setBorderWidth(); //设置 chart 边界线的宽度，单位 dp。
        //lineChart.setLogEnabled(true);//打印日志
        //lineChart.notifyDataSetChanged();//刷新数据
        //lineChart.invalidate();//重绘
        bindData();
    }

    /**
     * 绘图——绑定数据
     */
    private void bindData() {
        ArrayList<Entry> values1 = new ArrayList<>();
        ArrayList<Entry> values2 = new ArrayList<>();

        values1.add(new Entry(4, 10));
        values1.add(new Entry(6, 15));
        values1.add(new Entry(9, 20));
        values1.add(new Entry(12, 5));
        values1.add(new Entry(15, 30));

        values2.add(new Entry(3, 110));
        values2.add(new Entry(6, 115));
        values2.add(new Entry(9, 130));
        values2.add(new Entry(12, 85));
        values2.add(new Entry(15, 90));

        //LineDataSet每一个对象就是一条连接线
        LineDataSet set1;
        LineDataSet set2;

        //判断图表中原来是否有数据
        if (lineChart.getData() != null &&
                lineChart.getData().getDataSetCount() > 0) {
            //获取数据1
            set1 = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
            set1.setValues(values1);
            set2 = (LineDataSet) lineChart.getData().getDataSetByIndex(1);
            set2.setValues(values2);
            //刷新数据
            lineChart.getData().notifyDataChanged();
            lineChart.notifyDataSetChanged();
        } else {
            //设置数据1  参数1：数据源 参数2：图例名称
            set1 = new LineDataSet(values1, "测试数据1");
            set1.setColor(Color.BLACK);
            set1.setCircleColor(Color.BLACK);
            set1.setLineWidth(1f);//设置线宽
            set1.setCircleRadius(3f);//设置焦点圆心的大小
            set1.enableDashedHighlightLine(10f, 5f, 0f);//点击后的高亮线的显示样式
            set1.setHighlightLineWidth(2f);//设置点击交点后显示高亮线宽
            set1.setHighlightEnabled(true);//是否禁用点击高亮线
            set1.setHighLightColor(Color.RED);//设置点击交点后显示交高亮线的颜色
            set1.setValueTextSize(9f);//设置显示值的文字大小
            set1.setDrawFilled(false);//设置禁用范围背景填充

            //格式化显示数据
            final DecimalFormat mFormat = new DecimalFormat("###,###,##0");
            set1.setValueFormatter(new IValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    return mFormat.format(value);
                }
            });
            set1.setFillColor(Color.BLACK);


            //设置数据2
            set2 = new LineDataSet(values2, "测试数据2");
            set2.setColor(Color.GRAY);
            set2.setCircleColor(Color.GRAY);
            set2.setLineWidth(1f);
            set2.setCircleRadius(3f);
            set2.setValueTextSize(10f);

            //保存LineDataSet集合
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1); // add the datasets
            dataSets.add(set2);
            //创建LineData对象 属于LineChart折线图的数据集合
            LineData data = new LineData(dataSets);
            // 添加到图表中
            lineChart.setData(data);
            //绘制图表
            lineChart.invalidate();
        }

    }

    @Override
    public void onClick(View view) {
        for(Button each:buttons) {
            each.setTextColor(view.getResources().getColor(R.color.colorDarkGray, null));
        }
        Button chosen= (Button) view;
        chosen.setTextColor(view.getResources().getColor(R.color.colorButton,null));
        switch (view.getId()){
            case R.id.allocation_button:
                Log.d("Main","资产配置");
                childArray=new ArrayList<List<String>>();
                ArrayList<String> tempArray=new ArrayList<>();
                tempArray.add("期权4");
                tempArray.add("期权5");
                childArray.add(tempArray);
                expandableAdapter.setChildArray(childArray);
                break;
            case R.id.hedge_button:
                Log.d("Main","套期保值");
                break;
            case R.id.diy_button:
                Log.d("Main","DIY");
                break;
                default:break;
        }
        expandableListView.collapseGroup(0);
        expandableListView.expandGroup(0);
    }
}

