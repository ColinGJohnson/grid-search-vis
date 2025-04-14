package dev.cgj.searchvis.grid;

import dev.cgj.searchvis.search.SearchAlgorithm;

public class GridSearchConfig {
    final SearchAlgorithm searchAlgorithm;

    public GridSearchConfig(SearchAlgorithm searchAlgorithm) {
        this.searchAlgorithm = searchAlgorithm;
    }

    public SearchAlgorithm getSearchAlgorithm() {
        return searchAlgorithm;
    }
}
