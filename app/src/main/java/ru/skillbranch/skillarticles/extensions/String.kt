package ru.skillbranch.skillarticles.extensions

fun String.indexesOf(subStr: String, ignoreCase: Boolean = true): List<Int> {
    val result = ArrayList<Int>()
    if (this.isNotEmpty() && subStr.isNotEmpty()) {
        var lastIndex = 0
        while (lastIndex != -1) {
            lastIndex = this.indexOf(subStr, lastIndex, ignoreCase)
            if (lastIndex != -1) {
                result.add(lastIndex)
                lastIndex++
            }
        }
    }
    return result
}
