package utf8.optadvisor.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import utf8.optadvisor.R;
import utf8.optadvisor.activity.DetailActivity;
import utf8.optadvisor.domain.entity.Option;
import utf8.optadvisor.domain.entity.Portfolio;
import utf8.optadvisor.domain.response.MyCombinationResponse;
import utf8.optadvisor.domain.response.ResponseMsg;
import utf8.optadvisor.util.ActivityJumper;
import utf8.optadvisor.util.ChartMarkerView;
import utf8.optadvisor.util.CommaHandler;
import utf8.optadvisor.util.ExpandableAdapter;
import utf8.optadvisor.util.NetUtil;
import utf8.optadvisor.util.PortfolioXFormatter;

/**
 * 对我的组合的管理
 */
public class MyCombination extends Fragment implements View.OnClickListener {

    private List<String> groupArray;//expandList的标题
    private List<List<String>> childArray;//expandList的扩展
    private ExpandableListView expandableListView;

    private View view;

    private ProgressBar progressBar;

    private LineChart lineChart;

    private Button[] buttons;
    private int[] buttonChosen;
    private ExpandableAdapter expandableAdapter;

    private Portfolio currentPortfolio;//当前组合

    private List<String> portfolioNames;//下拉框里的组合名集合
    private List<Portfolio> currentPortfolioList;//下拉框里的组合集合

    private AlertDialog.Builder dialog;

    private ArrayAdapter<String> spinnerAdapter;

    private Spinner spinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_combination, container, false);

        initProgressBar();
        initButtons();
        initSpinner();
        initExpandList();
        initLineChart();

        setHasOptionsMenu(true);//必须在此设置菜单显示
        initDialog();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        setHasOptionsMenu(true);//必须在此设置菜单显示
    }

    /**
     * 刷新组合信息
     */
    private void refreshCombination(){
        NetUtil.INSTANCE.sendGetRequest(NetUtil.SERVER_BASE_ADDRESS + "/portfolio", this.getContext(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                dialog.setTitle("网络连接错误");
                dialog.setMessage("请稍后再试");
                refreshFromServerFail();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                portfolioNames.clear();
                currentPortfolioList=new ArrayList<>();
                ResponseMsg responseMsg=NetUtil.INSTANCE.parseJSONWithGSON(response);
                if(responseMsg.getData()!=null) {
                    java.lang.reflect.Type listType = new TypeToken<List<Portfolio>>() {}.getType();
                    Gson gs = new GsonBuilder()
                            .setPrettyPrinting()
                            .disableHtmlEscaping()
                            .create();
                    List<Portfolio> portfolios= gs.fromJson(CommaHandler.INSTANCE.commaChange(responseMsg.getData().toString()), listType);
                    for (Portfolio portfolio : portfolios) {
                        if (buttonChosen[0] == 1) {
                            if (portfolio.getType() == 0) {
                                currentPortfolioList.add(portfolio);
                                portfolioNames.add(portfolio.getName());
                            }
                        }
                        if (buttonChosen[1] == 1) {
                            if (portfolio.getType() == 1) {
                                currentPortfolioList.add(portfolio);
                                portfolioNames.add(portfolio.getName());
                            }
                        }
                        if (buttonChosen[2] == 1) {
                            if (portfolio.getType() == 2) {
                                currentPortfolioList.add(portfolio);
                                portfolioNames.add(portfolio.getName());
                            }
                        }
                    }
                }
                refreshFromServerSuccess();
            }
        });
    }

    /**
     * 从服务器端获取组合成功
     */
    private void refreshFromServerSuccess(){
        Objects.requireNonNull(this.getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                spinnerAdapter.notifyDataSetChanged();
                if(currentPortfolioList.size()>0){
                    currentPortfolio=currentPortfolioList.get(0);
                }else{
                    currentPortfolio=null;
                }
                currentPortfolioChange(false);
            }
        });
    }

    /**
     * 从服务器端获取组合失败
     */
    private void refreshFromServerFail(){
        Objects.requireNonNull(this.getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                dialogShow();
            }
        });
    }

    /**
     * 设置进度条
     */
    private void initProgressBar(){
        progressBar=view.findViewById(R.id.combination_progress);
        progressBar.setVisibility(View.VISIBLE);
    }

    /**
     * 设置下拉框
     */
    private void initSpinner(){
        spinner=view.findViewById(R.id.content_spinner);
        portfolioNames=new ArrayList<>();
        spinnerAdapter = new ArrayAdapter<>(Objects.requireNonNull(this.getContext()),
                android.R.layout.simple_spinner_item, portfolioNames);//数据源

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(0);
        refreshCombination();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                currentPortfolio=currentPortfolioList.get(position);
                currentPortfolioChange(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    /**
     * 设置弹窗
     */
    private void initDialog(){
        dialog=new AlertDialog.Builder(Objects.requireNonNull(this.getActivity()));
        dialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
    }

    /**
     * 设置菜单
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MyCombination.this.getActivity().getMenuInflater().inflate(R.menu.combination_menu, menu);
    }


    /**
     * 在碎片中想要控制选项菜单，必须从对应的活动中控制
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_combination:
                deleteCombination();
                break;
            case R.id.reset_combination_name:
                renameCombination();
                break;
            case R.id.change_combination_track:
                changeTrackState();
                break;
                default:
                    break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 删除组合
     */
    private void deleteCombination(){
        if(currentPortfolio!=null) {
            String id = String.valueOf(currentPortfolio.getId());
            NetUtil.INSTANCE.sendDeleteRequest(NetUtil.SERVER_BASE_ADDRESS + "/portfolio/" + id, this.getContext(), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    dialog.setTitle("网络连接错误");
                    dialog.setMessage("请稍后再试");
                    dialogShow();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    int index = currentPortfolioList.indexOf(currentPortfolio);
                    currentPortfolioList.remove(index);
                    portfolioNames.remove(index);
                    if (currentPortfolioList.size() > 0) {
                        currentPortfolio = currentPortfolioList.get(0);
                    } else {
                        currentPortfolio = null;
                    }
                    currentPortfolioChange(true);
                }
            });
        }
    }

    /**
     * 重命名组合
     */
    private void renameCombination(){
        if(currentPortfolio!=null) {
            final Portfolio toChange = currentPortfolio;//更改请求传回来的时候，当前组合未必是请求前的组合
            final EditText et = new EditText(this.getContext());
            new AlertDialog.Builder(Objects.requireNonNull(this.getContext())).setTitle("请输入新名称")
                    .setIcon(R.mipmap.ic_rename)
                    .setView(et)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialogInterface, int i) {
                            //按下确定键后的事件
                            final String newName = et.getText().toString();
                            Map<String, String> value = new HashMap<>();
                            value.put("name", newName);
                            String id = String.valueOf(currentPortfolio.getId());
                            NetUtil.INSTANCE.sendPutRequest(NetUtil.SERVER_BASE_ADDRESS + "/portfolio/" + id + "/name", value, getContext(), new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    dialog.setTitle("网络连接错误");
                                    dialog.setMessage("请稍后再试");
                                    dialogShow();
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    ResponseMsg responseMsg = NetUtil.INSTANCE.parseJSONWithGSON(response);
                                    if (responseMsg.getCode() == 0) {
                                        dialog.setTitle("重命名成功");
                                        dialog.setMessage("已更新组合信息");
                                        toChange.setName(newName);
                                        portfolioNames.clear();
                                        for (Portfolio portfolio : currentPortfolioList) {
                                             portfolioNames.add(portfolio.getName());
                                        }
                                        currentPortfolioChange(true);
                                    } else {
                                        dialog.setTitle("重命名失败");
                                        dialog.setMessage("请稍后再试");
                                    }
                                    dialogShow();
                                }
                            });
                        }
                    }).setNegativeButton("取消", null).show();
        }
    }

    /**
     * 更改追踪状态
     */
    private void changeTrackState(){
        if(currentPortfolio!=null) {
            final String id = String.valueOf(currentPortfolio.getId());
            final Portfolio toChange=currentPortfolio;
            NetUtil.INSTANCE.sendPatchRequest(NetUtil.SERVER_BASE_ADDRESS + "/portfolio/" + id + "/track", this.getContext(), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    dialog.setTitle("网络连接错误");
                    dialog.setMessage("请稍后再试");
                    dialogShow();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    ResponseMsg responseMsg = NetUtil.INSTANCE.parseJSONWithGSON(response);
                    if(responseMsg.getCode()==0) {
                        toChange.setTrackingStatus(!currentPortfolio.getTrackingStatus());
                        dialog.setTitle("更改成功");
                        String content = "已为您取消追踪"+toChange.getName();
                        if (currentPortfolio.getTrackingStatus()) {
                            content = "已为您追踪"+toChange.getName();
                        }
                        dialog.setMessage(content);
                    }else {
                        dialog.setTitle("更改时出现错误");
                        dialog.setMessage("请稍后再试");
                    }
                    dialogShow();
                }
            });
        }
    }

    /**
     * 当前组合变更时，通知expandList改变
     */
    private void currentPortfolioChange(final boolean spinnerChanged){
        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (spinnerChanged) {
                    spinnerAdapter.notifyDataSetChanged();
                }
                List<String> tempArray=new ArrayList<>();
                if(currentPortfolio!=null) {
                    for (Option option : currentPortfolio.getOptions()) {
                        tempArray.add(option.getName());
                    }
                }
                childArray.clear();
                childArray.add(tempArray);
                expandableAdapter.notifyDataSetChanged();
                expandableListView.collapseGroup(0);
                expandableListView.expandGroup(0);
            }
        });
        refreshChartData();//更新图片
    }

    /**
     * 显示弹窗
     */
    private void dialogShow(){
        Objects.requireNonNull(this.getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.show();
            }
        });
    }


    /**
     * 设置所有按钮
     */
    private void initButtons(){
        Button allocationButton=view.findViewById(R.id.allocation_button);
        Button hedgeButton=view.findViewById(R.id.hedge_button);
        Button diyButton=view.findViewById(R.id.diy_button);
        buttons= new Button[]{allocationButton, hedgeButton, diyButton};
        buttonChosen=new int[]{1,0,0};
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

        childArray.add(tempArray);

        expandableListView =view.findViewById(R.id.option_list);
        expandableAdapter=new ExpandableAdapter(getContext(), groupArray, childArray);
        expandableListView.setAdapter(expandableAdapter);
        expandableListView.expandGroup(0);
        setListViewHeight(expandableListView,tempArray.size()+1);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                Intent intent=new Intent(getContext(), DetailActivity.class);
                intent.putExtra("fromMyCombination",true);
                StringBuilder optionDetail=new StringBuilder("");
                Option option=currentPortfolio.getOptions()[i1];
                if(option.getType()>0){
                    optionDetail.append("状态:买入\n");
                }else {
                    optionDetail.append("状态:卖出\n");
                }

                if(option.getCp()>0){
                    optionDetail.append("判断:看涨\n");
                }else {
                    optionDetail.append("判断:看跌\n");
                }
                optionDetail.append("到期时间:").append(option.getExpireTime()).append("\n");
                optionDetail.append("执行价格:").append(option.getK()).append("\n");
                if(option.getType()>0){
                    optionDetail.append("买入价格:").append(option.getPrice1()).append("\n");
                }else {
                    optionDetail.append("卖出价格:").append(option.getPrice2()).append("\n");
                }
                optionDetail.append("比例:").append(Math.abs(option.getType())).append("\n");
                optionDetail.append("delta:").append(option.getDelta()).append("\n");
                optionDetail.append("gamma:").append(option.getGamma()).append("\n");
                optionDetail.append("theta:").append(option.getTheta()).append("\n");
                optionDetail.append("vega:").append(option.getVega()).append("\n");
                optionDetail.append("rho:").append(option.getRho()).append("\n");
                intent.putExtra("optionDetail",optionDetail.toString());
                intent.putExtra("optionName",option.getName());
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

    @Override
    public void onClick(View view) {
        for(Button each:buttons) {
            each.setTextColor(view.getResources().getColor(R.color.colorDarkGray, null));
        }
        Button chosen= (Button) view;
        chosen.setTextColor(view.getResources().getColor(R.color.colorButton,null));
        switch (view.getId()){
            case R.id.allocation_button:
                buttonChosen[0]=1;
                buttonChosen[1]=0;
                buttonChosen[2]=0;
                break;
            case R.id.hedge_button:
                buttonChosen[0]=0;
                buttonChosen[1]=1;
                buttonChosen[2]=0;
                break;
            case R.id.diy_button:
                buttonChosen[0]=0;
                buttonChosen[1]=0;
                buttonChosen[2]=1;
                break;
                default:break;
        }
        refreshCombination();
    }

    /**********************************************************************************************************************
     ***************************                   所有和制图直接相关的方法               **********************************
     **********************************************************************************************************************/

    /**
     * 画回测图
     */
    private void initLineChart() {
        lineChart = view.findViewById(R.id.linechart);
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
        xAxis.setValueFormatter(new PortfolioXFormatter());
        xAxis.setLabelCount(6,false);
        xAxis.setGranularity(1f);
        xAxis.setAxisLineWidth(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis yAxis=lineChart.getAxisRight();
        yAxis.setEnabled(false);

        ChartMarkerView markerView = new ChartMarkerView(MyCombination.this.getContext(), R.layout.marker_view);
        markerView.setChartView(lineChart);
        lineChart.setMarker(markerView);//设置交互小图标

        refreshChartData();
    }

    /**
     * 刷新数据
     */
    private void refreshChartData() {
        final Portfolio toDraw=currentPortfolio;

        if(toDraw==null){
            lineChart.clear();
        }
        else  {
            NetUtil.INSTANCE.sendGetRequest(NetUtil.SERVER_BASE_ADDRESS + "/portfolio/" + toDraw.getId(), this.getContext(), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    ResponseMsg responseMsg = NetUtil.INSTANCE.parseJSONWithGSON(response);
                    if (responseMsg.getData() != null) {
                        MyCombinationResponse myCombinationResponse = new Gson().fromJson(CommaHandler.INSTANCE.commaChange(responseMsg.getData().toString()), MyCombinationResponse.class);
                        String[][] graph = myCombinationResponse.getGraph();
                        if (graph != null) {
                            if (toDraw.getType() == 0) {//资产配置
                                drawRecommend(graph);
                            } else if (toDraw.getType() == 1) {
                                drawHedge(graph);
                            } else if (toDraw.getType() == 2) {
                                drawDiy(graph);
                            }
                        }
                    }
                }
            });
        }

    }

    private void drawRecommend(final String[][] graph){
        Objects.requireNonNull(this.getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Entry> backTestData = new ArrayList<>();
                ArrayList<Entry> profitData=new ArrayList<>();
                for (int i = 0; i < graph[0].length; i++) {
                    backTestData.add(new Entry(handleDate(graph[0][i]), Float.parseFloat(graph[1][i])));
                    profitData.add(new Entry(handleDate(graph[0][i]), Float.parseFloat(graph[2][i])));
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
        });
    }

    private void drawHedge(final String[][] graph){
        Objects.requireNonNull(this.getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Entry> data1 = new ArrayList<>();
                ArrayList<Entry> data2=new ArrayList<>();
                ArrayList<Entry> data3=new ArrayList<>();
                for (int i = 0; i < graph[0].length; i++) {
                    String date=graph[0][i].replace("-",".");
                    data1.add(new Entry(handleDate(graph[0][i]), Float.parseFloat(graph[1][i])));
                    data2.add(new Entry(handleDate(graph[0][i]), Float.parseFloat(graph[2][i])));
                    data3.add(new Entry(handleDate(graph[0][i]), Float.parseFloat(graph[3][i])));
                }
                LineDataSet set1= new LineDataSet(data1, "持有收益");
                LineDataSet set2= new LineDataSet(data2,"未持有收益");
                LineDataSet set3= new LineDataSet(data3,"收益差");
                setChartDataSet(set1,0);
                setChartDataSet(set2,1);
                setChartDataSet(set3,2);

                ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                dataSets.add(set1);
                dataSets.add(set2);
                dataSets.add(set3);

                LineData data = new LineData(dataSets);

                lineChart.setData(data);

                XAxis xAxis = lineChart.getXAxis();
                xAxis.setValueFormatter(new PortfolioXFormatter());

                lineChart.setVisibleXRangeMaximum(8f);

                lineChart.invalidate();
            }
        });
    }

    private void drawDiy(final String[][] graph){
        Objects.requireNonNull(this.getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Entry> data1 = new ArrayList<>();
                for (int i = 0; i < graph[0].length; i++) {
                    data1.add(new Entry(handleDate(graph[0][i]), Float.parseFloat(graph[1][i])));
                }

                LineDataSet set1= new LineDataSet(data1, "回测收益曲线");
                setChartDataSet(set1,0);

                ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                dataSets.add(set1);


                LineData data = new LineData(dataSets);

                lineChart.setData(data);

                XAxis xAxis = lineChart.getXAxis();
                xAxis.setValueFormatter(new PortfolioXFormatter());

                lineChart.setVisibleXRangeMaximum(8f);//只有在设置了数据源以后才能设置，x轴最大显示数
                lineChart.invalidate();
            }
        });
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
        lineDataSet.setHighlightLineWidth(0);//设置点击交点后显示高亮线宽
        lineDataSet.setHighlightEnabled(true);//是否使用点击高亮线
        lineDataSet.setHighLightColor(Color.RED);//设置点击交点后显示交高亮线的颜色
        lineDataSet.setDrawValues(false);//不显示值
        //lineDataSet.setValueTextSize(11f);//设置显示值的文字大小
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

