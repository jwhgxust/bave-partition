package cn.edu.gxust.jiweihuang.kotlin.util.collections

import java.io.File
import java.io.FileWriter
import java.io.IOException

fun <K, V> Map<K, V>.toColString(): String {
    val i = entries.iterator()
    if (!i.hasNext()) return ""
    val sb = StringBuilder()
    while (true) {
        val e = i.next()
        val key = e.key
        val value = e.value
        sb.append(if (key === this) "(this Map)" else key)
        sb.append(',')
        sb.append(if (value === this) "(this Map)" else value)
        if (!i.hasNext())
            return sb.toString()
        sb.append("\n")
    }
}

fun <K, V> Map<K, V>.toRowString(): String {
    val i = entries.iterator()
    if (!i.hasNext())
        return "{}"
    val sb = StringBuilder()
    sb.append('{')
    while (true) {
        val e = i.next()
        val key = e.key
        val value = e.value
        sb.append(if (key === this) "(this Map)" else key)
        sb.append('=')
        sb.append(if (value === this) "(this Map)" else value)
        if (!i.hasNext())
            return sb.append('}').toString()
        sb.append(',').append(' ')
    }
}

fun <K, V> Map<K, V>.toColCSV(csvFile: String) {
    val directory = File(File(csvFile).parent)
    if (!directory.exists()) {
        directory.mkdirs()
    }
    var fw: FileWriter? = null
    try {
        fw = FileWriter(csvFile)
        val it = entries.iterator()
        if (!it.hasNext()) return
        while (true) {
            val e = it.next()
            val key = e.key
            val value = e.value
            if (e === this) {
                fw.write("(this Map)")
            } else {
                fw.write(key.toString())
                fw.write(",")
                fw.write(value.toString())
            }
            if (!it.hasNext()) {
                break
            }
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

fun <K, V> Map<K, V>.toRowCSV(csvFile: String) {
    val directory = File(File(csvFile).parent)
    if (!directory.exists()) {
        directory.mkdirs()
    }
    var fw: FileWriter? = null
    try {
        fw = FileWriter(csvFile)
        val it = entries.iterator()
        if (!it.hasNext()) return
        while (true) {
            val e = it.next()
            val key = e.key
            val value = e.value
            if (e === this) {
                fw.write("(this Map)")
            } else {
                fw.write(key.toString())
                fw.write("=")
                fw.write(value.toString())
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