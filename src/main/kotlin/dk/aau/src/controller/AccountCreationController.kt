package dk.aau.src.controller

import dk.aau.src.view.LoginScreenView
import javafx.beans.property.SimpleStringProperty
import tornadofx.*

class AccountCreationController: Controller(){
    val statusProperty = SimpleStringProperty("")
    fun goBack(){
        // Switch back to login screen
        runLater {
            primaryStage.uiComponent<UIComponent>()?.replaceWith(LoginScreenView::class, sizeToScene = true, centerOnScreen = true)
        }
    }

    fun createAccount(username: String, password: String){
        print("Creating account with username: $username and password: $password... ")

        runLater() {
            statusProperty.value = "Account with username: $username created successfully..."
        }
    }
}