package utf8.optadvisor.util;

import android.support.v7.widget.RecyclerView;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Line;
import utf8.optadvisor.R;
import utf8.optadvisor.activity.MainActivity;
import utf8.optadvisor.activity.MoreInfoActivity;


public class LeftAdapter extends RecyclerView.Adapter<LeftAdapter.ViewHolder> {
    public interface OnItemClickListener{
        void onItemClick(View view);
    }
    private List<String[]> call_option_info;
    private OnItemClickListener mItemClickListener;
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView left1;
        TextView left2;
        TextView left3;
        TextView left4;
        TextView left5;
        TextView left6;
        TextView left7;
        TextView left8;
        LinearLayout click_item;

        public ViewHolder(View view){
            super(view);
            click_item=(LinearLayout) view.findViewById(R.id.click_area);
            left1=(TextView) view.findViewById(R.id.left1);
            left2=(TextView) view.findViewById(R.id.left2);
            left3=(TextView) view.findViewById(R.id.left3);
            left4=(TextView) view.findViewById(R.id.left4);
            left5=(TextView) view.findViewById(R.id.left5);
            left6=(TextView) view.findViewById(R.id.left6);
            left7=(TextView) view.findViewById(R.id.left7);
            left8=(TextView) view.findViewById(R.id.left8);
        }
    }
    public LeftAdapter(List<String[]> call_option_info){
        this.call_option_info=call_option_info;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.call_option_layout,parent,false);
        ViewHolder holder=new ViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClick(v);
            }
        });

        return holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        holder.itemView.setTag(position);
        String[] info=call_option_info.get(position);
        holder.left1.setText(info[0]);
        holder.left2.setText(info[1]);
        holder.left3.setText(info[2]);
        holder.left4.setText(info[3]);
        holder.left5.setText(info[4]);
        holder.left6.setText(info[5]);
        holder.left7.setText(info[38]);
        holder.left8.setText(info[6]);
    }
    @Override
    public int getItemCount(){
        return call_option_info.size();
    }
    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }
}
