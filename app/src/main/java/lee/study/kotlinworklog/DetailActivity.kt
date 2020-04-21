package lee.study.kotlinworklog

import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_detail.*
import java.util.*


class DetailActivity : AppCompatActivity() {

    companion object {
        val TAG = "detail"
        val USER_KEY = "USER_KEY"
    }

    var yyyy = ""
    var mm = ""
    var dd = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val info = intent.getParcelableExtra<WorkLog>(USER_KEY)

        val yyyymmdd = info.yyyymmdd

        yyyy = yyyymmdd.substring(0, 4)
        mm = yyyymmdd.substring(4, 6)
        dd = yyyymmdd.substring(6, 8)

//        starttime_button_detail.text = insertTimeChar(info.startTime)
//        endtime_button_detail.text = insertTimeChar(info.endTime)
//        breaktime_button_detail.text = insertTimeChar(info.breakTime)
//        etc_edittext_detail.setText(info.etc, TextView.BufferType.EDITABLE)

        getDateInfo()

        modify_button_detail.setOnClickListener {

            val starttime = removeTimeChar(starttime_button_detail.text.toString())
            val endtime = removeTimeChar(endtime_button_detail.text.toString())
            val breaktime = removeTimeChar(breaktime_button_detail.text.toString())
            val etc = etc_edittext_detail.text.toString().replace(":", "")

            val uid = FirebaseAuth.getInstance().uid
            val ref = FirebaseDatabase.getInstance().getReference(
                "/schedules/$uid/$yyyy/$mm/$dd"
            )

            ref.setValue(WorkLog(yyyymmdd, starttime, endtime, breaktime, etc))

            val intent = Intent(this, WorklogActivity::class.java)
            startActivity(intent)
        }

        starttime_button_detail.setOnClickListener {
            val cldr: Calendar = Calendar.getInstance()
            val hour: Int = cldr.get(Calendar.HOUR_OF_DAY)
            val minutes: Int = cldr.get(Calendar.MINUTE)
            // time picker dialog
            TimePickerDialog(
                this,
                OnTimeSetListener { tp, sHour, sMinute ->
                    starttime_button_detail.setText(
                        "${sHour.toString().padStart(2, '0')}:${sMinute.toString()
                            .padStart(2, '0')}"
                    )
                },
                hour,
                minutes,
                true
            ).show()

        }

        endtime_button_detail.setOnClickListener {
            val cldr: Calendar = Calendar.getInstance()
            val hour: Int = cldr.get(Calendar.HOUR_OF_DAY)
            val minutes: Int = cldr.get(Calendar.MINUTE)
            // time picker dialog
            TimePickerDialog(
                this,
                OnTimeSetListener { tp, sHour, sMinute ->
                    endtime_button_detail.setText(
                        "${sHour.toString().padStart(2, '0')}:${sMinute.toString()
                            .padStart(2, '0')}"
                    )
                },
                hour,
                minutes,
                true
            ).show()
        }

        breaktime_button_detail.setOnClickListener {
            val cldr: Calendar = Calendar.getInstance()
            val hour: Int = cldr.get(Calendar.HOUR_OF_DAY)
            val minutes: Int = cldr.get(Calendar.MINUTE)
            // time picker dialog
            TimePickerDialog(
                this,
                OnTimeSetListener { tp, sHour, sMinute ->
                    breaktime_button_detail.setText(
                        "${sHour.toString().padStart(2, '0')}:${sMinute.toString()
                            .padStart(2, '0')}"
                    )
                },
                hour,
                minutes,
                true
            ).show()
        }
    }

    fun getDateInfo() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/schedules/$uid/$yyyy/$mm/$dd")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val worklog = p0.getValue(WorkLog::class.java)
                if (worklog != null) {
                    starttime_button_detail.text = insertTimeChar(worklog.startTime)
                    endtime_button_detail.text = insertTimeChar(worklog.endTime)
                    breaktime_button_detail.text = insertTimeChar(worklog.breakTime)
                    etc_edittext_detail.setText(worklog.etc, TextView.BufferType.EDITABLE)
                }

            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })
    }

}
fun insertTimeChar(target: String): String {
    if (target.isEmpty() || target.length < 4) return ""
    return target.substring(0, 2) + ":" + target.substring(2, 4)
}

fun removeTimeChar(target: String): String {
    return target.replace(":", "")
}