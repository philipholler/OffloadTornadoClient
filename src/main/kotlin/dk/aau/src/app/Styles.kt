package dk.aau.src.app

import javafx.geometry.Pos
import javafx.scene.text.FontWeight
import tornadofx.Stylesheet
import tornadofx.box
import tornadofx.cssclass
import tornadofx.px

class Styles : Stylesheet() {
    companion object {
        val heading by cssclass()
    }

    init {
        label and heading {
            padding = box(20.px)
            fontSize = 50.px
            fontWeight = FontWeight.BOLD
            alignment = Pos.CENTER
        }
        root {
            prefHeight = 700.px
            prefWidth = 1000.px
        }
    }


}

