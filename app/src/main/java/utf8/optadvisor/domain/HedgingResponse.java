package utf8.optadvisor.domain;

import java.io.Serializable;

import utf8.optadvisor.domain.entity.Option;

public class HedgingResponse implements Serializable{
    private Option option=null;
    private double iK;
    private int iNum;
    private String[][] graph;
    private double pAsset;

    public HedgingResponse() {
    }

    public Option getOption() {
        return option;
    }

    public void setOption(Option option) {
        this.option = option;
    }

    public double getIk() {
        return iK;
    }

    public void setIk(double ik) {
        this.iK = ik;
    }

    public String[][] getGraph() {
        return graph;
    }

    public void setGraph(String[][] graph) {
        this.graph = graph;
    }

    public double getpAsset() {
        return pAsset;
    }

    public int getiNum() {
        return iNum;
    }
}
