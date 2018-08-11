package utf8.optadvisor;

import android.os.*;
import android.widget.Button;

/**计时工具
 *
 */
public class TimeCounter extends CountDownTimer {
    private Button button;
    public TimeCounter(long millisInFuture, long countDownInterval, Button bt) {
        super(millisInFuture, countDownInterval);
        this.button=bt;
    }
    @Override
    public void onTick(long millisUntilFinished) {
        button.setTextSize(13);
        button.setText(millisUntilFinished / 1000 +"秒");
        button.setClickable(false);
    }

    @Override
    public void onFinish() {
        button.setTextSize(13);
        button.setText("再次发送");
        button.setClickable(true);
    }




}
