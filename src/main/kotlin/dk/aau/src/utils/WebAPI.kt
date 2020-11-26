package dk.aau.src.utils

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FileDataPart
import com.github.kittinunf.fuel.core.Method
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.result.Result
import dk.aau.src.model.Job
import org.openapitools.client.models.UserCredentials

import java.io.File


/*
 * THIS IS THE OLD API INTERFACE
 */

val PATH_TO_DOWNLOAD_DIR = System.getProperty("user.home") + File.separator + "Downloads" + File.separator;
val BASE_API_PATH = "http://localhost:8080"

fun main() {
    println(JobAPI.getJobsForUser(UserCredentials("admin", "admin")))
    JobAPI.downLoadJobFile(Job("3", "myfile", "", 0, 2, ""), UserCredentials("admin", "admin"))
}

class JobAPI{
    companion object HTTPRequests{
        fun getJobsForUser(userCredentials: UserCredentials){
            var jobList: List<Job> = emptyList()
            val httpAsync = Fuel.get(BASE_API_PATH + "/jobs/UserCredentials(username=${userCredentials.username},password=${userCredentials.password})")
                    .responseString { request, response, result ->
                        println("Sending http request: $request")
                        when (result) {
                            is Result.Failure -> {
                                val ex = result.getException()
                                println("HTTP REQUEST FAILED WITH EXCEPTION: $ex")
                            }
                            is Result.Success -> {
                                val data = result.value // This holds the file data
                                println(data)
                            }
                        }
                    }

            httpAsync.join()
        }

        fun downLoadJobFile(job: Job, userCredentials: UserCredentials){
            val httpAsync = Fuel.get(BASE_API_PATH + "/jobs/UserCredentials(username=${userCredentials.username},password=${userCredentials.password})/${job.id}/files")
                    .responseString { request, response, result ->
                        println("Sending http request: $request")
                        when (result) {
                            is Result.Failure -> {
                                val ex = result.getException()
                                println("HTTP REQUEST FAILED WITH EXCEPTION: $ex")
                            }
                            is Result.Success -> {
                                val data = response.data // This holds the file data
                                val path = PATH_TO_DOWNLOAD_DIR + job.name
                                File(path).writeBytes(data)
                            }
                        }
                    }

            httpAsync.join()
        }

        fun postJob(pathToFile: String, username: String){
            val file = File(pathToFile)
            var pathToZipFile = pathToFile
            var fileName = file.name

            // Only zip directory, if it not already a zipped
            if(file.isDirectory){
                // Zip the folder given and save it to the same path
                zipDir(pathToFile, "$pathToFile.zip")
                pathToZipFile = "$pathToFile.zip"
            }

            println("Path to zip file: $pathToZipFile")

            val httpAsync = Fuel.upload(BASE_API_PATH + "/jobs", Method.POST, listOf("username" to username))
                    .add(FileDataPart(File(pathToZipFile), name = "file", filename="${fileName}.zip"))
                    .responseString { request, response, result ->
                        println("Sending http request: $request")
                        when (result) {
                            is Result.Failure -> {
                                val ex = result.getException()
                                println("HTTP REQUEST FAILED WITH EXCEPTION: $ex")
                            }
                            is Result.Success -> {
                                val data = response.data // This holds the file data
                                println("Success: ")
                                println(data.toString())
                            }
                        }
                    }
            httpAsync.join()
        }
    }
}

class UserAPI{
    companion object HTTPRequests{
        fun createUser(username: String){
            Fuel.post(BASE_API_PATH + "/users")
                    .body("{ \"username\" : \"${username}\" }")
                    .responseString { request, response, result ->
                        println("Sending http request: $request")
                        when (result) {
                            is Result.Failure -> {
                                val ex = result.getException()
                                println("HTTP REQUEST FAILED WITH EXCEPTION: $ex")
                            }
                            is Result.Success -> {
                                val data = response.data // This holds the file data
                                println("Success: ")
                                println(data.toString())
                            }
                        }
                    }
        }
    }
}