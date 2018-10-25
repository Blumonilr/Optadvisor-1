package utf8.optadvisor.domain.entity;

public class Portfolio {
    private Long id;

    private String name;

    private String username;

    private Option[] options;

    private int type; //type指0：资产配置组合 1：套期保值组合 2：DIY组合
    private boolean trackingStatus;

    //期权组合和DIY要有的东西
    private double M0;//警报需要
    private double k;//警报需要
    private double sigma1;
    private double sigma2;
    private double p1;
    private double p2;
    private double cost;//成本p1-p2
    private double bond;//保证金
    private double z_delta;
    private double z_gamma;
    private double z_vega;
    private double z_theta;
    private double z_rho;
    private double returnOnAssets; //资产收益率
    private double em;//组合的期望收益率
    private double beta;//组合风险值

    //套期保值需要的量
    private int N;//套期保值中的N
    private double iK;//套期保值中的iK
    private double pAsset;//套期保值中的pAsset
    private double sExp;
    private boolean flag;

/*    private String backTestData;
    private String backTestData1;*/
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Option[] getOptions() {
        return options;
    }

    public void setOptions(Option[] options) {
        this.options = options;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean getTrackingStatus() {
        return trackingStatus;
    }

    public void setTrackingStatus(boolean trackingStatus) {
        this.trackingStatus = trackingStatus;
    }

    public double getM0() {
        return M0;
    }

    public void setM0(double m0) {
        M0 = m0;
    }

    public double getK() {
        return k;
    }

    public void setK(double k) {
        this.k = k;
    }

    public double getSigma1() {
        return sigma1;
    }

    public void setSigma1(double sigma1) {
        this.sigma1 = sigma1;
    }

    public double getSigma2() {
        return sigma2;
    }

    public void setSigma2(double sigma2) {
        this.sigma2 = sigma2;
    }

    public double getP1() {
        return p1;
    }

    public void setP1(double p1) {
        this.p1 = p1;
    }

    public double getP2() {
        return p2;
    }

    public void setP2(double p2) {
        this.p2 = p2;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getBond() {
        return bond;
    }

    public void setBond(double bond) {
        this.bond = bond;
    }

    public double getZ_delta() {
        return z_delta;
    }

    public void setZ_delta(double z_delta) {
        this.z_delta = z_delta;
    }

    public double getZ_gamma() {
        return z_gamma;
    }

    public void setZ_gamma(double z_gamma) {
        this.z_gamma = z_gamma;
    }

    public double getZ_vega() {
        return z_vega;
    }

    public void setZ_vega(double z_vega) {
        this.z_vega = z_vega;
    }

    public double getZ_theta() {
        return z_theta;
    }

    public void setZ_theta(double z_theta) {
        this.z_theta = z_theta;
    }

    public double getZ_rho() {
        return z_rho;
    }

    public void setZ_rho(double z_rho) {
        this.z_rho = z_rho;
    }

    public double getEm() {
        return em;
    }

    public void setEm(double em) {
        this.em = em;
    }

    public double getBeta() {
        return beta;
    }

    public void setBeta(double beta) {
        this.beta = beta;
    }

    public int getN() {
        return N;
    }

    public void setN(int n) {
        N = n;
    }

    public double getiK() {
        return iK;
    }

    public void setiK(double iK) {
        this.iK = iK;
    }

    public double getpAsset() {
        return pAsset;
    }

    public void setpAsset(double pAsset) {
        this.pAsset = pAsset;
    }

    public double getsExp() {
        return sExp;
    }

    public void setsExp(double sExp) {
        this.sExp = sExp;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

/*    public String getBackTestData() {
        return backTestData;
    }

    public void setBackTestData(String backTestData) {
        this.backTestData = backTestData;
    }

    public String getBackTestData1() {
        return backTestData1;
    }

    public void setBackTestData1(String backTestData1) {
        this.backTestData1 = backTestData1;
    }*/

    public double getReturnOnAssets() {
        return returnOnAssets;
    }

    public void setReturnOnAssets(double returnOnAssets) {
        this.returnOnAssets = returnOnAssets;
    }

}
