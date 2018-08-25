package utf8.optadvisor.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
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
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.gson.Gson;
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
import utf8.optadvisor.domain.response.ResponseMsg;
import utf8.optadvisor.util.ActivityJumper;
import utf8.optadvisor.util.ExpandableAdapter;
import utf8.optadvisor.util.NetUtil;

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

    private Portfolio currentPortfolio;

    private List<String> portfolioNames;
    private List<Portfolio> currentPortfolioList;

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
                    List<Portfolio> portfolios= new Gson().fromJson(responseMsg.getData().toString(), listType);
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
        Objects.requireNonNull(getActivity()).getMenuInflater().inflate(R.menu.combination_menu, menu);
    }


    /**
     * 在碎片中想要控制选项菜单，必须从对应的活动中控制
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_combination:
                Log.d("我的组合","删除");
                deleteCombination();
                break;
            case R.id.reset_combination_name:
                Log.d("我的组合","重命名");
                renameCombination();
                break;
            case R.id.change_combination_track:
                Log.d("我的组合","更改状态");
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
                        Log.d("我的组合：更改追踪" + id, String.valueOf(responseMsg.getCode()));
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
     * 当前组合变更，通知expandList改变
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
        lineChart = view.findViewById(R.id.my_line_chart);
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
}

