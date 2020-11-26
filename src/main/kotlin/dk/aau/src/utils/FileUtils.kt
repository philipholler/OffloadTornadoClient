package dk.aau.src.utils

import java.io.*
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

fun encodeFileForUpload(path: String): ByteArray{
    var f = File(path)
    var encoded = Base64.getEncoder().encodeToString(f.readBytes())
    return encoded.toByteArray();
}


fun zipDir(directory: String, destPath: String) {
    val sourceFile = File(directory)

    ZipOutputStream(BufferedOutputStream(FileOutputStream(destPath))).use {
        it.use {
            zipFiles(it, sourceFile, "")
        }
    }
}

private fun zipFiles(zipOut: ZipOutputStream, sourceFile: File, parentDirPath: String) {
    val data = ByteArray(2048)

    for (f in sourceFile.listFiles()) {
        if (f.isDirectory) {
            val entry = ZipEntry(f.name + File.separator)
            entry.time = f.lastModified()
            entry.isDirectory
            entry.size = f.length()

            zipOut.putNextEntry(entry)

            //Call recursively to add files within this directory
            zipFiles(zipOut, f, f.name)
        } else {
            if (!f.name.contains(".zip")) { //If folder contains a file with extension ".zip", skip it
                FileInputStream(f).use { fi ->
                    BufferedInputStream(fi).use { origin ->
                        val path = parentDirPath + File.separator + f.name
                        val entry = ZipEntry(path)
                        entry.time = f.lastModified()
                        entry.isDirectory
                        entry.size = f.length()
                        zipOut.putNextEntry(entry)
                        while (true) {
                            val readBytes = origin.read(data)
                            if (readBytes == -1) {
                                break
                            }
                            zipOut.write(data, 0, readBytes)
                        }
                    }
                }
            } else {
                zipOut.closeEntry()
                zipOut.close()
            }
        }
    }
}