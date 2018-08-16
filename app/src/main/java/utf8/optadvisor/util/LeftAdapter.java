package utf8.optadvisor.util;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;



public class LeftAdapter extends RecyclerView.Adapter<LeftAdapter.ViewHolder> {
    private List<String[]> call_option_info;
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView left1;
        TextView left2;
        TextView left3;
        TextView left4;
        TextView left5;
        TextView left6;
        public ViewHolder(View view){
            super(view);
            left1=(TextView) view.findViewById(R.id.left1);
            left2=(TextView) view.findViewById(R.id.left2);
            left3=(TextView) view.findViewById(R.id.left3);
            left4=(TextView) view.findViewById(R.id.left4);
            left5=(TextView) view.findViewById(R.id.left5);
            left6=(TextView) view.findViewById(R.id.left6);
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
        return holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        String[] info=call_option_info.get(position);
        holder.left1.setText(info[1]);
        holder.left2.setText(info[3]);
        holder.left3.setText(info[39]);
        holder.left4.setText(info[40]);
        holder.left5.setText(info[6]);
        holder.left6.setText(info[38]);
    }
    @Override
    public int getItemCount(){
        return call_option_info.size();
    }
}
