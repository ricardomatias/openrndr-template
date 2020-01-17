@file:Suppress("UNUSED_LAMBDA_EXPRESSION")
import org.openrndr.draw.loadImage
import org.openrndr.extra.compositor.*
import org.openrndr.extra.olive.Reloadable
import org.openrndr.plugins.gui.*
import kotlin.math.roundToInt

{ program: CompositorSketch ->
    program.apply {
        val params = object : Reloadable() {
            @GuiIntParam("Int", 1, 10, 0)
            var intVal = 2
            @GuiDoubleParam("Double", 0.0, 1.0, 2)
            var doubleVal = 10.0
        }

        params.reload()

        val gui = GUI()
        val image = loadImage("data/images/pm5544.png")
        val shadow = image.shadow

        shadow.download()

        val drawing = compose {
            draw {
                for (x in 0 until 76) {
                    for (y in 0 until 57) {
                        drawer.stroke = null
                        drawer.fill = shadow[((x / 76.0) * image.effectiveWidth).roundToInt(), ((y / 57.0) * image.effectiveHeight).roundToInt()]
                        drawer.rectangle(origin.x + x * 10.0, origin.y + y * 10.0, 10.0, 10.0)
                    }
                }
            }
        }

        extend(gui) {
            add(params)
        }
        extend {
            drawing.draw(drawer)
        }
    }
}