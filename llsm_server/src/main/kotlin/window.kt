package org.sceext.llsm_server

import javafx.application.Application
import javafx.application.Platform
import javafx.stage.Stage
import javafx.scene.Scene
import javafx.scene.Group
import javafx.scene.paint.Color
import javafx.scene.image.WritableImage
import javafx.scene.image.PixelWriter
import javafx.scene.image.PixelFormat
import javafx.scene.image.ImageView
import javafx.beans.value.ObservableValue
import javafx.beans.value.ChangeListener
import javafx.event.EventHandler
import javafx.scene.input.KeyEvent
import javafx.scene.input.KeyCode
import javafx.scene.input.MouseEvent


var main_window_size_x: Int = 1280
var main_window_size_y: Int = 720
var main_window_title: String = ""
var main_window: VideoWindow? = null

class VideoWindow() : Application() {
    lateinit var image: WritableImage
    lateinit var writer: PixelWriter
    lateinit var iv: ImageView
    lateinit var _stage: Stage
    lateinit var root: Group

    var sx: Int = 0
    var sy: Int = 0

    override fun start(stage: Stage) {
        _stage = stage

        main_window = this
        sx = main_window_size_x
        sy = main_window_size_y
        // init image
        image = WritableImage(sx, sy)
        writer = image.pixelWriter

        stage.setTitle(main_window_title)

        iv = ImageView(image)
        iv.setFitWidth(sx.toDouble())
        iv.setFitHeight(sy.toDouble())
        iv.setPreserveRatio(true)
        iv.setSmooth(true)
        iv.setCache(true)

        root = Group()
        root.getChildren().add(iv)

        val scene = Scene(root, sx.toDouble(), sy.toDouble())
        scene.setFill(Color.BLACK)
        val window_size_listener = object: ChangeListener<Number> {
            override fun changed(observable: ObservableValue<out Number>, old_value: Number, new_value: Number) {
                val size_x = scene.getWidth()
                val size_y = scene.getHeight()
                _on_window_resize(size_x, size_y)
            }
        }
        scene.widthProperty().addListener(window_size_listener)
        scene.heightProperty().addListener(window_size_listener)
        scene.setOnKeyTyped(object: EventHandler<KeyEvent> {
            override fun handle(event: KeyEvent) {
                when(event.code) {
                    // F11 for fullscreen
                    KeyCode.F11 -> {
                        // TODO
                        _change_fullscreen()
                    }
                    else -> {
                        // nothing todo
                    }
                }
            }
        })
        scene.setOnMouseClicked(object: EventHandler<MouseEvent> {
            override fun handle(event: MouseEvent) {
                //root.requestFocus()

                // double click to fullscreen
                if (event.clickCount > 1) {
                    _change_fullscreen()
                }
            }
        })

        stage.setScene(scene)
        stage.show()

        root.requestFocus()
    }

    fun _change_fullscreen() {
        if (_stage.isFullScreen()) {
            _stage.setFullScreen(false)
        } else {
            _stage.setFullScreen(true)
        }
    }

    fun _on_window_resize(size_x: Double, size_y: Double) {
        iv.setFitWidth(size_x)
        iv.setFitHeight(size_y)
    }

    fun _do_update_frame(size_x: Int, size_y: Int, data: ByteArray) {
        // load pixel data in image
        writer.setPixels(0, 0, size_x, size_y, PixelFormat.getByteRgbInstance(), data, 0, size_x * 3)
    }

    fun update_frame(size_x: Int, size_y: Int, data: ByteArray) {
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
