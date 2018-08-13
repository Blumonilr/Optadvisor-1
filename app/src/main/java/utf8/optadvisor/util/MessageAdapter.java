package utf8.optadvisor.util;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import utf8.optadvisor.R;
import utf8.optadvisor.domain.Message;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{

    private List<Message> messageList;

    public MessageAdapter(List<Message> messageList) {
        this.messageList=messageList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message=messageList.get(position);
        //UI修改
        holder.title.setText(message.getTitle());
        holder.content.setText(message.getContent());
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        //声明控件
        TextView title;
        TextView content;


        public ViewHolder(View itemView) {
            super(itemView);
            //初始化控件
            title=itemView.findViewById(R.id.title);
            content=itemView.findViewById(R.id.content);
        }
    }


}
