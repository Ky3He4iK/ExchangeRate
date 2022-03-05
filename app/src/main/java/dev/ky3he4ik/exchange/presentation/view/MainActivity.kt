package dev.ky3he4ik.exchange.presentation.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import dev.ky3he4ik.exchange.databinding.ActivityMainBinding
import dev.ky3he4ik.exchange.presentation.repository.Repository
import dev.ky3he4ik.exchange.presentation.viewmodel.ExchangeViewModel
import kotlin.math.roundToLong

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var currentRate: Float = 1f
    var ignoreChanges = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Repository.initRepository(application)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.sendEdit.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                val v = s.toString().toFloatOrNull()
                v ?: return
                binding.getEdit.setText((v * currentRate).toString())
            }
        })
        binding.getEdit.isEnabled = false

        binding.chooseFirst.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                binding.chooseFirst.setSelection(0)
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                updateRate()
            }
        }
        binding.chooseSecond.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                binding.chooseSecond.setSelection(0)
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                updateRate()
            }
        }

        binding.reverseButton.setOnClickListener {
            val first = binding.chooseFirst.selectedItemPosition
            binding.chooseFirst.setSelection(binding.chooseSecond.selectedItemPosition)
            binding.chooseSecond.setSelection(first)
            updateRate()
        }
        binding.buttonExchange.setOnClickListener {
            updateRate()
        }
        ignoreChanges = false
    }

    private fun updateRate() {
        if (ignoreChanges)
            return
        val selectedF = binding.chooseFirst.selectedItem as String
        val selectedS = binding.chooseSecond.selectedItem as String
        val rate = ExchangeViewModel.getExchangeRate(selectedF, selectedS)?.rate
        if (rate == null)
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show()

        binding.sendEdit.doOnTextChanged { text, start, before, count ->
            val v = text.toString().toFloatOrNull()
            v ?: return@doOnTextChanged
            binding.getEdit.setText(((v * currentRate * 100).roundToLong() / 100f).toString())
        }
    }
}
