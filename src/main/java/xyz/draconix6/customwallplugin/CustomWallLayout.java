package xyz.draconix6.customwallplugin;

import java.awt.*;

/**
 * @author draconix6
 */
public class CustomWallLayout {
    public String name;
    public Rectangle focusGridArea;
    public Rectangle lockArea;
    public Rectangle bgArea;
    public boolean lockVertical;
    public boolean bgVertical;

    public CustomWallLayout() {
        this.name = "Layout";
        this.focusGridArea = new Rectangle(0, 0, 1920, 900);
        this.lockArea = new Rectangle(0, 900, 1920, 180);
        this.bgArea = new Rectangle(1920, 0, 100, 100);
        this.lockVertical = false;
        this.bgVertical = false;
    }

    public CustomWallLayout(String name, Rectangle focusGridArea, Rectangle lockArea, Rectangle bgArea, boolean lockVertical, boolean bgVertical) {
        this.name = name;
        this.focusGridArea = focusGridArea;
        this.lockArea = lockArea;
        this.bgArea = bgArea;
        this.lockVertical = lockVertical;
        this.bgVertical = bgVertical;
    }
}
