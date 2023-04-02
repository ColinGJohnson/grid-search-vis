package net.colinjohnson.vis.grid;

import net.colinjohnson.vis.search.SearchAlgorithm;

public class GridSearchConfig {
    SearchAlgorithm searchAlgorithm;

    public GridSearchConfig(SearchAlgorithm searchAlgorithm) {
        this.searchAlgorithm = searchAlgorithm;
    }

    public SearchAlgorithm getSearchAlgorithm() {
        return searchAlgorithm;
    }
}
