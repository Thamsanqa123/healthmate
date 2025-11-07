package com.example.healthmate

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.healthmate.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val handler = Handler(Looper.getMainLooper())
    private var isConnected = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        navController = findNavController(R.id.nav_host_fragment)
        binding.bottomNavigation.setupWithNavController(navController)
        setupToolbarActions()
        translateBottomNavigation()

        startConnectionMonitoring()
    }


    private fun translateBottomNavigation() {
        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        val language = prefs.getString("language", "english")

        val menu = binding.bottomNavigation.menu
        menu.findItem(R.id.dashboardFragment).title =
            if (language == "zulu") "I-Dashboard" else "Dashboard"
        menu.findItem(R.id.workoutsFragment).title =
            if (language == "zulu") "Ukuziqeqesha" else "Workouts"
        menu.findItem(R.id.mealsFragment).title =
            if (language == "zulu") "Izidlo" else "Meals"
        menu.findItem(R.id.profileFragment).title =
            if (language == "zulu") "Iphrofayela" else "Profile"
    }

    private fun setupToolbarActions() {
        binding.ivSync.setOnClickListener {
            if (!isInternetAvailable()) {
                showSlideNotification(getStringByLanguage("No internet connection. Data will sync later.",
                    "Ayikho i-inthanethi. Idatha izosinda kamuva."), false)
            } else {
                showSlideNotification(getStringByLanguage("Sync completed successfully.",
                    "Ukuvumelanisa kuqediwe ngempumelelo."), true)
            }
        }

        binding.ivSettings.setOnClickListener {
            showLanguageDialog()
        }
    }

    private fun startConnectionMonitoring() {
        handler.post(object : Runnable {
            override fun run() {
                val currentlyConnected = isInternetAvailable()
                if (currentlyConnected && !isConnected) {
                    showSlideNotification(getStringByLanguage("Internet connection restored. Syncing your data...",
                        "Uxhumano lwe-inthanethi lubuyisiwe. Idatha iyavumelaniswa..."), true)
                } else if (!currentlyConnected && isConnected) {
                    showSlideNotification(getStringByLanguage("No internet connection. You can continue working and data will sync later.",
                        "Ayikho i-inthanethi. Ungaqhubeka usebenza futhi idatha izovumelaniswa kamuva."), false)
                }
                isConnected = currentlyConnected
                handler.postDelayed(this, 1000)
            }
        })
    }

    private fun isInternetAvailable(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val capabilities = cm.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun showSlideNotification(message: String, success: Boolean) {
        val rootLayout = findViewById<FrameLayout>(android.R.id.content)
        val context = this
        val cardView = CardView(context).apply {
            cardElevation = 12f
            radius = 16f
            setCardBackgroundColor(if (success) Color.parseColor("#4CAF50") else Color.parseColor("#F44336"))
            useCompatPadding = true
        }
        val textView = TextView(context).apply {
            text = message
            setTextColor(Color.WHITE)
            textSize = 16f
            setPadding(40, 30, 40, 30)
            gravity = Gravity.CENTER
        }
        cardView.addView(textView)
        val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        params.gravity = Gravity.TOP
        params.setMargins(32, 80, 32, 0)
        cardView.layoutParams = params
        cardView.translationY = -300f
        cardView.alpha = 0f
        rootLayout.addView(cardView)
        cardView.animate().translationY(0f).alpha(1f).setDuration(400)
            .setInterpolator(AccelerateDecelerateInterpolator()).start()
        Handler(Looper.getMainLooper()).postDelayed({
            cardView.animate().translationY(-300f).alpha(0f).setDuration(300)
                .withEndAction { rootLayout.removeView(cardView) }.start()
        }, 3000)
    }

    private fun showLanguageDialog() {
        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        val currentLang = prefs.getString("language", "english")
        val rootLayout = findViewById<FrameLayout>(android.R.id.content)
        val context = this

        val cardView = CardView(context).apply {
            radius = 24f
            cardElevation = 12f
            setCardBackgroundColor(Color.WHITE)
            setContentPadding(50, 60, 50, 50)
        }

        val layout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
        }

        val title = TextView(context).apply {
            text = getStringByLanguage("Select Language", "Khetha Ulimi")
            textSize = 20f
            setTypeface(null, Typeface.BOLD)
            setTextColor(Color.BLACK)
            gravity = Gravity.CENTER
            setPadding(0, 0, 0, 40)
        }

        val englishBtn = Button(context).apply {
            text = getStringByLanguage("English", "IsiNgisi")
            setBackgroundColor(if (currentLang == "english") Color.parseColor("#2196F3") else Color.parseColor("#E0E0E0"))
            setTextColor(if (currentLang == "english") Color.WHITE else Color.BLACK)
            setPadding(40, 20, 40, 20)
            setOnClickListener {
                prefs.edit().putString("language", "english").apply()
                translateBottomNavigation()

                showSlideNotification(getStringByLanguage("Language changed to English", "Ulimi lushintshwe lube IsiNgisi"), true)
                rootLayout.removeView(cardView)
            }
        }

        val zuluBtn = Button(context).apply {
            text = getStringByLanguage("Zulu", "IsiZulu")
            setBackgroundColor(if (currentLang == "zulu") Color.parseColor("#2196F3") else Color.parseColor("#E0E0E0"))
            setTextColor(if (currentLang == "zulu") Color.WHITE else Color.BLACK)
            setPadding(40, 20, 40, 20)
            setOnClickListener {
                prefs.edit().putString("language", "zulu").apply()
                translateBottomNavigation()

                showSlideNotification(getStringByLanguage("Language changed to Zulu", "Ulimi lushintshwe lube IsiZulu"), true)
                rootLayout.removeView(cardView)
            }
        }

        val cancelBtn = Button(context).apply {
            text = getStringByLanguage("Cancel", "Khansela")
            setBackgroundColor(Color.parseColor("#F44336"))
            setTextColor(Color.WHITE)
            setPadding(40, 20, 40, 20)
            setOnClickListener { rootLayout.removeView(cardView) }
        }

        layout.addView(title)
        layout.addView(englishBtn)
        layout.addView(zuluBtn)
        layout.addView(cancelBtn)
        cardView.addView(layout)

        val widthInDp = 400
        val scale = resources.displayMetrics.density
        val widthInPx = (widthInDp * scale + 0.5f).toInt()

        val params = FrameLayout.LayoutParams(widthInPx, FrameLayout.LayoutParams.WRAP_CONTENT).apply {
            gravity = Gravity.CENTER
        }
        cardView.layoutParams = params
        cardView.alpha = 0f
        cardView.scaleX = 0.8f
        cardView.scaleY = 0.8f
        rootLayout.addView(cardView)

        cardView.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(300)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()
    }

    private fun getStringByLanguage(english: String, zulu: String): String {
        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        return if (prefs.getString("language", "english") == "zulu") zulu else english
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
