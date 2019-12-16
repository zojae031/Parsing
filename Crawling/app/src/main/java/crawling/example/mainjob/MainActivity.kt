package crawling.example.mainjob

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import crawling.example.R
import crawling.example.data.RepositoryImpl
import crawling.example.data.datasource.RemoteDataSourceImpl
import crawling.example.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(
            this,
            R.layout.activity_main
        )
    }

    override fun onStop() {
        viewModel.clearDisposable()
        super.onStop()
    }

    private val viewModel =
        MainViewModel(RepositoryImpl.getInstance(RemoteDataSourceImpl.getInstance()))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(binding) {
            vm = viewModel
            lifecycleOwner = this@MainActivity
            identity.addTextChangedListener {
                viewModel.changeStudentIdentityNum(it.toString())
            }
        }
    }

}
