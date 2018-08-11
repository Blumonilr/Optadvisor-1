package utf8.optadvisor.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import utf8.optadvisor.R;


public class OptionShow extends Fragment {
    private ShowCallOption f1;
    private ShowPutOption f2;

    @Override
    /**
     * 展示行情信息
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_option_show, container, false);
        initCallOption();
        Button callOption=(Button) view.findViewById(R.id.callOption);
        Button putOption=(Button) view.findViewById(R.id.putOption);
        callOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initCallOption();

            }
        });
        putOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPutOption();

            }
        });
        return view;
    }
    private void initCallOption(){
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        if(f1==null){
            f1=new ShowCallOption();
            transaction.add(R.id.show, f1);
        }
        hideFragment(transaction);
        transaction.show(f1);
        transaction.commit();
    }
    private void initPutOption(){
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        if(f2==null){
            f2=new ShowPutOption();
            transaction.add(R.id.show, f2);
        }
        hideFragment(transaction);
        transaction.show(f2);
        transaction.commit();
    }
    private void hideFragment(FragmentTransaction transaction){
        if(f1 != null){
            transaction.hide(f1);
        }
        if(f2 != null){
            transaction.hide(f2);
        }
    }
}

