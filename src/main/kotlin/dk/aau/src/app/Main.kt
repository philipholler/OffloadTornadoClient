package dk.aau.src.app

import tornadofx.*

public const val SERVER_IP = "http://85.218.169.108:8080"

fun main(args: Array<String>){
    launch<DesktopClient>(args)
}