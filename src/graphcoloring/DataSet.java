package graphcoloring;

/**
 * Klasa reprezentująca zestaw danych wejściowych
 *
 * @author Piotr Bednarz
 * @author Piotr Bajorek
 */
public enum DataSet {

    /**
     * Graf o 7 wierzchołkach
     */
    DATA1("data/data1.txt"),

    /**
     * Drzewo binarne o rozmiarze 3 i wysokości 1
     */
    BINARY_TREE_3("data/drzewo_binarne_3.txt"),

    /**
     * Graf regularny, 2 stopnia, cykliczny o parzystej liczbie wierzchołków
     */
    GRAF_CYKLICZNY_8("data/graf_cykliczny_8.txt"),

    /**
     * Graf regularny, 2 stopnia, cykliczny o nieparzystej liczbie wierzchołków
     */
    GRAF_CYKLICZNY_9("data/graf_cykliczny_9.txt"),

    /**
     * http://mat.gsia.cmu.edu/COLOR/instances/myciel3.col
     */
    MYCIEL3("data/myciel3.txt"),

    /**
     * http://mat.gsia.cmu.edu/COLOR/instances/myciel4.col
     */
    MYCIEL4("data/myciel4.txt"),

    /**
     * http://mat.gsia.cmu.edu/COLOR/instances/myciel5.col
     */
    MYCIEL5("data/myciel5.txt"),

    /**
     * http://mat.gsia.cmu.edu/COLOR/instances/myciel6.col
     */
    MYCIEL6("data/myciel6.txt"),

    /**
     * http://mat.gsia.cmu.edu/COLOR/instances/games120.col
     * Graph representing the games played in a college football season can be represented by a graph where the nodes represent each college team. Two teams are connected by an edge if they played each other during the season.
     */
    GAMES120("data/games120.txt"),

    /**
     * Queen Graphs. Given an n by n chessboard, a queen graph is a graph on n^2 nodes, each corresponding to a square of the board.
     * Two nodes are connected by an edge if the corresponding squares are in the same row, column, or diagonal.
     * Unlike some of the other graphs, the coloring problem on this graph has a natural interpretation:
     * Given such a chessboard, is it possible to place n sets of n queens on the board so that no two queens of the same
     * set are in the same row, column, or diagonal? The answer is yes if and only if the graph has coloring number n.
     * Martin Gardner states without proof that this is the case if and only if $n$ is not divisible by either 2 or 3. In all cases,
     * the maximum clique in the graph is no more than n, and the coloring value is no less than n.
     */
    /**
     * http://mat.gsia.cmu.edu/COLOR/instances/queen6_6.col
     */
    QUEEN6_6("data/queen6_6.txt"),

    /**
     * http://mat.gsia.cmu.edu/COLOR/instances/queen8_8.col
     */
    QUEEN8_8("data/queen8_8.txt"),

    /**
     * Graf dwudzielny
     */
    GRAF_DWUDZIELNY_6("data/graf_dwudzielny_6.txt"),

    /**
     * Graf pełny
     */
    GRAF_PELNY_5("data/graf_pelny_5.txt");

    private final String filename;

    DataSet(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
}