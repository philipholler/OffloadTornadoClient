package dk.aau.src.view

import dk.aau.src.app.Styles
import dk.aau.src.controller.LoginController
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.text.FontWeight
import tornadofx.*

class LoginScreen : View() {
    val model = ViewModel()
    val username = model.bind{SimpleStringProperty()}
    val password = model.bind{SimpleStringProperty()}
    val loginController: LoginController by inject()
    val TEXTFIELD_WIDTH = 200.0

    override val root = form {
        setPrefSize(800.0, 600.0)
            fieldset(title, labelPosition = Orientation.VERTICAL) {
                hbox {
                    alignment = Pos.CENTER
                    label("OFFLOADR"){
                        addClass(Styles.heading)
                        paddingBottom = 20
                    }
                }

                field("Username") {
                    alignment = Pos.CENTER
                    textfield(username).required()
                    maxWidth = TEXTFIELD_WIDTH
                }
                field("Password") {
                    alignment = Pos.CENTER
                    passwordfield(password).required()
                    maxWidth = TEXTFIELD_WIDTH
                }
                vbox{
                    alignment = Pos.CENTER
                    paddingTop = 10
                    button("Log in") {
                        enableWhen(model.valid)
                        isDefaultButton = true
                        maxWidth = TEXTFIELD_WIDTH
                        action {
                            runAsyncWithProgress {
                                loginController.login(username.value, password.value)
                            }
                        }
                    }
                }

                vbox{
                    alignment = Pos.CENTER
                    label(loginController.statusProperty){
                        style{
                            paddingTop = 20
                            textFill = javafx.scene.paint.Color.RED
                            fontWeight = FontWeight.BOLD

                        }
                    }
                }
        }


    }

    override fun onDock() {
        username.value = ""
        password.value = ""
        // Remove decorators (f.x. this field is required on username and password)
        model.clearDecorators()
        super.onDock()
    }
}
