package requestcleaner;

import burp.IMessageEditor;
import burp.IParameter;
import burp.IRequestInfo;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

/*
This panel is the inner view of each cleaning tab. It shows the original request
alongside the cleaned one
 */
public class CleanerPanel extends JPanel {

    /*
    This may and should change. Finding a definitive list of headers to clean
    is tough so we should probably find a better way. Shannon Entropy doesn't
    work well for headers because examples like this:

    Accept-Datetime: Thu, 31 May 2007 20:35:00 GMT

    Have high Shannon Entropy but obviously shouldn't be redacted.
    */
    String[] CLEAN_HEADERS = new String[]{"Authorization","Proxy" +
            "-Authorization"};

    public CleanerPanel(HttpRequestResponse requestResponseToClean) {
        this.setLayout(new BorderLayout());
        JTabbedPane reqRespTabbedPane = new JTabbedPane();
        IMessageEditor requestMessageToDisplay =
                ExtensionState.getCallbacks().createMessageEditor(
                new MessageEditorController(
                        requestResponseToClean.getHttpService(),
                        requestResponseToClean.getRequest(),
                        requestResponseToClean.getResponse()),
                false);
        requestMessageToDisplay.setMessage(requestResponseToClean.getRequest(),
                true);

        cleanRequest(requestResponseToClean);


        ExtensionState.getCallbacks().printOutput(new String(requestResponseToClean.getCleanedRequest()));
        IMessageEditor requestMessageToDisplayCleaned =
                ExtensionState.getCallbacks().createMessageEditor(
                        new MessageEditorController(
                                requestResponseToClean.getHttpService(),
                                requestResponseToClean.getCleanedRequest(),
                                requestResponseToClean.getResponse()),
                        true);
        requestMessageToDisplayCleaned.setMessage(requestResponseToClean.getCleanedRequest(),
                true);

        reqRespTabbedPane.addTab("Original",
                requestMessageToDisplay.getComponent());
        reqRespTabbedPane.addTab("Cleaned",
                requestMessageToDisplayCleaned.getComponent());
        this.add(reqRespTabbedPane);
    }

    //Cleans the Host header by removing all but the root FDQN
    // foo.bar.root.com ---> redacted.root.com
    // foo.root.com ----> redacted.root.com
    private String cleanHostHeader(String header) {
        String[] hostPieces = header.split(":")[1].split("\\.");
        if(hostPieces.length > 2) {
            return "Host: "+("redacted."+hostPieces[hostPieces.length-2]+"."+hostPieces[hostPieces.length-1]);
        } else {
            return "Host: "+("redacted."+hostPieces[hostPieces.length-1]);
        }
    }


    //Checks if a header should be redacted
    private boolean shouldCleanHeader(String header) {
        for(String skipHeader : CLEAN_HEADERS) {
            if(header.contains(skipHeader)){
                return true;
            }
        }
        return false;
    }

    //cleans headers that meet the configured Shannon Entropy level
    private void cleanHeaders(HttpRequestResponse requestResponseToClean) {
        IRequestInfo rinfo =
                ExtensionState.getCallbacks().getHelpers().analyzeRequest(requestResponseToClean.getRequest());
        ArrayList<String> rInfoHeaders = (ArrayList<String>) rinfo.getHeaders();
        ExtensionState.getCallbacks().printOutput("Cleaning headers");

        for(int i = 0;i<rInfoHeaders.size();i++)
        {
            ExtensionState.getCallbacks().printOutput(rInfoHeaders.get(i));
            if(rInfoHeaders.get(i).startsWith("Host")) {
                rInfoHeaders.set(i,cleanHostHeader(rInfoHeaders.get(i)));
                ExtensionState.getCallbacks().printOutput(rInfoHeaders.get(i));
            } else {
                //check entropy
                String[] headerPieces = rInfoHeaders.get(i).split(":");
                if(headerPieces.length > 1 && shouldCleanHeader(headerPieces[0])) {
                    String headerValue = headerPieces[1];
                    ExtensionState.getCallbacks().printOutput(headerValue);
                    rInfoHeaders.set(i, rInfoHeaders.get(i).split(":")[0] +
                            ": Redacted");
                    ExtensionState.getCallbacks().printOutput(rInfoHeaders.get(i));
                }

            }
        }
        requestResponseToClean.setCleanedRequest(ExtensionState.getCallbacks().getHelpers().buildHttpMessage(rInfoHeaders,
                Arrays.copyOfRange(requestResponseToClean.getRequest(), rinfo.getBodyOffset(), requestResponseToClean.getRequest().length)));
    }

    //cleans parameters that meet the configured Shannon Entropy level
    private void cleanParameters(HttpRequestResponse requestResponseToClean) {
        IRequestInfo rinfo =
                ExtensionState.getCallbacks().getHelpers().analyzeRequest(requestResponseToClean.getRequest());
        ArrayList<IParameter> rParams = (ArrayList<IParameter>) rinfo.getParameters();

        for (IParameter rParam : rParams) {
            ExtensionState.getCallbacks().printOutput(rParam.getValue());
            double entropy =
                    ExtensionState.getShannonEntropy(rParam.getValue());
            ExtensionState.getCallbacks().printOutput("Entropy: " + entropy);
            if (entropy > ExtensionState.getInstance().getParameterEntropyLevel()) {
                Parameter parameter = new Parameter(rParam);
                parameter.setValue("redacted");
                requestResponseToClean.setCleanedRequest(ExtensionState.getCallbacks().getHelpers().updateParameter(requestResponseToClean.getCleanedRequest(), parameter));
            }
        }
    }

    //main entry to cleaning a request
    private void cleanRequest(HttpRequestResponse requestResponseToClean) {
        ExtensionState.getCallbacks().printOutput("Cleaning request");
        requestResponseToClean.setCleanedRequest(requestResponseToClean.getRequest());
        cleanHeaders(requestResponseToClean);
        cleanParameters(requestResponseToClean);
        ExtensionState.getCallbacks().printOutput(new String(requestResponseToClean.getCleanedRequest()));
    }

}
