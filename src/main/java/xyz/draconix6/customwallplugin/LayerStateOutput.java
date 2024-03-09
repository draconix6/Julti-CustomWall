package xyz.draconix6.customwallplugin;

import xyz.duncanruns.julti.JultiOptions;
import xyz.duncanruns.julti.instance.MinecraftInstance;
import xyz.duncanruns.julti.management.InstanceManager;
import xyz.duncanruns.julti.util.FileUtil;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class LayerStateOutput {
    private static final LayerStateOutput INSTANCE = new LayerStateOutput();

    private static final Path STATE_OUT_PATH = JultiOptions.getJultiDir().resolve("layerstate");
    private String lastOut = "";

    public static LayerStateOutput getLayerStateOutput() {
        return INSTANCE;
    }

    public void tryOutputLayers() {
        CustomWallLayout layout = CustomWallOptions.getCustomWallOptions().currentLayout;
        CustomWallResetManager rm = CustomWallResetManager.getCustomWallResetManager();
        InstanceManager im = InstanceManager.getInstanceManager();
        StringBuilder out = new StringBuilder();
        List<Integer> focusLayer = new ArrayList<>();
        List<Integer> lockLayer = new ArrayList<>();
        List<Integer> bgLayer = new ArrayList<>();
        for (MinecraftInstance instance : im.getInstances()) {
            switch (rm.getInstanceLayer(instance)) {
                case "Focus":
                    focusLayer.add(im.getInstanceIndex(instance));
                    break;
                case "Lock":
                    lockLayer.add(im.getInstanceIndex(instance));
                    break;
                case "BG":
                    bgLayer.add(im.getInstanceIndex(instance));
                    break;
                default:
                    break;
            }
        }
        for (int x = 0; x < layout.layers.length; x++) {
            String layer = layout.layers[x];
            switch (layer) {
                case "Focus":
                    for (Integer i : focusLayer) {
                        out.append(i + 1).append(",");
                    }
                    break;
                case "Lock":
                    for (Integer i : lockLayer) {
                        out.append(i + 1).append(",");
                    }
                    break;
                case "BG":
                    for (Integer i : bgLayer) {
                        out.append(i + 1).append(",");
                    }
                    break;
                default:
                    break;
            }
        }

        String outString = out.toString();
        if (!outString.equals(this.lastOut)) {
            this.lastOut = outString;
            try {
                FileUtil.writeString(STATE_OUT_PATH, outString);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
