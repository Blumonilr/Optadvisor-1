package utf8.optadvisor.util;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import utf8.optadvisor.R;

public class CenterAdapter extends RecyclerView.Adapter<CenterAdapter.ViewHolder> {
    private List<String> center_info;
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView center;
        public ViewHolder(View view){
            super(view);
         center=(TextView) view.findViewById(R.id.strike_price);
        }
    }
    public CenterAdapter(List<String> center_info){
        this.center_info=center_info;
    }
    @Override
    public CenterAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.strike_price_layout,parent,false);
        CenterAdapter.ViewHolder holder=new CenterAdapter.ViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(CenterAdapter.ViewHolder holder, int position){
        String info=center_info.get(position);
        holder.center.setText(info);
    }
    @Override
    public int getItemCount(){
        return center_info.size();
    }
}

