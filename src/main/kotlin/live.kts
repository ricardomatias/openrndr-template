@file:Suppress("UNUSED_LAMBDA_EXPRESSION")
import org.openrndr.Program
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.loadFont
import org.openrndr.draw.loadImage
import org.openrndr.draw.renderTarget
import org.openrndr.extensions.Screenshots
import org.openrndr.extra.compositor.*
import org.openrndr.extra.fx.blend.Multiply
import org.openrndr.extra.fx.blur.ApproximateGaussianBlur
import org.openrndr.extra.fx.distort.HorizontalWave
import org.openrndr.extra.fx.distort.VerticalWave
import org.openrndr.extra.olive.Reloadable
import org.openrndr.extra.parameters.*
import org.openrndr.extra.gui.GUI
import org.openrndr.shape.Rectangle
import org.openrndr.text.Cursor
import org.openrndr.text.writer
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

{ program: Program ->
    program.apply {
        val rt = renderTarget(WIDTH, HEIGHT, contentScale = 2.0) {
            colorBuffer()
            depthBuffer()
        }

        val canvas = renderTarget(WIDTH, HEIGHT, contentScale = 2.0) {
            colorBuffer()
        }

        val gui = GUI()
        val params = object : Reloadable() {
            @DoubleParameter("x", 0.0, 770.0)
            var x: Double = 385.0

            @DoubleParameter("y", 0.0, 500.0)
            var y: Double = 385.0

            @DoubleParameter("separation", -150.0, 150.0)
            var separation: Double = 0.0

            @ColorParameter("background")
            var background = ColorRGBa.PINK
        }

        params.reload()

        gui.add(params, "General")

        // -- create a composite
        val composite = compose {
            layer {
                // -- load the image inside the layer
                val image = loadImage("data/images/cheeta.jpg")
                val gapX = (w - image.effectiveWidth) / 2.0
                val gapY = (w - image.effectiveHeight) / 2.0

                draw {
                    drawer.image(image, origin.x + gapX, origin.y + gapY)
                }
            }

            // -- add a second layer with text and a drop shadow
            layer {
                // -- notice how we load the font inside the layer
                // -- this only happens once
                val font = loadFont("data/fonts/IBMPlexMono-Regular.ttf", 112.0)
                draw {
                    drawer.fill = ColorRGBa.BLACK
                    drawer.fontMap = font
                    val message = "HELLO WORLD"
                    writer {
                        box = Rectangle(0.0, 0.0, width * 1.0, height * 1.0)
                        val w = textWidth(message)
                        cursor = Cursor((width - w) / 2.0, height / 2.0)
                        text(message)
                    }
                }
                // -- this effect is processed first
                post(gui.add(HorizontalWave())) {
                    amplitude = cos(seconds * PI) * 0.1
                    frequency = sin(seconds * PI * 0.5) * 4
                    segments = (1 + Math.random() * 20).toInt()
                    phase = seconds
                }
                // -- this is the second effect
                post(gui.add(VerticalWave())) {
                    amplitude = sin(seconds * PI) * 0.1
                    frequency = cos(seconds * PI * 0.5) * 4
                    phase = seconds
                }
                // -- and this effect is processed last
                post(gui.add(ApproximateGaussianBlur())) {
                    sigma = cos(seconds * 0.5 * PI) * 5.0 + 5.01
                    window = 25
                }
            }
        }

        extend(paletteStudio)
        extend(gui)
        extend(Screenshots()) {
            scale = 2.0
        }
        extend {
            composite.draw(drawer)
        }
    }
}