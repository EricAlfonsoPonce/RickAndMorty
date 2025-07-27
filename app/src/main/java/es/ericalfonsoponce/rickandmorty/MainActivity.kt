package es.ericalfonsoponce.rickandmorty

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import es.ericalfonsoponce.presentation.compose.main.MainComposeActivity
import es.ericalfonsoponce.presentation.xml.home.HomeActivity
import es.ericalfonsoponce.rickandmorty.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        initListeners()
    }

    private fun initListeners() {
        binding?.cardCompose?.setOnClickListener {
            val intent = Intent(this, MainComposeActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding?.cardXml?.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}