package utf8.optadvisor.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import utf8.optadvisor.BuildPortfolio;
import utf8.optadvisor.LoginActivity;
import utf8.optadvisor.MyCombination;
import utf8.optadvisor.OptionShow;
import utf8.optadvisor.R;
import utf8.optadvisor.UserCenter;

public class MainActivity extends AppCompatActivity {
    /**
     * 主界面
     */
    private SharedPreferences preferences;
    private SharedPreferences.Editor SPeditor;
    private OptionShow f1;
    private BuildPortfolio f2;
    private MyCombination f3;
    private UserCenter f4;



    @Override
    /**
     * 4个fragment切换
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        preferences=getSharedPreferences("userInfo",MODE_PRIVATE);
        SPeditor=preferences.edit();
        initF1();
        Button button1 = (Button) findViewById(R.id.bt1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              initF1();
            }
        });






        Button button2 = (Button) findViewById(R.id.bt2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(preferences.getBoolean("isChecked",false)) {
                   initF2();
                }else{
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });



        Button button3 = (Button) findViewById(R.id.bt3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(preferences.getBoolean("isChecked",true)) {
                  initF3();
                }else{
                    Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
            }
        });



        Button button4 = (Button) findViewById(R.id.bt4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(preferences.getBoolean("isChecked",true)) {
                    initF4();

                }else{
                    Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * 碎片初始化
     */
    private void initF1(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(f1==null){
            f1=new OptionShow();
            transaction.add(R.id.main_frame_layout, f1);
        }
        hideFragment(transaction);
        transaction.show(f1);
        transaction.commit();
    }
    private void initF2(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(f2==null){
            f2=new BuildPortfolio();
            transaction.add(R.id.main_frame_layout, f2);
        }
        hideFragment(transaction);
        transaction.show(f2);
        transaction.commit();
    }
    private void initF3(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(f3==null){
            f3=new MyCombination();
            transaction.add(R.id.main_frame_layout, f3);
        }
        hideFragment(transaction);
        transaction.show(f3);
        transaction.commit();
    }
    private void initF4(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(f4==null){
            f4=new UserCenter();
            transaction.add(R.id.main_frame_layout, f4);
        }
        hideFragment(transaction);
        transaction.show(f4);
        transaction.commit();
    }

    /**
     * 隐藏碎片
     */
     private void hideFragment(FragmentTransaction transaction){
        if(f1 != null){
            transaction.hide(f1);
        }
        if(f2 != null){
            transaction.hide(f2);
        }
        if(f3 != null){
            transaction.hide(f3);
        }
        if(f4 !=null){
            transaction.hide(f4);
        }
    }



}
