package utf8.optadvisor.util;

import android.util.Log;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

public class MyXFormatter implements IAxisValueFormatter {
    private boolean drawValue;

    public MyXFormatter(boolean drawValue) {
        /* mFormat = new DecimalFormat("###,###,###,##0");*/
        this.drawValue = drawValue;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        // "value" represents the position of the label on the axis (x or y)
        if (drawValue == true) {
           int hour=((int)value)/60;
           int minute=((int)value)%60;
           if(minute!=0) {
               return hour + ":" + minute;
           }
           else{
               return hour+":"+minute+"0";
           }
        }
        else{
            return "";
        }
    }
}
