package crawling.example.mainjob

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import crawling.example.R
import crawling.example.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), Contract.MainView {


    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(
            this,
            R.layout.activity_main
        )
    }

    private val presenter = MainPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.onCreate()
        binding.parsing.setOnClickListener {
            //파싱 하기
            if (binding.id.text.toString() != "" || binding.password.text.toString() != "") {
                presenter.parseBtnClicked(
                    binding.id.text.toString(),
                    binding.password.text.toString()
                )
            } else {
                Toast.makeText(this, "정보 입력하시오.", Toast.LENGTH_SHORT).show()
            }

        }
        binding.parse.setOnClickListener {
            //파스 입력 정보 생성
            binding.parsing.visibility = View.VISIBLE
            binding.text.visibility = View.VISIBLE
            binding.id.visibility = View.VISIBLE
            binding.password.visibility = View.VISIBLE

            binding.parse.visibility = View.INVISIBLE
            binding.left.visibility = View.INVISIBLE
            binding.right.visibility = View.INVISIBLE
            binding.image.visibility = View.INVISIBLE
            binding.identity.visibility = View.INVISIBLE
        }
        binding.student.setOnClickListener {
            presenter.studentBtnClicked()
        }
        binding.left.setOnClickListener {
            presenter.leftBtnClicked()
        }
        binding.right.setOnClickListener {
            presenter.rightBtnClicked()
        }
        binding.identity.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                presenter.changeStudentIdentityNum(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })

    }

    override fun showParseInfo(name: String, department: String, professor: String) {
        binding.text.visibility = View.VISIBLE


        binding.left.visibility = View.INVISIBLE
        binding.right.visibility = View.INVISIBLE
        binding.image.visibility = View.INVISIBLE
        binding.identity.visibility = View.INVISIBLE
        binding.text.text = "이름 : $name \n학과 : $department\n담당교수 : $professor"
    }


    override fun showStudent() {
        binding.text.visibility = View.INVISIBLE
        binding.id.visibility = View.INVISIBLE
        binding.password.visibility = View.INVISIBLE
        binding.parsing.visibility = View.INVISIBLE

        binding.parse.visibility = View.VISIBLE
        binding.left.visibility = View.VISIBLE
        binding.right.visibility = View.VISIBLE
        binding.image.visibility = View.VISIBLE
        binding.identity.visibility = View.VISIBLE
    }

    override fun drawUserImage(url: String) {
        Glide.with(this@MainActivity)
            .load(url)
            .fitCenter()
            .into(binding.image)
    }

    override fun setEditText(text: String) {
        binding.identity.setText(text)
    }

    @SuppressLint("ShowToast")
    override fun alertToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT)
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

}
