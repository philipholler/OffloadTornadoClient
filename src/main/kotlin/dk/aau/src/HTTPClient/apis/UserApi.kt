/**
 * Offloading
 * Semester project for 7th semester at Aalborg University
 *
 * OpenAPI spec version: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */
package io.swagger.client.apis

import io.swagger.client.models.DeviceId
import io.swagger.client.models.UserCredentials

import io.swagger.client.infrastructure.*

class UserApi(basePath: kotlin.String = "http://localhost:8080") : ApiClient(basePath) {

    /**
     * 
     * Creates a user
     * @param userCredentials Credentials used to create new user 
     * @return UserCredentials
     */
    @Suppress("UNCHECKED_CAST")
    fun createUser(userCredentials: UserCredentials): UserCredentials {
        
        val localVariableConfig = RequestConfig(
                RequestMethod.POST,
                "/users/{userCredentials}".replace("{" + "userCredentials" + "}", "$userCredentials")
        )
        val response = request<UserCredentials>(
                localVariableConfig
        )

        return when (response.responseType) {
            ResponseType.Success -> (response as Success<*>).data as UserCredentials
            ResponseType.Informational -> TODO()
            ResponseType.Redirection -> TODO()
            ResponseType.ClientError -> throw ClientException((response as ClientError<*>).body as? String ?: "Client error")
            ResponseType.ServerError -> throw ServerException((response as ServerError<*>).message ?: "Server error")
        }
    }
    /**
     * 
     * Deletes a user
     * @param userCredentials Credentials for user that should be deleted 
     * @return UserCredentials
     */
    @Suppress("UNCHECKED_CAST")
    fun deleteUser(userCredentials: UserCredentials): UserCredentials {
        
        val localVariableConfig = RequestConfig(
                RequestMethod.DELETE,
                "/users/{userCredentials}".replace("{" + "userCredentials" + "}", "$userCredentials")
        )
        val response = request<UserCredentials>(
                localVariableConfig
        )

        return when (response.responseType) {
            ResponseType.Success -> (response as Success<*>).data as UserCredentials
            ResponseType.Informational -> TODO()
            ResponseType.Redirection -> TODO()
            ResponseType.ClientError -> throw ClientException((response as ClientError<*>).body as? String ?: "Client error")
            ResponseType.ServerError -> throw ServerException((response as ServerError<*>).message ?: "Server error")
        }
    }
    /**
     * 
     * Fakes a login
     * @param userCredentials Credentials used to login user 
     * @param deviceId If logged in from a worker (optional)
     * @return UserCredentials
     */
    @Suppress("UNCHECKED_CAST")
    fun login(userCredentials: UserCredentials, deviceId: DeviceId? = null): UserCredentials {
        val localVariableQuery: MultiValueMap = mapOf("deviceId" to listOf("$deviceId"))
        val localVariableConfig = RequestConfig(
                RequestMethod.GET,
                "/users/{userCredentials}".replace("{" + "userCredentials" + "}", "$userCredentials"), query = localVariableQuery
        )
        val response = request<UserCredentials>(
                localVariableConfig
        )

        return when (response.responseType) {
            ResponseType.Success -> (response as Success<*>).data as UserCredentials
            ResponseType.Informational -> TODO()
            ResponseType.Redirection -> TODO()
            ResponseType.ClientError -> throw ClientException((response as ClientError<*>).body as? String ?: "Client error")
            ResponseType.ServerError -> throw ServerException((response as ServerError<*>).message ?: "Server error")
        }
    }
}
