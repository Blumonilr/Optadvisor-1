package utf8.optadvisor.util;

import android.util.Log;

import java.util.ArrayList;

public enum CommaHandler {
    INSTANCE;
    public String commaChange(String str){
        String[] warningText={"backTestData=","hedgeProfitHolden=","buildTime="};

        StringBuilder result=new StringBuilder(str);
        for(int i=0;i<str.length();i++) {
            for (int k = 0; k < warningText.length; k++) {
                String eachText=warningText[k];
                int length=eachText.length();
                if (i < str.length() - length) {
                    if (str.substring(i, i + length).equals(eachText)) {
                        int j = i + length;
                        while (j < str.length()) {
                            if (str.charAt(j) == ',') {
                                if (str.charAt(j + 1) == ' ') {
                                    break;
                                } else {
                                    result.setCharAt(j, '$');
                                }
                            }
                            if (str.charAt(j) == ':') {
                                if (str.charAt(j + 1) >='0'&&str.charAt(j + 1) <='9'&&str.charAt(j - 1) >='0'&&str.charAt(j - 1) <='9') {
                                    result.setCharAt(j, '$');
                                }
                            }
                            j++;
                        }
                    }
                }
            }
        }
        //Log.d("CommaHandler",result.toString());
        return result.toString();
    }
}
