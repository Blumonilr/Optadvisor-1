package utf8.optadvisor.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import utf8.optadvisor.R;


public class MyCombination extends Fragment {

    @Override
    /**
     * 对我的组合的管理
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_combination, container, false);
    }
}

