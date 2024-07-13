package com.example.tippyv2

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.vectordrawable.graphics.drawable.ArgbEvaluator

private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENT = 15
class MainActivity : AppCompatActivity() {
    private lateinit var etBase: EditText
    private lateinit var seekBarTip: SeekBar
    private lateinit var tvTipPercentLabel: TextView
    private lateinit var tvTip: TextView
    private lateinit var tvTotal: TextView
    private lateinit var tvTipDescription: TextView
    private lateinit var etPeople: EditText

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        etBase = findViewById(R.id.etBase)
        seekBarTip = findViewById(R.id.seekBarTip)
        tvTipPercentLabel = findViewById(R.id.tvTipPercentLabel)
        tvTip = findViewById(R.id.tvTip)
        tvTotal = findViewById(R.id.tvTotal)
        tvTipDescription = findViewById(R.id.tvTipDescription)
        etPeople = findViewById(R.id.etPeople)

        seekBarTip.progress = INITIAL_TIP_PERCENT
        tvTipPercentLabel.text = "$INITIAL_TIP_PERCENT%"
        updateTipDescription(INITIAL_TIP_PERCENT)
        seekBarTip.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG, "onProgressChanged $progress")
                tvTipPercentLabel.text = "$progress%"
                computeTipAndTotal()
                updateTipDescription(progress)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })
        etBase.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG, "afterTextChanged $s")
                computeTipAndTotal()
            }
        })
    }

    @SuppressLint("RestrictedApi")
    private fun updateTipDescription(tipPercent: Int) {
        val tipDescription = when (tipPercent) {
            in 0..9 -> "Poor"
            in 10..14 -> "Acceptable"
            in 15..19 -> "Good"
            in 20..24 -> "Great"
            else -> "Amazing"
        }
        tvTipDescription.text = tipDescription
        val color = ArgbEvaluator().evaluate(
            tipPercent.toFloat() / seekBarTip.max,
            ContextCompat.getColor(this, R.color.color_worst_tip),
            ContextCompat.getColor(this, R.color.color_best_tip)
        ) as Int
        tvTipDescription.setTextColor(color)
    }

    @SuppressLint("SetTextI18n")
    private fun computeTipAndTotal() {
        if (etBase.text.isEmpty()) {
            tvTip.text = ""
            tvTotal.text = ""
            return
        }
        if (etPeople.text.toString().isEmpty()) {etPeople.setText("1")}
        val baseAmount = etBase.text.toString().toDouble() / etPeople.text.toString().toDouble()
        val tipPercent = seekBarTip.progress
        val tip = (baseAmount * tipPercent) / 100
        tvTip.text = "%.2f".format(tip)
        tvTotal.text = "%.2f".format(tip + baseAmount)
    }
}