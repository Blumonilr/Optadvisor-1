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

import utf8.optadvisor.R;
import utf8.optadvisor.util.AllocationInfoPage;
import utf8.optadvisor.util.AllocationSettingPage;

public class AllocationSetting extends Fragment {

    Toolbar toolbar;
    LinearLayout ll;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_allocation, container, false);

        toolbar=(Toolbar)view.findViewById(R.id.allocation_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ll=(LinearLayout)view.findViewById(R.id.allocation_ll);
        ll.addView(new AllocationSettingPage(getContext()));


        final Context context=getContext();

        Button bt=(Button)view.findViewById(R.id.allocation_setting_next);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll.removeAllViews();
                ll.addView(new AllocationInfoPage(context));
            }
        });

        return view;
    }
}
