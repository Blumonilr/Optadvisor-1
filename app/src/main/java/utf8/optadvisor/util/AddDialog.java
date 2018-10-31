package utf8.optadvisor.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import utf8.optadvisor.R;
import utf8.optadvisor.domain.AllocationResponse;
import utf8.optadvisor.domain.HedgingResponse;
import utf8.optadvisor.domain.entity.Option;
import utf8.optadvisor.fragment.HedgingInfoDisplay;
import utf8.optadvisor.fragment.HedgingInfoSetting;

public class AddDialog extends Dialog {

    private EditText name;
    private Button confirm;
    private Button cancel;
    HedgingResponse hedgingResponse;
    HedgingInfoSetting hedgingInfoSetting;
    private AlertDialog.Builder sdialog;

    public AddDialog(Context context, HedgingResponse hedgingResponse, HedgingInfoSetting hedgingInfoSetting) {
        super(context);
        this.hedgingResponse=hedgingResponse;
        this.hedgingInfoSetting=hedgingInfoSetting;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    private void init() {

        LayoutInflater inflater = LayoutInflater.from(this.getContext());
        View view = inflater.inflate(R.layout.dialog_confirm, null);
        setContentView(view);

        name=(EditText)view.findViewById(R.id.dialog_confirm_name);
        confirm=(Button)view.findViewById(R.id.dialog_confirm_confirm);
        cancel=(Button)view.findViewById(R.id.dialog_confirm_cancel);

        initDialog();


        final AddDialog dialog=this;

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString() != null) {
                    Map<String, String> values = new HashMap<>();
                    List<Option> list=new ArrayList<>(); list.add(hedgingResponse.getOption());
                    values.put("options", new Gson().toJsonTree(list, new TypeToken<ArrayList<Option>>() {}.getType()).toString().replaceAll(" ",""));
                    values.put("name","\"" + name.getText().toString()+"\"");
                    values.put("type", "\"" + "1"+"\"");
                    values.put("trackingStatus", "\"" + "false"+"\"");
                    values.put("graph",new Gson().toJson(hedgingResponse.getGraph(), new TypeToken<String[][]>() {}.getType()).replaceAll(" ",""));
                    values.put("iK","\""+hedgingResponse.getIk()+"\"");
                    values.put("sExp","\""+hedgingInfoSetting.getsExp()+"\"");
                    values.put("pAsset","\""+hedgingResponse.getpAsset()+"\"");
                    values.put("n0","\""+hedgingInfoSetting.getN0()+"\"");
                    values.put("a","\""+hedgingInfoSetting.getA()+"\"");


                    NetUtil.INSTANCE.sendPostRequestForOptions(NetUtil.SERVER_BASE_ADDRESS + "/portfolio", values,getContext(), new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            sdialog.setTitle("网络连接错误");
                            sdialog.setMessage("请稍后再试");
                            dialogShow();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            sdialog.setMessage("添加成功");
                            dialogShow();
                            dialog.dismiss();
                        }
                    });
                }
                else{
                    sdialog.setTitle("信息填写不完整");
                    sdialog.setMessage("请填写完整信息");
                    dialogShow();
                }
            }
        });

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = this.getContext().getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
        dialogWindow.setAttributes(lp);
    }


    private void initDialog(){
        sdialog=new AlertDialog.Builder(hedgingInfoSetting.getActivity());
        sdialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
    }
    private void dialogShow(){
        hedgingInfoSetting.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sdialog.show();
            }
        });
    }

}
