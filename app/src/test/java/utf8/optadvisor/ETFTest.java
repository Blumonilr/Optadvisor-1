package utf8.optadvisor;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import utf8.optadvisor.fragment.OptionShow;
import utf8.optadvisor.util.NetUtil;

public class ETFTest {

    private static final int INFO_SUCCESS = 0;//获取50etf成功的标识
    private static final int INFO_FAILURE = 1;//获取50etf失败的标识

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage (Message msg) {//此方法在ui线程运行
            switch(msg.what) {
                case INFO_SUCCESS:
                    String info=(String) msg.obj;
                    System.out.println(info);
                    break;
                case INFO_FAILURE:
                    System.out.println("1fail");
                    break;
            }
        }
    };

    @Test
    public void test() throws IOException {

        OkHttpClient client=new OkHttpClient();
        String url="http://www.optbbs.com/d/csv/d/data.csv?v="+ Calendar.getInstance().getTimeInMillis();
        Request request2 = new Request.Builder()
                .url(url)
                .build();
        String response2=client.newCall(request2).execute().body().string();
        String[] sigams=response2.split("\n");
        String temp="";
        for (String s:sigams){
            if (s.length()>9)
                temp=s;
            else
                break;
        }
        System.out.println(temp.substring(temp.indexOf(",")+1,temp.indexOf(" ")));
    }

    @Test
    public void test2(){
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Bundle data = msg.getData();
                String userInfo = data.getString("userInfo");

            }
        };
                OkHttpClient client = new OkHttpClient();
                String url = "http://hq.sinajs.cn/list=s_sh510050";
                Request request2 = new Request.Builder()
                        .url(url)
                        .build();
                String response2 = null;
                try {
                    response2 = client.newCall(request2).execute().body().string();
                    System.out.println(Double.parseDouble(response2.substring(response2.indexOf(",") + 1, response2.indexOf(",") + 6)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
    }
}
