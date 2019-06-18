package crawling.example.mainjob

import android.os.Handler
import android.os.Message
import android.util.Log
import crawling.example.util.Parser

class MainPresenter(private val view: Contract.MainView) : Contract.MainPresenter {
    private val handler = mHandler()

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

    override fun parseBtnClicked() {
        Parser.parse(handler)
    }

    override fun studentBtnClicked() {
        view.showStudent()
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
    override fun changeStudentIdentityNum(identity:String) {

        try {
            url=url.replace(studentNumber.toString(), identity)
            studentNumber = identity.toInt()
        }
        catch (e:NumberFormatException){
            e.printStackTrace()

        }

        view.drawUserImage(url)
    }
    private fun changeUrl(vector:Vector) {
        val before = studentNumber
        when(vector){
            Vector.LEFT ->{
                studentNumber--
            }

            Vector.RIGHT ->{
                studentNumber++
            }
        }
        url=url.replace(before.toString(), studentNumber.toString())
    }



    private inner class mHandler : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            with(msg?.data) {
                val result = this?.getString("html")
                view.showParseInfo(result!!)
            }

        }
    }
}