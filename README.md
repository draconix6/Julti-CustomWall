Plugin for [Julti](https://github.com/duncanruns/julti) which allows for custom wall layouts. Currently, one focus grid area, lock area and background instance area are supported.
Credits to [Resetti's](https://github.com/tesselslate/resetti) configuration.

Usage:
Download the latest release, and place it in %UserProfile%\.Julti\plugins. Restart Julti if you had it open.
In Julti, go to Plugins > Open Custom Wall.
Here, you can modify the X, Y, width & height of the above areas. You can create and remove separate layouts if desired.

The "Vertical" checkbox specifies whether instances in this area should sort themselves from top to bottom (enabled), or from left to right (disabled).
The "Stretch Instances To Fit" checkbox specifies whether instances in this area should stretch to fill the entire area, independent of how many instances are in the grid.
The best example of these is the classic Boyenn moving layout for [MultiResetWall](https://github.com/Specnr/MultiResetWall/):

![image](https://github.com/draconix6/Julti-CustomWall/assets/30545768/739b80bc-7942-4b85-9cfc-8d39c18fc274)
![image](https://github.com/draconix6/Julti-CustomWall/assets/30545768/cd1ad339-783c-448b-ba65-1b640a43ea71)

The "Replace Locked Instances" checkbox behaves the same as it does with Julti's default dynamic wall setting - if enabled, instances that are locked on the focus grid will be replaced with a background instance immediately.

After clicking Save, go to the main Julti options. In the Resetting section, change Style to Custom Wall.
