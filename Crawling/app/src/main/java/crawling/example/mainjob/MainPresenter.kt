package crawling.example.mainjob

import android.os.Handler
import android.os.Message
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import crawling.example.data.StudentInfo
import crawling.example.util.Parser
import org.jsoup.select.Elements

class MainPresenter(private val view: Contract.MainView) : Contract.MainPresenter {
    private val handler = mHandler()

    private val _student = MutableLiveData<StudentInfo>().apply { this.value = StudentInfo() }
    val student: LiveData<StudentInfo>
        get() = _student

    private val _buttonFlag = MutableLiveData<Boolean>().apply { this.value = true }
    val buttonFlag: LiveData<Boolean>
        get() = _buttonFlag

    enum class Vector {
        LEFT,
        RIGHT
    }

    companion object {
        var url = "https://udream.sejong.ac.kr/upload/per/14011038.jpg"
        var studentNumber = 14011038
    }

    override fun onCreate() {

    }

    override fun onDestroy() {

    }

    override fun parseBtnClicked(id: String, pw: String) {
        _buttonFlag.value = true
        Parser.parse(handler, id, pw)

    }

    override fun studentBtnClicked() {
        _buttonFlag.value = false
    }

    override fun leftBtnClicked() {
        changeUrl(Vector.LEFT)
        view.drawUserImage(url)
        view.setEditText(studentNumber.toString())
    }

    override fun rightBtnClicked() {
        changeUrl(Vector.RIGHT)
        view.drawUserImage(url)
        view.setEditText(studentNumber.toString())
    }

    override fun changeStudentIdentityNum(identity: String) {
        try {
            url = url.replace(studentNumber.toString(), identity)
            studentNumber = identity.toInt()
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }

        view.drawUserImage(url)
    }

    private fun changeUrl(vector: Vector) {
        val before = studentNumber
        when (vector) {
            Vector.LEFT -> {
                studentNumber--
            }

            Vector.RIGHT -> {
                studentNumber++
            }
        }
        url = url.replace(before.toString(), studentNumber.toString())
    }


    private inner class mHandler : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            with(msg?.data) {
                val result = this?.get("html") as Elements
                val name = result[2].text()
                val department = result[3].text()
                val professor = result[4].text()
                view.showParseInfo(name, department, professor)
            }

        }
    }
}