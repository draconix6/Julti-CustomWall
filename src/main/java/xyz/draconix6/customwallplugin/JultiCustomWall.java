package xyz.draconix6.customwallplugin;

import com.google.common.io.Resources;
import org.apache.logging.log4j.Level;
import xyz.duncanruns.julti.Julti;
import xyz.duncanruns.julti.JultiAppLaunch;
import xyz.duncanruns.julti.gui.JultiGUI;
import xyz.duncanruns.julti.plugin.PluginInitializer;
import xyz.duncanruns.julti.plugin.PluginManager;

import javax.swing.*;
import java.io.IOException;
import java.nio.charset.Charset;

public class JultiCustomWall implements PluginInitializer {
    public static void main(String[] args) throws IOException {
        // This is only used to test the plugin in the dev environment

        JultiAppLaunch.launchWithDevPlugin(args, PluginManager.JultiPluginData.fromString(
                Resources.toString(Resources.getResource(JultiCustomWall.class, "/julti.plugin.json"), Charset.defaultCharset())
        ), new JultiCustomWall());
    }

    @Override
    public void initialize() {
        // This gets run once when Julti launches
        InitCustomWall.init();
        Julti.log(Level.INFO, "Custom Wall Plugin Initialized");
    }

    @Override
    public String getMenuButtonName() {
        return "Open";
    }

    @Override
    public void onMenuButtonPress() {
        JOptionPane.showMessageDialog(JultiGUI.getPluginsGUI(), "Hi", "Julti Custom Wall Config", JOptionPane.INFORMATION_MESSAGE);
    }
}
