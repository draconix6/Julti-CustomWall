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
    private JCheckBox lockVertical;
    private JCheckBox bgVertical;
    private JCheckBox bgStretch;
    private JCheckBox lockStretch;
    private JTextField nameField;
    private JLabel nameLabel;
    private JButton openLayerPanel;
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

        this.populateLayoutBox();
        this.updateForm();

        this.revalidate();
        this.setMinimumSize(new Dimension(300, 140));
        this.pack();
        this.setResizable(false);
        this.setVisible(true);

        this.newButton.addActionListener(e -> {
            CustomWallOptions options = CustomWallOptions.getCustomWallOptions();

            CustomWallLayout layout = new CustomWallLayout("New Layout");
            options.layouts.add(layout);
            options.currentLayout = layout;

            this.layoutBox.addItem(layout);
            this.layoutBox.setSelectedItem(layout);

            this.updateForm();
            this.save();
        });

        this.deleteButton.addActionListener(e -> {
            CustomWallOptions options = CustomWallOptions.getCustomWallOptions();

            if (options.layouts.size() == 1) return;

            this.layoutBox.removeItem(options.currentLayout);
            options.layouts.remove(options.currentLayout);

            options.currentLayout = options.layouts.get(0);
            this.layoutBox.setSelectedItem(options.currentLayout);

            this.updateForm();
            this.save();
        });

        this.saveButton.addActionListener(e -> this.save());

        this.openLayerPanel.addActionListener(e -> {
            CustomWallOptions.openConfigFile();
        });

        this.layoutBox.addActionListener(e -> {
            CustomWallOptions options = CustomWallOptions.getCustomWallOptions();

            options.currentLayout = (CustomWallLayout) (this.layoutBox.getSelectedItem());

            if (options.currentLayout != null) {
                this.updateForm();
                this.save();
            }
        });
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
//        this.layoutBox.removeAllItems();

        CustomWallOptions options = CustomWallOptions.getCustomWallOptions();
        for (CustomWallLayout layout : options.layouts) {
            this.layoutBox.addItem(layout);
        }

//        Julti.log(Level.INFO, Boolean.toString(options.currentLayout.equals(this.layoutBox.getItemAt(1))));
//        Julti.log(Level.INFO, Integer.toString(this.layoutBox.getItemCount()));
        this.layoutBox.setSelectedItem(options.currentLayout);
    }

    private void updateForm() {
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
        this.lockStretch.setSelected(layout.lockStretch);

        this.bgXField.setText(Integer.toString(layout.bgArea.x));
        this.bgYField.setText(Integer.toString(layout.bgArea.y));
        this.bgWidthField.setText(Integer.toString(layout.bgArea.width));
        this.bgHeightField.setText(Integer.toString(layout.bgArea.height));
        this.bgVertical.setSelected(layout.bgVertical);
        this.bgStretch.setSelected(layout.bgStretch);

        this.nameField.setText(layout.name);

        this.layoutBox.setSelectedItem(layout);
        this.layoutBox.repaint();
    }

    private synchronized void save() {
        CustomWallOptions options = CustomWallOptions.getCustomWallOptions();

        try {
            if (options.layouts.stream().anyMatch(l -> l.name.equals(this.nameField.getText())) && !options.currentLayout.name.equals(this.nameField.getText())) {
                JOptionPane.showMessageDialog(JultiGUI.getPluginsGUI(), "Layout name already exists. Please choose a new name.", "Custom Wall - Error", JOptionPane.ERROR_MESSAGE);
                return;
            } else {
                options.currentLayout.name = this.nameField.getText();
            }

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
            options.currentLayout.lockVertical = this.lockVertical.isSelected();
            options.currentLayout.lockStretch = this.lockStretch.isSelected();

            options.currentLayout.bgArea = new Rectangle(
                    Integer.parseInt(this.bgXField.getText()),
                    Integer.parseInt(this.bgYField.getText()),
                    Integer.parseInt(this.bgWidthField.getText()),
                    Integer.parseInt(this.bgHeightField.getText())
            );
            options.currentLayout.bgVertical = this.bgVertical.isSelected();
            options.currentLayout.bgStretch = this.bgStretch.isSelected();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(JultiGUI.getPluginsGUI(), "Please only use whole numbers.", "Custom Wall - Error", JOptionPane.ERROR_MESSAGE);
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(JultiGUI.getPluginsGUI(), "Please input a valid layout name.", "Custom Wall - Error", JOptionPane.ERROR_MESSAGE);
        }

        this.updateForm();
//        this.populateLayoutBox();
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
        mainPanel.setLayout(new GridLayoutManager(6, 1, new Insets(5, 5, 5, 5), -1, -1));
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
        layoutBox.setEditable(false);
        layoutBox.setEnabled(true);
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        layoutBox.setModel(defaultComboBoxModel1);
        layoutSelectPanel.add(layoutBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JSeparator separator1 = new JSeparator();
        mainPanel.add(separator1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        layoutEditPanel = new JPanel();
        layoutEditPanel.setLayout(new GridLayoutManager(8, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(layoutEditPanel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        focusGridPanel = new JPanel();
        focusGridPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        layoutEditPanel.add(focusGridPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 8, new Insets(0, 0, 0, 0), -1, -1));
        focusGridPanel.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("X");
        panel1.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        focusXField = new JTextField();
        focusXField.setText("0");
        panel1.add(focusXField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(30, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Y");
        panel1.add(label2, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        focusYField = new JTextField();
        focusYField.setText("0");
        panel1.add(focusYField, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(30, -1), null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Width");
        panel1.add(label3, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        focusWidthField = new JTextField();
        focusWidthField.setText("1920");
        panel1.add(focusWidthField, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(30, -1), null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Height");
        panel1.add(label4, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        focusHeightField = new JTextField();
        focusHeightField.setText("900");
        panel1.add(focusHeightField, new GridConstraints(0, 7, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(30, -1), null, 0, false));
        lockAreaPanel = new JPanel();
        lockAreaPanel.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        layoutEditPanel.add(lockAreaPanel, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 8, new Insets(0, 0, 0, 0), -1, -1));
        lockAreaPanel.add(panel2, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("X");
        panel2.add(label5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lockXField = new JTextField();
        lockXField.setText("0");
        panel2.add(lockXField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(30, -1), null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Y");
        panel2.add(label6, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lockYField = new JTextField();
        lockYField.setText("900");
        panel2.add(lockYField, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(30, -1), null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("Width");
        panel2.add(label7, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lockWidthField = new JTextField();
        lockWidthField.setText("1920");
        panel2.add(lockWidthField, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(30, -1), null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setText("Height");
        panel2.add(label8, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lockHeightField = new JTextField();
        lockHeightField.setText("180");
        panel2.add(lockHeightField, new GridConstraints(0, 7, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(30, -1), null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        lockAreaPanel.add(panel3, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        lockVertical = new JCheckBox();
        lockVertical.setSelected(false);
        lockVertical.setText("Vertical");
        panel3.add(lockVertical, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lockStretch = new JCheckBox();
        lockStretch.setText("Stretch Instances To Fit");
        panel3.add(lockStretch, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        backgroundAreaPanel = new JPanel();
        backgroundAreaPanel.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        backgroundAreaPanel.setEnabled(true);
        layoutEditPanel.add(backgroundAreaPanel, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 8, new Insets(0, 0, 0, 0), -1, -1));
        backgroundAreaPanel.add(panel4, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        label9.setText("X");
        panel4.add(label9, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        bgXField = new JTextField();
        bgXField.setText("1920");
        panel4.add(bgXField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(30, -1), null, 0, false));
        final JLabel label10 = new JLabel();
        label10.setText("Y");
        panel4.add(label10, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        bgYField = new JTextField();
        bgYField.setText("0");
        panel4.add(bgYField, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(30, -1), null, 0, false));
        final JLabel label11 = new JLabel();
        label11.setText("Width");
        panel4.add(label11, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        bgWidthField = new JTextField();
        bgWidthField.setText("100");
        panel4.add(bgWidthField, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(30, -1), null, 0, false));
        final JLabel label12 = new JLabel();
        label12.setText("Height");
        panel4.add(label12, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        bgHeightField = new JTextField();
        bgHeightField.setText("100");
        panel4.add(bgHeightField, new GridConstraints(0, 7, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(30, -1), null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        backgroundAreaPanel.add(panel5, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        bgVertical = new JCheckBox();
        bgVertical.setText("Vertical");
        panel5.add(bgVertical, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        bgStretch = new JCheckBox();
        bgStretch.setText("Stretch Instances To Fit");
        panel5.add(bgStretch, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label13 = new JLabel();
        label13.setText("Lock Area");
        layoutEditPanel.add(label13, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label14 = new JLabel();
        label14.setText("Focus Grid Area");
        layoutEditPanel.add(label14, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label15 = new JLabel();
        label15.setText("Background Instance Area");
        layoutEditPanel.add(label15, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JSeparator separator2 = new JSeparator();
        layoutEditPanel.add(separator2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JSeparator separator3 = new JSeparator();
        layoutEditPanel.add(separator3, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JSeparator separator4 = new JSeparator();
        mainPanel.add(separator4, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        savePanel = new JPanel();
        savePanel.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(savePanel, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        saveButton = new JButton();
        saveButton.setText("Save");
        savePanel.add(saveButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        nameField = new JTextField();
        savePanel.add(nameField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        nameLabel = new JLabel();
        nameLabel.setText("Layout Name");
        savePanel.add(nameLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        openLayerPanel = new JButton();
        openLayerPanel.setEnabled(false);
        openLayerPanel.setText("Customize Layers...");
        mainPanel.add(openLayerPanel, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        label1.setLabelFor(focusXField);
        label2.setLabelFor(focusYField);
        label3.setLabelFor(focusWidthField);
        label4.setLabelFor(focusHeightField);
        label5.setLabelFor(lockXField);
        label6.setLabelFor(lockYField);
        label7.setLabelFor(lockWidthField);
        label8.setLabelFor(lockHeightField);
        label9.setLabelFor(bgXField);
        label10.setLabelFor(bgYField);
        label11.setLabelFor(bgWidthField);
        label12.setLabelFor(bgHeightField);
        nameLabel.setLabelFor(nameField);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}
