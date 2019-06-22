package crawling.example.util

import android.os.Bundle
import android.os.Handler
import android.util.Log
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.FormElement

object Parser {
    private const val url: String = "https://udream.sejong.ac.kr/main/login.aspx?"
    private const val userAgent =
        "Mozilla/5.0 (LG G6 9.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.2526.73 Safari/537.36"

    private const val SecondSession1 = "hn_ck_login"
    private const val SecondSession2 = "mauth"
    private const val SecondSession3 = "hn_mauth"

    private val data = HashMap<String, String>().apply {
        put("rUserid", "14011038")
        put("rPW", "wodud31")
        put("pro", "1")
    }

    fun parse(handler: Handler) {
        Thread {
            val msg = handler.obtainMessage()

            // 첫 페이지 세션 얻어오기
            val res = Jsoup.connect(url)
                .method(Connection.Method.GET)
                .userAgent(userAgent)
                .header(
                    "Accept",
                    "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3"
                )
                .execute()

            val session = res.cookies()

            //로그인 폼 얻어오기
            val form = res.parse().select("form").first() as FormElement
            val login = form.select("input")

            //폼에 해당하는 값 저장
            login.select("[name=rUserid]").`val`("14011038")
            login.select("[name=rPW]").`val`("wodud31")
            login.select("[name=pro]").`val`("1")

            //첫번째 세션을 통해 제출
            val result = form
                .submit()
                .cookies(session)
                .userAgent(userAgent)
                .post()

            // 두번째 페이지에 해당하는 세션 얻어오기
            session += findSessionIndex(result.html())

            // 얻은 세션을 통해 실질 로그인 시도
            val parseR = Jsoup.connect("https://udream.sejong.ac.kr/Career/")
                .cookies(session)
                .userAgent(userAgent)
                .method(Connection.Method.POST)
                .data(data)
                .execute()

            //사용자 정보 크롤링
            val privateData =
                Jsoup.connect("https://udream.sejong.ac.kr/Office/Teacher/ProfileGetData.aspx?mode=2&pid=N")
                    .cookies(session)
                    .method(Connection.Method.POST)
                    .execute()
            val data = privateData.parse().select("div")


            // 핸들러를 통해 MainThread로 전송
            val bundle = Bundle()
            bundle.putSerializable("html", data)

            msg.data = bundle
            handler.sendMessage(msg)


        }.start()
    }

    private fun findSessionIndex(html: String): MutableMap<String, String> {
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


}