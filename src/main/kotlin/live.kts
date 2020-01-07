@file:Suppress("UNUSED_LAMBDA_EXPRESSION")
import org.openrndr.draw.loadImage
import org.openrndr.extra.compositor.*
import kotlin.math.roundToInt

{ program: CompositorSketch ->
    program.apply {
        val image = loadImage("data/images/pm5544.png")
        val shadow = image.shadow

        shadow.download()

        val drawing = compose {
            draw {
                for (x in 0 until 76) {
                    for (y in 0 until 57) {
                        drawer.stroke = null
                        drawer.fill = shadow[((x / 76.0) * image.effectiveWidth).roundToInt(), ((y / 57.0) * image.effectiveHeight).roundToInt()]
                        drawer.rectangle(x * 10.0, y * 10.0, 10.0, 10.0)
                    }
                }
            }
        }

        extend {
            drawing.draw(drawer)
        }
    }
}