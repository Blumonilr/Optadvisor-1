package utf8.optadvisor.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import utf8.optadvisor.R;


public class BuildPortfolio extends Fragment {

    Button portfolio;
    Button hedging;
    Button DIY;
    LinearLayout ll;
    AllocationSetting allocationSetting;
    HedgingInfoDisplay hedgingInfoDisplay;
    HedgingInfoSetting hedgingInfoSetting;
    Button hedge_next;
    @Override
    /**
     * 构建组合页面
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_build_portfolio, container, false);

        portfolio= view.findViewById(R.id.portfolio);
        hedging=view.findViewById(R.id.hedge);
        DIY=view.findViewById(R.id.DIY);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if(allocationSetting==null){
            allocationSetting=new AllocationSetting();
            transaction.add(R.id.build_portfolio_ll,allocationSetting);
            transaction.commit();
        }
        transaction.show(allocationSetting);


        portfolio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                if(allocationSetting==null){
                    allocationSetting=new AllocationSetting();
                    transaction.add(R.id.build_portfolio_ll,allocationSetting);
                }
                transaction.hide(hedgingInfoSetting);
                transaction.show(allocationSetting);
                transaction.commit();
            }
        });

        hedging.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                if(hedgingInfoSetting==null){
                    hedgingInfoSetting=new HedgingInfoSetting();
                    transaction.add(R.id.build_portfolio_ll,hedgingInfoSetting);
                }
                transaction.hide(allocationSetting);
                transaction.show(hedgingInfoSetting);
                transaction.commit();
            }
        });


        return view;
    }

}
