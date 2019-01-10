package com.plumbaria.e_5_1_hilos

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.ProgressBar
import android.app.ProgressDialog
import android.content.DialogInterface
import android.os.Message



class MainActivity : AppCompatActivity() {
    private lateinit var entrada:EditText
    private lateinit var salida:TextView

    /*
    En kotlin no surgen los mismos problemas que en Java,
    parece que sabe manejar bien los threads y no
    interrumpe el programa
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        entrada = findViewById(R.id.entrada)
        salida = findViewById(R.id.salida)

    }

    fun calcularOperacion(view:View){
        var n: Int = entrada.text.toString().toInt()
        salida.append("" + n + "! = ")
        /*
        var thread:Thread = MiThread(n)
        thread.start()
        */
        var tarea = MiTareaConPorgressDialog()
        tarea.execute(n)


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
            runOnUiThread(Runnable {
                salida.append("" + res + "\n")
            })
        }

    }

    /* AsyncTask <Parametros,Progreso,Resultado> */
    inner class MiTareaConProgressBar : AsyncTask<Int, Int, Int>(){
        /* progressDialog está deprecated y esta es la alternativa actual */
        private lateinit var barraProgreso:ProgressBar
        override fun onPreExecute() {
            barraProgreso = findViewById(R.id.determinateBar)
            barraProgreso.progress = 0
            barraProgreso.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg params: Int?): Int {
            var parametro:Int = params[0] as Int
            var progreso = 0
            var res = 1
            for (i in 1..parametro){
                res *= i
                SystemClock.sleep(1000)
                progreso = (i*100) / parametro
                publishProgress(progreso)
            }
            return res
        }

        override fun onProgressUpdate(vararg values: Int?) {
            var parametro:Int = values[0] as Int
            barraProgreso.progress = parametro
        }

        override fun onPostExecute(result: Int?) {
            barraProgreso.visibility = View.INVISIBLE
            salida.append(result.toString() + "\n")
        }


    }

    inner class MiTareaConPorgressDialog : AsyncTask<Int, Int, Int>() {
        private lateinit var progressDialog:ProgressDialog
        override fun onPreExecute() {
            progressDialog = ProgressDialog(this@MainActivity)
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
            progressDialog.setMessage("Calculando...")
            progressDialog.setCancelable(true)
            progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", {
                    dialog,
                    which -> this.cancel(true)
            })
            progressDialog.max = 100
            progressDialog.progress = 0
            progressDialog.show()
            progressDialog.setOnCancelListener {
                salida.append("\n Proceso cancelado \n")
            }

        }
        /*
            override fun onCancel(dialog: DialogInterface?) {
                this.cancel(true)
            }
    */
        override fun doInBackground(vararg params: Int?): Int {
            var parametro:Int = params[0] as Int
            var progreso = 0
            var res = 1
            for (i in 1..parametro){
                if(isCancelled) break
                res *= i
                SystemClock.sleep(1000)
                progreso = (i * 100) / parametro
                publishProgress(progreso)

            }
            return res
        }

        override fun onProgressUpdate(vararg values: Int?) {
            var parametro:Int = values[0] as Int
            progressDialog.progress = parametro
        }

        override fun onPostExecute(result: Int?) {
            progressDialog.dismiss()
            salida.append(result.toString() + "\n")
        }

        override fun onCancelled() {
            salida.append("\n Proceso cancelado \n")
        }

    }
}
