package nferno1.homework_15_1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import nferno1.homework_15_1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        (application as App).db.wordDao()

    }
}