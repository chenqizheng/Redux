package me.chen.redux

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import me.chen.redux.core.UnSubscribe

class MainActivity : AppCompatActivity() {

    private lateinit var valueTV: TextView
    private lateinit var add: Button
    private lateinit var sub: Button
    private lateinit var unSubscribe: UnSubscribe

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        valueTV = findViewById(R.id.value)
        add = findViewById(R.id.add)
        sub = findViewById(R.id.sub)
        add.setOnClickListener { AppStore.instance.dispatch(Add) }
        sub.setOnClickListener { AppStore.instance.dispatch(Sub) }
        unSubscribe = AppStore.instance.subscription { appState, _ -> valueTV.text = "value = ${appState.count}" }
    }

    override fun onDestroy() {
        super.onDestroy()
        unSubscribe.invoke()
    }
}
