package xyz.draconix6.customwallplugin;

import com.google.common.io.Resources;
import org.apache.logging.log4j.Level;
import xyz.duncanruns.julti.Julti;
import xyz.duncanruns.julti.JultiAppLaunch;
import xyz.duncanruns.julti.JultiOptions;
import xyz.duncanruns.julti.gui.JultiGUI;
import xyz.duncanruns.julti.gui.PluginsGUI;
import xyz.duncanruns.julti.plugin.PluginInitializer;
import xyz.duncanruns.julti.plugin.PluginManager;
import xyz.duncanruns.julti.resetting.ResetHelper;
import xyz.duncanruns.julti.util.ResourceUtil;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author DuncanRuns
 * @author draconix6
 */
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
        CustomWallOptions.load();
        ResetHelper.registerResetStyle("Custom Wall", CustomWallResetManager::getCustomWallResetManager);
        InitCustomWall.init();
//        try {
//            copyResourceToFile("/custom-wall-obs-link.lua", JultiOptions.getJultiDir().resolve("custom-wall-obs-link.lua"));
//        } catch (Exception e) {
//            Julti.log(Level.WARN, "Couldn't copy custom wall OBS script: " + e.getMessage());
//        }
        Julti.log(Level.INFO, "Custom Wall Plugin Initialized - using " + CustomWallOptions.getCustomWallOptions().currentLayout.name);
    }

    @Override
    public void onMenuButtonPress() {
        PluginsGUI pluginsGUI = JultiGUI.getPluginsGUI();
        CustomWallGUI.open(new Point(pluginsGUI.getX() + pluginsGUI.getWidth(), pluginsGUI.getY()));
    }

    public static InputStream getResourceAsStream(String name) {
        return ResourceUtil.class.getResourceAsStream(name);
    }

    public static void copyResourceToFile(String resourceName, Path destination) throws IOException {
        // Answer to https://stackoverflow.com/questions/10308221/how-to-copy-file-inside-jar-to-outside-the-jar
        InputStream inStream = getResourceAsStream(resourceName);
        OutputStream outStream = Files.newOutputStream(destination);
        int readBytes;
        byte[] buffer = new byte[4096];
        while ((readBytes = inStream.read(buffer)) > 0) {
            outStream.write(buffer, 0, readBytes);
        }
        inStream.close();
        outStream.close();
    }
}
