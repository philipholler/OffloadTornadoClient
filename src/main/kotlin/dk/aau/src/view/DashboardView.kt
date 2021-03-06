package dk.aau.src.view

import dk.aau.src.controller.DashboardController
import dk.aau.src.controller.LoginController
import dk.aau.src.model.UserModel
import javafx.geometry.Pos
import javafx.scene.control.ContextMenu
import javafx.scene.control.TableView.CONSTRAINED_RESIZE_POLICY
import javafx.scene.layout.Priority
import org.openapitools.client.models.Job
import tornadofx.*

class DashboardView : View("Offloading Dashboard"){
    val user: UserModel by inject()
    val loginController: LoginController by inject()
    val dbController: DashboardController by inject()

    override val root = hbox(){
        setPrefSize(1000.0, 700.0)

        // Left side containing the tableview of jobs assigned
        hbox{
            alignment = Pos.TOP_LEFT
            paddingAll = 10.0

            tableview(dbController.jobs){
                minWidth = 500.0
                columnResizePolicy = CONSTRAINED_RESIZE_POLICY
                vboxConstraints {
                    vGrow = Priority.ALWAYS
                }
                readonlyColumn("Job", Job::name)
                readonlyColumn("Workers requested", Job::answersNeeded)
                readonlyColumn("Workers Assigned", Job::workersAssigned)
                readonlyColumn("Status", Job::status)
                readonlyColumn("Confidence level", Job::confidenceLevel)
                readonlyColumn("Upload time", Job::timestamp)

                contextMenu = ContextMenu().apply {
                    item("Delete"){
                        action {
                            selectedItem?.apply {
                                dbController.deleteJob(this)
                            }
                        }
                    }
                    item("Download Result"){
                        action {
                            selectedItem?.apply {
                                dbController.downloadResults(this)
                            }
                        }
                    }
                    item("Download Job File"){
                        action {
                            selectedItem?.apply {
                                dbController.downloadJobFiles(this)
                            }
                        }
                    }
                }
            }

        }


        // Right side of window
        vbox{
            paddingAll = 10.0
            fitToParentSize()


            hbox{
                alignment = Pos.TOP_RIGHT
                vbox{
                    paddingTop = 5
                    button("Update table").action(dbController::fetchJobsForUser)
                    label ("Offload Jobs")
                }

                spacer(){
                    hgrow = Priority.ALWAYS
                    vgrow = Priority.ALWAYS
                }

                button("Logout").action(loginController::logout)
            }

            spacer() {
                hgrow = Priority.SOMETIMES
                maxHeight = 500.0
            }

            form {
                alignment = Pos.BOTTOM_LEFT
                fitToParentSize()

                fieldset() {
                    fitToParentWidth()
                    hbox {
                        vbox {
                            // Path to file
                            textfield(dbController.uploadPathTextField)
                            prefWidth = 400.0
                            paddingRight = 10
                        }
                        hbox {
                            field {
                                button("Select dir") {
                                    action {
                                        dbController.uploadPathTextField.value = chooseDirectory("Select Target Directory").toString()
                                    }
                                }
                                paddingRight = 10
                                minWidth = 80.0
                            }

                        }

                    }
                    vbox{
                        paddingTop = 10
                        alignment = Pos.TOP_LEFT
                        vbox{
                            text("Workers:")
                            field {
                                choicebox(dbController.workersRequestedSelected, dbController.workerRange) {
                                }
                                minWidth = 70.0
                                paddingRight = 10
                            }
                        }
                        vbox{
                            paddingTop = 10
                            text("Timeout in minutes")

                            field {
                                textfield(dbController.timeoutInMinute) {
                                    filterInput { it.controlNewText.isInt() }
                                }
                                minWidth = 70.0
                                paddingRight = 10
                            }
                        }
                    }

                    hbox{
                        maxWidth = 485.0
                        paddingTop = 20
                        paddingRight = 15
                        alignment = Pos.BOTTOM_RIGHT
                        button("Upload Job"){
                            action {
                                runAsyncWithProgress {
                                    dbController.uploadJob(dbController.uploadPathTextField.value,
                                            dbController.workersRequestedSelected.value,
                                            dbController.timeoutInMinute.value.toInt())

                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDock() {
        dbController.jobs.clear()
        dbController.fetchJobsForUser()
        super.onDock()
    }
}



