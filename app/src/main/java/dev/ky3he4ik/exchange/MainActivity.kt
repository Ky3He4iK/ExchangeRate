package dev.ky3he4ik.exchange

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import dev.ky3he4ik.exchange.databinding.ActivityMainBinding
import dev.ky3he4ik.exchange.presentation.repository.Repository
import dev.ky3he4ik.exchange.presentation.viewmodel.ExchangeViewModel

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var currentRate: Float = 1f
    var ignoreChanges = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Repository.initRepository(application)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        binding.sendEdit.doOnTextChanged { text, start, before, count ->
//            val v = text.toString().toFloatOrNull()
//            v ?: return@doOnTextChanged
//            binding.getEdit.setText((v * currentRate).toString())
//        }


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
        }
        ignoreChanges = false
    }

    private fun updateRate() {
        if (ignoreChanges)
            return
        val selectedF = binding.chooseFirst.selectedItem as String
        val selectedS = binding.chooseSecond.selectedItem as String
        ExchangeViewModel.getExchangeRate(selectedF, selectedS, this).observe(this) {
            if (it != null)
                currentRate = it.rate
        }
    }
}
