package crawling.example.data

import crawling.example.data.dao.ParsingData
import crawling.example.data.dao.StudentInfo
import io.reactivex.Single

interface Repository {
    fun getStudentInfo(student: StudentInfo): Single<ParsingData>
}