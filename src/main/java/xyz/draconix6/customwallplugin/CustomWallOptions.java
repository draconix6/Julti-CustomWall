package xyz.draconix6.customwallplugin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.Level;
import xyz.duncanruns.julti.Julti;
import xyz.duncanruns.julti.JultiOptions;
import xyz.duncanruns.julti.util.ExceptionUtil;
import xyz.duncanruns.julti.util.FileUtil;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * @author DuncanRuns
 * @author draconix6
 */
public class CustomWallOptions {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path SAVE_PATH = JultiOptions.getJultiDir().resolve("customwalloptions.json");

    private static CustomWallOptions instance = null;

    public List<CustomWallLayout> layouts = new ArrayList<>();
    public CustomWallLayout currentLayout = null;

    public static CustomWallOptions getCustomWallOptions() {
        return instance;
    }

    public static void openConfigFile() {
        try {
            Desktop.getDesktop().edit(SAVE_PATH.toFile());
        } catch (IOException err) {
            Julti.log(Level.INFO, "Couldn't open layer config file: " + err.getMessage());
        }
    }

    public static void load() {
        if (!Files.exists(SAVE_PATH)) {
            instance = new CustomWallOptions();
            instance.currentLayout = new CustomWallLayout();
            instance.layouts.add(instance.currentLayout);
        } else {
            String s;
            try {
                s = FileUtil.readString(SAVE_PATH);
            } catch (IOException e) {
                instance = new CustomWallOptions();
                return;
            }
            instance = GSON.fromJson(s, CustomWallOptions.class);
        }
    }

    public static void save() {
        try {
            FileUtil.writeString(SAVE_PATH, GSON.toJson(instance));
        } catch (IOException e) {
            Julti.log(Level.ERROR, "Failed to save Custom Wall Options: " + ExceptionUtil.toDetailedString(e));
        }
    }
}
