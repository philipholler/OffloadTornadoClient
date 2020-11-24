package dk.aau.src.controller

import dk.aau.src.model.UserModel
import dk.aau.src.utils.zipDir
import io.swagger.client.apis.JobApi
import io.swagger.client.models.Job
import io.swagger.client.models.UserCredentials
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import java.io.File
import java.lang.Exception

class DashboardController: Controller(){
    var jobs = observableListOf(listOf(
            Job()
    ))
    var jobAPI: JobApi = JobApi();
    var workerRange = (1..5).toList().asObservable()
    var workersRequestedSelected = SimpleObjectProperty(1)
    var uploadPathTextField = SimpleStringProperty("Insert path to dir")
    val user: UserModel by inject()

    init {
        try{
            var result = jobAPI.getJobsForUser(UserCredentials(user.name.value, user.password.value))
            for (r in result){
                println(r.toString())
            }
            jobs.clear()
            jobs.addAll(result)
        }
        catch(e: Exception){
            e.printStackTrace()
        }
    }

    fun uploadJob(pathToJobDir: String, workersRequested: Int): Boolean{
        val file = File(pathToJobDir)
        var pathToZipFile = pathToJobDir

        // Only zip directory, if it not already a zipped
        if(file.isDirectory){
            // Zip the folder given and save it to the same path
            zipDir(pathToJobDir, "$pathToJobDir.zip")
            pathToZipFile = "$pathToJobDir.zip"
        }

        val userCredentials = UserCredentials(user.name.value, user.password.value)

        try {
            val fileBytes = File(pathToZipFile).readBytes().toTypedArray()
            jobAPI.postJob(workersRequested, userCredentials, fileBytes)
            // Add job to list for UI
            runLater{
                var result = jobAPI.getJobsForUser(userCredentials)
                jobs.clear()
                jobs.addAll(result)
                for(r in jobs){
                    println(r)
                }
                uploadPathTextField.set("Insert path to dir")
            }
            return true
        }
        catch (e: Exception) {
            println("Could not upload job. Exception occured: ${e.javaClass}")
        }

        return false
    }

    fun deleteJob(job: Job){
        jobs.remove(job)
    }

    fun downloadResults(job: Job){
        println("Downloading $job")
    }

    fun downloadJobFiles(job: Job){
        println("Downloading job files for $job")
    }
}