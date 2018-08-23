package utf8.optadvisor.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import utf8.optadvisor.R;
import utf8.optadvisor.domain.Message;
import utf8.optadvisor.domain.response.ResponseMsg;
import utf8.optadvisor.util.NetUtil;

public class MessageDetailActivity extends AppCompatActivity {

    private AlertDialog.Builder dialog;

    private Message message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
        Toolbar toolbar=findViewById(R.id.toolBar);
        CollapsingToolbarLayout collapsingToolbar=findViewById(R.id.collapsing_toolbar);
        TextView textView=findViewById(R.id.content_text);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        dialog=new AlertDialog.Builder(MessageDetailActivity.this);

        message= (Message) getIntent().getSerializableExtra("message");
        collapsingToolbar.setTitle(message.getTitle());
        textView.setText(message.getMessage());
        //设为已读
        if(!message.getReadStatus()) setReaded();

        FloatingActionButton deleteButton=findViewById(R.id.delete_message_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,String> value=new HashMap<String,String>();
                value.put("id", String.valueOf(message.getId()));
                NetUtil.INSTANCE.sendPostRequest(NetUtil.SERVER_BASE_ADDRESS + "/message/deleteMessage", value,MessageDetailActivity.this, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        dialog.setTitle("网络连接错误");
                        dialog.setMessage("请稍后再试");
                        dialogShow();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        ResponseMsg responseMsg=NetUtil.INSTANCE.parseJSONWithGSON(response);
                        exit();
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        exit();
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

    /**
     * 设为已读
     */
    private void setReaded(){
        Map<String,String> value=new HashMap<>();
        value.put("id", String.valueOf(message.getId()));
        NetUtil.INSTANCE.sendPostRequest(NetUtil.SERVER_BASE_ADDRESS + "/message/setMessageRead", value, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
            }
        });
    }

    /**
     * 滑动退出
     */
    private void exit(){
        finish();
        overridePendingTransition(R.anim.activity_left_enter,R.anim.activity_right_exit);
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

}
