package ratheshan.arithmeticgame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    lateinit var welcomeMsg: TextView
    lateinit var newGameBtn: Button
    lateinit var aboutBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        welcomeMsg = findViewById(R.id.welcomeTxtView)
        newGameBtn = findViewById(R.id.newGameBtn)
        aboutBtn = findViewById(R.id.aboutBtn)

        aboutBtn.setOnClickListener(){
            displayAboutDetails()
        }

        newGameBtn.setOnClickListener(){
            moveToGamePage()
        }
    }

    fun displayAboutDetails(){
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.popup_window_layout, null)
        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable = true
        val popupWindow = PopupWindow(popupView,width,height,focusable)
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0,0)
    }

    fun moveToGamePage(){
        val gamePageIntent = Intent(this, GamePage::class.java)
        startActivity(gamePageIntent)
    }
}