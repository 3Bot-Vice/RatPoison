package rat.poison.ui.uiPanels

import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.widget.VisTable
import com.kotcrab.vis.ui.widget.VisWindow
import com.kotcrab.vis.ui.widget.tabbedpane.Tab
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPane
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPaneAdapter
import rat.poison.App.uiAimOverridenWeapons
import rat.poison.App.uiBombWindow
import rat.poison.App.uiSpecList
import rat.poison.game.CSGO
import rat.poison.opened
import rat.poison.ui.tabs.*
import rat.poison.ui.uiUpdate
import kotlin.math.sign
import kotlin.system.exitProcess

val mainTabbedPane = TabbedPane()
    var aimTab = AimTab()
    var visualsTab = VisualsTab()
    var rcsTab = RcsTab()
    var miscTab = MiscTab()
    var ranksTab = RanksTab()
    var nadeHelperTab = NadeHelperTab()
    var skinChangerTab = SkinChangerTab()
    var optionsTab = OptionsTab()

private var wantedHeight = 565F
private var wantedWidth = 535F
private var isResizingHeight = false
private var isResizingWidth = false

fun updateVisWindowsNames() {
    updateKeybinds()
    uiSpecList.update()
    uiBombWindow.update()
    uiAimOverridenWeapons.update()
}

class UIMenu : VisWindow("Rat Poison 1.7") {
    init {
        defaults().left()

        addCloseButton()

        //Main ui window settings
        x = 960F
        y = 540F
        align(Align.topLeft)
        isResizable = false

        //Main content pane for all tabs
        val mainTabbedPaneContent = VisTable()
        mainTabbedPaneContent.padTop(10F)
        mainTabbedPaneContent.padBottom(10F)
        mainTabbedPaneContent.align(Align.top)

        //Scroll pane for the content pane, content pane goes inside
        val mainScrollPane = ScrollPane(mainTabbedPaneContent) //Init scroll pane containing main content pane
        mainScrollPane.setFlickScroll(false)
        mainScrollPane.setScrollbarsVisible(true)
        mainScrollPane.setSize(565F, 535F)

        //Add tabs to the tab header
        mainTabbedPane.add(aimTab)
        mainTabbedPane.add(visualsTab)
        mainTabbedPane.add(rcsTab)
        mainTabbedPane.add(miscTab)
        mainTabbedPane.add(ranksTab)
        mainTabbedPane.add(nadeHelperTab)
        mainTabbedPane.add(skinChangerTab)
        mainTabbedPane.add(optionsTab)

        //Set aim tab as the first (init) tab
        mainTabbedPane.switchTab(aimTab)
        //Add aim tab content to the table
        mainTabbedPaneContent.add(aimTab.contentTable).growX()

        //Tab switch listener
        mainTabbedPane.addListener(object : TabbedPaneAdapter() {
            override fun switchedTab(tab: Tab?) {
                if (tab == null) return

                mainTabbedPaneContent.clear()

                val normHeight = 565F //Fuck you too
                val normWidth = 535F

                when (tab) { //Update table content to tab selected content
                    aimTab -> {
                        wantedHeight = normHeight
                        wantedWidth = normWidth
                        changeWidth()
                        changeHeight()
                        mainTabbedPaneContent.add(aimTab.contentTable).growX()
                    }
                    optionsTab -> {
                        wantedHeight = normHeight
                        wantedWidth = normWidth
                        changeWidth()
                        changeHeight()
                        optionsTab.updateCFGList()
                        optionsTab.updateLocalizationsList()
                        mainTabbedPaneContent.add(optionsTab.contentTable).growX()
                    }
                    rcsTab -> {
                        wantedHeight = normHeight
                        wantedWidth = normWidth
                        changeWidth()
                        changeHeight()
                        mainTabbedPaneContent.add(rcsTab.contentTable).growX()
                    }
                    visualsTab -> {
                        wantedHeight = normHeight
                        wantedWidth = normWidth
                        changeWidth()
                        changeHeight()
                        mainTabbedPaneContent.add(visualsTab.contentTable).growX()
                    }
                    miscTab -> {
                        wantedHeight = normHeight
                        wantedWidth = normWidth
                        changeWidth()
                        changeHeight()
                        miscTab.updateHitSoundList()
                        mainTabbedPaneContent.add(miscTab.contentTable).growX()
                    }
                    ranksTab -> {
                        wantedHeight = normHeight
                        wantedWidth = normWidth
                        changeWidth()
                        changeHeight()
                        mainTabbedPaneContent.add(ranksTab.contentTable).growX()
                    }
                    nadeHelperTab -> {
                        wantedHeight = normHeight
                        wantedWidth = normWidth
                        changeWidth()
                        changeHeight()
                        nadeHelperTab.updateNadeFileHelperList()
                        mainTabbedPaneContent.add(nadeHelperTab.contentTable).growX()
                    }
                    skinChangerTab -> {
                        wantedHeight = if (CSGO.gameHeight < 1000F) {
                            CSGO.gameHeight.toFloat() - 100F
                        } else {
                            1000F
                        }
                        wantedWidth = normWidth
                        changeWidth()
                        changeHeight()
                        mainTabbedPaneContent.add(skinChangerTab.contentTable).growX()
                    }
                }
            }
        })

        //Add tab pane & scroll pane to main ui window
        add(mainTabbedPane.table).growX().minSize(25F).row()
        add(mainScrollPane).minSize(500F, 500F).prefSize(500F, 500F).align(Align.left).growX().growY().row()
        pack()
        centerWindow()

        //Update all tab content
        uiUpdate()
        changeWidth()
        changeHeight()
    }

    override fun positionChanged() {
        updateChilds()
    }

    // That's a sin
    fun updateTabs() {
        mainTabbedPane.remove(aimTab)
        mainTabbedPane.remove(visualsTab)
        mainTabbedPane.remove(rcsTab)
        mainTabbedPane.remove(miscTab)
        mainTabbedPane.remove(ranksTab)
        mainTabbedPane.remove(nadeHelperTab)
        mainTabbedPane.remove(skinChangerTab)
        mainTabbedPane.remove(optionsTab)
        aimTab = AimTab()
        visualsTab = VisualsTab()
        rcsTab = RcsTab()
        miscTab = MiscTab()
        ranksTab = RanksTab()
        nadeHelperTab = NadeHelperTab()
        skinChangerTab = SkinChangerTab()
        optionsTab = OptionsTab()
        mainTabbedPane.add(aimTab)
        mainTabbedPane.add(visualsTab)
        mainTabbedPane.add(rcsTab)
        mainTabbedPane.add(miscTab)
        mainTabbedPane.add(ranksTab)
        mainTabbedPane.add(nadeHelperTab)
        mainTabbedPane.add(skinChangerTab)
        mainTabbedPane.add(optionsTab)
        mainTabbedPane.switchTab(optionsTab)
        visualsTab.updateESPTabs()
    }


    fun updateChilds() {
        if (opened) {
            uiAimOverridenWeapons.setPosition(x+width+4F, y)
        }
    }

    override fun close() {
        exitProcess(0)
    }

    internal fun changeAlpha(alpha: Float) {
        color.a = alpha
        uiAimOverridenWeapons.color.a = alpha
    }

    private fun changeHeight() {
        if (!isResizingHeight) {
            isResizingHeight = true
            Thread(Runnable {
                while (true) {
                    val difHeight = wantedHeight - height
                    val dChange = sign(difHeight)*2

                    if (height in wantedHeight - 4..wantedHeight + 4) {
                        isResizingHeight = false
                        break
                    }

                    height += dChange
                    y -= dChange
                    Thread.sleep(1)
                }
            }).start()
        }
    }

    private fun changeWidth() {
        if (!isResizingWidth) {
            isResizingWidth = true
            Thread(Runnable {
                while (true) {
                    val difWidth = wantedWidth - width
                    val dChange = sign(difWidth)

                    if (width in wantedWidth - 4..wantedWidth + 4) {
                        isResizingWidth = false
                        break
                    }

                    width += 2*dChange

                    if (dChange > 0) { //This shit d u m b
                        x -= 1F
                    } else if (dChange < 0) {
                        x += 1F
                    }
                    //x -= dChange/2F
                    Thread.sleep(1)
                }
            }).start()
        }
    }
}

