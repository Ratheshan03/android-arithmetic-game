package ratheshan.arithmeticgame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import kotlinx.android.synthetic.main.popup_window_layout.*

class MainActivity : AppCompatActivity() {
    lateinit var welcomeMsg: TextView
    lateinit var newGameBtn: Button
    lateinit var aboutBtn: Button
    lateinit var image: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        welcomeMsg = findViewById(R.id.welcomeTxtView)
        newGameBtn = findViewById(R.id.newGameBtn)
        aboutBtn = findViewById(R.id.aboutBtn)
        image = findViewById(R.id.imageView)

        // Event Listeners
        aboutBtn.setOnClickListener(){
            displayAboutDetails()
        }

        // Event Listeners
        newGameBtn.setOnClickListener(){
            moveToGamePage()
        }
    }

        // Displaying User details using POPUP window
    fun displayAboutDetails(){
        val window = PopupWindow(this)
        val view = layoutInflater.inflate(R.layout.popup_window_layout, null)
        window.contentView = view

        val textViewPopup = view.findViewById<TextView>(R.id.textViewPopup)
        textViewPopup.setOnClickListener{
            window.dismiss()
        }
        window.showAtLocation(aboutBtn,2,2,2)
    }

    // Entering the game page
    fun moveToGamePage(){
        val gamePageIntent = Intent(this, GamePage::class.java)
        startActivity(gamePageIntent)
    }
}