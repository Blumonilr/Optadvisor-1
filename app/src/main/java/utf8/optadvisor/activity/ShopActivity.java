package utf8.optadvisor.activity;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import utf8.optadvisor.R;
import utf8.optadvisor.domain.ShopBean;
import utf8.optadvisor.util.ShopCartAdapter;

public class ShopActivity extends AppCompatActivity {

    private ImageView ivShopCartSelect;
    private TextView tvShopCartSubmit,tvShopCartSelect,tvShopCartTotalNum;
    private View mEmptyView;

    private RecyclerView rlvShopCart,rlvHotProducts;
    private ShopCartAdapter mShopCartAdapter;
    private LinearLayout llPay;
    private RelativeLayout rlHaveProduct;
    private List<ShopBean.GoodsBean> mAllOrderList = new ArrayList<>();
    private ArrayList<ShopBean.GoodsBean> mGoPayList = new ArrayList<>();
    private List<String> mHotProductsList = new ArrayList<>();
    private TextView tvShopCartTotalPrice;
    private int mCount,mPosition;
    private float mTotalPrice1;
    private boolean mSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        ivShopCartSelect=(ImageView)findViewById(R.id.iv_addSelect);
        tvShopCartTotalPrice=(TextView)findViewById(R.id.tv_totalprice);
        tvShopCartTotalNum=(TextView)findViewById(R.id.tv_totalnum);

        rlHaveProduct=(RelativeLayout)findViewById(R.id.rl_shopCart);
        rlvShopCart=(RecyclerView)findViewById(R.id.rlv_shopCart);
        mEmptyView=(View)findViewById(R.id.emptyView);
        mEmptyView.setVisibility(View.GONE);
        llPay=(LinearLayout)findViewById(R.id.ll_sum);
        RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
        llPay.setLayoutParams(lp);

        tvShopCartSubmit=(TextView)findViewById(R.id.tv_submit);

        rlvShopCart.setLayoutManager(new LinearLayoutManager(this));
        mShopCartAdapter=new ShopCartAdapter(this,mAllOrderList);
        rlvShopCart.setAdapter(mShopCartAdapter);

        //删除商品接口
        mShopCartAdapter.setOnDeleteClickListener(new ShopCartAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(View view, int position,int cartid) {
                mShopCartAdapter.notifyDataSetChanged();
            }
        });
        //修改数量接口
        mShopCartAdapter.setOnEditClickListener(new ShopCartAdapter.OnEditClickListener() {
            @Override
            public void onEditClick(int position, int cartid, int count) {
                mCount = count;
                mPosition = position;
            }
        });

        //实时监控全选按钮
        mShopCartAdapter.setResfreshListener(new ShopCartAdapter.OnResfreshListener() {
            @Override
            public void onResfresh( boolean isSelect) {
                mSelect = isSelect;
                if(isSelect){
                    ivShopCartSelect.setImageResource(R.drawable.shopcart_selected);
                }else {
                    ivShopCartSelect.setImageResource(R.drawable.shopcart_unselected);
                }
                float mTotalPrice = 0;
                int mTotalNum = 0;
                mTotalPrice1 = 0;
                mGoPayList.clear();
                for(int i = 0;i < mAllOrderList.size(); i++)
                    if(mAllOrderList.get(i).isSelect()) {
                        mTotalPrice += mAllOrderList.get(i).getPrice() * mAllOrderList.get(i).getCount();
                        mTotalNum += 1;
                        mGoPayList.add(mAllOrderList.get(i));
                    }
                mTotalPrice1 = mTotalPrice;
                tvShopCartTotalPrice.setText("总价：" + mTotalPrice);
                tvShopCartTotalNum.setText("共" + mTotalNum + "件商品");
            }
        });

        //全选
        ivShopCartSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelect = !mSelect;
                if(mSelect){
                    ivShopCartSelect.setImageResource(R.drawable.shopcart_selected);
                    for(int i = 0;i < mAllOrderList.size();i++){
                        mAllOrderList.get(i).setSelect(true);
                        mAllOrderList.get(i).setShopSelect(true);
                    }
                }else{
                    ivShopCartSelect.setImageResource(R.drawable.shopcart_unselected);
                    for(int i = 0;i < mAllOrderList.size();i++){
                        mAllOrderList.get(i).setSelect(false);
                        mAllOrderList.get(i).setShopSelect(false);
                    }
                }
                mShopCartAdapter.notifyDataSetChanged();

            }
        });

        initData();
        mShopCartAdapter.notifyDataSetChanged();
    }

    private void initData(){
            ShopBean.GoodsBean sb1 = new ShopBean.GoodsBean();
            sb1.setShopId(1);
            sb1.setPrice(1.0);
            sb1.setDefaultPic("http://img2.3lian.com/2014/c7/25/d/40.jpg");
            sb1.setProName("狼牙龙珠鼠标");
            sb1.setShopName("狼牙龙珠");
            sb1.setInfo("蓝色");
            sb1.setCount(2);
            mAllOrderList.add(sb1);

            ShopBean.GoodsBean sb2 = new ShopBean.GoodsBean();
            sb2.setShopId(2);
            sb2.setPrice(2.0);
            sb2.setDefaultPic("http://img2.3lian.com/2014/c7/25/d/40.jpg");
            sb2.setProName("达尔优鼠标");
            sb2.setShopName("达尔优");
            sb2.setInfo("绿色");
            sb2.setCount(2);
            mAllOrderList.add(sb2);

            ShopBean.GoodsBean sb3 = new ShopBean.GoodsBean();
            sb3.setShopId(3);
            sb3.setPrice(20.0);
            sb3.setDefaultPic("http://img2.3lian.com/2014/c7/25/d/40.jpg");
            sb3.setProName("液晶显示屏");
            sb3.setShopName("英伟达");
            sb3.setInfo("黑色");
            sb3.setCount(2);
            mAllOrderList.add(sb3);

        ShopBean.GoodsBean sb4 = new ShopBean.GoodsBean();
        sb4.setShopId(4);
        sb4.setPrice(25.0);
        sb4.setDefaultPic("http://img2.3lian.com/2014/c7/25/d/40.jpg");
        sb4.setProName("液晶显示屏");
        sb4.setShopName("英伟达2店");
        sb4.setInfo("白色");
        sb4.setCount(3);
        mAllOrderList.add(sb4);

        ShopBean.GoodsBean sb5 = new ShopBean.GoodsBean();
        sb5.setShopId(4);
        sb5.setPrice(24.0);
        sb5.setDefaultPic("http://img2.3lian.com/2014/c7/25/d/40.jpg");
        sb5.setProName("液晶显示屏");
        sb5.setShopName("英伟达2店");
        sb5.setInfo("灰色");
        sb5.setCount(3);
        mAllOrderList.add(sb5);

        isSelectFirst(mAllOrderList);
    }

    public static void isSelectFirst(List<ShopBean.GoodsBean> list){
        if(list.size() > 0) {
            list.get(0).setIsFirst(1);
            for (int i = 1; i < list.size(); i++) {
                if (list.get(i).getShopId() == list.get(i - 1).getShopId()) {
                    list.get(i).setIsFirst(2);
                } else {
                    list.get(i).setIsFirst(1);
                }
            }
        }

    }
}
