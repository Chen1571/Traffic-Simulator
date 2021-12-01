import java.util.Scanner;
import java.util.ArrayList;

/**
 * This IntersectionSimulator represents the manager of the simulation.
 */
public class IntersectionSimulator {

    /**
     * Starts the application and asks user for following values:
     * simulationTime (int), arrivalProbability (double), numRoads (int),
     * a name for each road, and a "green" time for each road.
     * This method also parses command line for these args.
     * @param args
     *      The args to be used for the values.
     */
    public static void main(String[] args) {
        //If there are args, else do interactive
        if (args.length > 1) {
            int simTime = Integer.parseInt(args[0]);
            double prob = Double.parseDouble(args[1]);
            int numRoads = Integer.parseInt(args[2]);
            String[] names = new String[numRoads];
            int[] times = new int[numRoads];
            for (int i = 0; i < numRoads; ++i) {
                names[i] = args[3+i];
                times[i] = Integer.parseInt(args[3 + numRoads + i]);
            }
            simulate(simTime,prob,names,times);
        }
        else {
            Scanner sc = new Scanner(System.in);
            boolean stillInputting = true;
            while (stillInputting) {
                try {
                    System.out.print("Input the simulation time: ");
                    int simTime = sc.nextInt();
                    System.out.print("Input the arrival probability: ");
                    double simProb = sc.nextDouble();
                    System.out.print("Input number of streets: ");
                    int numStreets = sc.nextInt();
                    //Consumes the empty line from nextInt
                    sc.nextLine();
                    ArrayList<String> streetNames = new ArrayList<String>();
                    for (int i = 0; i < numStreets; i++) {
                        System.out.print("Input Street " + (i+1) + " name: ");
                        String curStreet = sc.nextLine();
                        while (streetNames.contains(curStreet)) {
                            System.out.println("Duplicate Detected.");
                            System.out.print("Input Street "
                              + (i+1) + " name: ");
                            curStreet = sc.nextLine();
                        }
                        streetNames.add(curStreet);
                    }
                    int[] allGTimes = new int[streetNames.size()];
                    for (int i = 0; i < numStreets; i++) {
                        System.out.print("Input max green time for "
                          + streetNames.get(i) + ": ");
                        int curGTime = sc.nextInt();
                        allGTimes[i] = curGTime;
                    }
                    String[] namesArray = new String[streetNames.size()];
                    for (int i = 0; i < streetNames.size(); i++)
                        namesArray[i] = streetNames.get(i);
                    stillInputting = false;
                    simulate(simTime,simProb,namesArray,allGTimes);
                }
                catch(Exception e) {
                    System.out.println("Invalid Input");
                    //Consumes the empty line from nextInt
                    sc.nextLine();
                }
            }
        }
    }

    /**
     * This method does the actual simulation and implements the algorithm
     * described by the "activity" diagram.
     * @param simulationTime
     *      The simulation time for this simulation (how many timeSteps
     *      can cars arrive).
     * @param arrivalProbability
     *      The probability used to check if a car arrives.
     * @param roadNames
     *      An array of Strings representing the names of each road.
     *      Must be equal to the number of roads.
     * @param maxGreenTimes
     *      An array of ints representing the "green" times for each road.
     *      Must be equal to the number of roads.
     */
    public static void simulate(int simulationTime, double arrivalProbability,
      String[] roadNames, int[] maxGreenTimes) {
        //Initializes simulation time and Boolean source
        int simTime = simulationTime;
        BooleanSource chSource = new BooleanSource(arrivalProbability);
        //roadNames length should be equal to maxGreenTimes
        TwoWayRoad[] roadList = new TwoWayRoad[roadNames.length];
        //Loops and makes the TwoWayRoad array
        for (int i = 0; i < roadNames.length; i++) {
            roadList[i] = new TwoWayRoad(roadNames[i], maxGreenTimes[i]);
        }

        //Makes intersection *****
        Intersection mainCrossway = new Intersection(roadList);

        //Keeps track of current time step
        int timeStep = 1;
        //Uses to list name of road to user
        String[] wayNames = new String[]{"FORWARD","BACKWARD"};;
        String[] laneNames = new String[]{"LEFT","MIDDLE","RIGHT"};;
        //Stores variables needed to print simulation summary
        int carsPassed = 0;
        int carsCurrently = 0;

        int maxWaitTime = 0;
        int totalWaitTime = 0;

        System.out.println("\nStarting Simulation...\n");
        //Main loop for simulation
        while (timeStep <= simulationTime || !mainCrossway.allRoadsEmpty()) {
            int carsQueued = 0;
            String printArrivals = "";
            //Keeps track of how many cars arrived/passed during this time step.
            if (timeStep <= simulationTime) {
                //CARS ARRIVE**
                //Store in String so we can print after the timeStep() operation
                printArrivals += "ARRIVING CARS:\n";
                //Calls occurs() for each road
                //roadNames.length represents how many roads there are
                for (int i = 0; i < roadNames.length; i++) {
                    for (int way = 0; way < 2; way++) {
                        for (int lane = 0; lane < 3; lane++) {
                            //If occurs, enqueue and store string to print later
                            if (chSource.occurs()) {
                                Vehicle newCar = new Vehicle(timeStep);
                                mainCrossway.enqueueVehicle(i,way,lane,newCar);
                                printArrivals += "    Car" + newCar +
                                  " entered " + roadNames[i] + ", going " +
                                  wayNames[way] + " in " + laneNames[lane] +
                                  " lane.\n";
                                carsQueued++;
                            }
                        }
                    }
                }
                carsCurrently += carsQueued;
            }
            else {
                printArrivals += "Cars no longer arriving.\n\n";
                printArrivals += "ARRIVING CARS:\n";
            }

            //MAIN TIMESTEP METHOD***********************
            Vehicle[] removedCars = mainCrossway.timeStep();

            //Initialize some variables
            int curIndex = mainCrossway.getLightIndex();
            String lightPrint = "" + mainCrossway.getCurrentLightValue();
            if (lightPrint.equals("RED") ||
              lightPrint.equals("LEFT_SIGNAL")) {
                lightPrint = "Left Signal";
            }
            else if (lightPrint.equals("GREEN")) {
                lightPrint = "Green Light";
            }
            if (carsQueued == 0 && removedCars == null) {
                lightPrint = "Red Light";
            }
            int lightTimer = mainCrossway.getCountdownTimer();

            //PRINT HEADER
            System.out.println("#".repeat(80) + "\n");
            System.out.println("Time step: " + timeStep + "\n");
            System.out.println(lightPrint + " for "
              + roadNames[curIndex] + ".");;
            System.out.println("Timer = " + (lightTimer + 1) + "\n");

            System.out.println(printArrivals);
            System.out.println("PASSING CARS: ");
            if (removedCars != null) {
                for (int i = 0; i < removedCars.length; i++) {
                    if (removedCars[i] != null) {
                        int waitTime =
                          timeStep - removedCars[i].getTimeArrived();
                        System.out.println("    Car" + removedCars[i]
                          + " passes through. Wait time of "
                          + waitTime + ".");
                        totalWaitTime += waitTime;
                        if (waitTime > maxWaitTime)
                            maxWaitTime = waitTime;
                        carsPassed++;
                        carsCurrently--;
                    }
                }
                System.out.println();
            }

            mainCrossway.display();

            //Time step increments at the end
            timeStep++;

            double currentAvgTime = (double)totalWaitTime / carsPassed;
            currentAvgTime = (Math.round(currentAvgTime * 100.0)) / 100.0;
            System.out.println("STATISTICS:");
            System.out.printf("    "
              + String.format("%-25s","Cars currently waiting:")
              + carsCurrently + " cars\n");
            System.out.printf("    "
              + String.format("%-25s","Total cars passed:")
              + carsPassed + " cars\n");
            System.out.printf("    "
              + String.format("%-25s","Total wait time:")
              + totalWaitTime + " turns\n");
            System.out.printf("    "
              + String.format("%-25s","Average wait time:")
              + currentAvgTime + " turns\n\n");

        }

        double avgWaitTime = (double)totalWaitTime / Vehicle.getSerialCounter();
        avgWaitTime = (Math.round(avgWaitTime * 100.0)) / 100.0;

        System.out.println((("#".repeat(80) + "\n")).repeat(3));
        System.out.println("SIMULATION SUMMARY\n");
        System.out.printf("    "
          + String.format("%-22s","Total Time:")
          + (timeStep - 1) + " steps\n");
        System.out.printf("    "
          + String.format("%-22s","Total vehicles:")
          + Vehicle.getSerialCounter() + " vehicles\n");
        System.out.printf("    "
          + String.format("%-22s","Longest wait time:")
          + maxWaitTime + " turns\n");
        System.out.printf("    "
          + String.format("%-22s","Total Time:")
          + totalWaitTime + " turns\n");
        System.out.printf("    "
          + String.format("%-22s","Average wait time:")
          + String.format("%.2f", avgWaitTime) + " turns\n\n");
        System.out.println("End simulation.");

    }
}
