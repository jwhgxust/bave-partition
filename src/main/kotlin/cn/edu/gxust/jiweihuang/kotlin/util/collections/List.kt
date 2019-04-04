package cn.edu.gxust.jiweihuang.kotlin.util.collections

fun fromString(dataString: String, separator: String = "\\s+",
               isIgnoreError: Boolean = true): List<Double> {
    val strList = dataString.split(separator.toRegex())
    val makeList = mutableListOf<Double>()
    strList.forEach { s ->
        var d: Double? = null
        try {
            d = s.trim().toDouble()
        } catch (nfe: NumberFormatException) {
            if (isIgnoreError) {
                return@forEach
            } else {
                nfe.printStackTrace()
            }
        } finally {
            if (d != null) {
                makeList.add(d)
            }
        }
    }
    return makeList.toList()
}