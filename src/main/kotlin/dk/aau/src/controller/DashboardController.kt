package dk.aau.src.controller

import dk.aau.src.model.Job
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.ObservableList
import tornadofx.*

class DashboardController: Controller(){
    var jobs = observableListOf(listOf(
            Job("Photo Analysis", "Started", 0, 5),
            Job("World peace", "Working", 2, 1),
            Job("Photoshop Trumps hair", "Done", 15, 15)))
    var hostRange = (0..25).toList().asObservable()
    var selectedHost = SimpleObjectProperty(0)
    var textFieldValue = SimpleStringProperty("Insert path to dir")

    fun uploadJob(pathToJobDir: String, numbOfHostsRequested: Int): Boolean{
        // Check if job lives up to criteria
        if (true){
            runLater{
                val name = pathToJobDir.subSequence(pathToJobDir.lastIndexOf('/') + 1, pathToJobDir.length).toString()
                val job: Job = Job(name, "Created", 0, numbOfHostsRequested)
                jobs.add(job)
            }
            return true
        }
        return false
    }
}