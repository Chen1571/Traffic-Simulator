/**
 * This Vehicle class represents a car which passes through the intersection.
 * Each instance contains a serialId (first car to arrive at the intersection
 * is serialId 1, 2nd is 2, n'th car to arrive is n stored as an int). The car
 * must be initialized with a serialId and the time it arrived.
 *
 * The vehicle class is immutable (no data within the instance can be changed
 * once it is constructed) but data can be read with getter methods.
 */
public class Vehicle {

    /**
     * The number of vehicles that have arrived at the intersection so far.
     */
    private static int serialCounter = 0;

    /**
     * The first car to arrive at the intersection is serialId 1, 2nd car is 2,
     * n'th car to arrive is n.
     */
    private int serialId;

    /**
     * The time the vehicle arrived at the intersection;
     */
    private int timeArrived;

    /**
     * Default constructor.
     * The Vehicle's serialId and timeArrived is logged and serialCounter
     * is incremented.
     * @param initTimeArrived
     *      Time the vehicle arrived at the intersection.
     * @throws IllegalArgumentException
     *      Indicates that initTimeArrived < 0.
     */
    public Vehicle(int initTimeArrived) {
        try {
            if (initTimeArrived <= 0)
                throw new IllegalArgumentException();
            timeArrived = initTimeArrived;
            serialId = serialCounter + 1;
            serialCounter++;
        }
        catch(IllegalArgumentException e) {
            System.out.println("Vehicle timeArrived <= 0");
        }

    }

    /**
     * Returns the serial ID of this Vehicle.
     * @return
     *      The serialId of this Vehicle.
     */
    public int getSerialId() {
        return serialId;
    }

    /**
     * Returns the time this Vehicle arrived.
     * @return
     *      The time this Vehicle arrived.
     */
    public int getTimeArrived() {
        return timeArrived;
    }

    /**
     * The string representation of this Vehicle class.
     * @return
     *      The string representation of this Vehicle class.
     */
    public String toString() {
        String output = "";
        if (serialId < 10)
            output += "[00" + serialId + "]";
        else if (serialId < 100)
            output += "[0" + serialId + "]";
        else
            output += "[" + serialId + "]";
        return output;
    }

    /**
     * Returns the number of vehicles that have arrived at the intersection
     * so far (serialCounter).
     * @return
     *      The number of vehicles that have arrived at the intersection so far.
     */
    public static int getSerialCounter() {
        return serialCounter;
    }
}
