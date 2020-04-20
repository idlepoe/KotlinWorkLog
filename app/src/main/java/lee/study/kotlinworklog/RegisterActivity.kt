package lee.study.kotlinworklog

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    companion object {
        val TAG = "register"
        val USER_KEY = "USER_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val loginUser = intent.getParcelableExtra<LoginUser>(LoginActivity.USER_KEY)
        if (loginUser!=null) {
            email_edittext_register.setText(loginUser.email)
            password_edittext_register.setText(loginUser.password)
        }

        register_button_register.setOnClickListener {
            val username = username_edittext_register.text.toString()
            val email = email_edittext_register.text.toString()
            val pass = password_edittext_register.text.toString()


            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,pass)
                .addOnSuccessListener {
                    Log.d(TAG,"success createUserWithEmail:$email pass:$pass")
                }
                .addOnFailureListener {
                    Log.d(TAG,"failed createUserWithEmail:$email pass:$pass")
                }

            val uid = FirebaseAuth.getInstance().uid?:""
            val ref = FirebaseDatabase.getInstance().getReference("/user/$uid")
            val regUser = User(uid,username)

            ref.setValue(regUser).addOnSuccessListener {
                Log.d(TAG,"success createUserWithEmail:$email pass:$pass")

            }

        }
    }
}