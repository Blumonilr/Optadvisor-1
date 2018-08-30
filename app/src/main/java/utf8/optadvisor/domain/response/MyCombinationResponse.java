package utf8.optadvisor.domain.response;

import java.util.List;

import utf8.optadvisor.domain.entity.Portfolio;

public class MyCombinationResponse {
    private List<Portfolio> portfolios;
    private String[][] graph;

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
}
