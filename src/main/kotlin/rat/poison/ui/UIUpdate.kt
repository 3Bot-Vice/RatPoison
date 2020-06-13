package rat.poison.ui

import rat.poison.App.uiAimOverridenWeapons
import rat.poison.App.uiMenu
import rat.poison.opened
import rat.poison.ui.tabs.*
import rat.poison.ui.tabs.visualstabs.*
import rat.poison.ui.uiPanelTables.overridenWeaponsUpdate

fun uiUpdate() {
    if (!opened) return

    overridenWeaponsUpdate()
    visualsTabUpdate()
    glowEspTabUpdate()
    chamsEspTabUpdate()
    indicatorEspTabUpdate()
    boxEspTabUpdate()
    hitMarkerTabUpdate()
    skinChangerTabUpdate()
    nadesVTUpdate()
    snaplinesEspTabUpdate()
    footStepsEspTabUpdate()
    miscTabUpdate()
    rcsTabUpdate()
    nadeHelperTabUpdate()
    updateTrig()
    updateAim()
    updateBacktrack()
    updateDisableEsp()

    //Update windows
    uiAimOverridenWeapons.setPosition(uiMenu.x+uiMenu.width+4F, uiMenu.y)
}