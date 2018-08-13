package utf8.optadvisor.util;

import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import utf8.optadvisor.R;

public class ShopCartAdapter extends RecyclerView.Adapter<ShopCartAdapter.MyViewHolder> {
    public ShopCartAdapter() {
        super();
    }

    @NonNull
    @Override
    public ShopCartAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ShopCartAdapter.MyViewHolder holder, int position) {

    }

    @Override
    public void onBindViewHolder(@NonNull ShopCartAdapter.MyViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public void onViewRecycled(@NonNull ShopCartAdapter.MyViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public boolean onFailedToRecycleView(@NonNull ShopCartAdapter.MyViewHolder holder) {
        return super.onFailedToRecycleView(holder);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ShopCartAdapter.MyViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ShopCartAdapter.MyViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public void registerAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
    }

    @Override
    public void unregisterAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver observer) {
        super.unregisterAdapterDataObserver(observer);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView ivShopCartShopSelect;
        private TextView tvShopCartShopName;
        private TextView tvShopCartGoodsName;
        private TextView tvShopCartGoodsPrice;
        private TextView tvShopCartGoodsNum;
        private TextView tvShopCartGoodsInfo;
        private ImageView ivShopCartGoodsSelect;
        private ImageView ivShopCartGoodsMinus;
        private ImageView ivShopCartGoodsAdd;
        private ImageView ivShopCartGoodsDelete;
        private ImageView ivShopCartGoodsPic;
        private LinearLayout llShopCartHeader;

        public MyViewHolder(View itemView) {
            super(itemView);
            llShopCartHeader=(LinearLayout)itemView.findViewById(R.id.ll_shopcart_header);
        }
    }
}
