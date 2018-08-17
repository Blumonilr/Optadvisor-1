package utf8.optadvisor.activity;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lecho.lib.hellocharts.model.Line;
import utf8.optadvisor.R;
import utf8.optadvisor.util.AllocationSettingPage;

public class AllocationActivity extends AppCompatActivity {

    Toolbar toolbar;
    LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allocation);

        toolbar=(Toolbar)findViewById(R.id.allocation_toolbar);
        setSupportActionBar(toolbar);
        ll=(LinearLayout)findViewById(R.id.allocation_ll);
        ll.addView(new AllocationSettingPage(this));



    }
}
