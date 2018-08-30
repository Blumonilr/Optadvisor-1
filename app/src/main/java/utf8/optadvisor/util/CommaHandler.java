package utf8.optadvisor.util;

import java.util.ArrayList;

public enum CommaHandler {
    INSTANCE;
    public String commaChange(String str){
        StringBuilder result=new StringBuilder(str);
        ArrayList<Integer> toChange=new ArrayList<>();
        for(int i=0;i<str.length();i++){
            if(str.charAt(i)==','){
                if(i-1>=0&&i+1<str.length()){
                    if(str.charAt(i-1)>='0'&&str.charAt(i-1)<='9'){
                        if((str.charAt(i+1)>='0'&&str.charAt(i+1)<='9')||str.charAt(i+1)=='-') {
                            result.setCharAt(i,'$');
                        }
                    }
                }
            }
        }
        return result.toString();
    }
}
