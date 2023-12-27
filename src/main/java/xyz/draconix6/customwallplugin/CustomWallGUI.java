package xyz.draconix6.customwallplugin;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
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
    private JCheckBox bgStretch;
    private boolean closed = false;

    public CustomWallGUI() {
        this.setTitle("Julti Custom Wall Config");
        this.setContentPane(mainPanel);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                CustomWallGUI.this.onClose();
            }
        });

        this.newButton.addActionListener(e -> {
            CustomWallOptions options = CustomWallOptions.getCustomWallOptions();

            CustomWallLayout layout = new CustomWallLayout("Layout " + Integer.toString(options.layouts.size() + 1));
            options.layouts.add(layout);
            options.currentLayout = layout;

            this.layoutBox.addItem(layout);
            this.layoutBox.setSelectedItem(layout);

            this.updateTextBoxes();
            this.save();
        });

        this.deleteButton.addActionListener(e -> {
            CustomWallOptions options = CustomWallOptions.getCustomWallOptions();

            if (options.layouts.size() == 1) return;

            this.layoutBox.removeItem(options.currentLayout);
            options.layouts.remove(options.currentLayout);

            options.currentLayout = options.layouts.get(0);
            this.layoutBox.setSelectedItem(options.currentLayout);

            this.updateTextBoxes();
            this.save();
        });

        this.layoutBox.addActionListener(e -> {
            CustomWallOptions options = CustomWallOptions.getCustomWallOptions();

            options.currentLayout = (CustomWallLayout) (this.layoutBox.getSelectedItem());

            this.updateTextBoxes();
            this.save();
        });

        this.populateLayoutBox();
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

    private void populateLayoutBox() {
        CustomWallOptions options = CustomWallOptions.getCustomWallOptions();
        for (CustomWallLayout layout : options.layouts) {
            this.layoutBox.addItem(layout);
        }
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
        this.lockVertical.setSelected(layout.lockVertical);

        this.bgXField.setText(Integer.toString(layout.bgArea.x));
        this.bgYField.setText(Integer.toString(layout.bgArea.y));
        this.bgWidthField.setText(Integer.toString(layout.bgArea.width));
        this.bgHeightField.setText(Integer.toString(layout.bgArea.height));
        this.bgVertical.setSelected(layout.bgVertical);
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
                    Integer.parseInt(this.focusYField.getText()),
                    Integer.parseInt(this.focusWidthField.getText()),
                    Integer.parseInt(this.focusHeightField.getText())
            );

            options.currentLayout.lockArea = new Rectangle(
                    Integer.parseInt(this.lockXField.getText()),
                    Integer.parseInt(this.lockYField.getText()),
                    Integer.parseInt(this.lockWidthField.getText()),
                    Integer.parseInt(this.lockHeightField.getText())
            );

            options.currentLayout.bgArea = new Rectangle(
                    Integer.parseInt(this.bgXField.getText()),
                    Integer.parseInt(this.bgYField.getText()),
                    Integer.parseInt(this.bgWidthField.getText()),
                    Integer.parseInt(this.bgHeightField.getText())
            );

            options.replaceLocked = this.replaceLockedInstancesCheckBox.isEnabled();
            options.currentLayout.lockVertical = this.lockVertical.isSelected();
            options.currentLayout.bgVertical = this.bgVertical.isSelected();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(JultiGUI.getPluginsGUI(), "Invalid input detected - please only use whole numbers.", "Custom Wall - Error", JOptionPane.ERROR_MESSAGE);
        }

        CustomWallOptions.save();
    }

    private void onClose() {
        this.save();
        this.closed = true;
    }

    private boolean isClosed() {
        return this.closed;
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(5, 1, new Insets(5, 5, 5, 5), -1, -1));
        layoutSelectPanel = new JPanel();
        layoutSelectPanel.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(layoutSelectPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        newButton = new JButton();
        newButton.setText("New");
        layoutSelectPanel.add(newButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, 1, 1, null, new Dimension(50, 30), null, 0, false));
        deleteButton = new JButton();
        deleteButton.setText("Delete");
        layoutSelectPanel.add(deleteButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, 1, 1, null, new Dimension(50, 30), null, 0, false));
        layoutBox = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        layoutBox.setModel(defaultComboBoxModel1);
        layoutSelectPanel.add(layoutBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JSeparator separator1 = new JSeparator();
        mainPanel.add(separator1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        layoutEditPanel = new JPanel();
        layoutEditPanel.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(layoutEditPanel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        focusGridPanel = new JPanel();
        focusGridPanel.setLayout(new GridLayoutManager(2, 8, new Insets(0, 0, 0, 0), -1, -1));
        layoutEditPanel.add(focusGridPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        focusYField = new JTextField();
        focusYField.setText("0");
        focusGridPanel.add(focusYField, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(30, -1), null, 0, false));
        focusWidthField = new JTextField();
        focusWidthField.setText("1920");
        focusGridPanel.add(focusWidthField, new GridConstraints(1, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(30, -1), null, 0, false));
        focusHeightField = new JTextField();
        focusHeightField.setText("900");
        focusGridPanel.add(focusHeightField, new GridConstraints(1, 7, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(30, -1), null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("X");
        focusGridPanel.add(label1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Focus Grid Area");
        focusGridPanel.add(label2, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Y");
        focusGridPanel.add(label3, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Width");
        focusGridPanel.add(label4, new GridConstraints(1, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Height");
        focusGridPanel.add(label5, new GridConstraints(1, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        focusXField = new JTextField();
        focusXField.setText("0");
        focusGridPanel.add(focusXField, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(30, -1), null, 1, false));
        lockAreaPanel = new JPanel();
        lockAreaPanel.setLayout(new GridLayoutManager(2, 9, new Insets(0, 0, 0, 0), -1, -1));
        layoutEditPanel.add(lockAreaPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        lockYField = new JTextField();
        lockYField.setText("900");
        lockAreaPanel.add(lockYField, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(30, -1), null, 0, false));
        lockWidthField = new JTextField();
        lockWidthField.setText("1920");
        lockAreaPanel.add(lockWidthField, new GridConstraints(1, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(30, -1), null, 0, false));
        lockHeightField = new JTextField();
        lockHeightField.setText("180");
        lockAreaPanel.add(lockHeightField, new GridConstraints(1, 7, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(30, -1), null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("X");
        lockAreaPanel.add(label6, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("Lock Area");
        lockAreaPanel.add(label7, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lockXField = new JTextField();
        lockXField.setText("0");
        lockAreaPanel.add(lockXField, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(30, -1), null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setText("Y");
        lockAreaPanel.add(label8, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        label9.setText("Width");
        lockAreaPanel.add(label9, new GridConstraints(1, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label10 = new JLabel();
        label10.setText("Height");
        lockAreaPanel.add(label10, new GridConstraints(1, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lockVertical = new JCheckBox();
        lockVertical.setSelected(false);
        lockVertical.setText("Vertical");
        lockAreaPanel.add(lockVertical, new GridConstraints(1, 8, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        backgroundAreaPanel = new JPanel();
        backgroundAreaPanel.setLayout(new GridLayoutManager(2, 9, new Insets(0, 0, 0, 0), -1, -1));
        layoutEditPanel.add(backgroundAreaPanel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        bgYField = new JTextField();
        bgYField.setText("0");
        backgroundAreaPanel.add(bgYField, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(30, -1), null, 0, false));
        bgWidthField = new JTextField();
        bgWidthField.setText("100");
        backgroundAreaPanel.add(bgWidthField, new GridConstraints(1, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(30, -1), null, 1, false));
        bgHeightField = new JTextField();
        bgHeightField.setText("100");
        backgroundAreaPanel.add(bgHeightField, new GridConstraints(1, 7, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(30, -1), null, 0, false));
        final JLabel label11 = new JLabel();
        label11.setText("X");
        backgroundAreaPanel.add(label11, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label12 = new JLabel();
        label12.setText("Background Instance Area");
        backgroundAreaPanel.add(label12, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label13 = new JLabel();
        label13.setText("Y");
        backgroundAreaPanel.add(label13, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label14 = new JLabel();
        label14.setText("Width");
        backgroundAreaPanel.add(label14, new GridConstraints(1, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label15 = new JLabel();
        label15.setText("Height");
        backgroundAreaPanel.add(label15, new GridConstraints(1, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        bgXField = new JTextField();
        bgXField.setText("1920");
        backgroundAreaPanel.add(bgXField, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(30, -1), null, 0, false));
        bgVertical = new JCheckBox();
        bgVertical.setText("Vertical");
        backgroundAreaPanel.add(bgVertical, new GridConstraints(1, 8, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JSeparator separator2 = new JSeparator();
        mainPanel.add(separator2, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        savePanel = new JPanel();
        savePanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(savePanel, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        saveButton = new JButton();
        saveButton.setText("Save");
        savePanel.add(saveButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        replaceLockedInstancesCheckBox = new JCheckBox();
        replaceLockedInstancesCheckBox.setText("Replace Locked Instances");
        savePanel.add(replaceLockedInstancesCheckBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        label1.setLabelFor(focusXField);
        label3.setLabelFor(focusYField);
        label4.setLabelFor(focusWidthField);
        label5.setLabelFor(focusHeightField);
        label6.setLabelFor(lockXField);
        label8.setLabelFor(lockYField);
        label9.setLabelFor(lockWidthField);
        label10.setLabelFor(lockHeightField);
        label11.setLabelFor(bgXField);
        label13.setLabelFor(bgYField);
        label14.setLabelFor(bgWidthField);
        label15.setLabelFor(bgHeightField);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}
