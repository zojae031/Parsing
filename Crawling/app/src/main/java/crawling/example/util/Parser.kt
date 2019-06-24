package crawling.example.util

import android.os.Bundle
import android.os.Handler
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.FormElement

object Parser {
    private const val url: String = "https://udream.sejong.ac.kr/main/login.aspx?"

    private const val SecondSession1 = "hn_ck_login"
    private const val SecondSession2 = "mauth"
    private const val SecondSession3 = "hn_mauth"


    fun parse(handler: Handler, id: String, pw: String) {
        Thread {
            val msg = handler.obtainMessage()

            // 첫 페이지 세션 얻어오기
            val res = Jsoup.connect(url)
                .method(Connection.Method.GET)
                .execute()

            val session = res.cookies()
            //로그인 폼 얻어오기
            val form = res.parse().select("form").first() as FormElement

            //폼에 해당하는 값 저장
            with(form.select("input")) {
                select("[name=rUserid]").`val`(id)
                select("[name=rPW]").`val`(pw)
                select("[name=pro]").`val`("1")
            }

            //첫번째 세션을 통해 제출
            val result = form
                .submit()
                .cookies(session)
                .post()

            // 두번째 페이지에 해당하는 세션 얻어오기
            try {
                session += findSessionIndex(result.html())
            }catch (e: StringIndexOutOfBoundsException) {
                e.printStackTrace()
            }

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

    @Throws(StringIndexOutOfBoundsException::class)
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


}