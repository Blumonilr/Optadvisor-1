package utf8.optadvisor.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v4.app.FragmentTransaction;

import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import utf8.optadvisor.R;
import utf8.optadvisor.activity.LoginActivity;
import utf8.optadvisor.util.MyXFormatter;
import utf8.optadvisor.util.NetUtil;


public class OptionShow extends Fragment {
    TextView present_price;
    TextView ups_and_downs;
    TextView yesterday_end;
    TextView today_start;
    TextView highest_price;
    TextView lowest_price;
    TextView quantity;
    TextView volume;
    TextView update_time;
    TextView status;
    LineChart lineChart;
    DIY diy;
    OptionContract optionContract;
    CandleStickChart candleStickChart;
    LineChartInfo lineChartInfo=new LineChartInfo();
    CandleStickChartInfo candleStickChartInfo=new CandleStickChartInfo();

    private static final int INFO_SUCCESS = 0;//获取50etf成功的标识
    private static final int INFO_FAILURE = 1;//获取50etf失败的标识
    private static final int LINE_SUCCESS = 2;//获取折线成功的标识
    private static final int LINE_FAILURE = 3;//获取折线失败的标识
    private static final int CANDLE_SUCCESS = 4;//获取k线成功的标识
    private static final int CANDLE_FAILURE = 5;//获取k线失败的标识
    private Gson gson=new Gson();
    private AlertDialog.Builder dialog;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage (Message msg) {//此方法在ui线程运行
            switch(msg.what) {
                case INFO_SUCCESS:
                    String info=(String) msg.obj;
                    draw50ETF(info);
                    break;
                case INFO_FAILURE:
                    System.out.println("1fail");
                    break;
                case LINE_SUCCESS:
                    String jsonData=(String)msg.obj;
                    lineChartInfo=gson.fromJson(jsonData,LineChartInfo.class);
                    drawLineChart(lineChartInfo);
                    break;
                case LINE_FAILURE:
                    System.out.println("2fail");
                    break;
                case CANDLE_FAILURE:
                    System.out.println("3fail");
                    break;
                case CANDLE_SUCCESS:
                    String jsonData1=(String)msg.obj;
                    candleStickChartInfo=gson.fromJson(jsonData1,CandleStickChartInfo.class);
                    drawCandleStickChart(candleStickChartInfo);
                    break;
            }
        }
    };




    @Override
    /**
     * 展示行情信息
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_option_show, container, false);
        present_price= view.findViewById(R.id.present_price);
        ups_and_downs= view.findViewById(R.id.ups_and_downs);
        yesterday_end=view.findViewById(R.id.yesterday_end);
        today_start=(TextView) view.findViewById(R.id.today_start);
        highest_price=(TextView) view.findViewById(R.id.highest_price);
        lowest_price=(TextView) view.findViewById(R.id.lowest_price);
        quantity=(TextView) view.findViewById(R.id.quantity);
        volume=(TextView) view.findViewById(R.id.volume);
        update_time=(TextView) view.findViewById(R.id.update_time);
        status=(TextView) view.findViewById(R.id.status);
        initDialog();
        get50ETFinfo();
        lineChart=(LineChart) view.findViewById(R.id.line_chart);
        initLineChart();
        candleStickChart=(CandleStickChart) view.findViewById(R.id.candle_stick_chart);
        initCandleStickChart();





        Button button1=view.findViewById(R.id.button_50ETF);
        Button button2= view.findViewById(R.id.button_option);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                if(diy!=null){
                    transaction.hide(diy);
                    transaction.commit();
                }
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                if(diy==null){
                    diy=new DIY();
                    transaction.add(R.id.frame_50ETF, diy);
                }
                transaction.show(diy);
                transaction.commit();
            }
        });


        return view;
    }


    //初始化线性图
    private void initLineChart(){
        NetUtil.INSTANCE.sendGetRequest("http://yunhq.sse.com.cn:32041/v1/sh1/line/510050?select=time,price,volume", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.obtainMessage(LINE_FAILURE).sendToTarget();
                dialog.setTitle("网络连接错误");
                dialog.setMessage("请稍后再试");
                dialogShow();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                mHandler.obtainMessage(LINE_SUCCESS,response.body().string()).sendToTarget();

            }
        });
    }
    private void drawLineChart(LineChartInfo lineChartInfo){
        if(lineChartInfo.getLine()!=null) {
            lineChart.setDrawGridBackground(false);//chart 绘图区后面的背景矩形将绘制
            lineChart.setDrawBorders(false);//禁止绘制图表边框的线
            lineChart.setScaleEnabled(true);
            ArrayList<Entry> entries = new ArrayList<>();
            for (int i = 0; i < lineChartInfo.getLine().length; i++) {
                int x = Integer.valueOf(lineChartInfo.getLine()[i][0]) / 10000 * 60 + Integer.valueOf(lineChartInfo.getLine()[i][0]) % 10000 / 100;
                entries.add(new Entry(x, Float.parseFloat(lineChartInfo.getLine()[i][1])));
            }
            LineDataSet set1;
            if (lineChart.getData() != null &&
                    lineChart.getData().getDataSetCount() > 0) {
                //获取数据1
                set1 = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
                set1.setValues(entries);
                lineChart.getData().notifyDataChanged();
                lineChart.notifyDataSetChanged();
            } else {
                set1 = new LineDataSet(entries, "分时线");
                set1.setLineWidth(1f);//设置线宽
                set1.setColor(Color.parseColor("#D15FEE"));
                set1.setDrawCircles(false);
                set1.enableDashedHighlightLine(10f, 5f, 0f);//点击后的高亮线的显示样式
                set1.setHighlightLineWidth(2f);//设置点击交点后显示高亮线宽
                set1.setHighlightEnabled(true);//是否禁用点击高亮线
                set1.setHighLightColor(Color.parseColor("#D15FEE"));//设置点击交点后显示交高亮线的颜色
                set1.setDrawFilled(false);//设置禁用范围背景填充
                ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                dataSets.add(set1); // add the datasets
                XAxis xAxis = lineChart.getXAxis();
                xAxis.setValueFormatter(new MyXFormatter(true));//x轴自定义格式
                YAxis leftAxis = lineChart.getAxisLeft();
                YAxis rightAxis = lineChart.getAxisRight();
                //设置图表右边的y轴禁用
                rightAxis.setEnabled(false);
                //创建LineData对象 属于LineChart折线图的数据集合
                LineData data = new LineData(dataSets);// 添加到图表中
                lineChart.setData(data);//绘制图表
                lineChart.invalidate();
            }
        }



    }
    //线性图数据
    private class LineChartInfo {
        private String code;
        private String pre_close;
        private String date;
        private String time;
        private String total;
        private String begin;
        private String end;
        private String[][] line;
        //"code":"510050","prev_close":2.526,"date":20180814,"time":151455,"total":241,"begin":0,"end":241,"line":

        public void setBegin(String begin) {
            this.begin = begin;
        }


        public void setCode(String code) {
            this.code = code;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public void setEnd(String end) {
            this.end = end;
        }

        public void setLine(String[][] line) {
            this.line = line;
        }

        public void setPre_close(String pre_close) {
            this.pre_close = pre_close;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getBegin() {
            return begin;
        }

        public String getCode() {
            return code;
        }

        public String getDate() {
            return date;
        }

        public String getEnd() {
            return end;
        }

        public String getPre_close() {
            return pre_close;
        }

        public String getTime() {
            return time;
        }

        public String getTotal() {
            return total;
        }

        public String[][] getLine() {
            return line;
        }
    }



    //初始化k线图
    private void initCandleStickChart(){
        NetUtil.INSTANCE.sendGetRequest("http://yunhq.sse.com.cn:32041/v1/sh1/dayk/510050?select=date,open,high,low,close,volume&begin=-300&end=-1", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.obtainMessage(CANDLE_FAILURE).sendToTarget();
                dialog.setTitle("网络连接错误");
                dialog.setMessage("请稍后再试");
                dialogShow();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                mHandler.obtainMessage(CANDLE_SUCCESS,response.body().string()).sendToTarget();
            }
        });
    }
    private void drawCandleStickChart(CandleStickChartInfo candleStickChartInfo){
        if(candleStickChartInfo.getKline()!=null) {
            candleStickChart.setDrawGridBackground(false);//chart 绘图区后面的背景矩形将绘制
            candleStickChart.setDrawBorders(false);//禁止绘制图表边框的线
            candleStickChart.setScaleXEnabled(true);
            candleStickChart.setScaleYEnabled(false);
            candleStickChart.setMaxVisibleValueCount(8);
            ArrayList<CandleEntry> candleEntries = new ArrayList<>();
            for (int i = 0; i < 299; i++) {
                CandleEntry entry = new CandleEntry(i,
                        Float.valueOf(candleStickChartInfo.getKline()[i][2]),
                        Float.valueOf(candleStickChartInfo.getKline()[i][3]),
                        Float.valueOf(candleStickChartInfo.getKline()[i][1]),
                        Float.valueOf(candleStickChartInfo.getKline()[i][4]));
                candleEntries.add(entry);
            }
            CandleDataSet set1;
            if (candleStickChart.getData() != null &&
                    candleStickChart.getData().getDataSetCount() > 0) {
                //获取数据1
                set1 = (CandleDataSet) candleStickChart.getData().getDataSetByIndex(0);
                set1.setValues(candleEntries);
                candleStickChart.getData().notifyDataChanged();
                candleStickChart.notifyDataSetChanged();
            } else {
                set1 = new CandleDataSet(candleEntries, "日k线");
                set1.setColor(Color.parseColor("#D15FEE"));
                set1.enableDashedHighlightLine(10f, 5f, 0f);//点击后的高亮线的显示样式
                set1.setHighlightLineWidth(2f);//设置点击交点后显示高亮线宽
                set1.setHighlightEnabled(true);//是否禁用点击高亮线
                set1.setHighLightColor(Color.parseColor("#D15FEE"));//设置点击交点后显示交高亮线的颜色
                set1.setAxisDependency(YAxis.AxisDependency.LEFT);
                set1.setShadowColor(Color.DKGRAY);//影线颜色
                set1.setShadowColorSameAsCandle(true);//影线颜色与实体一致
                set1.setShadowWidth(0.7f);//影线
                set1.setDecreasingColor(Color.RED);
                set1.setDecreasingPaintStyle(Paint.Style.FILL);//红涨，实体
                set1.setIncreasingColor(Color.GREEN);
                set1.setIncreasingPaintStyle(Paint.Style.STROKE);//绿跌，空心
                set1.setNeutralColor(Color.RED);//当天价格不涨不跌（一字线）颜色
                set1.setHighlightLineWidth(1f);//选中蜡烛时的线宽
                set1.setDrawValues(false);//在图表中的元素上面是否显示数值


                ArrayList<ICandleDataSet> dataSets = new ArrayList<>();
                dataSets.add(set1); // add the datasets
                XAxis xAxis = candleStickChart.getXAxis();
                Matrix matrix = new Matrix();
                matrix.postScale(3.0f, 1f);

                YAxis rightAxis = candleStickChart.getAxisRight();
                //设置图表右边的y轴禁用
                rightAxis.setEnabled(false);
                //创建LineData对象 属于LineChart折线图的数据集合
                CandleData data = new CandleData(dataSets);// 添加到图表中
                candleStickChart.setData(data);//绘制图表
                candleStickChart.invalidate();
            }
        }



    }
    //k线图数据
    private class CandleStickChartInfo{
        private String code;
        private String total;
        private String begin;
        private String end;
        private String[][] kline;

        public void setTotal(String total) {
            this.total = total;
        }

        public void setEnd(String end) {
            this.end = end;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public void setBegin(String begin) {
            this.begin = begin;
        }

        public void setKline(String[][] kline) {
            this.kline = kline;
        }

        public String getTotal() {
            return total;
        }

        public String getEnd() {
            return end;
        }

        public String getCode() {
            return code;
        }

        public String getBegin() {
            return begin;
        }

        public String[][] getKline() {
            return kline;
        }
    }



    //初始化50etf信息
    private void get50ETFinfo(){
        NetUtil.INSTANCE.sendGetRequest("http://hq.sinajs.cn/list=s_sh510050,sh510050", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.obtainMessage(INFO_FAILURE).sendToTarget();
                dialog.setTitle("网络连接错误");
                dialog.setMessage("请稍后再试");
                dialogShow();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                mHandler.obtainMessage(INFO_SUCCESS,response.body().string()).sendToTarget();
            }
        });










    }
    private void draw50ETF(String information){
        if(information!=null) {
            int comma1 = information.indexOf(",");
            int comma2 = information.indexOf(",", comma1 + 1);
            int comma3 = information.indexOf(",", comma2 + 1);
            int comma4 = information.indexOf(",", comma3 + 1);
            int comma5 = information.indexOf(",", comma4 + 1);
            int next = information.indexOf("var hq_str_sh510050");
            int comma1_2 = information.indexOf(",", next);
            int comma2_2 = information.indexOf(",", comma1_2 + 1);
            int comma3_2 = information.indexOf(",", comma2_2 + 1);
            int comma4_2 = information.indexOf(",", comma3_2 + 1);
            int comma5_2 = information.indexOf(",", comma4_2 + 1);
            int comma6_2 = information.indexOf(",", comma5_2 + 1);
            int last1comma = information.lastIndexOf(",");
            int last2comma = information.lastIndexOf(",", last1comma - 1);


            present_price.setText(information.substring(comma1 + 1, comma2));
            ups_and_downs.setText(information.substring(comma2 + 1, comma3));
            if (information.substring(comma2 + 1, comma3).startsWith("-")) {
                ups_and_downs.setTextColor(Color.parseColor("#FF0000"));
            }
            quantity.setText(information.substring(comma4 + 1, comma5) + "手");
            volume.setText(information.substring(comma5 + 1, information.indexOf("\";")) + "万元");
            today_start.setText(information.substring(comma1_2 + 1, comma2_2));
            yesterday_end.setText(information.substring(comma2_2 + 1, comma3_2));
            highest_price.setText(information.substring(comma4_2 + 1, comma5_2));
            lowest_price.setText(information.substring(comma5_2 + 1, comma6_2));
            update_time.setText(information.substring(last2comma + 1, last1comma));
            int h=Integer.parseInt(information.substring(last2comma + 1, information.indexOf(":")));
            int m=Integer.parseInt(information.substring(information.indexOf(":")+1,information.lastIndexOf(":")));
            if (     ((h*60+m)>=570&&(h*60+m)<690)||
                    ((h*60>=780)&&(h*60<900))   ) {
                status.setText("开盘");
            } else {
                status.setText("闭市");
            }
        }
    }



//dialog信息
    private void initDialog(){
        dialog=new AlertDialog.Builder(getActivity());
        dialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
    }
    private void dialogShow(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.show();
            }
        });
    }

}





