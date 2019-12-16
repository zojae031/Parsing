package crawling.example.data.datasource

import crawling.example.data.dao.ParsingData
import crawling.example.data.dao.StudentInfo
import io.reactivex.Single

interface RemoteDataSource {
    fun parseStudent(student: StudentInfo): Single<ParsingData>
}