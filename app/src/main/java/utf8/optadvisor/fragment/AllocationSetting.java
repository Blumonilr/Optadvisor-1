package utf8.optadvisor.fragment;


import android.support.v4.app.Fragment;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import utf8.optadvisor.R;
import utf8.optadvisor.domain.AllocationResponse;
import utf8.optadvisor.domain.HedgingResponse;
import utf8.optadvisor.domain.response.ResponseMsg;
import utf8.optadvisor.util.AllocationInfoPage;
import utf8.optadvisor.util.AllocationSettingPage;
import utf8.optadvisor.util.NetUtil;

public class AllocationSetting extends Fragment {

    Toolbar toolbar;
    LinearLayout ll;
    AllocationSettingPage setting;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_allocation, container, false);

        toolbar=(Toolbar)view.findViewById(R.id.allocation_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ll=(LinearLayout)view.findViewById(R.id.allocation_ll);
        setting=new AllocationSettingPage(getContext());
        ll.addView(setting);

        final Context context=getContext();

        Button bt=(Button)view.findViewById(R.id.allocation_setting_next);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,String> values=new HashMap<>();
                values.put("m0",setting.getM0());
                values.put("k",setting.getK());
                values.put("t",setting.getDate());
                values.put("combination",setting.getCombination()+"");
                values.put("p1",setting.getP1());
                values.put("p2",setting.getP2());
                values.put("sigma1",setting.getSigma1());
                values.put("sigma2",setting.getSigma2());

                NetUtil.INSTANCE.sendPostRequest(NetUtil.SERVER_BASE_ADDRESS + "/recommend/recommendPortfolio", values, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Toast.makeText(context,"网络连接错误",Toast.LENGTH_SHORT);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        ResponseMsg responseMsg = NetUtil.INSTANCE.parseJSONWithGSON(response);
                        AllocationResponse responseAllocation=new Gson().fromJson(responseMsg.getData().toString(),AllocationResponse.class);
                        ll.removeAllViews();
                        ll.addView(new AllocationInfoPage(context,responseAllocation));
                    }
                });


            }
        });

        Button back=(Button)view.findViewById(R.id.allocation_info_bt_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll.removeAllViews();
                setting=new AllocationSettingPage(context);
                ll.addView(setting);
            }
        });



        return view;
    }
}
