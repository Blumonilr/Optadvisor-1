package utf8.optadvisor.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
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
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
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
import utf8.optadvisor.domain.Constant;
import utf8.optadvisor.domain.entity.Option;
import utf8.optadvisor.domain.entity.Portfolio;
import utf8.optadvisor.domain.response.MyCombinationResponse;
import utf8.optadvisor.domain.response.ResponseMsg;
import utf8.optadvisor.util.ChartMarkerView;
import utf8.optadvisor.util.CommaHandler;
import utf8.optadvisor.util.ExpandableAdapter;
import utf8.optadvisor.util.NetUtil;
import utf8.optadvisor.util.PortfolioXFormatter;

/**
 * 对我的组合的管理
 */
public class MyCombination extends Fragment implements View.OnClickListener{

    private List<String> groupArray;//expandList的标题
    private List<List<String>> childArray;//expandList的扩展
    private ExpandableListView expandableListView;

    private View view;

    private ProgressBar progressBar;

    private LineChart lineChart1;
    private LineChart lineChart2;
    private LineChart lineChart3;

    private Button[] buttons;
    private int[] buttonChosen;
    private ExpandableAdapter expandableAdapter;

    private Portfolio currentPortfolio;//当前组合

    private List<String> portfolioNames;//下拉框里的组合名集合
    private List<Portfolio> currentPortfolioList;//下拉框里的组合集合

    private AlertDialog.Builder dialog;

    private ArrayAdapter<String> spinnerAdapter;

    private Spinner spinner;

    private TextView cost;
    private TextView bond;
    private TextView timeLine;
    private TextView em;
    private TextView beta;
    private TextView returnAsset;

    private LinearLayout optionInformation;
    private TextView optionName;
    private TextView optionCode;
    private TextView theoriticValue;
    private TextView valueState;
    private TextView innerValue;
    private TextView timeValue;
    private TextView dealAmount;
    private TextView optionDelta;
    private TextView optionGamma;
    private TextView optionTheta;
    private TextView optionVega;
    private TextView optionVolatility;
    private TextView optionMaxPrice;
    private TextView optionMinPrice;

    private TextView betaTitle;
    private TextView returnAssetTitle;

    private static final int DISTANCE=35;
    private static final int STANDARD_OPTION_RESULT_LENGTH=14;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_combination, container, false);

        initAllText();
        initProgressBar();
        initButtons();
        initSpinner();
        initExpandList();
        initLineChart();

        setHasOptionsMenu(true);//必须在此设置菜单显示
        initDialog();

        return view;
    }

    private void initAllText(){
        cost=view.findViewById(R.id.cost);
        bond=view.findViewById(R.id.bond);
        timeLine=view.findViewById(R.id.time_line);
        em=view.findViewById(R.id.em);
        beta=view.findViewById(R.id.beta);
        returnAsset=view.findViewById(R.id.return_asset);

        optionInformation=view.findViewById(R.id.option_information);
        optionInformation.setVisibility(View.GONE);

        optionName=view.findViewById(R.id.option_name);
        optionCode=view.findViewById(R.id.option_code);
        theoriticValue=view.findViewById(R.id.theoritic_value);
        valueState=view.findViewById(R.id.value_state);
        innerValue=view.findViewById(R.id.inner_value);
        timeValue=view.findViewById(R.id.time_value);
        dealAmount=view.findViewById(R.id.deal_amount);
        optionDelta=view.findViewById(R.id.option_delta);
        optionGamma=view.findViewById(R.id.option_gamma);
        optionTheta=view.findViewById(R.id.option_theta);
        optionVega=view.findViewById(R.id.option_vega);
        optionVolatility=view.findViewById(R.id.option_volatility);
        optionMaxPrice=view.findViewById(R.id.option_max_price);
        optionMinPrice=view.findViewById(R.id.option_min_price);

        betaTitle=view.findViewById(R.id.beta_title);
        returnAssetTitle=view.findViewById(R.id.return_asset_title);
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
                    String str=responseMsg.getData().toString();
                    List<Portfolio> portfolios= gs.fromJson(CommaHandler.INSTANCE.commaChange(str), listType);
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

                    cost.setText(String.format("%.4f",currentPortfolio.getCost()));
                    if(currentPortfolio.getOptions()!=null&&currentPortfolio.getOptions().length>0){
                        timeLine.setText(currentPortfolio.getOptions()[0].getExpireTime());
                    }else {
                        timeLine.setText("");
                    }
                    beta.setText(String.format("%.4f",currentPortfolio.getBeta()));
                    bond.setText(String.format("%.4f",currentPortfolio.getBond()));
                    em.setText(String.format("%.4f",currentPortfolio.getEm())+"%");
                    returnAsset.setText(String.format("%.4f",currentPortfolio.getReturnOnAssets()));
                    switch (currentPortfolio.getType()){
                        case Constant.RECOMMEND_PORTFOLIO:
                            returnAsset.setVisibility(View.VISIBLE);
                            returnAssetTitle.setVisibility(View.VISIBLE);
                            beta.setVisibility(View.VISIBLE);
                            betaTitle.setVisibility(View.VISIBLE);
                            break;
                        case Constant.HEDGE:
                            returnAsset.setVisibility(View.GONE);
                            returnAssetTitle.setVisibility(View.GONE);
                            beta.setVisibility(View.GONE);
                            betaTitle.setVisibility(View.GONE);
                            break;
                        case Constant.DIY:
                            returnAsset.setVisibility(View.GONE);
                            returnAssetTitle.setVisibility(View.GONE);
                            beta.setVisibility(View.VISIBLE);
                            betaTitle.setVisibility(View.VISIBLE);
                            break;
                    }
                }else {
                    cost.setText("");
                    timeLine.setText("");
                    beta.setText("");
                    bond.setText("");
                    em.setText("");
                    returnAsset.setText("");
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
                final Option option=currentPortfolio.getOptions()[i1];
                final int type=option.getCp();

                String optionCodePart=option.getOptionCode().replace("OP","SO");

                NetUtil.INSTANCE.sendGetRequest("http://hq.sinajs.cn/list="+optionCodePart, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        dialog.setTitle("网络连接错误");
                        dialog.setMessage("期权网页出错");
                        dialogShow();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String optionDetail=response.body().string();
                        currentOptionChange(optionDetail,type);
                    }
                });

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
                optionInformation.setVisibility(View.GONE);
            }
        });

    }

    private void currentOptionChange(final String optionDetail, final int type){
        Objects.requireNonNull(this.getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(optionDetail!=null&&optionDetail.contains("\"")) {
                    optionInformation.setVisibility(View.VISIBLE);
                    int index = optionDetail.indexOf("\"");
                    String str1 = optionDetail.substring(index + 1).replace(",,,,", ",");
                    String[] result = str1.split(",");
                    if(result.length==14) {
                        optionName.setText(String.format("合约简称 %s", result[0]));
                        optionCode.setText(String.format("交易代码 %s", result[9]));
                        dealAmount.setText(String.format("成交量 %s", result[1]));
                        optionDelta.setText(String.format("Delta %s", result[2]));
                        optionGamma.setText(String.format("Gamma %s", result[3]));
                        optionTheta.setText(String.format("Theta %s", result[4]));
                        optionVega.setText(String.format("Vega %s", result[5]));
                        optionVolatility.setText(String.format("隐含波动率 %s", result[6]));
                        optionMaxPrice.setText(String.format("最高价 %s", result[7]));
                        optionMinPrice.setText(String.format("最低价 %s", result[8]));

                        final double latestPrice=Double.parseDouble(result[11]);
                        final double usePrice=Double.parseDouble(result[10]);

                        theoriticValue.setText(String.format("理论价值 %s", result[12]));
                        NetUtil.INSTANCE.sendGetRequest("http://hq.sinajs.cn/list=s_sh510050,sh510050", new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String etfValue = response.body().string();
                                try {
                                    setOtherOptionValue(latestPrice, usePrice, etfValue, type);
                                }catch (NumberFormatException e){
                                    dialog.setTitle("网络错误");
                                    dialog.setMessage("期权数据计算可能存在误差");
                                    dialogShow();
                                }
                            }
                        });
                    }
                }else {
                    optionName.setText("合约简称");
                    optionCode.setText("交易代码");
                    dealAmount.setText("成交量");
                    optionDelta.setText("Delta");
                    optionGamma.setText("Gamma");
                    optionTheta.setText("Theta");
                    optionVega.setText("Vega");
                    optionVolatility.setText("隐含波动率");
                    optionMaxPrice.setText("最高价");
                    optionMinPrice.setText("最低价");
                    theoriticValue.setText("理论价值");
                    timeValue.setText("时间价值");
                    innerValue.setText("内在价值");
                    valueState.setText("价值状态");
                }
            }
        });
    }

    private void setOtherOptionValue(final double latestPrice, final double usePrice, final String etfValue, final int type)throws NumberFormatException{
        Objects.requireNonNull(this.getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int index=etfValue.indexOf("50ETF,");
                String temp1=etfValue.substring(index+"50ETF,".length());
                index=temp1.indexOf(",");
                double priceMark=Double.parseDouble(temp1.substring(0,index))-usePrice;
                double innerPrice;
                if(type==1){
                    if(priceMark>0){
                        valueState.setText("价值状态 实值");
                        innerPrice=priceMark;
                    }
                    else{
                        if(priceMark==0){
                            valueState.setText("价值状态 平值");
                        }else {
                            valueState.setText("价值状态 虚值");
                        }
                        innerPrice=0;
                    }
                    double timeValueText=((latestPrice-innerPrice)<=0)?0:latestPrice-innerPrice;
                    innerValue.setText(String.format("内在价值 %.4f", innerPrice));
                    timeValue.setText(String.format("时间价值 %.4f", timeValueText));
                }else if(type==-1){
                    if(priceMark<0){
                        valueState.setText("价值状态 实值");
                        innerPrice=-priceMark;
                    }
                    else{
                        if(priceMark==0){
                            valueState.setText("价值状态 平值");
                        }else {
                            valueState.setText("价值状态 虚值");
                        }
                        innerPrice=0;
                    }
                    double timeValueText=latestPrice-innerPrice;
                    innerValue.setText(String.format("内在价值 %.4f", innerPrice));
                    timeValue.setText(String.format("时间价值 %.4f", timeValueText));
                }
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
        params.height = totalHeight+DISTANCE
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
     *                                              所有和制图直接相关的方法                                               *
     **********************************************************************************************************************/

    /**
     * 画回测图
     */
    private void initLineChart() {
        lineChart1 = view.findViewById(R.id.linechart1);
        lineChart2=view.findViewById(R.id.linechart2);
        lineChart3=view.findViewById(R.id.linechart3);
        setLineChart(lineChart1);
        setLineChart(lineChart2);
        setLineChart(lineChart3);

        XAxis xAxis=lineChart2.getXAxis();
        xAxis.setDrawLabels(false);
        refreshChartData();
    }

    private void setLineChart(LineChart lineChart){
        lineChart.setNoDataText("暂无数据显示");//没有数据时显示的文字
        lineChart.setNoDataTextColor(Color.BLUE);//没有数据时显示文字的颜色
        lineChart.setDrawGridBackground(false);//chart 绘图区后面的背景矩形将绘制
        lineChart.setDrawBorders(false);//禁止绘制图表边框的线
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleXEnabled(true);// 缩放
        lineChart.setScaleYEnabled(false);
        lineChart.setDescription(null);

        lineChart.setVisibility(View.GONE);

        XAxis xAxis=lineChart.getXAxis();
        xAxis.setLabelCount(6,false);
        xAxis.setAxisLineWidth(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis yAxis=lineChart.getAxisRight();
        yAxis.setEnabled(false);

        ChartMarkerView markerView = new ChartMarkerView(MyCombination.this.getContext(), R.layout.marker_view);
        markerView.setChartView(lineChart);
        lineChart.setMarker(markerView);//设置交互小图标
    }

    /**
     * 刷新数据
     */
    private void refreshChartData(){
        final Portfolio toDraw=currentPortfolio;

        if(toDraw==null){
            clearChart();
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
                        clearChart();
                        if (toDraw.getType() == 0) {//资产配置
                            String[][] assertPrice2Profit=myCombinationResponse.getAssertPrice2Profit();
                            String[][] profit2Probability=myCombinationResponse.getProfit2Probability();
                            String[][] historyProfit2Probability=myCombinationResponse.getHistoryProfit2Probability();
                            if(assertPrice2Profit!=null&&profit2Probability!=null&&historyProfit2Probability!=null) {
                                drawRecommend(assertPrice2Profit,profit2Probability,historyProfit2Probability);
                            }
                        } else if (toDraw.getType() == 1) {
                            String[][] graph=myCombinationResponse.getGraph();
                            if(graph!=null) {
                                drawHedge(graph);
                            }
                        } else if (toDraw.getType() == 2) {
                            String[][] assertPrice2Profit=myCombinationResponse.getAssertPrice2Profit();
                            String[][] historyProfit2Probability=myCombinationResponse.getHistoryProfit2Probability();
                            if(assertPrice2Profit!=null&&historyProfit2Probability!=null) {
                                drawDiy(assertPrice2Profit, historyProfit2Probability);
                            }
                        }
                    }
                }
            });
        }

    }

    private void clearChart(){
        Objects.requireNonNull(this.getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lineChart1.clear();
                lineChart1.setData(null);
                lineChart1.invalidate();
                lineChart1.setVisibility(View.GONE);
                lineChart2.clear();
                lineChart2.setData(null);
                lineChart2.invalidate();
                lineChart2.setVisibility(View.GONE);
                lineChart3.clear();
                lineChart3.setData(null);
                lineChart3.invalidate();
                lineChart3.setVisibility(View.GONE);
            }
        });


    }

    private void drawRecommend(final String[][] assertPrice2Profit, final String[][] profit2Probability, final String[][] historyProfit2Probability){
        Objects.requireNonNull(this.getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Entry> data1 = new ArrayList<>();
                ArrayList<Entry> data2=new ArrayList<>();
                ArrayList<Entry> data3=new ArrayList<>();
                for (int i = 0; i < assertPrice2Profit[0].length; i++) {
                    data1.add(new Entry(Float.parseFloat(assertPrice2Profit[0][i]), Float.parseFloat(assertPrice2Profit[1][i])));
                }
                boolean cancel=true;
                double base=0;
                for (int i = 0; i <profit2Probability[0].length; i++) {
                    float y=Float.parseFloat(profit2Probability[1][i]);
                    if(cancel){
                        if(y>0){
                            cancel=false;
                            base=Double.parseDouble(profit2Probability[0][i]);
                            ChartMarkerView markerView= (ChartMarkerView) lineChart2.getMarkerView();
                            markerView.setMode(1,base);
                            double dis=(Double.parseDouble(profit2Probability[0][i])-base)*1000000;
                            data2.add(new Entry((float) dis, y));
                        }
                    }else {
                        double dis=(Double.parseDouble(profit2Probability[0][i])-base)*1000000;
                        data2.add(new Entry((float) dis, y));
                    }
                }
                for (int i = 0; i <historyProfit2Probability[0].length; i++) {
                    data3.add(new Entry(Float.parseFloat(historyProfit2Probability[0][i]), Float.parseFloat(historyProfit2Probability[1][i])));
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

                //绘制图表
                lineChart1.invalidate();
                lineChart2.invalidate();
                lineChart3.invalidate();
                lineChart1.setVisibility(View.VISIBLE);
                lineChart2.setVisibility(View.VISIBLE);
                lineChart3.setVisibility(View.VISIBLE);
            }
        });
    }

    private void drawHedge(final String[][] graph){
        Objects.requireNonNull(this.getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Entry> data1 = new ArrayList<>();
                ArrayList<Entry> data2=new ArrayList<>();
                float baseX=0;
                for (int i = 0; i < graph[1].length; i++) {
                    data1.add(new Entry(baseX, Float.parseFloat(graph[1][i])));
                    data2.add(new Entry(baseX, Float.parseFloat(graph[2][i])));
                    baseX+=0.01;
                }
                LineDataSet set1= new LineDataSet(data1, "不持有时损失");
                LineDataSet set2= new LineDataSet(data2,"持有时损失");

                setChartDataSet(set1,0);
                setChartDataSet(set2,1);

                ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                dataSets.add(set1);
                dataSets.add(set2);

                LineData data = new LineData(dataSets);

                lineChart1.setData(data);

                lineChart1.invalidate();
                lineChart1.setVisibility(View.VISIBLE);
                lineChart2.setVisibility(View.GONE);
                lineChart3.setVisibility(View.GONE);
            }
        });
    }

    private void drawDiy(final String[][] assertPrice2Profit, final String[][] historyProfit2Probability){
        Objects.requireNonNull(this.getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Entry> data1 = new ArrayList<>();
                ArrayList<Entry> data3=new ArrayList<>();
                for (int i = 0; i < assertPrice2Profit[0].length; i++) {
                    data1.add(new Entry(Float.parseFloat(assertPrice2Profit[0][i]), Float.parseFloat(assertPrice2Profit[1][i])));
                }

                for (int i = 0; i < historyProfit2Probability[0].length; i++) {
                    data3.add(new Entry(Float.parseFloat(historyProfit2Probability[0][i]), Float.parseFloat(historyProfit2Probability[1][i])));
                }
                LineDataSet set1= new LineDataSet(data1, "不同标的价格下组合收益");
                LineDataSet set3=new LineDataSet(data3,"组合收益在历史市场内的概率分布");

                setChartDataSet(set1,0);
                setChartDataSet(set3,2);

                //保存LineDataSet集合
                ArrayList<ILineDataSet> dataSets1= new ArrayList<>();
                dataSets1.add(set1);
                ArrayList<ILineDataSet> dataSets3= new ArrayList<>();
                dataSets3.add(set3);
                //创建LineData对象 属于LineChart折线图的数据集合
                LineData lineData1 = new LineData(dataSets1);
                LineData lineData3 = new LineData(dataSets3);
                // 添加到图表中
                lineChart1.setData(lineData1);
                lineChart3.setData(lineData3);

                //绘制图表
                lineChart1.invalidate();
                lineChart3.invalidate();
                lineChart1.setVisibility(View.VISIBLE);
                lineChart2.setVisibility(View.GONE);
                lineChart3.setVisibility(View.VISIBLE);
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
        lineDataSet.setDrawCircles(false);

        lineDataSet.enableDashedHighlightLine(10f, 5f, 0f);//点击后的高亮线的显示样式
        lineDataSet.setHighlightLineWidth(0);//设置点击交点后显示高亮线宽
        lineDataSet.setHighlightEnabled(true);//是否使用点击高亮线
        lineDataSet.setHighLightColor(Color.RED);//设置点击交点后显示交高亮线的颜色
        lineDataSet.setDrawValues(false);//不显示值
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

