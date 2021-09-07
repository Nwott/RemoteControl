package com.nwott.remotecontrol

import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.hardware.ConsumerIrManager
import android.widget.Button
import android.widget.Toast
import java.util.*
import java.util.function.Consumer

class MainActivity : AppCompatActivity() {

    lateinit var powerBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val check = packageManager.hasSystemFeature(PackageManager.FEATURE_CONSUMER_IR)
        val controller: ConsumerIrManager = getSystemService(Context.CONSUMER_IR_SERVICE) as ConsumerIrManager

        powerBtn = findViewById(R.id.powerBtn)
        // Dictionary of my TV brands and infrared codes
        powerBtn.setOnClickListener {
            var patternDict = mapOf("Scientific Atlanta" to "192 192 48 144 48 144 48 48 48 144 48 144 48 144 48 144 48 144 48 48 48 48 48 48 48 48 48 48 48 144 48 48 48 48 48 48 48 48 48 48 48 144 48 144 48 144 48 2534",
            "Samsung" to "170 170 20 63 20 63 20 63 20 20 20 20 20 20 20 20 20 20 20 63 20 63 20 63 20 20 20 20 20 20 20 20 20 20 20 20 20 63 20 20 20 20 20 20 20 20 20 20 20 20 20 63 20 20 20 63 20 63 20 63 20 63 20 63 20 63 20 1798",
            "LG" to "1 69 340 169 20 20 20 20 20 64 20 20 20 20 20 20 20 20 20 20 20 64 20 64 20 20 20 64 20 64 20 64 20 64 20 64 20 20 20 20 20 20 20 64 20 20 20 20 20 20 20 20 20 64 20 64 20 64 20 20 20 64 20 64 20 64 20 64 20 1544 340 85 20 3663")

            var intList : MutableList<Int> = mutableListOf()

            patternDict.keys.forEach()
            {
                var name : String = it
                var currString : String = patternDict[it] as String
                var strList : List<String> = currString.split(" ")

                strList.forEach()
                {
                    var str = it.replace("\\s+".toRegex(), " ")

                    if(name == "Scientific Atlanta")
                    {
                        intList.add(str.toInt() * (1000000 / 58000))
                    }
                    else if(name == "Samsung" || name == "LG")
                    {
                        intList.add(str.toInt() * (1000000 / 38000))
                    }
                }

                val pattern : IntArray = intList.toIntArray()

                ButtonPress(pattern, controller, it)
            }
        }

        if(!check)
        {
            toast("Infrared not supported")
        }

        if(!controller.hasIrEmitter())
        {
            toast("Infrared not supported")
        }


    }

    private fun Context.toast(message: CharSequence) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    fun ButtonPress(pattern: IntArray, controller: ConsumerIrManager, type: String)
    {
        if(type == "Scientific Atlanta")
        {
            controller.transmit(58000, pattern)
        }
        else if(type == "Samsung")
        {
            controller.transmit(38000, pattern)
        }
        else if(type == "LG")
        {
            controller.transmit(38000, pattern)
        }
    }
}