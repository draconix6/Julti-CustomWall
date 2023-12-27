package xyz.draconix6.customwallplugin;

import xyz.duncanruns.julti.JultiOptions;
import xyz.duncanruns.julti.affinity.AffinityManager;
import xyz.duncanruns.julti.instance.MinecraftInstance;
import xyz.duncanruns.julti.management.ActiveWindowManager;
import xyz.duncanruns.julti.management.InstanceManager;
import xyz.duncanruns.julti.management.OBSStateManager;
import xyz.duncanruns.julti.resetting.ActionResult;
import xyz.duncanruns.julti.resetting.DynamicWallResetManager;
import xyz.duncanruns.julti.util.DoAllFastUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author DuncanRuns
 * @author draconix6
 */
public class CustomWallResetManager extends DynamicWallResetManager {
    private static final CustomWallResetManager INSTANCE = new CustomWallResetManager();

    private List<Integer> displayInstancesIndices = new ArrayList<>();
    private List<Integer> bgInstancesIndices = new ArrayList<>();
    private boolean isFirstReset = true;

    public static CustomWallResetManager getCustomWallResetManager() {
        return INSTANCE;
    }

    private List<MinecraftInstance> getDisplayInstances() {
        List<MinecraftInstance> allInstances = InstanceManager.getInstanceManager().getInstances();
        return this.displayInstancesIndices.stream().map(i -> i == null || i >= allInstances.size() ? null : allInstances.get(i)).collect(Collectors.toList());
    }

    private void saveDisplayInstances(List<MinecraftInstance> displayInstances) {
        List<MinecraftInstance> allInstances = InstanceManager.getInstanceManager().getInstances();
        this.displayInstancesIndices = displayInstances.stream().map(i -> i == null ? null : allInstances.indexOf(i)).collect(Collectors.toList());
    }

    private List<MinecraftInstance> getBGInstances() {
        List<MinecraftInstance> allInstances = InstanceManager.getInstanceManager().getInstances();
        return this.bgInstancesIndices.stream().map(i -> i == null || i >= allInstances.size() ? null : allInstances.get(i)).collect(Collectors.toList());
    }

    private void saveBGInstances(List<MinecraftInstance> bgInstances) {
        List<MinecraftInstance> allInstances = InstanceManager.getInstanceManager().getInstances();
        this.bgInstancesIndices = bgInstances.stream().map(i -> i == null ? null : allInstances.indexOf(i)).collect(Collectors.toList());
    }

    @Override
    public void refreshDisplayInstances() {
        JultiOptions options = JultiOptions.getJultiOptions();
        List<MinecraftInstance> displayInstances = this.getDisplayInstances();

        int totalRows = 2;
        int totalColumns = 2;
        if (!options.autoCalcWallSize) {
            totalRows = options.overrideRowsAmount;
            totalColumns = options.overrideColumnsAmount;
        }

        int numToDisplay = totalColumns * totalRows;

        if (displayInstances.size() != numToDisplay) {
            displayInstances = new ArrayList<>();
            for (int i = 0; i < numToDisplay; i++) {
                displayInstances.add(null);
            }
        }

        for (int i = 0; i < numToDisplay; i++) {
            MinecraftInstance instance = displayInstances.get(i);
            if (instance != null && ((displayInstances.get(i).isWindowMarkedMissing()) || this.getLockedInstances().contains(displayInstances.get(i)))) {
                displayInstances.set(i, null);
            }
        }

        if (!displayInstances.contains(null)) {
            this.saveDisplayInstances(displayInstances);
            return;
        }

        List<MinecraftInstance> instancePool = new ArrayList<>(InstanceManager.getInstanceManager().getInstances());
        instancePool.removeIf(instance -> this.getLockedInstances().contains(instance));
        instancePool.removeIf(displayInstances::contains);
        instancePool.sort((o1, o2) -> o2.getResetSortingNum() - o1.getResetSortingNum());

        while (displayInstances.contains(null)) {
            if (instancePool.isEmpty()) {
                break;
            }
            MinecraftInstance removed = instancePool.remove(0);
            removed.setVisible();
            displayInstances.set(displayInstances.indexOf(null), removed);
        }
        this.saveDisplayInstances(displayInstances);
        this.saveBGInstances(instancePool);
    }

    @Override
    public Rectangle getInstancePosition(MinecraftInstance instance, Dimension sceneSize) {
        JultiOptions jOptions = JultiOptions.getJultiOptions();
        CustomWallOptions cwOptions = CustomWallOptions.getCustomWallOptions();

        List<MinecraftInstance> displayInstances = this.getDisplayInstances();

        if (this.getLockedInstances().contains(instance)) {
            return this.getLockedInstancePosition(instance, sceneSize);
        } else if (!displayInstances.contains(instance)) {
            return this.getBackgroundInstancePosition(instance, sceneSize);
        }

        int totalRows = 2;
        int totalColumns = 2;
        if (!jOptions.autoCalcWallSize) {
            totalRows = Math.max(1, jOptions.overrideRowsAmount);
            totalColumns = Math.max(1, jOptions.overrideColumnsAmount);
        }

        int instanceInd = displayInstances.indexOf(instance);

        // Dimensions are manually defined in the custom wall layout, so sceneSize is ignored
        Dimension dwInnerSize = cwOptions.currentLayout.focusGridArea.getSize();

        // Using floats here so there won't be any gaps in the wall after converting back to int
        float iWidth = dwInnerSize.width / (float) totalColumns;
        float iHeight = dwInnerSize.height / (float) totalRows;

        int row = instanceInd / totalColumns;
        int col = instanceInd % totalColumns;

        int x = (int) (col * iWidth);
        int y = (int) (row * iHeight);
        return new Rectangle(
                x + cwOptions.currentLayout.focusGridArea.x,
                y + cwOptions.currentLayout.focusGridArea.y,
                (int) ((col + 1) * iWidth) - x,
                (int) ((row + 1) * iHeight) - y
        );
    }

    private Rectangle getLockedInstancePosition(MinecraftInstance instance, Dimension sceneSize) {
        CustomWallOptions cwOptions = CustomWallOptions.getCustomWallOptions();

        Rectangle lockArea = cwOptions.currentLayout.lockArea;

        int instanceIndex = this.getLockedInstances().indexOf(instance);

        // Dimensions are manually defined in the custom wall layout, so sceneSize is ignored
        Dimension dwInnerSize = lockArea.getSize();

        // Forcing 16:9 ratio for non-focus instance size
        Dimension lockedInstanceSize = new Dimension((int) (dwInnerSize.height * 16.0f / 9.0f), dwInnerSize.height);

        if (cwOptions.currentLayout.lockVertical) {
            return new Rectangle(lockArea.x, lockArea.y + lockedInstanceSize.height * instanceIndex, lockedInstanceSize.width, lockedInstanceSize.height);
        }
        return new Rectangle(lockArea.x + lockedInstanceSize.width * instanceIndex, lockArea.y, lockedInstanceSize.width, lockedInstanceSize.height);
    }

    private Rectangle getBackgroundInstancePosition(MinecraftInstance instance, Dimension sceneSize) {
        CustomWallOptions cwOptions = CustomWallOptions.getCustomWallOptions();

        Rectangle bgArea = cwOptions.currentLayout.bgArea;

        List<MinecraftInstance> bgInstances = this.getBGInstances();
        int instanceIndex = bgInstances.indexOf(instance);

        // Dimensions are manually defined in the custom wall layout, so sceneSize is ignored
        Dimension dwInnerSize = bgArea.getSize();

        // Forcing 16:9 ratio for non-focus instance size
        Dimension bgInstanceSize = new Dimension((int) (dwInnerSize.height * 16.0f / 9.0f), dwInnerSize.height);

        if (cwOptions.currentLayout.bgVertical) {
            return new Rectangle(bgArea.x, bgArea.y + bgInstanceSize.height * instanceIndex, bgInstanceSize.width, bgInstanceSize.height);
        }
        return new Rectangle(bgArea.x + bgInstanceSize.width * instanceIndex, bgArea.y, bgInstanceSize.width, bgInstanceSize.height);
    }
}
