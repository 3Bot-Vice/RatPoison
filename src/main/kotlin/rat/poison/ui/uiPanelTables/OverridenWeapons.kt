package rat.poison.ui.uiPanelTables

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.widget.*
import rat.poison.curSettings
import rat.poison.settings.*
import rat.poison.strToBool
import rat.poison.toWeaponClass
import rat.poison.ui.changed
import rat.poison.ui.uiHelpers.overrideWeaponsUI.OverrideVisCheckBoxCustom
import rat.poison.ui.uiHelpers.overrideWeaponsUI.OverrideVisSliderCustom
import rat.poison.ui.uiPanels.overridenWeapons
import rat.poison.ui.uiUpdate
import kotlin.math.round

var weaponOverrideSelected = "DESERT_EAGLE"

class OverridenWeapons : VisTable(true) {
    //private val table = VisTable(true)

    var categorySelected = "PISTOL"
    var weaponOverride = false
    var enableOverride = false

    private val categorySelectionBox = VisSelectBox<String>()

    //Override Weapon Checkbox & Selection Box
    private val categorySelectLabel = VisLabel("Category: ")

    private val weaponOverrideSelectionBox = VisSelectBox<String>()
    val weaponOverrideEnableCheckBox = VisCheckBox("Enable Override")

    val enableFactorRecoil = OverrideVisCheckBoxCustom("Factor Recoil", "tFRecoil")
    val enableFlatAim = OverrideVisCheckBoxCustom("Write Angles", "tFlatAim")
    val enablePathAim = OverrideVisCheckBoxCustom("Mouse Movement", "tPathAim")
    val enableScopedOnly = OverrideVisCheckBoxCustom("Scoped Only", "tScopedOnly")

    private val aimBoneLabel = VisLabel("Bone: ")
    val aimBoneBox = VisSelectBox<String>()

    val aimFov = OverrideVisSliderCustom("FOV", "tAimFov", 1F, 180F, 1F, true, width1 = 125F, width2 = 125F)
    val aimSpeed = OverrideVisSliderCustom("Speed", "tAimSpeed", 0F, 10F, 1F, true, width1 = 125F, width2 = 125F)
    val aimSmoothness = OverrideVisSliderCustom("Smooth", "tAimSmooth", 1F, 5F, .1F, false, width1 = 125F, width2 = 125F)
    val aimAfterShots = OverrideVisSliderCustom("Aim After #", "tAimAfterShots", 0F, 10F, 1F, true, width1 = 125F, width2 = 125F)

    //Perfect Aim Collapsible
    val perfectAimCheckBox = OverrideVisCheckBoxCustom("Perfect Aim", "tPerfectAim")
    private val perfectAimTable = VisTable()
    val perfectAimCollapsible = CollapsibleWidget(perfectAimTable)
    val perfectAimFov = OverrideVisSliderCustom("FOV", "tPAimFov", 1F, 180F, 1F, true, width1 = 125F, width2 = 125F)
    val perfectAimChance = OverrideVisSliderCustom("Chance", "tPAimChance", 1F, 100F, 1F, true, width1 = 125F, width2 = 125F)

    init {
        align(Align.left)

        //Create Category Selector Box
        val categorySelection = VisTable()
        categorySelectionBox.setItems("PISTOL", "RIFLE", "SMG", "SNIPER", "SHOTGUN")
        categorySelectionBox.selected = "PISTOL"
        categorySelected = categorySelectionBox.selected
        categorySelection.add(categorySelectLabel).padRight(125F-categorySelectLabel.width)
        categorySelection.add(categorySelectionBox).width(125F)

        categorySelectionBox.changed { _, _ ->
            categorySelected = categorySelectionBox.selected
            when (categorySelected)
            {
                "PISTOL" -> { weaponOverrideSelectionBox.clearItems(); weaponOverrideSelectionBox.setItems("DESERT_EAGLE", "DUAL_BERRETA", "FIVE_SEVEN", "GLOCK", "USP_SILENCER", "CZ75A", "R8_REVOLVER", "P2000", "TEC9", "P250") }
                "SMG" -> { weaponOverrideSelectionBox.clearItems(); weaponOverrideSelectionBox.setItems("MAC10", "P90", "MP5", "UMP45", "MP7", "MP9", "PP_BIZON") }
                "RIFLE" -> { weaponOverrideSelectionBox.clearItems(); weaponOverrideSelectionBox.setItems("AK47", "AUG", "FAMAS", "SG553", "GALIL", "M4A4", "M4A1_SILENCER", "NEGEV", "M249") }
                "SNIPER" -> { weaponOverrideSelectionBox.clearItems(); weaponOverrideSelectionBox.setItems("AWP", "G3SG1", "SCAR20", "SSG08") }
                "SHOTGUN" -> { weaponOverrideSelectionBox.clearItems(); weaponOverrideSelectionBox.setItems("XM1014", "MAG7", "SAWED_OFF", "NOVA") }
            }

            if (categorySelected == "SNIPER") {
                enableScopedOnly.disable(false)
            } else {
                enableScopedOnly.disable(true)
            }

            if (categorySelected == "RIFLE" || categorySelected == "SMG") {
                aimAfterShots.disable(false, Color(255F, 255F, 255F, 1F))
            } else {
                aimAfterShots.disable(true, Color(105F, 105F, 105F, .2F))
            }

            weaponOverrideSelectionBox.selected = weaponOverrideSelectionBox.items[0]
            uiUpdate()
            true
        }

        //Create Override Weapon Selector
        val weaponOverrideSelection = VisTable()
        weaponOverrideSelectionBox.setItems("DESERT_EAGLE", "DUAL_BERRETA", "FIVE_SEVEN", "GLOCK", "USP_SILENCER", "CZ75A", "R8_REVOLVER", "P2000", "TEC9", "P250")
        weaponOverrideSelectionBox.selected = "DESERT_EAGLE"
        weaponOverrideSelected = weaponOverrideSelectionBox.selected
        weaponOverrideSelection.add(weaponOverrideSelectionBox).width(125F).padLeft(125F)

        weaponOverrideSelectionBox.changed { _, _ ->
            if (!weaponOverrideSelectionBox.selected.isNullOrEmpty()) {
                weaponOverrideSelected = weaponOverrideSelectionBox.selected
            }
            uiUpdate()
            true
        }
        //End Override Weapon Selection Box

        //Create Enable Override Toggle
        weaponOverrideEnableCheckBox.isChecked = curSettings[weaponOverrideSelected].toWeaponClass().tOverride
        weaponOverrideEnableCheckBox.changed { _, _ ->
            val curWep = curSettings[weaponOverrideSelected].toWeaponClass()
            curWep.tOverride = weaponOverrideEnableCheckBox.isChecked
            curSettings[weaponOverrideSelected] = curWep.toString()
            enableOverride = weaponOverrideEnableCheckBox.isChecked
            uiUpdate()
            true
        }

        //Create Aim Bone Selector Box
        val aimBone = VisTable()
        aimBoneBox.setItems("HEAD", "NECK", "CHEST", "STOMACH", "NEAREST", "RANDOM")
        aimBoneBox.selected = when (curSettings[categorySelected + "_AIM_BONE"].toInt()) {
            HEAD_BONE -> "HEAD"
            NECK_BONE -> "NECK"
            CHEST_BONE -> "CHEST"
            STOMACH_BONE -> "STOMACH"
            NEAREST_BONE -> "NEAREST"
            else -> "RANDOM"
        }
        aimBone.add(aimBoneLabel).width(125F)
        aimBone.add(aimBoneBox).width(125F)

        aimBoneBox.changed { _, _ ->
            val setBone = curSettings[aimBoneBox.selected + "_BONE"].toInt()

            if (weaponOverride) {
                val curWep = curSettings[weaponOverrideSelected].toWeaponClass()
                curWep.tAimBone = setBone
                curSettings[weaponOverrideSelected] = curWep.toString()
            }
        }

        //Create Perfect Aim Collapsible Check Box
        perfectAimCollapsible.setCollapsed(!curSettings[categorySelected + "_PERFECT_AIM"].strToBool(), true)

        perfectAimTable.add(perfectAimFov).padLeft(20F).left().row()
        perfectAimTable.add(perfectAimChance).padLeft(20F).left().row()

        perfectAimCheckBox.changed { _, _ ->
            perfectAimCollapsible.setCollapsed(!perfectAimCollapsible.isCollapsed, true)
        }
        //End Perfect Aim Collapsible Check Box


        //Add all items to label for tabbed pane content
        add(categorySelection).left().row()
        add(weaponOverrideSelection).left().row()
        add(weaponOverrideEnableCheckBox).left().row()
        add(enableFactorRecoil).left().row()
        add(enableFlatAim).left().row()
        add(enablePathAim).left().row()
        add(enableScopedOnly).left().row()
        add(aimBone).left().row()
        add(aimSpeed).left().row()
        add(aimFov).left().row()
        add(aimSmoothness).left().row()
        add(aimAfterShots).left().row()
        add(perfectAimCheckBox).left().row()
        add(perfectAimCollapsible).left().row()
    }
}

fun overridenWeaponsUpdate() { //This isn't needed... because the Override Weapons hides it
    overridenWeapons.apply {
        val curWep = curSettings[weaponOverrideSelected].toWeaponClass()

        overridenWeapons.weaponOverrideEnableCheckBox.isChecked = curWep.tOverride

        if (categorySelected == "SNIPER") {
            enableScopedOnly.disable(false)
        } else {
            enableScopedOnly.disable(true)
        }

        enableFactorRecoil.update()
        enableFlatAim.update()
        enablePathAim.update()
        enableScopedOnly.update()

        if (categorySelected == "RIFLE" || categorySelected == "SMG") {
            aimAfterShots.disable(false, Color(255F, 255F, 255F, 1F))
        } else {
            aimAfterShots.disable(true, Color(105F, 105F, 105F, .2F))
        }

        aimBoneBox.selected = when (curWep.tAimBone) {
            HEAD_BONE -> "HEAD"
            NECK_BONE -> "NECK"
            CHEST_BONE -> "CHEST"
            STOMACH_BONE -> "STOMACH"
            NEAREST_BONE -> "NEAREST"
            else -> "RANDOM"
        }
        aimFov.update()
        aimSpeed.update()
        aimSmoothness.update()
        aimAfterShots.update()

        perfectAimCheckBox.isChecked = curWep.tPerfectAim
        perfectAimCollapsible.isCollapsed = !curWep.tPerfectAim
        perfectAimFov.update()
        perfectAimChance.update()
    }
}