/*
 * Created by JFormDesigner on Mon Dec 28 22:28:33 CST 2020
 */

package requestcleaner;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/*
This is the GUI for the extension. Lots of this is auto-generated
 */
public class BurpCleanerPanel extends JPanel {
    public BurpCleanerPanel() {
        initComponents();
    }

    //This is the only custom bit, we create out tab and update the state
    public void addTab(JPanel cleanerTab) {
        String tabTitle = String.valueOf(ExtensionState.getInstance().getNumberOfCleanedRequests());
        this.cleanerTabPane.addTab(tabTitle,cleanerTab);
        this.cleanerTabPane.setTabComponentAt(ExtensionState.getInstance().getNumberOfCleanedRequests(),new TabButton(this.cleanerTabPane));
        ExtensionState.getInstance().setNumberOfCleanedRequests(ExtensionState.getInstance().getNumberOfCleanedRequests()+1);
    }

    private void parameterSetLevelActionPerformed(ActionEvent e) {
        ExtensionState.getInstance().setParameterEntropyLevel(Integer.parseInt(parameterLevelText.getText()));
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - unknown
        cleanerTabPane = new JTabbedPane();
        panel1 = new JPanel();
        label2 = new JLabel();
        parameterLevelText = new JTextField();
        parameterSetLevel = new JButton();

        //======== this ========
        setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(
        0,0,0,0), "JF\u006frm\u0044es\u0069gn\u0065r \u0045va\u006cua\u0074io\u006e",javax.swing.border.TitledBorder.CENTER,javax.swing.border.TitledBorder
        .BOTTOM,new java.awt.Font("D\u0069al\u006fg",java.awt.Font.BOLD,12),java.awt.Color.
        red), getBorder())); addPropertyChangeListener(new java.beans.PropertyChangeListener(){@Override public void propertyChange(java.
        beans.PropertyChangeEvent e){if("\u0062or\u0064er".equals(e.getPropertyName()))throw new RuntimeException();}});
        setLayout(new BorderLayout());
        add(cleanerTabPane, BorderLayout.CENTER);

        //======== panel1 ========
        {
            panel1.setLayout(new GridLayout(1, 3));

            //---- label2 ----
            label2.setText("Parameter Entropy Level");
            panel1.add(label2);
            panel1.add(parameterLevelText);

            //---- parameterSetLevel ----
            parameterSetLevel.setText("Set");
            parameterSetLevel.addActionListener(e -> parameterSetLevelActionPerformed(e));
            panel1.add(parameterSetLevel);
        }
        add(panel1, BorderLayout.NORTH);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - unknown
    private JTabbedPane cleanerTabPane;
    private JPanel panel1;
    private JLabel label2;
    private JTextField parameterLevelText;
    private JButton parameterSetLevel;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
