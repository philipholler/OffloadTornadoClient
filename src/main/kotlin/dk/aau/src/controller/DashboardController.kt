package dk.aau.src.controller

import dk.aau.src.model.Job
import dk.aau.src.model.UserModel
import dk.aau.src.utils.JobAPI
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*

class DashboardController: Controller(){
    var jobs = observableListOf(listOf(
            Job("0","hej.py.zip", "Computing", 5, 5, "LINK"),
            Job("1","legitjob.txt", "Computing", 5, 5, "LINK")
    ))
    var workerRange = (1..5).toList().asObservable()
    var workersRequestedSelected = SimpleObjectProperty(1)
    var uploadPathTextField = SimpleStringProperty("Insert path to dir")
    val user: UserModel by inject()

    init {
        JobAPI.getJobsForUser(username = user.name.value)
    }

    fun uploadJob(pathToJobDir: String, workersRequested: Int): Boolean{
        JobAPI.postJob(pathToJobDir, user.name.value)

        // Add job to list for UI
        runLater{
            val name = pathToJobDir.subSequence(pathToJobDir.lastIndexOf('/') + 1, pathToJobDir.length).toString()
            val job = Job("0", name, "Created", 0, workersRequested, "LINK")
            jobs.add(job)
            uploadPathTextField.set("Insert path to dir")
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

        JobAPI.downLoadJobFile(job, user.name.value)
    }
}