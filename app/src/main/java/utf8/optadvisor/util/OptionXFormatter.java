package utf8.optadvisor.util;

import android.util.Log;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.Calendar;

public class OptionXFormatter implements IAxisValueFormatter {
    private boolean drawValue;
    private String string;

    public OptionXFormatter(boolean drawValue,String string) {
        /* mFormat = new DecimalFormat("###,###,###,##0");*/
        this.drawValue = drawValue;
        this.string=string;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        // "value" represents the position of the label on the axis (x or y)
        if (drawValue == true) {
            Calendar calendar=Calendar.getInstance();
            int year=Integer.valueOf(string.substring(0,4));
            int month=Integer.valueOf(string.substring(4,6));
            int day=Integer.valueOf(string.substring(6));
            calendar.set(year,month,day);
            calendar.add(Calendar.DAY_OF_YEAR,(int)value);
            int m=calendar.get(Calendar.MONTH);
            int d=calendar.get(Calendar.DAY_OF_MONTH);
            return m+"/"+d;
            }
            else{
                return "";
            }
        }
    }