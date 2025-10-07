package com.example.calculator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var display: TextView
    private var operand1: Double? = null
    private var pendingOp: String? = null
    private var shouldClear = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        display = findViewById(R.id.txtDisplay)

        val digits = intArrayOf(
            R.id.btn0,R.id.btn1,R.id.btn2,R.id.btn3,R.id.btn4,
            R.id.btn5,R.id.btn6,R.id.btn7,R.id.btn8,R.id.btn9
        )
        digits.forEach { id ->
            findViewById<Button>(id).setOnClickListener { inputDigit((it as Button).text.toString()) }
        }

        findViewById<Button>(R.id.btnDot).setOnClickListener { inputDot() }
        findViewById<Button>(R.id.btnC).setOnClickListener { clearAll() }
        findViewById<Button>(R.id.btnSign).setOnClickListener { negate() }
        findViewById<Button>(R.id.btnPercent).setOnClickListener { percent() }

        findViewById<Button>(R.id.btnPlus).setOnClickListener { setOp("+") }
        findViewById<Button>(R.id.btnMinus).setOnClickListener { setOp("-") }
        findViewById<Button>(R.id.btnMul).setOnClickListener { setOp("*") }
        findViewById<Button>(R.id.btnDiv).setOnClickListener { setOp("/") }
        findViewById<Button>(R.id.btnEq).setOnClickListener { equalsOp() }
    }

    private fun inputDigit(d: String) {
        val cur = display.text.toString()
        display.text = if (cur == "0" || shouldClear) d else cur + d
        shouldClear = false
    }

    private fun inputDot() {
        var cur = display.text.toString()
        if (shouldClear) { cur = "0"; shouldClear = false }
        if (!cur.contains(".")) display.text = "$cur."
    }

    private fun clearAll() {
        display.text = "0"; operand1 = null; pendingOp = null; shouldClear = false
    }

    private fun negate() {
        val v = display.text.toString().toDoubleOrNull() ?: 0.0
        display.text = stripZero(-v)
    }

    private fun percent() {
        val v = display.text.toString().toDoubleOrNull() ?: 0.0
        display.text = stripZero(v / 100.0)
    }

    private fun setOp(op: String) {
        val v = display.text.toString().toDoubleOrNull() ?: return
        if (operand1 == null) {
            operand1 = v
        } else if (pendingOp != null) {
            operand1 = calc(operand1!!, v, pendingOp!!)
            display.text = stripZero(operand1!!)
        }
        pendingOp = op
        shouldClear = true
    }

    private fun equalsOp() {
        val v2 = display.text.toString().toDoubleOrNull() ?: return
        if (operand1 != null && pendingOp != null) {
            val res = calc(operand1!!, v2, pendingOp!!)
            display.text = stripZero(res)
            operand1 = null; pendingOp = null; shouldClear = true
        }
    }

    private fun calc(a: Double, b: Double, op: String): Double = when (op) {
        "+" -> a + b
        "-" -> a - b
        "*" -> a * b
        "/" -> if (b == 0.0) Double.NaN else a / b
        else -> b
    }

    private fun stripZero(x: Double): String {
        val s = x.toString()
        return if (s.endsWith(".0")) s.dropLast(2) else s
    }
}
