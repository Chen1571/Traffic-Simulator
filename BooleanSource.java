/**
 * This BooleanSource class abstracts a random occurrence generator.
 */
public class BooleanSource {

    /**
     * The initial likelihood that a Vehicle will arrive at any particular lane
     * at the beginning of each time step.
     * 0.0 < probability <= 1.0.
     */
    private double probability;

    /**
     * Default constructor which initializes the probability to the indicated
     * parameter.
     * @param initProbability
     *      Probability used to construct this BooleanSource object.
     *      0 < initProbability <= 1.
     */
    public BooleanSource(double initProbability) {
        try {
            if (initProbability <= 0 || initProbability > 1)
                throw new IllegalArgumentException();

            probability = initProbability;
        }
        catch(IllegalArgumentException e) {
            System.out.println("Illegal probability");
        }

    }

    /**
     * A method which returns true with the probability indicated by the
     * member variable probability.
     * @return
     *      Boolean value indicating whether an event occured or not.
     */
    public boolean occurs() {
        return Math.random() < probability;
    }

    /**
     * Sets the probability to newProb.
     * @param newProb
     *      The new value the member variable probability should be
     *      set to.
     */
    public void setProb(double newProb) {
        probability = newProb;
    }

    /**
     * Returns the probability of this BooleanSource.
     * @return
     *      The probability of this BooleanSource.
     */
    public double getProb() {
        return probability;
    }

}
