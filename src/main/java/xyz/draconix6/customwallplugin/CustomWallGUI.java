package xyz.draconix6.customwallplugin;

import org.apache.logging.log4j.Level;
import xyz.duncanruns.julti.Julti;
import xyz.duncanruns.julti.gui.JultiGUI;

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
    private JComboBox<CustomWallLayout> layoutBox;
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
    private JCheckBox lockVertical;
    private JCheckBox bgVertical;
    private boolean closed = false;

    public CustomWallGUI() {
        CustomWallOptions options = CustomWallOptions.getCustomWallOptions();

        this.setTitle("Julti Custom Wall Config");

        this.setContentPane(this.mainPanel);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                CustomWallGUI.this.onClose();
            }
        });

        this.newButton.addActionListener(e -> {
            CustomWallLayout layout = new CustomWallLayout();
            options.layouts.add(layout);
            options.currentLayout = layout;

            layoutBox.addItem(layout);
            layoutBox.setSelectedItem(layout);

            this.updateTextBoxes();
            this.save();
        });

        this.deleteButton.addActionListener(e -> {
            if (options.layouts.size() == 1) return;

            layoutBox.removeItem(options.currentLayout);
            options.layouts.remove(options.currentLayout);

            options.currentLayout = options.layouts.get(0);
            layoutBox.setSelectedItem(options.currentLayout);

            this.updateTextBoxes();
            this.save();
        });

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

    private void updateTextBoxes() {
        CustomWallLayout layout = CustomWallOptions.getCustomWallOptions().currentLayout;

        this.focusXField.setText(Integer.toString(layout.focusGridArea.x));
        this.focusYField.setText(Integer.toString(layout.focusGridArea.y));
        this.focusWidthField.setText(Integer.toString(layout.focusGridArea.width));
        this.focusHeightField.setText(Integer.toString(layout.focusGridArea.height));

        this.lockXField.setText(Integer.toString(layout.lockArea.x));
        this.lockYField.setText(Integer.toString(layout.lockArea.y));
        this.lockWidthField.setText(Integer.toString(layout.lockArea.width));
        this.lockHeightField.setText(Integer.toString(layout.lockArea.height));

        this.bgXField.setText(Integer.toString(layout.bgArea.x));
        this.bgYField.setText(Integer.toString(layout.bgArea.y));
        this.bgWidthField.setText(Integer.toString(layout.bgArea.width));
        this.bgHeightField.setText(Integer.toString(layout.bgArea.height));
    }

    private boolean hasChanges() {
//        CustomWallOptions options = CustomWallOptions.getCustomWallOptions();
//        return (this.checkBoxEnabled() != options.enabledForPlugin) || (!Objects.equals(this.getKeyBoxText(), options.accessKey));
        return true;
    }

    private void save() {
        CustomWallOptions options = CustomWallOptions.getCustomWallOptions();

        try {
            options.currentLayout.focusGridArea = new Rectangle(
                    Integer.parseInt(this.focusXField.getText()),
                    Integer.parseInt(this.focusXField.getText()),
                    Integer.parseInt(this.focusXField.getText()),
                    Integer.parseInt(this.focusXField.getText())
            );

            options.currentLayout.lockArea = new Rectangle(
                    Integer.parseInt(this.lockXField.getText()),
                    Integer.parseInt(this.lockXField.getText()),
                    Integer.parseInt(this.lockXField.getText()),
                    Integer.parseInt(this.lockXField.getText())
            );

            options.currentLayout.bgArea = new Rectangle(
                    Integer.parseInt(this.bgXField.getText()),
                    Integer.parseInt(this.bgXField.getText()),
                    Integer.parseInt(this.bgXField.getText()),
                    Integer.parseInt(this.bgXField.getText())
            );
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(JultiGUI.getPluginsGUI(), "Invalid input detected - please only use whole numbers.", "Custom Wall - Error", JOptionPane.ERROR_MESSAGE);
        }

        CustomWallOptions.save();
    }

    private void onClose() {
        this.save();
        this.closed = true;
    }

    private boolean isClosed() { return this.closed; }
}
