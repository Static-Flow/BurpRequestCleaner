package burp;


import requestcleaner.BurpCleanerPanel;
import requestcleaner.ExtensionState;
import requestcleaner.ManualRequestSenderContextMenu;

import java.awt.*;

/*
This extension lets you send requests to a cleaning operation which uses
Shannon Entropy to detect potentially sensitive headers or parameter values
and redacts them. Made because of this tweet https://twitter.com/pry0cc/status/1343629699487039495
 */
public class BurpExtender implements IBurpExtender, ITab {
    private IBurpExtenderCallbacks callbacks;

    public void registerExtenderCallbacks(IBurpExtenderCallbacks iBurpExtenderCallbacks) {
        callbacks = iBurpExtenderCallbacks;
        ExtensionState.setCallbacks(iBurpExtenderCallbacks);
        callbacks.setExtensionName("Burp Suite Request Cleaner");
        iBurpExtenderCallbacks.registerContextMenuFactory(new ManualRequestSenderContextMenu());
        iBurpExtenderCallbacks.addSuiteTab(this);


    }

    public String getTabCaption() {
        return "Request Cleaner";
    }

    public Component getUiComponent() {
        BurpCleanerPanel panel = ExtensionState.getInstance().getCleanerPanel();
        callbacks.customizeUiComponent(panel);
        return panel;
    }

}