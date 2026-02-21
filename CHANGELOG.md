[1.1.3]

**Fixed**
* Potion duration shown in drink tooltips not matching the actual applied effect duration
* Duplicate application of food-based effects when consuming drinks
* Prevented a server crash when tea leaf drying tick runs on a blockstate without the DRYING property

**Changed**
* Tooltips now match the style of other Let’s Do mods
* Minor texture improvements

**Added** 
* Ko_Kr translation (thanks to cs8minty)
*** 

[1.1.2]

**Fixed**
* Loot tables for `wild_coffee`, `wild_rooibos`, `wild_yerba_mate`, `hibiscus`, `lavender` now properly drop the plant when harvested with Shears or Silk Touch
* StoveBlock crash on place/load 

***

[1.1.1]

**Fixed**
* TeaCupBlock no longer crashes on dedicated servers by removing client-only class references
* Corrected block destruction handling to ensure potion contents drop properly outside Creative mode
* All flower loot tables unified with vanilla grass-style logic (normal drop = flower, with shears = tea_blossom)

***

[1.1.0]

** Ported to 1.21.1 **

***

[1.0.12]

**Fixed**
* `dried_green_tea_leaf_block` and `dried_out_green_tea_leaf_block` not being included in the `mineable` tag
