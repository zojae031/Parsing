package crawling.example.util

object ParsingUtil {

    fun findSessionIndex(html: String): MutableMap<String, String> {
        val idx = html.indexOf(SecondSession1)
        var lastIdx = idx
        var arr = String()

        while (html[lastIdx] != ';') {
            lastIdx++
        }

        for (i in idx + SecondSession1.length until lastIdx) {
            arr += html[i]
        }

        return mutableMapOf(SecondSession1 to arr, SecondSession2 to "1", SecondSession3 to "1")
    }

    private const val SecondSession3 = "hn_mauth"
    private const val SecondSession2 = "mauth"
    private const val SecondSession1 = "hn_ck_login"
}