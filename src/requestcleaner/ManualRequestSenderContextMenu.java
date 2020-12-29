package requestcleaner;

import burp.IContextMenuFactory;
import burp.IContextMenuInvocation;
import burp.IHttpRequestResponse;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/*
This handles our context menu for sending requests to the cleaner
 */
public class ManualRequestSenderContextMenu implements IContextMenuFactory {


    @Override
    public List<JMenuItem> createMenuItems(IContextMenuInvocation invocation) {
        ArrayList<JMenuItem> menues = new ArrayList<>();
        if (Objects.equals(IContextMenuInvocation.CONTEXT_MESSAGE_EDITOR_REQUEST , invocation.getInvocationContext())||
                Objects.equals(IContextMenuInvocation.CONTEXT_MESSAGE_VIEWER_REQUEST, invocation.getInvocationContext()) ||
                Objects.equals(IContextMenuInvocation.CONTEXT_TARGET_SITE_MAP_TREE, invocation.getInvocationContext()) ||
                Objects.equals(IContextMenuInvocation.CONTEXT_TARGET_SITE_MAP_TABLE, invocation.getInvocationContext())) {
            menues.addAll(createCleaningMenu(invocation));
        }
        return menues;
    }

    private Collection<? extends JMenuItem> createCleaningMenu(IContextMenuInvocation invocation) {
        JMenuItem sendToCleaner = new JMenuItem("Send to cleaner");
        sendToCleaner.addActionListener(e ->
                sendRequestToCleaner(invocation));

        ArrayList<JMenuItem> menuList = new ArrayList<>();
        menuList.add(sendToCleaner);
        return menuList;
    }


    private void sendRequestToCleaner(IContextMenuInvocation invocation) {
        HttpRequestResponse httpRequestResponse =
                new HttpRequestResponse();
        for (IHttpRequestResponse message : invocation.getSelectedMessages()) {
            new SwingWorker<Boolean, Void>() {
                @Override
                public Boolean doInBackground() {
                    httpRequestResponse.setRequest(message.getRequest());
                    httpRequestResponse.setHttpService(message.getHttpService());
                    ExtensionState.getInstance().getCleanerPanel().addTab(new CleanerPanel(httpRequestResponse));
                    return Boolean.TRUE;
                }

                @Override
                public void done() {
                    //we don't need to do any cleanup so this is empty
                }
            }.execute();
        }

    }
}
