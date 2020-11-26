package dk.aau.src.model

import javafx.beans.property.SimpleStringProperty
import org.openapitools.client.models.UserCredentials
import tornadofx.*
import javax.json.JsonObject

class User : JsonModel{
    val nameProperty = SimpleStringProperty()
    private var name by nameProperty
    val passwordProperty = SimpleStringProperty()
    private var password by passwordProperty

    override fun updateModel(json: JsonObject) {
        with(json){
            name = string("name")
            password = string("password")
        }
    }
}

class UserModel : ItemViewModel<User>() {
    val name = bind(User::nameProperty)
    val password = bind(User::passwordProperty)

    fun getCredentials():UserCredentials{
        return UserCredentials(name.value, password.value)
    }
}
