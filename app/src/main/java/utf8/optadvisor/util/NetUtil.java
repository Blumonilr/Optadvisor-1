package utf8.optadvisor.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
    public final static String SERVER_BASE_ADDRESS="http://172.28.165.118:8088";
    public static SharedPreferences sharedPreference;

    public final static int CONNECT_TIMEOUT =60;
    public final static int READ_TIMEOUT=60;
    public final static int WRITE_TIMEOUT=60;
    /**
     * GET请求
     */
    public void sendGetRequest(String address, okhttp3.Callback callback){
        OkHttpClient client=getNormalClient();
        Request request=new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
    /**
     * GET请求带拦截器
     */
    public void sendGetRequest(String address, Context context,okhttp3.Callback callback){
        OkHttpClient client=getInterceptorClient(context);
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
        OkHttpClient client=getNormalClient();
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
        OkHttpClient client=getNormalClient();
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
        System.out.println(stringBuilder.toString());
        //发送
        OkHttpClient client=getInterceptorClient(context);
        Request.Builder builder=new Request.Builder();
        RequestBody requestBody=RequestBody.create(mediaType,stringBuilder.toString());
        Request request=builder.url(address).post(requestBody).build();
        client.newCall(request).enqueue(callback);
    }

    /**
     * POST请求带拦截器(Options专用)
     */
    public void sendPostRequestForOptions(String address, Map<String,String> value,Context context, okhttp3.Callback callback){
        //设置数据格式为json
        MediaType mediaType= MediaType.parse("application/json;charset=utf-8");
        //构建json字符串
        StringBuilder stringBuilder=new StringBuilder("{");
        for(Map.Entry<String, String> each:value.entrySet()){
            stringBuilder.append("\"").append(each.getKey()).append("\":").append(each.getValue()).append(",");
        }
        stringBuilder.replace(stringBuilder.length()-1,stringBuilder.length(),"}");
        System.out.println(stringBuilder.toString());
        //发送
        OkHttpClient client=getInterceptorClient(context);
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
        OkHttpClient client=getInterceptorClient(context);
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
        OkHttpClient client=getInterceptorClient(context);
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

        OkHttpClient client=getNormalClient();
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

        OkHttpClient client=getInterceptorClient(context);
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
        OkHttpClient client=getInterceptorClient(context);
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
        OkHttpClient client=getInterceptorClient(context);
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

    /**
     * 获取普通的（超时加长）okHttpClient
     */
    private OkHttpClient getNormalClient(){
        return new OkHttpClient.Builder()
                .readTimeout(READ_TIMEOUT,TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(WRITE_TIMEOUT,TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(CONNECT_TIMEOUT,TimeUnit.SECONDS)//设置连接超时时间
                .build();
    }

    /**
     * 获取带拦截器的（超时加长）okHttpClient
     */
    private OkHttpClient getInterceptorClient(Context context){
        return new OkHttpClient.Builder()
                .readTimeout(READ_TIMEOUT,TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(WRITE_TIMEOUT,TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)//设置连接超时时间
                .addInterceptor(new MyLogInterceptor(context)).build();
    }


}

