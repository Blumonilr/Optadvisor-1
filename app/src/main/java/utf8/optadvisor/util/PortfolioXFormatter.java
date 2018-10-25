package utf8.optadvisor.util;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

public class PortfolioXFormatter implements IAxisValueFormatter {

    public static int baseYear=2015;
    public static int baseMonth=1;
    public double base;
    //2015年1月，序号为0
    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        /*int exact=(int)value;
        int extraYear= exact/12;
        int extraMonth=exact-extraYear*12;
        return (baseYear+extraYear)+"/"+(baseMonth+extraMonth);*/
        double result=base+value/1000000;
        return String.valueOf(result);
    }

    public void setBase(double base){
        this.base=base;
    }
}
