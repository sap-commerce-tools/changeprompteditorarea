# Change Prompt Editor Area

Warn the user that he/she will lose unsaved changes when a new item loaded.


## Installation

1. Download the latest release
1. Unpack the zip to `hybris/bin/custom/`
1. Rename the folder (remove the version suffix from the name)
       
       mv changeprompteditorarea-* changeprompteditorarea
       
1. Add `changeprompteditorarea` to your `localextensions.xml`
1. Build

## Notes

Getting the custom controller to work involves patching the OOTB backoffice widget
config files via `buildcallback.xml`, because there is no easy way yet(!) to 
modify / replace existing widgets.

## Known Limitations

- "Save & Continue" does not show any validation warnings. If the dirty item has validation errors,
  the button is not available.

- This extension only modifies the Backoffice Editor Area so it prompts the user
  when the currently displayed item is "dirty".

  This means that the list view will may a different selected item, as will the
  tree view if you change types, if the user cancels. 

  To prevent those changes too, one would have to create some kind of logical gate
  between all involved widgets that block the propagation of the change if the item
  in the editor area is "dirty", and maybe even send additional socket events to
  reset widgets to their previous state...