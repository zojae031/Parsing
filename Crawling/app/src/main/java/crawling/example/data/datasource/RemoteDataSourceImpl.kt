package crawling.example.data.datasource

import android.annotation.SuppressLint
import crawling.example.data.dao.ParsingData
import crawling.example.data.dao.StudentInfo
import crawling.example.util.ParsingUtil
import io.reactivex.Single
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.FormElement

class RemoteDataSourceImpl private constructor() : RemoteDataSource {
    @SuppressLint("CheckResult")
    override fun parseStudent(student: StudentInfo): Single<ParsingData> {
        return Single.create {
            try {
                // 첫 페이지 세션 얻어오기
                val res = Jsoup.connect(URL)
                    .method(Connection.Method.GET)
                    .execute()

                val session = res.cookies()
                //로그인 폼 얻어오기
                val form = res.parse().select("form").first() as FormElement

                //폼에 해당하는 값 저장
                with(form.select("input")) {
                    select("[name=rUserid]").`val`(student.id)
                    select("[name=rPW]").`val`(student.pw)
                    select("[name=pro]").`val`("1")
                }

                //첫번째 세션을 통해 제출
                val result = form
                    .submit()
                    .cookies(session)
                    .post()

                // 두번째 페이지에 해당하는 세션 얻어오기
                try {
                    session += ParsingUtil.findSessionIndex(result.html())
                } catch (e: StringIndexOutOfBoundsException) {
                    e.printStackTrace()
                }

                //사용자 정보 크롤링
                val privateData =
                    Jsoup.connect("https://udream.sejong.ac.kr/Office/Teacher/ProfileGetData.aspx?mode=2&pid=N")
                        .cookies(session)
                        .method(Connection.Method.POST)
                        .execute()

                val data = privateData.parse().select("div").run {
                    ParsingData(
                        this[2].text(),
                        this[3].text(),
                        this[4].text()
                    )
                }

                it.onSuccess(data)
            } catch (e: Exception) {
                it.tryOnError(e)
            }
        }
    }

    companion object {
        private const val URL: String = "https://udream.sejong.ac.kr/main/login.aspx?"
        private var INSTANCE: RemoteDataSource? = null
        fun getInstance(): RemoteDataSource {
            if (INSTANCE == null) INSTANCE = RemoteDataSourceImpl()
            return INSTANCE!!
        }
    }

}