package dk.aau.src.utils

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FileDataPart
import com.github.kittinunf.fuel.core.Method
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.result.Result
import dk.aau.src.model.Job
import java.io.File

val PATH_TO_DOWNLOAD_DIR = System.getProperty("user.home") + File.separator + "Downloads" + File.separator;
val BASE_API_PATH = "http://localhost:8080"

class JobAPI{
    companion object HTTPRequests{

        fun getJobsForUser(username: String){
            var jobList: List<Job> = emptyList()
            val httpAsync = Fuel.get(BASE_API_PATH + "/users/username,${username}/jobs")
                    .responseString { request, response, result ->
                        println("Sending http request: $request")
                        when (result) {
                            is Result.Failure -> {
                                val ex = result.getException()
                                println("HTTP REQUEST FAILED WITH EXCEPTION: $ex")
                            }
                            is Result.Success -> {
                                val data = response.data // This holds the file data
                                println(data)
                            }
                        }
                    }

            httpAsync.join()
        }

        fun downLoadJobFile(job: Job, username: String){
            val httpAsync = Fuel.get(BASE_API_PATH + "/jobs/" + job.id, listOf("username" to username))
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
                    .add(FileDataPart(File(pathToZipFile), name = "file", filename=fileName))
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