package org.sceext.llsm_server

import javafx.application.Application
import javafx.stage.Stage
import javafx.scene.Scene
import javafx.scene.layout.BorderPane

import org.bytedeco.javacpp.avutil.AVFrame

var main_window_size_x: Int = 1280
var main_window_size_y: Int = 720
var main_window: VideoWindow? = null


class VideoWindow() : Application() {

    override fun start(ps: Stage) {
        main_window = this

        val root = BorderPane()
        val scene = Scene(root, main_window_size_x.toDouble(), main_window_size_y.toDouble())
        // TODO
        ps.setScene(scene)
        ps.show()
    }

    fun update_frame(frame: AVFrame) {
        // TODO
    }
}

class VideoThread(val screen_size_x: Int, val screen_size_y: Int) : Runnable {

    override fun run() {
        main_window_size_x = screen_size_x
        main_window_size_y = screen_size_y
        Application.launch(VideoWindow::class.java, "main")
    }
}
