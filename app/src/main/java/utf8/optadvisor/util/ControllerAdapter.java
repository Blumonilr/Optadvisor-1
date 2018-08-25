package utf8.optadvisor.util;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utf8.optadvisor.R;


public class ControllerAdapter extends RecyclerView.Adapter<ControllerAdapter.ViewHolder> {
    private List<Integer> controller_data;
    private Map<Integer,Integer> amount_map=new HashMap<>();
    static class ViewHolder extends RecyclerView.ViewHolder{
        AmountView controller;
        public ViewHolder(View view){
            super(view);
            controller=view.findViewById(R.id.add_sub);
        }
    }
    public ControllerAdapter(List<Integer> controller_data){
        this.controller_data=controller_data;
    }
    @Override
    public ControllerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.quantity_controllers,parent,false);
        ControllerAdapter.ViewHolder holder=new ControllerAdapter.ViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(ControllerAdapter.ViewHolder holder, final int position){
        holder.controller.setGoods_storage(99);
        holder.controller.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
                amount_map.put(position,amount);
            }
        });
    }
    @Override
    public int getItemCount(){
        return controller_data.size();
    }


    public Map<Integer, Integer> getAmount_map() {
        return amount_map;
    }
}
