package utf8.optadvisor.domain.entity;

public class Portfolio {
    private Long id;
    private String username;
    private Option[] options;
    private Enum type; //type指1：资产配置组合 2：套期保值组合 3：DIY组合
    private boolean trackingStatus;
}
