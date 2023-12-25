package xyz.draconix6.customwallplugin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author DuncanRuns
 * @author draconix6
*/
public class CustomWallGUI extends JFrame {
    private static CustomWallGUI instance = null;
//    private JCheckBox enabledCheckBox;
//    private JPasswordField accessKeyField;
    private JPanel mainPanel;
    private JPanel layoutSelectPanel;
    private JComboBox layoutBox;
    private JButton newButton;
    private JButton deleteButton;
    private JPanel layoutEditPanel;
    private JTextField focusXField;
    private JTextField focusYField;
    private JTextField focusWidthField;
    private JTextField focusHeightField;
    private JPanel focusGridPanel;
    private JPanel lockAreaPanel;
    private JPanel backgroundAreaPanel;
    private JPanel savePanel;
    private JButton saveButton;
    private JTextField lockXField;
    private JTextField lockYField;
    private JTextField lockWidthField;
    private JTextField lockHeightField;
    private JTextField bgXField;
    private JTextField bgYField;
    private JTextField bgWidthField;
    private JTextField bgHeightField;
    private JCheckBox replaceLockedInstancesCheckBox;
    private boolean closed = false;

    public CustomWallGUI() {
        this.setTitle("Julti Custom Wall Config");

        this.setContentPane(this.mainPanel);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                CustomWallGUI.this.onClose();
            }
        });

        CustomWallOptions options = CustomWallOptions.getCustomWallOptions();
//        this.enabledCheckBox.setSelected(options.enabledForPlugin);
//        this.enabledCheckBox.addActionListener(e -> {
//            this.saveButton.setEnabled(this.hasChanges());
//            if (asPlugin) {
//                this.accessKeyField.setEnabled(this.checkBoxEnabled());
//            }
//        });
//        this.accessKeyField.setText(options.accessKey);
//        this.accessKeyField.addKeyListener(new KeyAdapter() {
//            @Override
//            public void keyReleased(KeyEvent e) {
//                if (e.getKeyChar() == '\n') {
//                    PaceManTrackerGUI.this.save();
//                }
//                PaceManTrackerGUI.this.saveButton.setEnabled(PaceManTrackerGUI.this.hasChanges());
//            }
//
//        });
//        if (asPlugin) {
//            this.accessKeyField.setEnabled(options.enabledForPlugin);
//        }
        this.saveButton.addActionListener(e -> this.save());
        this.saveButton.setEnabled(this.hasChanges());
        this.revalidate();
        this.setMinimumSize(new Dimension(300, 140));
        this.pack();
        this.setResizable(false);
        this.setVisible(true);
    }

    public static CustomWallGUI open(Point initialLocation) {
        if (instance == null || instance.isClosed()) {
            instance = new CustomWallGUI();
            if (initialLocation != null) {
                instance.setLocation(initialLocation);
            }
        } else {
            instance.requestFocus();
        }
        return instance;
    }

    private boolean hasChanges() {
        CustomWallOptions options = CustomWallOptions.getCustomWallOptions();
//        return (this.checkBoxEnabled() != options.enabledForPlugin) || (!Objects.equals(this.getKeyBoxText(), options.accessKey));
        return true;
    }

    private void save() {
        CustomWallOptions options = CustomWallOptions.getCustomWallOptions();
        CustomWallOptions.save();
        this.saveButton.setEnabled(this.hasChanges());
    }

    private void onClose() { this.closed = true; }

    private boolean isClosed() { return this.closed; }
}
