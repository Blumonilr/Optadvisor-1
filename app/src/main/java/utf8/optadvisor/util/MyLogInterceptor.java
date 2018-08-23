package utf8.optadvisor.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import utf8.optadvisor.activity.LoginActivity;
import utf8.optadvisor.activity.MainActivity;
import utf8.optadvisor.domain.response.ResponseMsg;

public class MyLogInterceptor implements Interceptor {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    public String cookie;
    private Context context;

    public MyLogInterceptor(Context context){
        this.context=context;
    }
    @Override
    public Response intercept(Chain chain) throws IOException {
        initSp(context);
        Gson gson=new Gson();
        Request request = chain.request();
        Response response = chain.proceed(request);
        String data=response.body().string();
        ResponseMsg msg=gson.fromJson(data,ResponseMsg.class);


        if(msg.getCode()==1008){
            Map<String,String> value=new HashMap<String,String>();
            value.put("username",sharedPreferences.getString("username",null));
            value.put("password",sharedPreferences.getString("password",null));
            NetUtil.INSTANCE.sendPostRequest(NetUtil.SERVER_BASE_ADDRESS+"/login", value, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Headers headers = response.headers();
                    Log.d("来自Net拦截器的消息:info_headers", "header " + headers);
                    List<String> cookies = headers.values("Set-Cookie");
                    String session = cookies.get(0);
                    Log.d("来自Net拦截器的消息:info_cookies", "onResponse-size: " + cookies);
                    cookie = session.substring(0, session.indexOf(";"));
                    Log.i("来自Net拦截器的消息:info_s", "session is  :" + cookie);
                    editor.putString("cookie",cookie);
                    editor.apply();
                }
            });
            Request requestAgain = chain.request().newBuilder()
                    .header("cookie",sharedPreferences.getString("cookie","456789"))
                    .build();
            Response responseAgain = chain.proceed(requestAgain);
            String content= responseAgain.body().string();
            MediaType mediaType = responseAgain.body().contentType();
            return response.newBuilder()
                    .body(ResponseBody.create(mediaType,content))
                    .build();
        }
        else{
            MediaType mediaType = response.body().contentType();
            return response.newBuilder()
                    .body(ResponseBody.create(mediaType,data))
                    .build();
        }
    }
    private void initSp(Context context){
        sharedPreferences=context.getSharedPreferences("userInfo",Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
    }
}
