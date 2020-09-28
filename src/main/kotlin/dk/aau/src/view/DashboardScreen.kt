package dk.aau.src.view

import dk.aau.src.app.Styles
import dk.aau.src.controller.DashboardController
import dk.aau.src.controller.LoginController
import dk.aau.src.model.Job
import dk.aau.src.model.UserModel
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.TableView.CONSTRAINED_RESIZE_POLICY
import javafx.scene.layout.Priority
import javafx.scene.text.FontWeight
import tornadofx.*

class DashboardScreen : View("Offloading Dashboard"){
    val user: UserModel by inject()
    val loginController: LoginController by inject()
    val dashboardController: DashboardController by inject()

    val jobs = listOf(
            Job("Photo Analysis", "Started", 0, 5),
            Job("World peace", "Working", 2, 1),
            Job("Photoshop Trumps hair", "Done", 15, 15),
    ).asObservable()

    override val root = hbox(){
        setPrefSize(1000.0, 700.0)


        // Left side containing the tableview of jobs assigned
        hbox{
            alignment = Pos.TOP_LEFT
            paddingAll = 10.0

            tableview(jobs){
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
            alignment = Pos.TOP_RIGHT

            hbox{
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

            label(user.name) {
                style {
                    fontWeight = FontWeight.BOLD
                    fontSize = 24.px
                }
            }

        }
    }
}