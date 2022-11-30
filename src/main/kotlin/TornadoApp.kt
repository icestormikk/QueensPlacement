import tornadofx.App
import tornadofx.launch
import views.MainView

class TornadoApp: App(MainView::class) {
    fun main(args: Array<String>) {
        launch<TornadoApp>(args)
    }
}