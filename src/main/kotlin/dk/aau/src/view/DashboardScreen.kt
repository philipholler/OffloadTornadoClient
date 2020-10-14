package dk.aau.src.view

import dk.aau.src.controller.DashboardController
import dk.aau.src.controller.LoginController
import dk.aau.src.model.Job
import dk.aau.src.model.UserModel
import javafx.geometry.Pos
import javafx.scene.control.ChoiceBox
import javafx.scene.control.TableView.CONSTRAINED_RESIZE_POLICY
import javafx.scene.layout.Priority
import javafx.scene.paint.Color
import tornadofx.*

class DashboardScreen : View("Offloading Dashboard"){
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
                readonlyColumn("Hosts requested", Job::hostsRequested)
                readonlyColumn("Hosts Assigned", Job::hostsAssigned)
                readonlyColumn("Status", Job::status)
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
                            textfield(dbController.textFieldValue)
                            prefWidth = 400.0
                            paddingRight = 10
                        }
                        hbox {
                            field {
                                button("Select dir") {
                                    action {
                                        dbController.textFieldValue.value = chooseDirectory("Select Target Directory").toString()
                                    }
                                }
                                paddingRight = 10
                                minWidth = 80.0
                            }
                            field {
                                choicebox(dbController.selectedHost, dbController.hostRange) {
                                    
                                }
                                minWidth = 70.0
                                paddingRight = 10
                            }
                        }
                    }
                    hbox{
                        maxWidth = 550.0
                        paddingTop = 20
                        paddingRight = 15
                        alignment = Pos.BOTTOM_RIGHT
                        button("Upload Job"){
                            action {
                                runAsyncWithProgress {
                                    dbController.uploadJob(dbController.textFieldValue.value,
                                            dbController.selectedHost.value)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}



