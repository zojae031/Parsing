package crawling.example.mainjob

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import crawling.example.data.Repository
import crawling.example.data.dao.ParsingData
import crawling.example.data.dao.StudentInfo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainViewModel(private val repo: Repository) : ViewModel() {

    private val compositeDisposable by lazy { CompositeDisposable() }

    private val _student = MutableLiveData<StudentInfo>().apply {
        this.value =
            StudentInfo()
    }

    val student: LiveData<StudentInfo>
        get() = _student

    private val _buttonFlag = MutableLiveData<Boolean>().apply { this.value = true }
    val buttonFlag: LiveData<Boolean>
        get() = _buttonFlag

    private val _parseData = MutableLiveData<ParsingData>()
    val parseData: LiveData<ParsingData>
        get() = _parseData

    val studentIdentity = MutableLiveData<String>().apply { this.value = "14011038" }


    private val _url = MutableLiveData<String>().apply {
        this.value = "https://udream.sejong.ac.kr/upload/per/14011038.jpg"
    }
    val url: LiveData<String>
        get() = _url


    fun parseBtnClicked(id: String, pw: String) {
        _buttonFlag.value = true
        repo.getStudentInfo(StudentInfo(id, pw))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { data ->
                _parseData.value = data
            }.also { compositeDisposable.add(it) }
    }

    @MainThread
    fun studentBtnClicked() {
        _buttonFlag.value = false
    }

    @MainThread
    fun leftBtnClicked() {
        changeUrl(Vector.LEFT)
    }

    @MainThread
    fun rightBtnClicked() {
        changeUrl(Vector.RIGHT)
    }

    @MainThread
    fun changeStudentIdentityNum(identity: String) {
        try {
            _url.value!!.substringAfterLast('/').let {
                if (it.isNotEmpty()) {
                    _url.value = _url.value?.replace(it, "$identity.jpg")
                    studentIdentity.value = identity
                }
            }
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
    }

    @MainThread
    private fun changeUrl(vector: Vector) {
        val before = studentIdentity.value
        when (vector) {
            Vector.LEFT -> {
                studentIdentity.value = before?.let { (it.toInt() - 1).toString() }
            }
            Vector.RIGHT -> {
                studentIdentity.value = before?.let { (it.toInt() + 1).toString() }
            }
        }
        _url.value = _url.value?.replace(before.toString(), studentIdentity.value.toString())
    }

    @MainThread
    fun clearDisposable() {
        compositeDisposable.clear()
    }

    enum class Vector {
        LEFT, RIGHT
    }
}

