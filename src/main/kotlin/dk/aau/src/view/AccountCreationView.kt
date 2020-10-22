package dk.aau.src.view

import dk.aau.src.app.Styles
import dk.aau.src.controller.AccountCreationController
import dk.aau.src.controller.LoginController
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.text.FontWeight
import tornadofx.*

class AccountCreationView : View() {
    val model = ViewModel()
    val username = model.bind { SimpleStringProperty() }
    val password = model.bind { SimpleStringProperty() }
    val password2 = model.bind { SimpleStringProperty() }
    val accountCreationController: AccountCreationController by inject()
    val TEXTFIELD_WIDTH = 200.0

    override val root = form {
        setPrefSize(1000.0, 700.0)

        fieldset(title, labelPosition = Orientation.VERTICAL) {
            hbox {
                alignment = Pos.CENTER
                label("Create Account") {
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
            field("Repeat Password") {
                alignment = Pos.CENTER
                passwordfield(password2).required()
                maxWidth = TEXTFIELD_WIDTH
            }
        }

        vbox {
            alignment = Pos.CENTER
            paddingTop = 10
            button("Create account"){
                enableWhen(model.valid)
                isDefaultButton = true
                action{
                    if (password.value == password2.value){

                        accountCreationController.createAccount(username.value, password.value)

                        username.value = ""
                        password.value = ""
                        password2.value = ""
                        model.clearDecorators()
                    }
                    else {
                        accountCreationController.statusProperty.value = "Passwords do not match"
                    }

                }
            }

        }

        vbox {
            alignment = Pos.CENTER
            label(accountCreationController.statusProperty) {
                style {
                    paddingTop = 20
                    fontSize = 15.px
                    fontWeight = FontWeight.BOLD
                }
            }
        }

        button("Go back").action(accountCreationController::goBack)
    }

    override fun onDock() {
        username.value = ""
        password.value = ""
        password2.value = ""
        // Remove decorators (f.x. this field is required on username and password)
        model.clearDecorators()
        super.onDock()
    }
}
