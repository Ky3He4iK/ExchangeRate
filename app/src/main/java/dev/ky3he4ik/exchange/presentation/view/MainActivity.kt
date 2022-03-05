package dev.ky3he4ik.exchange.presentation.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dev.ky3he4ik.exchange.databinding.ActivityMainBinding
import dev.ky3he4ik.exchange.presentation.repository.Repository
import dev.ky3he4ik.exchange.presentation.viewmodel.ExchangeViewModel
import kotlinx.coroutines.*
import kotlin.math.roundToLong

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var currentRate: Float? = null
    var ignoreChanges = true
    lateinit var handler: CoroutineExceptionHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Repository.initRepository(application)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.sendEdit.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                updateRate(s.toString())
            }
        })

        binding.getEdit.isEnabled = false

        binding.chooseFirst.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                binding.chooseFirst.setSelection(0)
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                getRate()
            }
        }
        binding.chooseSecond.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                binding.chooseSecond.setSelection(0)
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                getRate()
            }
        }

        binding.reverseButton.setOnClickListener {
            val first = binding.chooseFirst.selectedItemPosition
            binding.chooseFirst.setSelection(binding.chooseSecond.selectedItemPosition)
            binding.chooseSecond.setSelection(first)
            getRate()
        }
//        binding.buttonExchange.setOnClickListener {
//            getRate()
//        }
        getRate()
        ignoreChanges = false

        handler = CoroutineExceptionHandler { _, throwable ->
            Log.e("Exchange/Coroutine", throwable.message, throwable)
            GlobalScope.launch(Dispatchers.Main) {
                Toast.makeText(
                    this@MainActivity,
                    "No internet connection",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun getRate() {
        val selectedF = binding.chooseFirst.selectedItem as String
        val selectedS = binding.chooseSecond.selectedItem as String
        lifecycleScope.launch(handler) {
            currentRate = ExchangeViewModel.getExchangeRate(selectedF, selectedS)?.rate
            if (currentRate == null)
                Toast.makeText(this@MainActivity, "No internet connection", Toast.LENGTH_SHORT)
                    .show()
            withContext(Dispatchers.Main) {
                updateRate(binding.sendEdit.text.toString())
            }
        }
    }

    private fun updateRate(text: String) {
        val v = text.toFloatOrNull() ?: return

        if (currentRate == null) {
            Toast.makeText(this@MainActivity, "No internet connection", Toast.LENGTH_SHORT).show()
            return
        }
        binding.getEdit.setText(((v * currentRate!! * 100).roundToLong() / 100f).toString())
    }
}
