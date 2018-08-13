package utf8.optadvisor.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

import utf8.optadvisor.R;


public class OptionShow extends Fragment {


    @Override
    /**
     * 展示行情信息
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_option_show, container, false);
        

        return view;
    }


}




