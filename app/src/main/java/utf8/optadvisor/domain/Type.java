package utf8.optadvisor.domain;

public enum Type {
    //资产配置，套期保值，DIY
    RECOMMEND_PORTFOLIO, HEDGE, DIY;

    public int toValue() {
        return ordinal();
    }
}
