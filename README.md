Plugin for [Julti](https://github.com/duncanruns/julti) which allows for custom wall layouts. Currently, one focus grid area, lock area and background instance area are supported.
Credits to [Resetti's](https://github.com/tesselslate/resetti) configuration.

### Installation and usage

[Download the .jar file from the latest release](https://github.com/draconix6/Julti-CustomWall/releases/latest), then in Julti, press Plugins > Open Plugins Folder, and place the .jar in this folder. Then, restart Julti.

Now, go back to Plugins > Open Custom Wall.
Here, you can modify the X, Y, width & height of the above areas. You can create and remove separate layouts if desired.

Once you're finished, click Save, and go to the main Julti options. In the Resetting section, change Style to Custom Wall.

### Additional options

The "Vertical" checkbox specifies whether instances in this area should sort themselves from top to bottom (enabled), or from left to right (disabled).
The "Stretch Instances To Fit" checkbox specifies whether instances in this area should stretch to fill the entire area, independent of how many instances are in the grid.
The best example of these is the classic Boyenn moving layout for [MultiResetWall](https://github.com/Specnr/MultiResetWall/):

![image](https://github.com/draconix6/Julti-CustomWall/assets/30545768/739b80bc-7942-4b85-9cfc-8d39c18fc274)
![Untitled](https://github.com/draconix6/Julti-CustomWall/assets/30545768/40bf5df9-d272-44f5-b510-9b5b06d12584)

If you would like to have each group of instances on their own "layer" in OBS (i.e. instances in the focus grid always appear above/below locked instances, etc.), click "Customize Layers" in the Custom Wall configuration, then reorder the items in the "layers" parameter as you desire. Click Save on the Layers panel, then close it and press Save on the Custom Wall configuration window as well. **Note**: this option requires at least Julti 1.4.0.

Credits to Priffin for the idea.
