package utf8.optadvisor.util;

import android.util.Log;

import java.util.ArrayList;

public enum CommaHandler {
    INSTANCE;
    public String commaChange(String str){
        StringBuilder result=new StringBuilder(str);
        for(int i=0;i<str.length();i++){
            if(i<str.length()-13) {
                if (str.substring(i, i + 13).equals("backTestData=")) {
                    int j = i + 13;
                    while (j < str.length()) {
                        if (str.charAt(j) == ',') {
                            if(str.charAt(j+1)==' '){
                                break;
                            }else {
                                result.setCharAt(j, '$');
                            }
                        }
                        j++;
                    }
                }
            }
            if(i<str.length()-18) {
                if (str.substring(i, i + 18).equals("hedgeProfitHolden=")) {
                    int j = i + 18;
                    while (j < str.length()) {
                        if (str.charAt(j) == ',') {
                            if(str.charAt(j+1)==' '){
                                break;
                            }else {
                                result.setCharAt(j, '$');
                            }
                        }
                        j++;
                    }
                }
            }
        }
        //Log.d("CommaHandler",result.toString());
        return result.toString();
    }
}
