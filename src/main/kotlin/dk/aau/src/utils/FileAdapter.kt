package dk.aau.src.utils

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.io.File
import java.io.FileInputStream
import java.lang.StringBuilder
import java.util.*

class FileAdapter {
    @ToJson
    fun toJson(file: File) {
        var sb: StringBuilder = StringBuilder()

        file.forEachLine { sb.append(it) }

        return
    }

    @FromJson
    fun fromJson(s: String) = UUID.fromString(s)
}