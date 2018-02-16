package org.sceext.llsm_server

import javafx.application.Application
import javafx.application.Platform
import javafx.stage.Stage
import javafx.scene.Scene
import javafx.scene.Group
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.image.WritableImage
import javafx.scene.image.PixelWriter
import javafx.scene.image.PixelFormat


var main_window_size_x: Int = 1280
var main_window_size_y: Int = 720
var main_window_title: String = ""
var main_window: VideoWindow? = null


class VideoWindow() : Application() {
    lateinit var canvas: Canvas
    lateinit var gc: GraphicsContext
    lateinit var image: WritableImage
    lateinit var writer: PixelWriter

    var sx: Int = 0
    var sy: Int = 0

    override fun start(stage: Stage) {
        main_window = this
        sx = main_window_size_x
        sy = main_window_size_y
        // init image
        image = WritableImage(sx, sy)
        writer = image.pixelWriter

        stage.setTitle(main_window_title)

        val root = Group()
        canvas = Canvas(sx.toDouble(), sy.toDouble())
        gc = canvas.getGraphicsContext2D()
        root.getChildren().add(canvas)

        val scene = Scene(root, sx.toDouble(), sy.toDouble())
        stage.setScene(scene)
        stage.show()

        // init draw canvas
        gc.setFill(Color.BLACK)
        gc.fillRect(0.0, 0.0, sx.toDouble(), sy.toDouble())
    }

    fun _do_update_frame(size_x: Int, size_y: Int, data: ByteArray) {
        // load pixel data in image
        writer.setPixels(0, 0, size_x, size_y, PixelFormat.getByteRgbInstance(), data, 0, size_x * 3)

        gc.drawImage(image, 0.0, 0.0)
    }

    fun update_frame(size_x: Int, size_y: Int, data: ByteArray) {
        // TODO
        Platform.runLater(object: Runnable {
            override fun run() {
                _do_update_frame(size_x, size_y, data)
            }
        })
    }
}

class VideoThread(val screen_size_x: Int, val screen_size_y: Int, val debug: String) : Runnable {

    override fun run() {
        main_window_size_x = screen_size_x
        main_window_size_y = screen_size_y
        main_window_title = "LLSM: ${debug}  ${screen_size_x} x ${screen_size_y}"
        Application.launch(VideoWindow::class.java, "main")
    }
}
