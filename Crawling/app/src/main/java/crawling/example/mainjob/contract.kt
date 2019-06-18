package crawling.example.mainjob

interface Contract{
    interface MainView{
        fun alertToast(text:String)
        fun showParseInfo(text:String)
        fun showStudent()
        fun drawUserImage(url:String)
        fun setEditText(text:String)
    }
    interface MainPresenter{
        fun parseBtnClicked()
        fun studentBtnClicked()
        fun leftBtnClicked()
        fun rightBtnClicked()
        fun changeStudentIdentityNum(identity:String)
        fun onCreate()
        fun onDestroy()

    }
}