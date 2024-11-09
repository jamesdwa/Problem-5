import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class StableMatching {
    /* I knew they didn't accept it because it was wrong. It was too general. You pay more because it's more credit per credit. It's less.
     * Generates a stable matching.
     * 
     * @param prefHorses Preferences of the horses. prefHorses[i] lists the indices
     * of the riders that the i-th horse prefers, in order of preference.
     * 
     * @param prefRiders Preferences of the riders. prefHorses[i] lists the
     * indices of the horses that the i-th rider prefers, in order of preference.
     * 
     * @param horseOptimal if true, the generated stable matching should be most
     * optimal for the horses. Otherwise, it should be most optimal for the riders.
     * 
     * @return Computed stable matching. It is a 1D array, where arr[i]=j means the
     * i-th horse is matched to the j-th rider.
     */
    public static int[] findStableMatching(int[][] prefHorses, int[][] prefRiders, boolean horseOptimal) {
        assert prefHorses.length == prefRiders.length;
        
        int[] result = { -1 };
        if (horseOptimal == true) {
            result = computedStableMatching(prefHorses, prefRiders, horseOptimal);
        } else {
            result = computedStableMatching(prefRiders, prefHorses, horseOptimal);
        }
        return result;
    }

    /*
     * Generates a stable matching that is horse-optimal.
     * 
     * @param proposers Preferences of the proposers. proposers[i] lists the indices
     * of the choosers that the i-th proposers prefers, in order of preference.
     * 
     * @param choosers Preferences of the choosers. choosers[i] lists the
     * indices of the proposers that the i-th choosers prefers, in order of preference.
     * 
     * @param proposerOptimal if true, the generated stable matching should be most
     * optimal for the proposers. Otherwise, it should be most optimal for the choosers.
     * 
     * @return Computed stable matching. It is a 1D array, where arr[i]=j means the
     * i-th horse is matched to the j-th rider.
     */
    private static int[] computedStableMatching(int[][] proposers, int[][] choosers, boolean proposerOptimal) {
        // Maintains partial matching
        int[] tempProp = new int [proposers.length];
        int[] tempChoos = new int [choosers.length];

        // Preferences for the proposers
        int[] prefProp = new int [proposers.length];
        
        // Initialize arrays with -1 to represent a value that hasn't been matched yet
        Arrays.fill(tempProp, -1);
        Arrays.fill(tempChoos, -1);
        Arrays.fill(prefProp, -1);

        // Queue to maintain free proposers
        Queue<Integer> freeProp = new LinkedList<Integer>();
        for (int i = 0; i <  choosers.length; i++) {
            freeProp.add(i); 
        
        }
        
        while (!freeProp.isEmpty()) {
            int currProp = freeProp.poll();
            
            // Iterates through the preferences of the following proposer
            for (int i = prefProp[currProp] + 1; i < choosers.length; i++) {
                // Preferences of the current proposer
                int prefCurrProp = proposers[currProp][i];
                
                if (tempChoos[prefCurrProp] == -1) { 
                    // Assign the chooser to the proposer
                    tempProp[currProp] = prefCurrProp;
                    // Assign the proposer to the chooser
                    tempChoos[prefCurrProp] = currProp;
                    // Adds last preference that has been successfully assigned to a proposer
                    prefProp[currProp] = i;
                    break;
                } else {
                    for (int j = 0; j < choosers.length; j++) {
                        // Checks whether the current chooser's preference is current proposer
                        if (choosers[prefCurrProp][j] == currProp) { 
                            // Adds the current proposer to the queue of free proposers
                            freeProp.add(tempChoos[prefCurrProp]);
                            // Assign the chooser to the proposer
                            tempProp[currProp] = prefCurrProp;
                            // Assign the proposer to the chooser
                            tempChoos[prefCurrProp] = currProp;
                            
                            // Find the next available preference for the current proposer in the list of preferences
                            prefProp[currProp] = findPreference(proposers[currProp], prefCurrProp);

                            // Terminates the outer loop to ensure that the current proposer's matching process is considered complete
                            i = choosers.length;
                            break;
                        }
                        // Checks if the partial choosers are matched to the proposer's preference
                        if (choosers[prefCurrProp][j] == tempChoos[prefCurrProp]) {
                            break;
                        }
                    }
                }
            }
        }

        if (proposerOptimal == true) {
            return tempProp;
        } else {
            return tempChoos;
        }
    }

    // Private method to find the index of a preference in the preferences array
    private static int findPreference(int[] pref, int prefCurrProp) {
        if (pref == null) {
            return -1;
        }

        int len = pref.length;
        int i = 0;

        // Iterate through preferences array to find the index of the current chooser        
        while (i < len) {
            if (pref[i] == prefCurrProp) {
                return i;
            }
            else {
                i++;
            } 
        }
        return -1;
    }

    /*
     * A short sanity check is provided to help you see what the input and output
     * look like :)
     * 
     * You can also modify the provided main method for your own test cases. This
     * method will not be graded.
     */
    public static void main(String[] args) {
        int[][] prefHorses = { { 0, 1 }, // Preferences of h0
                { 1, 0 }, // Preferences of h1
        };
        int[][] prefRiders = { { 0, 1 }, // Preferences of r0
                { 1, 0 }, // Preferences of r1
        };

        System.out.printf("Horse-optimal: ");
        System.out.println(Arrays.toString(findStableMatching(prefHorses, prefRiders, true)));
        System.out.printf("Rider-optimal: ");
        System.out.println(Arrays.toString(findStableMatching(prefHorses, prefRiders, false)));
    }
}