package utf8.optadvisor.util;

public class CustomOption {
    private String expireTime;
    private int type;
    private int cp;
    private String optionCode;
    public CustomOption(String expireTime,int type,int cp,String optionCode){
        this.expireTime=expireTime;
        this.type=type;
        this.cp=cp;
        this.optionCode=optionCode;
    }


    public int getCp() {
        return cp;
    }

    public int getType() {
        return type;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public String getOptionCode() {
        return optionCode;
    }

    public void setCp(int cp) {
        this.cp = cp;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public void setOptionCode(String optionCode) {
        this.optionCode = optionCode;
    }

    public void setType(int type) {
        this.type = type;
    }
}
