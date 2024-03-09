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
    public boolean lockStretch;
    public boolean bgVertical;
    public boolean bgStretch;
    public String[] layers;

    public CustomWallLayout() {
        this.name = "Layout";
        this.focusGridArea = new Rectangle(0, 0, 1920, 900);
        this.lockArea = new Rectangle(0, 900, 1920, 180);
        this.bgArea = new Rectangle(1920, 0, 100, 100);
        this.lockVertical = false;
        this.lockStretch = false;
        this.bgVertical = false;
        this.bgStretch = false;
        this.layers = new String[]{"Focus", "Lock", "BG"}; // 0 = focus, 1 = lock, 2 = bg. pos 1 = top layer
    }

    public CustomWallLayout(String name) {
        this.name = name;
        this.focusGridArea = new Rectangle(0, 0, 1920, 900);
        this.lockArea = new Rectangle(0, 900, 1920, 180);
        this.bgArea = new Rectangle(1920, 0, 100, 100);
        this.lockVertical = false;
        this.lockStretch = false;
        this.bgVertical = false;
        this.bgStretch = false;
        // TODO: enumerate?
        this.layers = new String[]{"Focus", "Lock", "BG"}; // 0 = focus, 1 = lock, 2 = bg. pos 1 = top layer
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof CustomWallLayout)) return false;
        CustomWallLayout that = (CustomWallLayout) obj;
        return that.name.equals(this.name);
    }
}
