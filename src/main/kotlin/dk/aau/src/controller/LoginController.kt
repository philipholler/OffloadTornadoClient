package dk.aau.src.controller

import dk.aau.src.model.UserModel
import dk.aau.src.view.AccountCreationView
import dk.aau.src.view.LoginScreenView
import dk.aau.src.view.DashboardView
import javafx.beans.property.SimpleStringProperty
import tornadofx.*

class LoginController: Controller(){
    val statusProperty = SimpleStringProperty("")
    var status by statusProperty
    val user: UserModel by inject()

    val api: Rest by inject()

    init{
        api.baseURI = "https://api.github.com/"
    }
    fun login(username: String, password: String){
        if(username == "admin" && password == "admin"){
            runLater{
                find(LoginScreenView::class).replaceWith(DashboardView::class, sizeToScene = true, centerOnScreen = true)
            }
            return
        }

        runLater{
            status = ""
        }
        api.setBasicAuth(username, password)
        val response = api.get("user")
        val json = response.one()
        runLater{
            if (response.ok()){
                user.item = json.toModel()
                find(LoginScreenView::class).replaceWith(DashboardView::class, sizeToScene = true, centerOnScreen = true)
            } else {
                status = json.string("message") ?: "Login failed"
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