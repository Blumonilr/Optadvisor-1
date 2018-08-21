package utf8.optadvisor.util;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import utf8.optadvisor.R;


public class ControllerAdapter extends RecyclerView.Adapter<ControllerAdapter.ViewHolder> {
    private List<Integer> controller_data;
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
    public void onBindViewHolder(ControllerAdapter.ViewHolder holder, int position){
        holder.controller.setGoods_storage(99);
    }
    @Override
    public int getItemCount(){
        return controller_data.size();
    }


}
