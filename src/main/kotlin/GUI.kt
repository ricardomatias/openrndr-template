import org.openrndr.Program
import org.openrndr.color.ColorRGBa
import org.openrndr.panel.ControlManager
import org.openrndr.panel.controlManager
import org.openrndr.panel.elements.*
import org.openrndr.panel.style.*

interface GuiParams {
    var value: Double
}

class GUI(program: Program, params: GuiParams) {
    var onResetListener = {}

    fun onReset(listener: () -> Unit) {
        onResetListener = listener
    }

    val panel: ControlManager

    init {
        panel = program.controlManager {
            styleSheet(has class_ "container") {
                this.display = Display.FLEX
                this.flexDirection = FlexDirection.Row
                this.width = 200.px
                this.height = 100.percent
            }

            styleSheet(has class_ "sidebar") {
                this.width = 200.px
                this.paddingBottom = 20.px
                this.paddingTop = 10.px
                this.paddingLeft = 10.px
                this.paddingRight = 10.px
                this.marginRight = 2.px
                this.height = 100.percent
                this.background = Color.RGBa(ColorRGBa.GRAY.copy(a = 0.2)) // this is somewhat inconvenient
            }

            styleSheet(has type "dropdown-button") {
                this.width = 175.px
            }

            layout {
                div("container") {
                    id = "container"
                    div("sidebar") {
                        id = "sidebar"
                        slider {
                            label = "Value"
                            range = Range(0.0, 1.0)
                            precision = 3
                            bind(params::value)
                        }
                        button {
                            label = "Reset"
                            clicked {
                                onResetListener()
                            }
                        }
                    }
                }
            }
        }
    }
}