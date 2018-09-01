package utf8.optadvisor.domain;

import java.io.Serializable;
import java.util.List;

import utf8.optadvisor.domain.entity.Option;

public class AllocationResponse implements Serializable {

    private List<Option> optionList;
    private int[] buyAndSell;
    private int num;
    private double cost;
    private double bond;
    private double z_delta;
    private double z_gamma;
    private double z_vega;
    private double z_theta;
    private double z_rho;
    private double em;
    private double beta;
    private List<List<String>> graph;
    private double m0;
    private double k;
    private double p1;
    private double p2;
    private double sigma1;
    private double sigma2;
    private double returnOnAssets;


    public int[] getBuyAndSell() {
        return buyAndSell;
    }

    public int getNum() {
        return num;
    }

    public double getCost() {
        return cost;
    }

    public double getBond() {
        return bond;
    }

    public double getZ_delta() {
        return z_delta;
    }

    public double getZ_gamma() {
        return z_gamma;
    }

    public double getZ_vega() {
        return z_vega;
    }

    public double getZ_theta() {
        return z_theta;
    }

    public double getZ_rho() {
        return z_rho;
    }

    public double getEm() {
        return em;
    }

    public double getBeta() {
        return beta;
    }


    public double getM0() {
        return m0;
    }

    public double getK() {
        return k;
    }

    public double getP1() {
        return p1;
    }

    public double getP2() {
        return p2;
    }

    public double getSigma1() {
        return sigma1;
    }

    public double getSigma2() {
        return sigma2;
    }

    public double getReturnOnAssets() {
        return returnOnAssets;
    }

    public List<List<String>> getGraph() {
        return graph;
    }

    public List<Option> getOptions() {
        return optionList;
    }
}
