package utf8.optadvisor.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import utf8.optadvisor.domain.response.ResponseMsg;

public enum NetUtil {
    INSTANCE;
    private Gson gson=new Gson();
    public final static String SERVER_BASE_ADDRESS="http://192.168.1.105:8088";
    public static SharedPreferences sharedPreference;
    /**
     * GET请求
     */
    public void sendGetRequest(String address, okhttp3.Callback callback){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
    /**
     * GET请求带拦截器
     */
    public void sendGetRequest(String address, Context context,okhttp3.Callback callback){
        OkHttpClient client=new OkHttpClient.Builder().addInterceptor(new MyLogInterceptor(context)).build();
        Request request=new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }


    /**
     * POST请求
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
     * Post请求(forgetPassword专用)
     */
    public void sendPostRequest(String address, Map<String,String> value,String cookie, okhttp3.Callback callback){
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
        Request request=builder
                .url(address)
                .header("cookie",cookie)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }
    /**
     * POST请求带拦截器
     */
    public void sendPostRequest(String address, Map<String,String> value,Context context, okhttp3.Callback callback){
        //设置数据格式为json
        MediaType mediaType= MediaType.parse("application/json;charset=utf-8");
        //构建json字符串
        StringBuilder stringBuilder=new StringBuilder("{");
        for(Map.Entry<String, String> each:value.entrySet()){
            stringBuilder.append("\"").append(each.getKey()).append("\":\"").append(each.getValue()).append("\",");
        }
        stringBuilder.replace(stringBuilder.length()-1,stringBuilder.length(),"}");
        //发送
        OkHttpClient client=new OkHttpClient.Builder().addInterceptor(new MyLogInterceptor(context)).build();
        Request.Builder builder=new Request.Builder();
        RequestBody requestBody=RequestBody.create(mediaType,stringBuilder.toString());
        Request request=builder.url(address).post(requestBody).build();
        client.newCall(request).enqueue(callback);
    }

    /**
     * PUT请求带拦截器
     */
    public void sendPutRequest(String address, Map<String,String> value,Context context, okhttp3.Callback callback){
        //设置数据格式为json
        MediaType mediaType= MediaType.parse("application/json;charset=utf-8");
        //构建json字符串
        StringBuilder stringBuilder=new StringBuilder("{");
        for(Map.Entry<String, String> each:value.entrySet()){
            stringBuilder.append("\"").append(each.getKey()).append("\":\"").append(each.getValue()).append("\",");
        }
        stringBuilder.replace(stringBuilder.length()-1,stringBuilder.length(),"}");
        //发送
        OkHttpClient client=new OkHttpClient.Builder().addInterceptor(new MyLogInterceptor(context)).build();
        Request.Builder builder=new Request.Builder();
        RequestBody requestBody=RequestBody.create(mediaType,stringBuilder.toString());
        Request request=builder.url(address).put(requestBody).build();
        client.newCall(request).enqueue(callback);
    }

    /**
     *POST请求带拦截器，无传参
     */
    public void sendPostRequest(String address,Context context, okhttp3.Callback callback){
        //设置数据格式为json
        MediaType mediaType= MediaType.parse("application/json;charset=utf-8");

        //发送
        OkHttpClient client=new OkHttpClient.Builder().addInterceptor(new MyLogInterceptor(context)).build();
        Request.Builder builder=new Request.Builder();
        RequestBody requestBody=RequestBody.create(mediaType,"");
        Request request=builder.url(address).post(requestBody).build();
        client.newCall(request).enqueue(callback);
    }


    /**
     * POST请求，手动构建参数
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
     * POST请求带拦截器，手动构建参数
     */
    public void sendPostRequest(String address, String value, Context context,okhttp3.Callback callback){
        MediaType mediaType= MediaType.parse("application/json;charset=utf-8");

        OkHttpClient client=new OkHttpClient.Builder().addInterceptor(new MyLogInterceptor(context)).build();
        Request.Builder builder=new Request.Builder();
        RequestBody requestBody=RequestBody.create(mediaType,value);
        Request request=builder.url(address).post(requestBody).build();
        client.newCall(request).enqueue(callback);
    }


    /**
     *DELETE请求带拦截器，无传参
     */
    public void sendDeleteRequest(String address,Context context, okhttp3.Callback callback){
        //设置数据格式为json
        MediaType mediaType= MediaType.parse("application/json;charset=utf-8");

        //发送
        OkHttpClient client=new OkHttpClient.Builder().addInterceptor(new MyLogInterceptor(context)).build();
        Request.Builder builder=new Request.Builder();
        RequestBody requestBody=RequestBody.create(mediaType,"");
        Request request=builder.url(address).delete(requestBody).build();
        client.newCall(request).enqueue(callback);
    }

    /**
     *PATCH请求带拦截器，无传参
     */
    public void sendPatchRequest(String address,Context context, okhttp3.Callback callback){
        //设置数据格式为json
        MediaType mediaType= MediaType.parse("application/json;charset=utf-8");

        //发送
        OkHttpClient client=new OkHttpClient.Builder().addInterceptor(new MyLogInterceptor(context)).build();
        Request.Builder builder=new Request.Builder();
        RequestBody requestBody=RequestBody.create(mediaType,"");
        Request request=builder.url(address).patch(requestBody).build();
        client.newCall(request).enqueue(callback);
    }

    /**
     * Response解析为对象
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
