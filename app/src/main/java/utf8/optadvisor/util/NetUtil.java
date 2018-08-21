package utf8.optadvisor.util;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import utf8.optadvisor.domain.response.ResponseMsg;

public enum NetUtil {
    INSTANCE;
    private Gson gson=new Gson();
    public final static String SERVER_BASE_ADDRESS="http://192.168.1.108:8088";
    /**
     * 不带参GET请求
     */
    public void sendGetRequest(String address, okhttp3.Callback callback){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }

    /**
     * 带参POST请求
     */
    public void sendPostRequest(String address, Map<String,String> value, okhttp3.Callback callback){
        //设置数据格式为json
        MediaType mediaType= MediaType.parse("application/json;charset=utf-8");
        //构建json字符串
        StringBuilder stringBuilder=new StringBuilder("{");
        for(Map.Entry<String, String> each:value.entrySet()){
            stringBuilder.append("\"").append(each.getKey()).append("\":\"").append(each.getValue()).append("\",");
        }
        stringBuilder.replace(stringBuilder.length()-1,stringBuilder.length(),"}");
        //发送
        OkHttpClient client=new OkHttpClient();
        Request.Builder builder=new Request.Builder();
        RequestBody requestBody=RequestBody.create(mediaType,stringBuilder.toString());
        Request request=builder.url(address).post(requestBody).build();
        client.newCall(request).enqueue(callback);
    }

    /**
     * 带参POST请求(json字符串手动构建)
     */
    public void sendPostRequest(String address, String value, okhttp3.Callback callback){
        MediaType mediaType= MediaType.parse("application/json;charset=utf-8");

        OkHttpClient client=new OkHttpClient();
        Request.Builder builder=new Request.Builder();
        RequestBody requestBody=RequestBody.create(mediaType,value);
        Request request=builder.url(address).post(requestBody).build();
        client.newCall(request).enqueue(callback);
    }

    /**
     * response解析为对象
     **/
    public ResponseMsg parseJSONWithGSON(Response response){
        String responseData= null;
        try {
            responseData = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return gson.fromJson(responseData,ResponseMsg.class);
    }

}
