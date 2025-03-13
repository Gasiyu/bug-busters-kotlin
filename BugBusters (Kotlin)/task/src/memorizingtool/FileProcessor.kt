package memorizingtool

import java.io.BufferedReader
import java.io.BufferedWriter
import java.nio.file.Paths

class FileReader<T : Any>(private val converter: (String) -> T) {
    fun read(fileName: String): ArrayList<T> {
        val result = ArrayList<T>()
        BufferedReader(java.io.FileReader(Paths.get(fileName).toFile())).use { reader ->
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                line?.let { result.add(converter(it)) }
            }
        }
        return result
    }
}

class FileWriter<T : Any>(private val converter: (T) -> String) {
    fun write(fileName: String, data: ArrayList<T>) {
        BufferedWriter(java.io.FileWriter(Paths.get(fileName).toFile())).use { writer ->
            for (item in data) {
                writer.write(converter(item))
                writer.newLine()
                writer.flush()
            }
        }
    }
}
