package dk.aau.src.controller

import dk.aau.src.model.UserModel
import dk.aau.src.view.AccountCreationView
import dk.aau.src.view.LoginScreenView
import dk.aau.src.view.DashboardView

import javafx.beans.property.SimpleStringProperty
import org.openapitools.client.apis.UserApi
import org.openapitools.client.models.DeviceId
import org.openapitools.client.models.UserCredentials
import tornadofx.*

class LoginController: Controller(){
    val statusProperty = SimpleStringProperty("")
    var status by statusProperty
    val user: UserModel by inject()
    val userAPI: UserApi = UserApi()

    val api: Rest by inject()

    init{
        api.baseURI = "https://api.github.com/"
    }

    fun login(userCredentials: UserCredentials){
        try{
            var response = userAPI.login(userCredentials, DeviceId(""))
            user.name.value = response.username
            user.password.value = response.password
            runLater{
                find(LoginScreenView::class).replaceWith(DashboardView::class, sizeToScene = true, centerOnScreen = true)
            }
        }
        catch (e: Exception){
            runLater {
                status = "Could not login with given credentials"
            }
        }

    }

    fun switchToAccountCreation(){
        // Switch back to login screen
        runLater{
            primaryStage.uiComponent<UIComponent>()?.replaceWith(AccountCreationView::class, sizeToScene = true, centerOnScreen = true)
        }

    }

    fun logout(){
        user.item = null
        // Switch back to login screen
        runLater{
            primaryStage.uiComponent<UIComponent>()?.replaceWith(LoginScreenView::class, sizeToScene = true, centerOnScreen = true)
        }
    }
}