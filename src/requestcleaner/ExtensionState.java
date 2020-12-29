package requestcleaner;

import burp.IBurpExtenderCallbacks;

import java.util.HashMap;
import java.util.Map;


/*
This stores our state for the extension as a Singleton
 */
public class ExtensionState {

    //State object
    private static ExtensionState instance = null;
    //Burp callbacks
    private IBurpExtenderCallbacks callbacks;
    //Number of current cleaned requests
    private int numberOfCleanedRequests;
    //User defined Shannon Entropy level for headers
    private int headerEntropyLevel;
    //User defined Shannon Entropy level for parameters
    private int parameterEntropyLevel;
    //Reference to the UI panel
    private final BurpCleanerPanel teamPanel;


    private ExtensionState() {
        headerEntropyLevel = 3;
        parameterEntropyLevel = 3;
        numberOfCleanedRequests = 0;
        teamPanel = new BurpCleanerPanel();
    }

    public static void setCallbacks(IBurpExtenderCallbacks callbacks) {
        getInstance().callbacks = callbacks;
    }

    public static IBurpExtenderCallbacks getCallbacks() {
        return getInstance().callbacks;
    }

    public BurpCleanerPanel getCleanerPanel() {
        return getInstance().teamPanel;
    }

    private static double log2(double a) {
        return Math.log(a) / Math.log(2);
    }
    //Shannon Entropy taken from here: https://rosettacode.org/wiki/Entropy#Java
    public static double getShannonEntropy(String s) {
        int n = 0;
        Map<Character, Integer> occ = new HashMap<>();

        for (int c_ = 0; c_ < s.length(); ++c_) {
            char cx = s.charAt(c_);
            if (occ.containsKey(cx)) {
                occ.put(cx, occ.get(cx) + 1);
            } else {
                occ.put(cx, 1);
            }
            ++n;
        }

        double e = 0.0;
        for (Map.Entry<Character, Integer> entry : occ.entrySet()) {
            double p = (double) entry.getValue() / n;
            e += p * log2(p);
        }
        return -e;
    }

    public static ExtensionState getInstance() {
        if(instance==null) {
            instance = new ExtensionState();
        }
        return instance;
    }

    public int getNumberOfCleanedRequests() {
        return numberOfCleanedRequests;
    }

    public void setNumberOfCleanedRequests(int numberOfCleanedRequests) {
        this.numberOfCleanedRequests = numberOfCleanedRequests;
    }

    public int getHeaderEntropyLevel() {
        return headerEntropyLevel;
    }

    public void setHeaderEntropyLevel(int entropyLevel) {
        this.headerEntropyLevel = entropyLevel;
    }

    public int getParameterEntropyLevel() {
        return parameterEntropyLevel;
    }

    public void setParameterEntropyLevel(int entropyLevel) {
        this.parameterEntropyLevel = entropyLevel;
    }
}
