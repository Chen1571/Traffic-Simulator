/**
 * This TwoWayRoad class represents one of the roads in our intersection.
 */
public class TwoWayRoad {
    /**
     * Each road has 2 directions: forward and backward.
     */
    public final int FORWARD_WAY = 0;
    public final int BACKWARD_WAY = 1;
    public final int NUM_WAYS = 2;

    /**
     * Each road has 3 lanes: left, middle, and right.
     */
    public final int LEFT_LANE = 0;
    public final int MIDDLE_LANE = 1;
    public final int RIGHT_LANE = 2;
    public final int NUM_LANES = 3;

    /**
     * The name of the road.
     */
    private String name;

    /**
     * The maximum total number of steps this road can be active.
     * This number is inclusive of the leftSignalGreenTime.
     */
    private int greenTime;

    /**
     * The number of steps this road remains in the LEFT_SIGNAL state.
     */
    private int leftSignalGreenTime;

    /**
     * Stores the lanes of this road. 1st index indicates direction of
     * travel on the road and the 2nd index indicates the lane of the road.
     */
    private VehicleQueue lanes[][];

    /**
     * The current light value of this road.
     */
    private LightValue lightValue;

    /**
     * Default constructor.
     * All instance variables initialized and the road is initialized
     * with all lanes initialized to empty queues.
     * @param initName
     *      The name of this road.
     * @param initGreenTime
     *      The amount of time that the light will be active for this
     *      particular road. This is the total time the light should
     *      display green for cars going forward/right, as well as
     *      for cars going left.
     * @throws IllegalArgumentException
     *      Indicates that initGreenTime <= 0 or initName = null.
     */
    public TwoWayRoad(String initName, int initGreenTime) {
        try {
            if (initGreenTime <= 0 || initName == null)
                throw new IllegalArgumentException();

            //Array representation:
            //F Way: Left, Middle, Right
            //B way: Left, Middle, Right

            //2d Array of Vehicle Queues
            lanes = new VehicleQueue[NUM_WAYS][NUM_LANES];
            //Initialize array
            for (int i = 0; i < lanes.length; i++) {
                for (int j = 0; j < lanes[0].length; j++) {
                    lanes[i][j] = new VehicleQueue();
                }
            }

            name = initName;
            greenTime = initGreenTime;
            leftSignalGreenTime =
                    (int)(Math.floor((1.0/NUM_LANES) * initGreenTime));
            lightValue = LightValue.RED;
        }
        catch(IllegalArgumentException e) {
            System.out.println("Cannot construct TwoWayRoad. initGreenTime <= 0 or initName = null.");
        }
    }

    /**
     * Enqueues a vehicle into a specified lane.
     * @param wayIndex
     *      The direction the car is going in.
     * @param laneIndex
     *      The lane the car arrives in.
     * @param vehicle
     *      The vehicle to enqueue.
     * @throws IllegalArgumentException
     *      If wayIndex or laneIndex are not in the appropriate bounds
     *      or Vehicle is null.
     */
    public void enqueueVehicle(int wayIndex, int laneIndex, Vehicle vehicle) {
        try {
            if (wayIndex > 1 || wayIndex < 0 || laneIndex < 0
              || laneIndex > 2 || vehicle == null)
                throw new IllegalArgumentException();
            lanes[wayIndex][laneIndex].enqueue(vehicle);
        }
        catch(IllegalArgumentException e) {
            System.out.println("Cannot enqueue vehicle. " +
              "Indexes out of range or vehicle is null.");
        }
    }

    /**
     * Executes the passage of time in the simulation. The light should be
     * in state GREEN any time timerVal >= leftSignalGreenTime. When
     * timerVal <= leftSignalGreenTime, the light should change to LEFT_SIGNAL.
     * After the execution of timerVal == 1 or if there are no vehicles left,
     * the light should change to RED.
     * @param timerVal
     *      The current value of a countdown timer counting down total green
     *      time steps.
     * @return
     *      Indicates that timerVal <= 0.
     */
    public Vehicle[] proceed(int timerVal) {
        try {
            if (timerVal <= 0)
                throw new IllegalArgumentException();
            lightValue = lightValue.GREEN;
            boolean leftEmpty = true;
            boolean midRightEmpty = true;
            //Loops through number of rows
            for (int i = 0; i < lanes.length; i++) {
                //Loops through number of columns
                for (int j = 0; j < lanes[0].length; j++) {
                    //j == 0 refers to left lane, checks to see if its empty
                    if (!lanes[i][j].isEmpty() && j == 0)
                        leftEmpty = false;
                        //If any other lane is empty
                    else if (!lanes[i][j].isEmpty())
                        midRightEmpty = false;
                }
            }
            if (timerVal <= leftSignalGreenTime || midRightEmpty) {
                lightValue = LightValue.LEFT_SIGNAL;
                //If leftLane empty, change light to red
                if (leftEmpty) {
                    //Will return null indicating to increment lightIndex
                    lightValue = LightValue.RED;
                }
            }
            //ANY lane can only be enqueued and dequeued once
            //Max. 6 can be enqueued while Max. 4 can be dequeued
            //Ex. 4 Green: F.Mid, F.Right, B.Mid, B.Right
            //Ex. 2 Left: F.Left, B.Left

            //If the light will be green
            if (lightValue == LightValue.GREEN) {
                Vehicle[] output = new Vehicle[4];
                //Represents the index we are on in the output array
                int vehicleIndex = 0;
                //First loop loops through the rows, lanes.length is # of rows
                for (int i = 0; i < lanes.length; i++) {
                    //Second loop loops through the columns, initial 1 because
                    // we want to skip the first column (left lane)
                    for (int j = 1; j < lanes[0].length; j++) {
                        if (!lanes[i][j].isEmpty()) {
                            output[vehicleIndex] = lanes[i][j].dequeue();
                            vehicleIndex++;
                        }
                    }
                }
                if (timerVal == 1)
                    lightValue = LightValue.RED;
                return output;
            }
            //If the light will be left
            else if (lightValue == LightValue.LEFT_SIGNAL) {
                Vehicle[] output = new Vehicle[2];
                //Represents the index we are on in the output array
                int vehicleIndex = 0;
                //First loop loops through the rows, lanes.length is # of rows
                for (int i = 0; i < lanes.length; i++) {
                    //Second loop loops through columns, stops at 1 because we only
                    // want to loop the first column
                    for (int j = 0; j < 1; j++) {
                        if (!lanes[i][j].isEmpty()) {
                            output[vehicleIndex] = lanes[i][j].dequeue();
                            vehicleIndex++;
                        }
                    }
                }
                if (timerVal == 1)
                    lightValue = LightValue.RED;
                return output;
            }
            return null;
        }
        catch(IllegalArgumentException e) {
            System.out.println("Cannot proceed()! timerVal <= 0");
            return null;
        }
    }

    /**
     * Checks if specified lane is empty.
     * @param wayIndex
     *      The direction of the lane.
     * @param laneIndex
     *      The index of the lane to check.
     * @return
     *      true if the lane is empty, else false.
     * @throws IllegalArgumentException
     *      Indicates that wayIndex or laneIndex is out of range.
     */
    public boolean isLaneEmpty(int wayIndex, int laneIndex) {
        try {
            if (wayIndex > 1 || wayIndex < 0 || laneIndex < 0 || laneIndex > 2)
                throw new IllegalArgumentException();
            return lanes[wayIndex][laneIndex].isEmpty();
        }
        catch(IllegalArgumentException e) {
            System.out.println("Lane empty indexes out of range.");
        }
        return false;
    }

    /**
     * Returns the maximum total number of steps this road can be active.
     * @return
     *      The maximum total number of steps this road can be active.
     */
    public int getGreenTime() {
        return greenTime;
    }

    /**
     * Returns the current light value of this road.
     * @return
     *      The current light value of this road.
     */
    public LightValue getLightValue() {
        return lightValue;
    }

    /**
     * Returns the name of this TwoWayRoad.
     * @return
     *      The name of this TwoWayRoad.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the string representation of this TwoWayRoad class.
     * @return
     *      The string representation of this TwoWayRoad class.
     */
    public String toString() {
        String output = "";
        //2nd index of 2d array represents the lane of the
        String[] laneSymbols = new String[]{" [L] "," [M] "," [R] "};
        //We start at index 2 to print the right roads because order reversed
        int rightIndex = 2;
        //Basically, print FORWARD lanes 0,1,2 and BACKWARD lanes 2,1,0

        output += " ".repeat(23) + "FORWARD";
        //Gap in the middle is 15 units
        output += " ".repeat(15) + "BACKWARD\n";
        output += "=".repeat(30) + " ".repeat(14) + "=".repeat(31) + "\n";
        //Loops 3 times to print the 3 lanes on both sides
        for (int i = 0; i < 3; i++) {
            String currentLane = "";
            //Gets String representation of a FORWARD lane
            //Front is towards the right
            for (int j = lanes[0][i].size() - 1; j >= 0; j--)
                currentLane += lanes[0][i].get(j);
            //Without dash, right justify
            output += String.format("%30s", currentLane) + laneSymbols[i] + xStr(i);

            output += "   "; //PRINTING RIGHT SIDE OF LINE NOW

            //Reset currentLane for backward way printing
            currentLane = "";
            //Gets String representation of a BACKWARD lane
            //Front is towards to left
            for (int j = 0; j < lanes[1][rightIndex].size(); j++)
                currentLane += lanes[1][rightIndex].get(j);
            output += xStr(rightIndex) + laneSymbols[rightIndex] + String.format("%-30s", currentLane) + "\n";
            if (i != 2)
                output += "-".repeat(30) + " ".repeat(14) + "-".repeat(31) + "\n";
            rightIndex--;
        }
        output += "=".repeat(30) + " ".repeat(14) + "=".repeat(31) + "\n";

        return output;
    }

    /**
     * Returns a string containing a 'x' or an empty space.
     * This is determined based on the current light value
     * and the lane.
     * @param lane
     *      The lane that this string should apply to.
     * @return
     *      A string that is either a 'x' or an empty space.
     */
    public String xStr(int lane) {
        //Returns if there should be a x printed regarding a lane
        String output = "";
        String[] xGreen = new String[]{"x"," "," "};
        String[] xLeft = new String[]{" ","x","x"};
        String[] xRed = new String[]{"x","x","x"};
        if (lightValue == LightValue.GREEN) {
            output += xGreen[lane];
        }
        else if (lightValue == LightValue.LEFT_SIGNAL) {
            output += xLeft[lane];
        }
        else if (lightValue == LightValue.RED) {
            output += xRed[lane];
        }
        return output;
    }

    /**
     * Checks to see if all the lanes are empty.
     * @return
     *      true if all lanes are empty, else falase.
     */
    public boolean allLanesEmpty() {
        for (int i = 0; i < lanes.length; i++) {
            for (int j = 0; j < lanes[0].length; j++) {
                if (!lanes[i][j].isEmpty())
                    return false;
            }
        }
        return true;
    }

}
