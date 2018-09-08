package utf8.optadvisor.activity;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import utf8.optadvisor.R;
import utf8.optadvisor.domain.entity.Message;
import utf8.optadvisor.domain.MessageList;
import utf8.optadvisor.domain.response.ResponseMsg;
import utf8.optadvisor.util.MessageAdapter;
import utf8.optadvisor.util.NetUtil;

public class MessageActivity extends AppCompatActivity {

    private List<Message> messageList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MessageAdapter messageAdapter;

    private AlertDialog.Builder dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        messageList=new ArrayList<>();
        initDialog();
        initToolbar();
        initRefreshLayout();
        initFloatButton();
        initRecyclerView();

        //初始化消息列表
        swipeRefreshLayout.setRefreshing(true);
        Log.d("On create size", String.valueOf(messageList.size()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshMessage();
        Log.d("On resume size", String.valueOf(messageList.size()));
    }

    /**
     * 设置消息列表
     */
    private void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);
    }

    /**
     * 设置弹窗
     */
    private void initDialog(){
        dialog=new AlertDialog.Builder(MessageActivity.this);
        dialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
    }

    /**
     * 更新消息列表
     */
    private void refreshMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                NetUtil.INSTANCE.sendPostRequest(NetUtil.SERVER_BASE_ADDRESS + "/message/getMessage",MessageActivity.this, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("MessageAct on fail","not  wrong yet");
                        dialog.setTitle("网络连接错误");
                        dialog.setMessage("请稍后再试");
                        dialogShow();
                        notifyRefreshFail();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        ResponseMsg responseMsg=NetUtil.INSTANCE.parseJSONWithGSON(response);
                        System.out.println(responseMsg.getData());
                        messageList.clear();
                        Log.d("On refresh size", String.valueOf(messageList.size()));
                        if(responseMsg.getData()!=null) {
                            MessageList messages = new Gson().fromJson(responseMsg.getData().toString(), MessageList.class);
                            List<Message> read = messages.getRead();
                            List<Message> unread = messages.getUnread();
                            messageList.addAll(unread);
                            messageList.addAll(read);
                        }
                        notifyRefreshSuccess();
                    }
                });

            }
        }).start();
    }

    private void notifyRefreshSuccess(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void notifyRefreshFail(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    /**
     * 设置下拉刷新
     */
    private void initRefreshLayout(){
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorButton,R.color.colorPrimaryDark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshMessage();
            }
        });
    }

    /**
     * 设置标题栏
     */
    private void initToolbar(){
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * 设置浮动按钮
     */
    private void initFloatButton(){
        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //do something
            }
        });
    }


    /**
     * 弹出弹窗
     */
    private void dialogShow(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.show();
            }
        });
    }

    /**
     * 重写Home键实现滑动切换界面
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                exit();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        exit();
    }
    /**
     * 滑动退出
     */
    private void exit(){
        finish();
        overridePendingTransition(R.anim.activity_left_enter,R.anim.activity_right_exit);
    }
}