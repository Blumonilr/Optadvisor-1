package utf8.optadvisor.domain.response;

import java.util.List;

import utf8.optadvisor.domain.entity.Portfolio;

public class MyCombinationResponse {
    private List<Portfolio> portfolios;
    private String[][] graph;
    private String[][] assertPrice2Profit;
    private String[][] profit2Probability;
    private String[][] historyProfit2Probability;

    public String[][] getGraph() {
        return graph;
    }

    public void setGraph(String[][] graph) {
        this.graph = graph;
    }

    public List<Portfolio> getPortfolios() {
        return portfolios;
    }

    public void setPortfolios(List<Portfolio> portfolios) {
        this.portfolios = portfolios;
    }

    public String[][] getAssertPrice2Profit() {
        return assertPrice2Profit;
    }

    public void setAssertPrice2Profit(String[][] assertPrice2Profit) {
        this.assertPrice2Profit = assertPrice2Profit;
    }

    public String[][] getProfit2Probability() {
        return profit2Probability;
    }

    public void setProfit2Probability(String[][] profit2Probability) {
        this.profit2Probability = profit2Probability;
    }

    public String[][] getHistoryProfit2Probability() {
        return historyProfit2Probability;
    }

    public void setHistoryProfit2Probability(String[][] historyProfit2Probability) {
        this.historyProfit2Probability = historyProfit2Probability;
    }
}
