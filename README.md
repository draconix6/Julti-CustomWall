Plugin for [Julti](https://github.com/duncanruns/julti) which allows for custom wall layouts. Currently, one focus grid area, lock area and background instance area are supported.
Credits to [Resetti's](https://github.com/tesselslate/resetti) configuration.

Usage:
[Download the .jar file from the latest release](https://github.com/draconix6/Julti-CustomWall/releases/latest), and place it in %UserProfile%/.Julti/plugins. Restart Julti if you had it open.
In Julti, go to Plugins > Open Custom Wall.
Here, you can modify the X, Y, width & height of the above areas. You can create and remove separate layouts if desired.

The "Vertical" checkbox specifies whether instances in this area should sort themselves from top to bottom (enabled), or from left to right (disabled).
The "Stretch Instances To Fit" checkbox specifies whether instances in this area should stretch to fill the entire area, independent of how many instances are in the grid.
The best example of these is the classic Boyenn moving layout for [MultiResetWall](https://github.com/Specnr/MultiResetWall/):

![image](https://github.com/draconix6/Julti-CustomWall/assets/30545768/739b80bc-7942-4b85-9cfc-8d39c18fc274)
![image](https://github.com/draconix6/Julti-CustomWall/assets/30545768/cd1ad339-783c-448b-ba65-1b640a43ea71)

After clicking Save, go to the main Julti options. In the Resetting section, change Style to Custom Wall.

If you would like to have each group of instances on their own "layer" in OBS (i.e. focus grid always overlaps locks, etc.) this will require additional setup:

Check the latest release, and download "custom-wall-obs-link.lua". Place this file in `%UserProfile%/.Julti`.

Click "Customize Layers" in the Custom Wall configuration, then reorder the items in the "layers" parameter as you desire. Click Save on the Layers panel, then close it and press Save on the Custom Wall configuration window as well.

In OBS, go to Tools > Scripts, and add a new script. Navigate to `%UserProfile%/.Julti`, and add custom-wall-obs-link.lua.

Credits to Priffin for the idea.
