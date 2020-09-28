package dk.aau.src.controller

import dk.aau.src.model.UserModel
import dk.aau.src.view.LoginScreen
import dk.aau.src.view.DashboardScreen
import javafx.beans.property.SimpleStringProperty
import tornadofx.*

class LoginController: Controller(){
    val statusProperty = SimpleStringProperty("")
    var status by statusProperty
    val user : UserModel by inject()

    val api : Rest by inject()

    init{
        api.baseURI = "https://api.github.com/"
    }
    fun login(username: String, password: String){
        if(username == "admin" && password == "admin"){
            runLater{
                find(LoginScreen::class).replaceWith(DashboardScreen::class, sizeToScene = true, centerOnScreen = true)
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
                find(LoginScreen::class).replaceWith(DashboardScreen::class, sizeToScene = true, centerOnScreen = true)
            } else {
                status = json.string("message") ?: "Login failed"
            }
        }
    }

    fun logout(){
        user.item = null
        // Switch back to login screen
        primaryStage.uiComponent<UIComponent>()?.replaceWith(LoginScreen::class, sizeToScene = true, centerOnScreen = true)
    }
}