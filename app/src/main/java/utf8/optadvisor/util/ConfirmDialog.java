package utf8.optadvisor.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import utf8.optadvisor.R;
import utf8.optadvisor.domain.AllocationResponse;
import utf8.optadvisor.domain.entity.Option;

public class ConfirmDialog extends Dialog {

    private EditText name;
    private Spinner type;
    private Spinner back;
    private Button confirm;
    private Button cancel;
    AllocationResponse allocationResponse;
    AllocationInfoPage allocationInfoPage;


    public ConfirmDialog(Context context, AllocationResponse allocationResponse, AllocationInfoPage allocationInfoPage) {
        super(context);
        this.allocationResponse=allocationResponse;
        this.allocationInfoPage=allocationInfoPage;
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
        type=(Spinner)view.findViewById(R.id.dialog_confirm_type);
        back=(Spinner)view.findViewById(R.id.dialog_confirm_back);
        confirm=(Button)view.findViewById(R.id.dialog_confirm_confirm);
        cancel=(Button)view.findViewById(R.id.dialog_confirm_cancel);

        ArrayList<String> arr=new ArrayList<>();
        arr.add("资产配置");
        type.setAdapter(new ArrayAdapter<String>(this.getContext(),android.R.layout.simple_dropdown_item_1line,arr));

        ArrayList<String > array=new ArrayList<>();
        array.add("是"); array.add("否");
        back.setAdapter(new ArrayAdapter<String>(this.getContext(),android.R.layout.simple_dropdown_item_1line,array));

        final ConfirmDialog dialog=this;

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString() != null && type.getSelectedItem() != null && back.getSelectedItem() != null) {
                    Map<String, String> values = new HashMap<>();
                    values.put("options", new Gson().toJson(allocationResponse.getOptions(), new TypeToken<ArrayList<Option>>() {
                    }.getType()));
                    values.put("type", "0");
                    values.put("trackingStatus", back.getSelectedItem().toString().equals("是") ? "true" : "false");
                    values.put("m0", allocationResponse.getM0() + "");
                    values.put("k", allocationResponse.getK() + "");
                    values.put("p1", "" + allocationResponse.getP1());
                    values.put("p2", "" + allocationResponse.getP2());
                    values.put("sigma1", "" + allocationResponse.getSigma1());
                    values.put("sigma2", "" + allocationResponse.getSigma2());
                    values.put("cost", "" + allocationResponse.getCost());
                    values.put("bond", "" + allocationResponse.getBond());
                    values.put("z_delta", "" + allocationResponse.getZ_delta());
                    values.put("z_gamma", "" + allocationResponse.getZ_gamma());
                    values.put("z_vega", "" + allocationResponse.getZ_vega());
                    values.put("z_theta", "" + allocationResponse.getZ_theta());
                    values.put("z_rho", "" + allocationResponse.getZ_rho());
                    values.put("em", "" + allocationResponse.getEm());
                    values.put("beta", "" + allocationResponse.getBeta());

                    NetUtil.INSTANCE.sendPostRequest(NetUtil.SERVER_BASE_ADDRESS + "/portfolio", values, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Toast.makeText(allocationInfoPage.getContext(), "添加失败", Toast.LENGTH_SHORT);
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            Toast.makeText(allocationInfoPage.getContext(), "添加成功", Toast.LENGTH_SHORT);
                            dialog.dismiss();
                        }
                    });
                }
                else{
                    Toast.makeText(allocationInfoPage.getContext(), "请填写完整信息", Toast.LENGTH_SHORT);
                }
            }
        });

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = this.getContext().getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
        dialogWindow.setAttributes(lp);
    }


}
