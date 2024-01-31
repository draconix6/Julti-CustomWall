package xyz.draconix6.customwallplugin;

import xyz.duncanruns.julti.JultiOptions;
import xyz.duncanruns.julti.affinity.AffinityManager;
import xyz.duncanruns.julti.instance.MinecraftInstance;
import xyz.duncanruns.julti.management.ActiveWindowManager;
import xyz.duncanruns.julti.management.InstanceManager;
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
//        StringBuilder ha = new StringBuilder();
//        for (int i : this.bgInstancesIndices) {
//            ha.append(i).append(", ");
//        }
//        Julti.log(Level.INFO, ha.toString());
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
        this.refreshBGInstances();
    }

    public void refreshBGInstances() {
        this.refreshBGInstances(new ArrayList<>());
    }

    public void refreshBGInstances(List<MinecraftInstance> bgInstances) {
        if (!bgInstances.isEmpty()) {
            this.saveBGInstances(bgInstances);
            return;
        }

        JultiOptions options = JultiOptions.getJultiOptions();
        List<MinecraftInstance> displayInstances = this.getDisplayInstances();

        List<MinecraftInstance> instancePool = new ArrayList<>(InstanceManager.getInstanceManager().getInstances());
        instancePool.removeIf(instance -> this.getLockedInstances().contains(instance));
        instancePool.removeIf(displayInstances::contains);
        instancePool.sort((o1, o2) -> o2.getResetSortingNum() - o1.getResetSortingNum());
        this.saveBGInstances(instancePool);
    }

    // mostly same - just forcing adding left instance to BG instance indices
    @Override
    public List<ActionResult> doReset() {
        List<MinecraftInstance> instances = InstanceManager.getInstanceManager().getInstances();
        // Return if no instances
        if (instances.size() == 0) {
            return Collections.emptyList();
        }

        // Get selected instance, return if no selected instance
        MinecraftInstance selectedInstance = InstanceManager.getInstanceManager().getSelectedInstance();
        if (selectedInstance == null) {
            return Collections.emptyList();
        }

        // if there is only a single instance, reset it and return.
        if (instances.size() == 1) {
            selectedInstance.reset();
            return Collections.singletonList(ActionResult.INSTANCE_RESET);
        }

        // Only place leaveInstance is used, but it is a big method
        List<ActionResult> out = this.leaveInstance(selectedInstance, instances);

//        this.bgInstancesIndices.add(instances.indexOf(selectedInstance));
        this.refreshBGInstances();

        return out;
    }


    // refresh display instances so BG instances are populated correctly
    @Override
    public List<ActionResult> doWallSingleReset(Point mousePosition) {
        List<ActionResult> results = super.doWallSingleReset(mousePosition);
        this.refreshDisplayInstances();
        return results;
    }

    // TODO: almost the same as DynamicWallResetManager - should be a better way to do this
    // calling super doesn't quite work - locked instances are reset otherwise
    @Override
    public List<ActionResult> doWallFullReset() {
        List<ActionResult> actionResults = new ArrayList<>();
        if (this.isFirstReset && !(actionResults = super.doWallFullReset()).isEmpty()) {
            this.isFirstReset = false;
            return actionResults;
        }
        if (!ActiveWindowManager.isWallActive()) {
            return actionResults;
        }

        List<ActionResult> finalActionResults = actionResults;
        // Do special reset so that display instances don't get replaced because it will be filled with null anyway
        DoAllFastUtil.doAllFast(this.getDisplayInstances().stream().filter(Objects::nonNull).collect(Collectors.toList()), instance -> {
            if (this.resetNoWallUpdate(instance)) {
                synchronized (finalActionResults) {
                    finalActionResults.add(ActionResult.INSTANCE_RESET);
                }
            }
        });

        if (JultiOptions.getJultiOptions().useAffinity) {
            AffinityManager.ping();
        }
        // Fill display with null then refresh to ensure good order
        Collections.fill(this.displayInstancesIndices, null);
        this.refreshDisplayInstances();
        // Return true if something has happened: instances were reset OR the display was updated
        return actionResults;
    }

    @Override
    public List<ActionResult> doWallFocusReset(Point mousePosition) {
        if (!ActiveWindowManager.isWallActive()) {
            return Collections.emptyList();
        }
        // Regular play instance method
        MinecraftInstance clickedInstance = this.getHoveredWallInstance(mousePosition);
        if (clickedInstance == null) {
            return Collections.emptyList();
        }
        List<ActionResult> actionResults = new ArrayList<>(this.playInstanceFromWall(clickedInstance, false));

        // Get list of instances to reset
        List<MinecraftInstance> toReset = this.getDisplayInstances();
        toReset.removeIf(Objects::isNull);
        toReset.remove(clickedInstance);

        // Reset all others
        DoAllFastUtil.doAllFast(toReset, instance -> {
            if (this.resetInstance(instance)) {
                synchronized (actionResults) {
                    actionResults.add(ActionResult.INSTANCE_RESET);
                }
            }
        });

        if (JultiOptions.getJultiOptions().useAffinity) {
            AffinityManager.ping();
        }
        return actionResults;
    }

    @Override
    public boolean resetInstance(MinecraftInstance instance, boolean bypassConditions) {
        List<MinecraftInstance> displayInstances = this.getDisplayInstances();

        if (super.resetInstance(instance, bypassConditions)) {
            if (displayInstances.contains(instance)) {
                this.displayInstancesIndices.set(displayInstances.indexOf(instance), null);
            }
            this.refreshDisplayInstances();
            return true;
        }
        return false;
    }

    // TODO: almost identical to DynamicWallResetManager - has to be a better way
    @Override
    public boolean lockInstance(MinecraftInstance instance) {
        // If super.lockInstance is true then it actually got locked and checked to unsquish
        if (super.lockInstance(instance)) {
            List<MinecraftInstance> displayInstances = this.getDisplayInstances();
            if (Collections.replaceAll(displayInstances, instance, null)) {
                this.saveDisplayInstances(displayInstances);
            }
            if (JultiOptions.getJultiOptions().dwReplaceLocked) {
                this.refreshDisplayInstances();
            }
            return true;
        }
        return false;
    }

    @Override
    public void notifyPreviewLoaded(MinecraftInstance instance) {
        List<MinecraftInstance> displayInstances = this.getDisplayInstances();
        if (displayInstances.contains(instance)) {
            return;
        }
        for (MinecraftInstance replaceCandidateInstance : displayInstances) {
            if (replaceCandidateInstance != null && replaceCandidateInstance.shouldCoverWithDirt()) {
                Collections.replaceAll(displayInstances, replaceCandidateInstance, instance);
                this.saveDisplayInstances(displayInstances);
                this.refreshBGInstances();
                return;
            }
        }
    }

    @Override
    public Rectangle getInstancePosition(MinecraftInstance instance, Dimension sceneSize) {
        JultiOptions jOptions = JultiOptions.getJultiOptions();
        CustomWallOptions cwOptions = CustomWallOptions.getCustomWallOptions();

        List<MinecraftInstance> displayInstances = this.getDisplayInstances();

        try {
            if (this.getLockedInstances().contains(instance)) {
                return this.getLockedInstancePosition(instance, sceneSize);
            } else if (!displayInstances.contains(instance)) {
                return this.getBackgroundInstancePosition(instance, sceneSize);
            }
        // TODO: divide by zero error - bad fix
        } catch (ArithmeticException e) {
            return new Rectangle(sceneSize.width, 0, 100, 100);
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

        if (cwOptions.currentLayout.lockVertical) {
            // Forcing 16:9 ratio for non-focus instance size
            Dimension lockedInstanceSize = new Dimension(
                    dwInnerSize.width,
                    !cwOptions.currentLayout.lockStretch ?
                            (int) (dwInnerSize.width * 9.0f / 16.0f) :
                            (dwInnerSize.height / this.getLockedInstances().size())
            );
            return new Rectangle(lockArea.x, lockArea.y + lockedInstanceSize.height * instanceIndex, lockedInstanceSize.width, lockedInstanceSize.height);
        }
        Dimension lockedInstanceSize = new Dimension(
                !cwOptions.currentLayout.lockStretch ?
                        (int) (dwInnerSize.height * 16.0f / 9.0f) :
                        (dwInnerSize.width / this.getLockedInstances().size()),
                dwInnerSize.height
        );
        return new Rectangle(lockArea.x + lockedInstanceSize.width * instanceIndex, lockArea.y, lockedInstanceSize.width, lockedInstanceSize.height);
    }

    private Rectangle getBackgroundInstancePosition(MinecraftInstance instance, Dimension sceneSize) {
        CustomWallOptions cwOptions = CustomWallOptions.getCustomWallOptions();

        Rectangle bgArea = cwOptions.currentLayout.bgArea;

        List<MinecraftInstance> bgInstances = this.getBGInstances();
        int instanceIndex = bgInstances.indexOf(instance);

        // Dimensions are manually defined in the custom wall layout, so sceneSize is ignored
        Dimension dwInnerSize = bgArea.getSize();

        if (cwOptions.currentLayout.bgVertical) {
            // Forcing 16:9 ratio for non-focus instance size
            Dimension bgInstanceSize = new Dimension(
                    dwInnerSize.width,
                    !cwOptions.currentLayout.bgStretch ?
                            (int) (dwInnerSize.width * 9.0f / 16.0f) :
                            (dwInnerSize.height / this.getBGInstances().size())
            );
            return new Rectangle(bgArea.x, bgArea.y + bgInstanceSize.height * instanceIndex, bgInstanceSize.width, bgInstanceSize.height);
        }
        Dimension bgInstanceSize = new Dimension(
                !cwOptions.currentLayout.bgStretch ?
                        (int) (dwInnerSize.height * 16.0f / 9.0f) :
                        (dwInnerSize.width / this.getBGInstances().size()),
                dwInnerSize.height
        );
        return new Rectangle(bgArea.x + bgInstanceSize.width * instanceIndex, bgArea.y, bgInstanceSize.width, bgInstanceSize.height);
    }
}
