package lee.study.kotlinworklog

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_worklog.*
import kotlinx.android.synthetic.main.row_worklog.*
import kotlinx.android.synthetic.main.row_worklog.view.*
import kotlinx.android.synthetic.main.row_worklog.view.date_button_row_worklog
import java.util.*

class WorklogActivity : AppCompatActivity() {

    companion object {
        val TAG = "worklog"
        val USER_KEY = "USER_KEY"
    }

    val adapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worklog)

        worklog_recyclerview_worklog.adapter = adapter

        if (month_textview_worklog.text.toString().isEmpty()) {
            val cal = Calendar.getInstance()
            val c_month = cal.get(Calendar.MONTH).toString()
            month_textview_worklog.text = c_month
        }

        getMySchedule()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.menu_sign_out -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(
                    this,
                    LoginActivity::class.java
                )
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    fun initialSchedule() {
        val uid = FirebaseAuth.getInstance().uid
        val cal = Calendar.getInstance()
        val c_year = cal.get(Calendar.YEAR).toString()
        val c_month = cal.get(Calendar.MONTH).toString().padStart(2, '0')
        val monthMaxDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

        (1..monthMaxDays).map {
            it.toString()
                .padStart(2, '0')
        }
            .forEach {
                val ref = FirebaseDatabase.getInstance()
                    .getReference("/schedules/$uid/$c_year/$c_month/${it}")
                ref.setValue(WorkLog(c_year + c_month + it, "", "","", ""))
            }
    }

    fun getMySchedule() {
        val uid = FirebaseAuth.getInstance().uid
        val cal = Calendar.getInstance()
        val c_year = cal.get(Calendar.YEAR).toString()
        val c_month = cal.get(Calendar.MONTH).toString().padStart(2, '0')
        val monthMaxDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

        val ref = FirebaseDatabase.getInstance().getReference("/schedules/$uid/$c_year/$c_month")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.children.count() == 0) {
                    initialSchedule()
                    getMySchedule()
                } else {
                    (1..monthMaxDays).map {
                        it.toString()
                            .padStart(2, '0')
                    }
                        .forEach {
                            val ref = FirebaseDatabase.getInstance()
                                .getReference("/schedules/$uid/$c_year/$c_month/$it")
                            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(p0: DataSnapshot) {
                                    val workLog = p0.getValue(WorkLog::class.java)
                                    if (workLog != null) {
                                        adapter.add(WorkLogRow(workLog))
                                    }
                                }

                                override fun onCancelled(p0: DatabaseError) {

                                }
                            })


                        }
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })


    }
}

@Parcelize
class WorkLog(
    val yyyymmdd: String,
    val startTime: String,
    val endTime: String,
    val breakTime: String,
    val etc: String
) :
    Parcelable {
    constructor() : this("", "", "", "", "")
}

class WorkLogRow(val worklog: WorkLog) : Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.date_button_row_worklog.text = worklog.yyyymmdd
        viewHolder.itemView.starttime_textview_row_worklog.text = worklog.startTime
        viewHolder.itemView.endtime_textview_row_worklog.text = worklog.endTime
        viewHolder.itemView.etc_textview_row_worklog.text = worklog.etc

        viewHolder.itemView.date_button_row_worklog.setOnClickListener {
            Log.d("worklog", worklog.yyyymmdd)

            val intent = Intent(it.context, DetailActivity::class.java)
            intent.putExtra(
                "USER_KEY", worklog
            )
            it.context.startActivity(intent)
        }
    }

    override fun getLayout(): Int {
        return R.layout.row_worklog
    }


}