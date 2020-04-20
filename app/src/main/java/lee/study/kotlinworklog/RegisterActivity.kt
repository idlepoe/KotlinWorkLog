package lee.study.kotlinworklog

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity(){

    companion object{
        val TAG = "DEBUG"
        val USER_KEY = "USER_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val loginUser = intent.getParcelableExtra<LoginUser>(LoginActivity.USER_KEY)
        Log.d(TAG,loginUser.toString())
    }
}