package dk.aau.src.controller

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.ToJson
import dk.aau.src.model.UserModel
import dk.aau.src.utils.encodeFileForUpload
import dk.aau.src.utils.zipDir
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import org.openapitools.client.apis.AssignmentApi
import org.openapitools.client.apis.JobApi
import org.openapitools.client.infrastructure.Serializer
import org.openapitools.client.models.Job
import org.openapitools.client.models.UserCredentials

import tornadofx.*
import java.io.File
import java.util.*


class DashboardController: Controller(){
    val PATH_TO_DOWNLOAD_DIR = System.getProperty("user.home") + File.separator + "Downloads" + File.separator;

    var jobs = observableListOf<Job>(listOf<Job>(
    ))
    var jobAPI: JobApi = JobApi();
    var assignmentAPI: AssignmentApi = AssignmentApi();
    var workerRange = (1..10).toList().asObservable()
    var workersRequestedSelected = SimpleObjectProperty(1)
    var uploadPathTextField = SimpleStringProperty("Insert path to dir")
    var timeoutInMinute = SimpleStringProperty("30")
    val user: UserModel by inject()
    val adapter: JsonAdapter<Job> = Serializer.moshi.adapter(Job::class.java)

    init {
        try{
            var jobNewList = getJobsForUserParsed(user.getCredentials())

            runLater{
                jobs.clear()
                jobs.addAll(jobNewList)
            }

        }
        catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun uploadJob(pathToJobDir: String, workersRequested: Int, timeoutInMinutes: Int): Boolean{
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
            var f = File(pathToZipFile)
            jobAPI.postJob(userCredentials, workersRequested, f.name, timeoutInMinutes, encodeFileForUpload(pathToZipFile))
            // Add job to list for UI
            runLater{
                var newJobList = getJobsForUserParsed(user.getCredentials())
                jobs.clear()
                jobs.addAll(newJobList)
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
        try{
            jobAPI.deleteJob(job.id!!, UserCredentials(user.name.value, user.password.value))
            jobs.remove(job)
        }
        catch (e: Exception){
            println("Could not delete job $job")
        }
    }

    fun downloadResults(job: Job){
        try{
            var resultFile = jobAPI.getJobResult(job.id!!, user.getCredentials())
            println("Downloaded result files: $resultFile")

            var bytes = Base64.getDecoder()!!.decode(resultFile.data!!)

            File(PATH_TO_DOWNLOAD_DIR + File.separator + "result_" + job.name!!).writeBytes(bytes)
        }
        catch (e: Exception){
            println("Could not download results for job $job")
            println("Exception ${e} : ${e.message}")
        }
    }

    fun downloadJobFiles(job: Job){
        try{
            var jobFile = jobAPI.getJobFiles(job.id!!, user.getCredentials())

            var bytes = Base64.getDecoder()!!.decode(jobFile.data!!)


            File(PATH_TO_DOWNLOAD_DIR + File.separator + job.name!!).writeBytes(bytes)
        }
        catch (e: Exception){
            println("Could not download results for job $job")
            println("Exception: ${e.toString()}")
            println("Message: ${e.message}")
        }
    }

    private fun getJobsForUserParsed(userCredentials: UserCredentials): MutableList<Job>{
        try {
            var jsonResult = jobAPI.getJobsForUser(userCredentials)

            /* 4.3.1 version
            var newJobList: MutableList<Job> = mutableListOf()
            for(v in jsonResult){
                newJobList.add(v)
            }*/

            // 5.0.0-beta3 version
            var newJobList: MutableList<Job> = mutableListOf()

            val length: Int = jsonResult.size

            for (i in 0 until length){
                var j: Job? = adapter.fromJsonValue(jsonResult[i])
                if(j != null){
                    newJobList.add(j)
                }
            }

            return newJobList
        }
        catch (e: Exception){
            println("Could not get jobs for user: $userCredentials")
            return mutableListOf()
        }
    }
}
