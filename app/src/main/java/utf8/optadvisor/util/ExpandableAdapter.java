package utf8.optadvisor.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;

import utf8.optadvisor.R;


public class ExpandableAdapter extends BaseExpandableListAdapter {
    private Context context;

    private  List<String> groupArray;
    private  List<List<String>> childArray;

    public  ExpandableAdapter(Context context,List<String> groupArray,List<List<String>> childArray)
    {
        this.context=context;
        this.groupArray=groupArray;
        this.childArray=childArray;
    }

    public void setChildArray(List<List<String>> childArray) {
        this.childArray = childArray;
    }

    @Override
    public int getGroupCount() {
        return groupArray.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return childArray.get(i).size();
    }

    @Override
    public Object getGroup(int i) {
        return groupArray.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return childArray.get(i).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String string = groupArray.get(i);
        return  getGenericView(string,true);
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View childView = LayoutInflater.from(context).inflate(R.layout.item_expand_child, null);
        TextView textView=childView.findViewById(R.id.child_name);
        textView.setText(childArray.get(0).get(childPosition));
        return childView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    /**
     * 自动生成view
     */
    private TextView getGenericView(String string,boolean isParent)
    {
        //ExpandableListView的布局参数
        AbsListView.LayoutParams layoutParams = new  AbsListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 100 );
        TextView text = new TextView(context);
        text.setLayoutParams(layoutParams);
        text.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        //设置文本位置
        text.setPadding(80 , 0 , 0 , 0 );
        text.setTextSize(30);
        text.setText(string);
        return  text;
    }

}
