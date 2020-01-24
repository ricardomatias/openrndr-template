import org.openrndr.Program
import org.openrndr.application
import org.openrndr.extra.olive.Olive
import org.openrndr.extra.olive.Resources
import org.openrndr.extra.palette.PaletteStudio
import org.openrndr.math.Vector2
import org.openrndr.plugins.fps.FPS

val olive = Olive<Program>(Resources(listOf(".yaml")))
val paletteStudio = PaletteStudio()

const val WIDTH = 768
const val HEIGHT = 768
const val w = WIDTH.toDouble()
const val h = HEIGHT.toDouble()
const val w2 = w / 2.0
const val h2 = h / 2.0

val origin = Vector2(200.0, 0.0)

fun main() = application {
    configure {
        width = WIDTH + origin.x.toInt()
        height = HEIGHT
    }
    program {
        extend(FPS()) {
            corner = FPS.Corner.BOTTOM_LEFT
        }
        extend(olive)
    }
}