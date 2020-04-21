package lee.study.kotlinworklog

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
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

            if(email.isEmpty()){
                Toast.makeText(applicationContext,"email.isEmpty()",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if(pass.isEmpty()){
                Toast.makeText(applicationContext,"pass.isEmpty()",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if(username.isEmpty()){
                Toast.makeText(applicationContext,"username.isEmpty()",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener {
                    if(!it.isSuccessful) return@addOnCompleteListener

                    Log.d(TAG,"success createUserWithEmail:$email pass:$pass")
                    userInfoToFirebaseDatabase()
                }
                .addOnFailureListener {
                    Log.d(TAG,"failed createUserWithEmail:$email pass:$pass")
                    Toast.makeText(applicationContext,it.toString(),Toast.LENGTH_LONG).show()

                }


        }
    }

    private fun userInfoToFirebaseDatabase(){
        val username = username_edittext_register.text.toString()
        val email = email_edittext_register.text.toString()
        val pass = password_edittext_register.text.toString()

        val uid = FirebaseAuth.getInstance().uid?:""
        val ref = FirebaseDatabase.getInstance().getReference("/user/$uid")
        val regUser = User(uid,username)

        ref.setValue(regUser).addOnSuccessListener {
            Log.d(TAG,"success createUserWithEmail:$email pass:$pass")
            val intent = Intent(this,WorklogActivity::class.java)
            startActivity(intent)
        }.addOnFailureListener {
            Log.d(TAG,"failed createUserWithEmail:$email pass:$pass")
            Toast.makeText(applicationContext,it.toString(),Toast.LENGTH_LONG).show()
        }
    }
}