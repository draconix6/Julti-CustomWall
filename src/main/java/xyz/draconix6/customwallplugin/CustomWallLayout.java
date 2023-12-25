package xyz.draconix6.customwallplugin;

import java.awt.*;

public class CustomWallLayout {
    public String name;
    public Rectangle focusGridArea;
    public Rectangle lockArea;
    public Rectangle bgArea;

    public CustomWallLayout(String name, Rectangle focusGridArea, Rectangle lockArea, Rectangle bgArea) {
        this.name = name;
        this.focusGridArea = focusGridArea;
        this.lockArea = lockArea;
        this.bgArea = bgArea;
    }
}
