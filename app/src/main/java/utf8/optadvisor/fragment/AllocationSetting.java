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
        setting=new AllocationSettingPage(getContext(),this);
        ll.addView(setting);

        return view;
    }

    public LinearLayout getLL() {
        return ll;
    }

    public void setView(AllocationResponse responseAllocation) {
        this.ll.removeAllViews();
        this.ll.addView(new AllocationInfoPage(this.getContext(), responseAllocation, this));
    }
}
