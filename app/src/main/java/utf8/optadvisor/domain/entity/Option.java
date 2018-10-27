package utf8.optadvisor.domain.entity;

public class Option {
    private Long persisentId;//存储用主键

    private String tradeCode; //交易代码
    private String optionCode; //期权代码
    private String name;//例如:50ETF购8月2600
    private int type;//1为买入0为卖出
    private int cp;//1为看涨 -1为看跌
    private String expireTime;//到期时间
    private double transactionPrice;//成交价(即交易时的买价或卖价)
    private double yclose;//期权前一天收盘价
    private double price1;//期权实时买入价格
    private double price2;//期权实时卖出价格
    private double k;//期权行权价格
    private double realTimePrice;//期权实时价
    private double delta;
    private double gamma;
    private double vega;
    private double theta;
    private double rho;
    private double beta;

    public Long getPersisentId() {
        return persisentId;
    }

    public void setPersisentId(Long persisentId) {
        this.persisentId = persisentId;
    }

    public String getTradeCode() {
        return tradeCode;
    }

    public void setTradeCode(String tradeCode) {
        this.tradeCode = tradeCode;
    }

    public String getOptionCode() {
        return optionCode;
    }

    public void setOptionCode(String optionCode) {
        this.optionCode = optionCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCp() {
        return cp;
    }

    public void setCp(int cp) {
        this.cp = cp;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public double getTransactionPrice() {
        return transactionPrice;
    }

    public void setTransactionPrice(double transactionPrice) {
        this.transactionPrice = transactionPrice;
    }

    public double getYclose() {
        return yclose;
    }

    public void setYclose(double yclose) {
        this.yclose = yclose;
    }

    public double getPrice1() {
        return price1;
    }

    public void setPrice1(double price1) {
        this.price1 = price1;
    }

    public double getPrice2() {
        return price2;
    }

    public void setPrice2(double price2) {
        this.price2 = price2;
    }

    public double getK() {
        return k;
    }

    public void setK(double k) {
        this.k = k;
    }

    public double getRealTimePrice() {
        return realTimePrice;
    }

    public void setRealTimePrice(double realTimePrice) {
        this.realTimePrice = realTimePrice;
    }

    public double getDelta() {
        return delta;
    }

    public void setDelta(double delta) {
        this.delta = delta;
    }

    public double getGamma() {
        return gamma;
    }

    public void setGamma(double gamma) {
        this.gamma = gamma;
    }

    public double getVega() {
        return vega;
    }

    public void setVega(double vega) {
        this.vega = vega;
    }

    public double getTheta() {
        return theta;
    }

    public void setTheta(double theta) {
        this.theta = theta;
    }

    public double getRho() {
        return rho;
    }

    public void setRho(double rho) {
        this.rho = rho;
    }

    public double getBeta() {
        return beta;
    }

    public void setBeta(double beta) {
        this.beta = beta;
    }
}
