package graphcoloring;

public interface CrossoverName {
	 /** one point crossover */
    public static final String ctOnePoint = "One Point";
    /** two point crossover */
    public static final String ctTwoPoint = "Two point";
    /** uniform crossover */
    public static final String ctUniform = "Uniform";
    /** roulette crossover (either one point, two point, or uniform) */
    public static final String ctRoulette = "Roulette";
}
