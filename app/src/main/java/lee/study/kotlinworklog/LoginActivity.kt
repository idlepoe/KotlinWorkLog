package lee.study.kotlinworklog

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    companion object{
        val TAG = "Login"
        val USER_KEY = "USER_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login_button_login.setOnClickListener {
            val email = email_edittext_login.text.toString()
            val pass = password_edittext_login.text.toString()
            Log.d(TAG, "login attempt email:$email password:$pass")
        }

        register_button_login.setOnClickListener {
            val email = email_edittext_login.text.toString()
            val pass = password_edittext_login.text.toString()
            val loginUser = LoginUser(email,pass)

            val intent = Intent(this,RegisterActivity::class.java)
            intent.putExtra(USER_KEY,loginUser)
            startActivity(intent)
        }

    }
}

@Parcelize
class User(val uid:String, val username:String):Parcelable{
    constructor():this("","")
}

@Parcelize
class LoginUser(val email:String, val password:String):Parcelable{
    constructor():this("","")
}