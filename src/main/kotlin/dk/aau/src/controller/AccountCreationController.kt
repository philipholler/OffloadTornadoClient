package dk.aau.src.controller

import dk.aau.src.app.SERVER_IP
import dk.aau.src.view.LoginScreenView
import javafx.beans.property.SimpleStringProperty
import org.openapitools.client.apis.UserApi
import org.openapitools.client.models.UserCredentials
import tornadofx.*
import java.lang.Exception

class AccountCreationController: Controller(){
    val statusProperty = SimpleStringProperty("")
    var userApi: UserApi = UserApi(SERVER_IP)

    fun goBack(){
        // Switch back to login screen
        runLater {
            primaryStage.uiComponent<UIComponent>()?.replaceWith(LoginScreenView::class, sizeToScene = true, centerOnScreen = true)
        }
    }

    fun createAccount(username: String, password: String){
        print("Creating account with username: $username and password: $password... ")

        try{
            var userCredentials: UserCredentials = userApi.createUser(UserCredentials(username, password))
            runLater() {
                statusProperty.value = "Account with username: $username created successfully..."
            }
        }
        catch (e: Exception){
            runLater() {
                statusProperty.value = "Could not create user with username: $username. Exception occured: ${e.javaClass}"
            }
        }


    }
}