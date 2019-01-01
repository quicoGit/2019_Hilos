package com.plumbaria.e_5_1_hilos

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private lateinit var entrada:EditText
    private lateinit var salida:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        entrada = findViewById(R.id.entrada)
        salida = findViewById(R.id.salida)

    }

    fun calcularOperacion(view:View){
        var n: Int = entrada.text.toString().toInt()
        salida.append("" + n + "! = ")
        var thread:Thread = MiThread(n)
        thread.start()
    }

    fun factorial(n: Int): Int {
        var res:Int = 1
        for (i in 1..n){
            res *= i
            SystemClock.sleep(1000)
        }
        return res
    }

    inner class MiThread(n:Int):Thread() {
        private var n:Int = 0
        private var res:Int = 0

        init {
            this.n = n
        }

        override fun run() {
            res = factorial(n)
            salida.append("" + res + "\n")
        }

    }
}
