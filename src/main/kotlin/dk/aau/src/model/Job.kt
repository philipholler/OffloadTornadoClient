package dk.aau.src.model

class Job(val id: String, val name: String, val status: String, val hostsAssigned: Int, val hostsRequested: Int, val downloadLink: String){


    override fun toString(): String {
        return "Job(id='$id', name='$name', status='$status', hostsAssigned=$hostsAssigned, hostsRequested=$hostsRequested, downloadLink='$downloadLink')"
    }
}

