package cn.edu.gxust.jiweihuang.kotlin.util.collections

import java.io.File
import java.io.FileWriter
import java.io.IOException


fun <E> Collection<E>.toColString(): String {
    val it = this.iterator()
    if (!it.hasNext())
        return ""
    val sb = StringBuilder()
    while (true) {
        val e = it.next()
        sb.append(if (e === this) "(this Collection)" else e)
        if (!it.hasNext())
            return sb.toString()
        sb.append("\n")
    }
}

fun <E> Collection<E>.toRowString(): String {
    val it = this.iterator()
    if (!it.hasNext())
        return "[]"
    val sb = StringBuilder()
    sb.append("[")
    while (true) {
        val e = it.next()
        sb.append(if (e === this) "(this Collection)" else e)
        if (!it.hasNext())
            return sb.append("]").toString()
        sb.append(",").append("\b")
    }
}

fun <E> Collection<E>.toColCSV(csvFile: String) {
    val directory = File(File(csvFile).parent)
    if (!directory.exists()) {
        directory.mkdirs()
    }
    var fw: FileWriter? = null
    try {
        fw = FileWriter(csvFile)
        val it = this.iterator()
        if (!it.hasNext()) return
        while (true) {
            val e = it.next()
            if (e === this) {
                fw.write("(this Collection)")
            } else {
                fw.write(e.toString())
            }
            if (!it.hasNext()) break
            fw.write("\n")
        }
    } catch (ioe: IOException) {
        ioe.printStackTrace()
    } finally {
        try {
            fw?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}

fun <E> Collection<E>.toRowCSV(csvFile: String) {
    val directory = File(File(csvFile).parent)
    if (!directory.exists()) {
        directory.mkdirs()
    }
    var fw: FileWriter? = null
    try {
        fw = FileWriter(csvFile)
        val it = this.iterator()
        if (!it.hasNext()) return
        while (true) {
            val e = it.next()
            if (e === this) {
                fw.write("(this Collection)")
            } else {
                fw.write(e.toString())
            }
            if (!it.hasNext()) {
                break
            }
            fw.write(",")
        }
    } catch (ioe: IOException) {
        ioe.printStackTrace()
    } finally {
        try {
            fw?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}