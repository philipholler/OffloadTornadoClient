package dk.aau.src.controller

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.Method
import com.github.kittinunf.result.Result;
import dk.aau.src.model.Job
import dk.aau.src.utils.zipDir
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import java.io.*

val PATH_TO_DOWNLOAD_DIR = System.getProperty("user.home") + File.separator + "Downloads" + File.separator;
val API_PATH = "http://localhost:8080/jobs"

class DashboardController: Controller(){
    var jobs = observableListOf(listOf(
            Job("0","hej.py.zip", "Computing", 5, 5, "LINK"),
            Job("1","legitjob.txt", "Computing", 5, 5, "LINK")
    ))
    var hostRange = (1..25).toList().asObservable()
    var selectedHost = SimpleObjectProperty(1)
    var textFieldValue = SimpleStringProperty("Insert path to dir")

    fun uploadJob(pathToJobDir: String, numbOfHostsRequested: Int): Boolean{

        // Only zip directory, if it not already a zipped
        if(pathToJobDir.substring(pathToJobDir.length - 4) != ".zip"){
            // Zip the folder given and save it to the same path
            zipDir(pathToJobDir, "$pathToJobDir.zip")
        }

        // POST FILE.... todo

        // Add job to list for UI
        runLater{
            val name = pathToJobDir.subSequence(pathToJobDir.lastIndexOf('/') + 1, pathToJobDir.length).toString()
            val job = Job("0", name, "Created", 0, numbOfHostsRequested, "LINK")
            jobs.add(job)
        }
        return true
    }

    fun deleteJob(job: Job){
        jobs.remove(job)
    }

    fun downloadResults(job: Job){
        println("Downloading $job")
    }

    fun downloadJobFiles(job: Job){
        println("Downloading job files for $job")

        val httpAsync = Fuel.get(API_PATH, listOf("name" to job.name, "id" to job.id))
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
}