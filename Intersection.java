/**
 * This Intersection class represents a crossing of two or more roads at a stop
 * light in our simulation.
 */
public class Intersection {

    /**
     * The maximum amount of roads the intersection can have.
     */
    private int MAX_ROADS = 4;

    /**
     * Array of roads which cross at this intersection.
     */
    private TwoWayRoad[] roads;

    /**
     * Indicates the road in roads with the active light.
     */
    private int lightIndex;

    /**
     * Tracks the remaining time steps available for the road currently
     * indicated by lightIndex.
     */
    private int countdownTimer;

    /**
     * Default constructor.
     * @param initRoads
     *      The array of roads used to initialized instance variable roads.
     * @throws IllegalArgumentException
     *      Indicates that initRoads is null, any index of initRoads is null,
     *      or initRoads.length > MAX_ROADS.
     */
    public Intersection(TwoWayRoad[] initRoads) {
        boolean allNull = false;
        try {
            if (initRoads == null || initRoads.length > MAX_ROADS)
                throw new IllegalArgumentException();

            for (int i = 0; i < initRoads.length; i++) {
                if (initRoads[i] == null)
                    allNull = true;
            }

            if (allNull)
                throw new IllegalArgumentException();

            roads = initRoads;
            lightIndex = 0;
            countdownTimer = roads[lightIndex].getGreenTime();
        }
        catch(IllegalArgumentException e) {
            System.out.println("Cannot construct intersection:");
            if (initRoads == null)
                System.out.println("initRoads is null.");
            else if (allNull == true)
                System.out.println("An index of initRoads is null.");
            else if (initRoads.length > MAX_ROADS)
                System.out.println("initRoads.length > MAX_ROADS");
        }
    }

    /**
     * Performs a single iteration through the intersection.
     * @return
     *      An array of Vehicles which have passed through the intersection
     *      during this time step.
     */
    public Vehicle[] timeStep() {
        //Used if there are no cars in any of the lanes
        //When the whole array is checked, stop while loop
        if (countdownTimer == 0) {
            lightIndex++;
            if (lightIndex == roads.length)
                lightIndex = 0;
            countdownTimer = roads[lightIndex].getGreenTime();
        }
        Vehicle[] removedCars = roads[lightIndex].proceed(countdownTimer);
        //Loop array until there are cars or until we get back to this index
        int currentIndex = lightIndex;
        int currentTimer = countdownTimer;
        while (removedCars == null) {
            lightIndex++;
            if (lightIndex == roads.length)
                lightIndex = 0;
            //This means we looped through the whole array and still no cars
            if (lightIndex == currentIndex) {
                //Set countdownTimer since it still used up a timeStep
                countdownTimer = currentTimer - 1;
                return null;
            }
            countdownTimer = roads[lightIndex].getGreenTime();
            removedCars = roads[lightIndex].proceed(countdownTimer);
        }
        countdownTimer--;
        return removedCars;
    }

    /**
     * Enqueues a vehicle onto a lane in the intersection.
     * @param roadIndex
     *      Index of the road in roads which contains the lane to enqueue onto.
     * @param wayIndex
     *      Index of the direction the vehicle is headed.
     *      Can be either TwoWayRoad.FORWARD or TwoWayRoad.BACKWARD.
     * @param laneIndex
     *      Index of the lane on which the vehicle is to be enqueued.
     *      Can be TwoWayRoad.RIGHT_LANE, TwoWayRoad.MIDDLE_LANE, or
     *      TwoWayRoad.LEFT_LANE.
     * @param vehicle
     *      The Vehicle to enqueue onto the lane.
     * @throws IllegalArgumentException
     *      Indicates that vehicle is null or any of the index parameters are
     *      not within the valid range.
     */
    public void enqueueVehicle(int roadIndex, int wayIndex, int laneIndex,
      Vehicle vehicle) {
        try {
            if (wayIndex > 1 || wayIndex < 0 || laneIndex < 0 || laneIndex > 2
              || vehicle == null || roadIndex < 0 || roadIndex > roads.length)
                throw new IllegalArgumentException();
            roads[roadIndex].enqueueVehicle(wayIndex, laneIndex, vehicle);
        }
        catch(IllegalArgumentException e) {
            System.out.println("Cannot enqueue vehicle onto lane. Vehicle" +
              " is null or indexes not in valid range");
        }
    }

    /**
     * Prints the intersection to the terminal in a neatly formatted manner.
     */
    public void display() {
        //Loops through each TwoWayRoad
        for (int i = 0; i < roads.length; i++) {
            System.out.println(roads[i].getName() + ":");
            System.out.println(roads[i]);
        }
    }

    /**
     * Returns the number of roads this intersection has.
     * @return
     *      The number of roads this intersection has.
     */
    public int getNumRoads() {
        return roads.length;
    }

    /**
     * Returns the index of the road with the active light,
     * can be either green or left turn signal.
     * @return
     *      The index of the road with the active light.
     */
    public int getLightIndex() {
        return lightIndex;
    }

    /**
     * Returns the remaining time steps available for the road currently
     * indicated by lightIndex.
     * @return
     *      The remaining time steps available for the road.
     */
    public int getCountdownTimer() {
        return countdownTimer;
    }

    /**
     * Returns the current light value of the active road.
     * @return
     *      The current light value of the active road.
     */
    public LightValue getCurrentLightValue() {
        return roads[lightIndex].getLightValue();
    }

    /**
     * Checks to see if every road is empty.
     * @return
     *      true if every road is empty, else false.
     */
    public boolean allRoadsEmpty() {
        for (int i = 0; i < roads.length; i++) {
            if (!roads[i].allLanesEmpty())
                return false;
        }
        return true;
    }

}
