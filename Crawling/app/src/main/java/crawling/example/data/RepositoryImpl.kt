package crawling.example.data

import crawling.example.data.dao.ParsingData
import crawling.example.data.dao.StudentInfo
import crawling.example.data.datasource.RemoteDataSource
import io.reactivex.Single

class RepositoryImpl private constructor(private val remote: RemoteDataSource) : Repository {
    override fun getStudentInfo(student: StudentInfo): Single<ParsingData> {
        return remote.parseStudent(student)
    }

    companion object {
        private var INSTANCE: Repository? = null
        fun getInstance(remote: RemoteDataSource): Repository {
            if (INSTANCE == null) INSTANCE = RepositoryImpl(remote)
            return INSTANCE!!
        }
    }
}