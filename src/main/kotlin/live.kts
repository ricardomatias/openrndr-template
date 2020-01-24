@file:Suppress("UNUSED_LAMBDA_EXPRESSION")
import org.openrndr.Program
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.colorBuffer
import org.openrndr.draw.loadImage
import org.openrndr.draw.renderTarget
import org.openrndr.extensions.Screenshots
import org.openrndr.extra.compositor.*
import org.openrndr.extra.olive.Reloadable
import org.openrndr.math.Vector2
import org.openrndr.plugins.gui.*
import org.openrndr.shape.Rectangle
import kotlin.math.roundToInt

{ program: Program ->
    program.apply {
        val rt = renderTarget(WIDTH, HEIGHT, contentScale = 2.0) {
            colorBuffer()
            depthBuffer()
        }

        val canvas = renderTarget(WIDTH, HEIGHT, contentScale = 2.0) {
            colorBuffer()
        }

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

        data class Pixel(val rect: Rectangle, val size: Double, val color: ColorRGBa)

        val pixels = mutableListOf<Pixel>()

        for (x in 0 until 76) {
            for (y in 0 until 57) {
                val color = shadow[((x / 76.0) * image.effectiveWidth).roundToInt(), ((y / 57.0) * image.effectiveHeight).roundToInt()]
                val rectangle = Rectangle(Vector2(origin.x + x * 10.0, origin.y + y * 10.0), 10.0, 10.0)
                val pixel = Pixel(rectangle, 10.0, color)
                pixels.add(pixel)
            }
        }

        val drawing = compose {
            draw {
                drawer.stroke = null

                for (pixel in pixels) {
                    drawer.fill = pixel.color
                    drawer.rectangles(pixel.rect)
                }
            }
        }

        extend(paletteStudio)
        extend(gui) {
            add(params)
        }
        extend(Screenshots()) {
            scale = 2.0
        }
        extend {
            drawing.draw(drawer)
        }
    }
}