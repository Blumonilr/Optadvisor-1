package utf8.optadvisor.util;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import utf8.optadvisor.R;
import utf8.optadvisor.activity.ShopActivity;
import utf8.optadvisor.domain.ShopBean;

public class ShopCartAdapter extends RecyclerView.Adapter<ShopCartAdapter.MyViewHolder> {

    private Context context;
    private List<ShopBean.GoodsBean> data;
    private View headerView;
    private OnDeleteClickListener mOnDeleteClickListener;
    private OnEditClickListener mOnEditClickListener;
    private OnResfreshListener mOnResfreshListener;

    public ShopCartAdapter(Context context, List<ShopBean.GoodsBean> data){
        this.context = context;
        this.data = data;
    }

    @Override
    public ShopCartAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.item_shopgoods, parent, false);
        return new ShopCartAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ShopCartAdapter.MyViewHolder holder, final int position) {

        Glide.with(context).load(data.get(position).getDefaultPic()).into(holder.ivShopCartGoodsPic);
        if (position > 0) {
            if (data.get(position).getShopId() == data.get(position - 1).getShopId()) {
                holder.llShopCartHeader.setVisibility(View.GONE);
            } else {
                holder.llShopCartHeader.setVisibility(View.VISIBLE);
            }
        }else {
            holder.llShopCartHeader.setVisibility(View.VISIBLE);
        }

        holder.tvShopCartGoodsInfo.setText(data.get(position).getInfo());
        holder.tvShopCartGoodsName.setText(data.get(position).getProName());
        holder.tvShopCartShopName.setText(data.get(position).getShopName());
        String price="¥" + data.get(position).getPrice();
        holder.tvShopCartGoodsPrice.setText(price);
        String count=data.get(position).getCount() + "";
        holder.tvShopCartGoodsNum.setText(count);

        if(mOnResfreshListener != null){
            boolean isSelect = false;
            for(int i = 0;i < data.size(); i++){
                if(!data.get(i).isSelect()){
                    isSelect = false;
                    break;
                }else{
                    isSelect = true;
                }
            }
            mOnResfreshListener.onResfresh(isSelect);
        }

        holder.ivShopCartGoodsMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(data.get(position).getCount() > 1) {
                    int count = data.get(position).getCount() - 1;
                    if (mOnEditClickListener != null) {
                        mOnEditClickListener.onEditClick(position, data.get(position).getId(), count);
                    }
                    data.get(position).setCount(count);
                    notifyItemChanged(position,"null");
                }
            }
        });

        holder.ivShopCartGoodsAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = data.get(position).getCount() + 1;
                if(mOnEditClickListener != null){
                    mOnEditClickListener.onEditClick(position,data.get(position).getId(),count);
                }
                data.get(position).setCount(count);
                notifyItemChanged(position,"null");
            }
        });

        if(data.get(position).isSelect()){
            holder.ivShopCartGoodsSel.setImageResource(R.drawable.shopcart_selected);
        }else {
            holder.ivShopCartGoodsSel.setImageResource(R.drawable.shopcart_unselected);
        }

        if(data.get(position).isShopSelect()){
            holder.ivShopCartShopSel.setImageResource(R.drawable.shopcart_selected);
        }else {
            holder.ivShopCartShopSel.setImageResource(R.drawable.shopcart_unselected);
        }

        holder.ivShopCartGoodsDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(v,position);
            }
        });

        holder.ivShopCartGoodsSel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.get(position).setSelect(!data.get(position).isSelect());
                //通过循环找出不同商铺的第一个商品的位置
                for(int i = 0;i < data.size(); i++){
                    if(data.get(i).getIsFirst() == 1) {
                        //遍历去找出同一家商铺的所有商品的勾选情况
                        for(int j = 0;j < data.size();j++){
                            //如果是同一家商铺的商品，并且其中一个商品是未选中，那么商铺的全选勾选取消
                            if(data.get(j).getShopId() == data.get(i).getShopId() && !data.get(j).isSelect()){
                                data.get(i).setShopSelect(false);
                                notifyItemChanged(i,"null");
                                break;
                            }else{
                                //如果是同一家商铺的商品，并且所有商品是选中，那么商铺的选中全选勾选
                                data.get(i).setShopSelect(true);
                                notifyItemChanged(i,"null");
                            }
                        }
                    }
                }
                notifyItemChanged(position,"null");

            }
        });

        holder.ivShopCartShopSel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(data.get(position).getIsFirst() == 1) {
                    data.get(position).setShopSelect(!data.get(position).isShopSelect());
                    for(int i = 0;i < data.size();i++){
                        if(data.get(i).getShopId() == data.get(position).getShopId()){
                            data.get(i).setSelect(data.get(position).isShopSelect());
                        }
                    }
                    notifyItemChanged(position,"null");
                    for (int i=0;i<data.size();i++){
                        if (data.get(i).getShopId()==data.get(position).getShopId()){
                            data.get(i).setSelect(data.get(position).isShopSelect());
                            notifyItemChanged(i,"null");
                        }
                    }
                }
            }
        });

    }

    private void showDialog(final View view, final int position){
        //调用删除某个规格商品的接口
        if(mOnDeleteClickListener != null){
            mOnDeleteClickListener.onDeleteClick(view,position,data.get(position).getId());
        }
        data.remove(position);
        //重新排序，标记所有商品不同商铺第一个的商品位置
        ShopActivity.isSelectFirst(data);
        notifyItemChanged(position,"null");
    }

    @Override
    public int getItemCount() {
        int count = (data == null ? 0 : data.size());
        if(headerView != null){
            count++;
        }
        return count;
    }


    class MyViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView ivShopCartShopSel;
        private TextView tvShopCartShopName;
        private TextView tvShopCartGoodsName;
        private TextView tvShopCartGoodsPrice;
        private TextView tvShopCartGoodsNum;
        private TextView tvShopCartGoodsInfo;
        private ImageView ivShopCartGoodsSel;
        private ImageView ivShopCartGoodsMinus;
        private ImageView ivShopCartGoodsAdd;
        private ImageView ivShopCartGoodsDelete;
        private ImageView ivShopCartGoodsPic;
        private LinearLayout llShopCartHeader;

        public MyViewHolder(View view)
        {
            super(view);
            llShopCartHeader = (LinearLayout) view.findViewById(R.id.ll_shopcart_header);
            ivShopCartShopSel = (ImageView) view.findViewById(R.id.iv_item_shopcart_shopselect);
            tvShopCartShopName = (TextView) view.findViewById(R.id.tv_item_shopcart_shopname);
            tvShopCartGoodsName = (TextView) view.findViewById(R.id.tv_item_shopcart_goodsname);
            tvShopCartGoodsPrice = (TextView) view.findViewById(R.id.tv_item_shopcart_goods_price);
            tvShopCartGoodsNum = (TextView) view.findViewById(R.id.tv_item_shopcart_goods_num);
            tvShopCartGoodsInfo = (TextView) view.findViewById(R.id.tv_item_shopcart_goods_info);
            ivShopCartGoodsSel = (ImageView) view.findViewById(R.id.iv_item_shopcart_goodsselect);
            ivShopCartGoodsMinus = (ImageView) view.findViewById(R.id.iv_item_shopcart_goods_minus);
            ivShopCartGoodsAdd = (ImageView) view.findViewById(R.id.iv_item_shopcart_goods_add);
            ivShopCartGoodsPic = (ImageView) view.findViewById(R.id.iv_item_shopcart_goods_pic);
            ivShopCartGoodsDelete = (ImageView) view.findViewById(R.id.iv_item_shopcart_goods_delete);
        }
    }


    public View getHeaderView(){
        return headerView;
    }

    private ShopCartAdapter.OnItemClickListener mOnItemClickListener;
    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(ShopCartAdapter.OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public interface OnDeleteClickListener{
        void onDeleteClick(View view, int position, int cartid);
    }

    public void setOnDeleteClickListener(OnDeleteClickListener mOnDeleteClickListener){
        this.mOnDeleteClickListener = mOnDeleteClickListener;
    }

    public interface OnEditClickListener{
        void onEditClick(int position, int cartid, int count);
    }

    public void setOnEditClickListener(OnEditClickListener mOnEditClickListener){
        this.mOnEditClickListener = mOnEditClickListener;
    }

    public interface OnResfreshListener{
        void onResfresh(boolean isSelect);
    }

    public void setResfreshListener(OnResfreshListener mOnResfreshListener){
        this.mOnResfreshListener = mOnResfreshListener;
    }

}
