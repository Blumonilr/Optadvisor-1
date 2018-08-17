package utf8.optadvisor.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import utf8.optadvisor.R;
import utf8.optadvisor.fragment.BuildPortfolio;
import utf8.optadvisor.fragment.MyCombination;
import utf8.optadvisor.fragment.OptionShow;
import utf8.optadvisor.util.ActivityJumper;

/**
 * 主界面
 */
public class MainActivity extends AppCompatActivity {
    private SharedPreferences preferences;

    private OptionShow optionShow;
    private BuildPortfolio buildPortfolio;
    private MyCombination myCombination;

    private DrawerLayout drawerLayout;

    private Button toOptionShowButton;
    private Button toBulidPortfolioButton;
    private Button toMyCombinationButton;

    TextView innerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences=getSharedPreferences("userInfo",MODE_PRIVATE);

        initOptionShowButton();
        initBuildPortfolioButton();
        initMyCombinationButton();
        initToolBar();
        initDrawer();
        showOptionShow();
    }

    /**
     * 显示“当前行情”
     */
    private void showOptionShow(){
        innerText.setText("当前行情");

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(optionShow==null){
            optionShow=new OptionShow();
            transaction.add(R.id.main_frame_layout, optionShow);
        }
        hideFragment(transaction);
        transaction.show(optionShow);
        changeButton(true,toOptionShowButton);
        changeButton(false,toBulidPortfolioButton);
        changeButton(false,toMyCombinationButton);
        transaction.commit();
    }

    /**
     * 显示“构建组合”
     */
    private void showBuildPortfolio(){
        innerText.setText("构建组合");

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(buildPortfolio==null){
            buildPortfolio=new BuildPortfolio();
            transaction.add(R.id.main_frame_layout, buildPortfolio);
        }
        hideFragment(transaction);
        transaction.show(buildPortfolio);
        changeButton(false,toOptionShowButton);
        changeButton(true,toBulidPortfolioButton);
        changeButton(false,toMyCombinationButton);
        transaction.commit();
    }

    /**
     * 显示“我的组合”
     */
    private void showMyCombination(){
        innerText.setText("我的组合");

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(myCombination==null){
            myCombination=new MyCombination();
            transaction.add(R.id.main_frame_layout, myCombination);
        }
        hideFragment(transaction);
        transaction.show(myCombination);
        changeButton(false,toOptionShowButton);
        changeButton(false,toBulidPortfolioButton);
        changeButton(true,toMyCombinationButton);
        transaction.commit();
    }

    /**
     * 设置“行情展示”按钮
     */
    private void initOptionShowButton(){
        toOptionShowButton = findViewById(R.id.option_show_button);

        toOptionShowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptionShow();
            }
        });
    }

    /**
     * 设置“构建组合”按钮
     */
    private void initBuildPortfolioButton(){
        toBulidPortfolioButton = findViewById(R.id.build_portfolio_button);
        toBulidPortfolioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (preferences.getBoolean("isLogined", true)) {
                    showBuildPortfolio();
                } else {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * 设置“我的组合”按钮
     */
    private void initMyCombinationButton(){
        toMyCombinationButton = findViewById(R.id.my_combination_button);
        toMyCombinationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(preferences.getBoolean("isLogined",true)) {
                    showMyCombination();
                }else{
                    Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * 设置标题栏
     */
    private void initToolBar(){
        Toolbar toolbar =findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        innerText=findViewById(R.id.inner_text);
        innerText.setText(R.string.option_show_text);
    }

    /**
     * 设置滑动菜单
     */
    private void initDrawer(){
        drawerLayout= findViewById(R.id.drawer_layout) ;
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.personal_info);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.personal_info:
                        Log.d("Main","toPersonalInfo");
                        break;
                    case R.id.message_center:
                        Log.d("Main","toMessageCenter");
                        ActivityJumper.rightEnterLeftExit(MainActivity.this,MainActivity.this,MessageActivity.class);
                        break;
                    case R.id.about:
                        Log.d("Main","toAbout");
                        break;
                    case R.id.quit:
                        SharedPreferences.Editor editor=preferences.edit();
                        editor.putBoolean("isLogined",false);
                        editor.apply();
                        ActivityJumper.rightEnterLeftExit(MainActivity.this,MainActivity.this,LoginActivity.class);
                        break;
                    default:
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 主界面点击back键程序直接退出+程序退出的附加逻辑
     */
    @Override
    public void onBackPressed() {
        finish();
    }

    /**
     * 隐藏碎片
     */
     private void hideFragment(FragmentTransaction transaction){
        if(optionShow != null){
            transaction.hide(optionShow);
        }
        if(buildPortfolio != null){
            transaction.hide(buildPortfolio);
        }
        if(myCombination != null){
            transaction.hide(myCombination);
        }
    }

    private void changeButton(boolean chosen,Button button){
         if(chosen){
             button.setTextColor(getResources().getColor(R.color.colorButton,null));
         }else{
             button.setTextColor(getResources().getColor(R.color.colorDarkGray,null));
         }
         Drawable picChosen=null;
         switch (button.getId()){
             case R.id.option_show_button:
                 if(chosen){
                     picChosen=getResources().getDrawable(R.mipmap.ic_option_show_chosen,null);
                 }else{
                     picChosen=getResources().getDrawable(R.mipmap.ic_option_show,null);
                 }
                 break;
             case R.id.build_portfolio_button:
                 if(chosen){
                     picChosen=getResources().getDrawable(R.mipmap.ic_build_portfolio_chosen,null);
                 }else{
                     picChosen=getResources().getDrawable(R.mipmap.ic_build_portfolio,null);
                 }
                 break;
             case R.id.my_combination_button:
                 if(chosen){
                     picChosen=getResources().getDrawable(R.mipmap.ic_my_combination_chosen,null);
                 }else{
                     picChosen=getResources().getDrawable(R.mipmap.ic_my_combination,null);
                 }
                 break;
                 default:break;
         }
         picChosen.setBounds(0, 0, 100,100);
         button.setCompoundDrawables(null, picChosen, null, null);
    }

}
