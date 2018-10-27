package utf8.optadvisor.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;

import utf8.optadvisor.R;

@SuppressLint("ViewConstructor")
public class ChartMarkerView extends MarkerView {
    private TextView tvContent;

    private int mode;
    private double base;

    public ChartMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);

        tvContent= findViewById(R.id.tvContent);
    }

    public void setMode(int mode,double base){
        this.mode=mode;
        this.base=base;
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        try {
            if(mode==1){
                if (e instanceof CandleEntry) {

                    CandleEntry ce = (CandleEntry) e;

                    if (Math.abs(ce.getHigh()) < 1) {
                        tvContent.setText(String.format("%s ,%s", ce.getX()/1000000+base, Utils.formatNumber(ce.getHigh(), 4, true)));
                    } else {
                        tvContent.setText(String.format("%s ,%s", ce.getX()/1000000+base, Utils.formatNumber(ce.getHigh(), 2, true)));
                    }
                } else {
                    if (Math.abs(e.getY()) < 1) {
                        tvContent.setText(String.format("%s ,%s", e.getX()/1000000+base, Utils.formatNumber(e.getY(), 4, true)));
                    } else {
                        tvContent.setText(String.format("%s ,%s", e.getX()/1000000+base, Utils.formatNumber(e.getY(), 2, true)));
                    }
                }
            }else {
                if (e instanceof CandleEntry) {

                    CandleEntry ce = (CandleEntry) e;

                    if (Math.abs(ce.getHigh()) < 1) {
                        tvContent.setText(String.format("%s ,%s", ce.getX(), Utils.formatNumber(ce.getHigh(), 4, true)));
                    } else {
                        tvContent.setText(String.format("%s ,%s", ce.getX(), Utils.formatNumber(ce.getHigh(), 2, true)));
                    }
                } else {
                    if (Math.abs(e.getY()) < 1) {
                        tvContent.setText(String.format("%s ,%s", e.getX(), Utils.formatNumber(e.getY(), 4, true)));
                    } else {
                        tvContent.setText(String.format("%s ,%s", e.getX(), Utils.formatNumber(e.getY(), 2, true)));
                    }
                }
            }

            super.refreshContent(e, highlight);
        }catch (NullPointerException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }

}
