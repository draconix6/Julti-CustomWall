package xyz.draconix6.customwallplugin;

import org.apache.logging.log4j.Level;
import xyz.duncanruns.julti.Julti;
import xyz.duncanruns.julti.plugin.PluginEvents;

/**
 * @author DuncanRuns
 * @author draconix6
 */
public class InitCustomWall {
    public static void init() {
        PluginEvents.RunnableEventType.RELOAD.register(() -> {
            // This gets run when Julti launches and every time the profile is switched
            Julti.log(Level.DEBUG, "Custom Wall Plugin Reloaded!");
        });

        PluginEvents.RunnableEventType.STOP.register(() -> {
            // This gets run when Julti is shutting down
            Julti.log(Level.INFO, "Custom Wall plugin shutting down...");
        });

        PluginEvents.RunnableEventType.START_TICK.register(() -> {
            LayerStateOutput.getLayerStateOutput().tryOutputLayers();
        });
    }
}
