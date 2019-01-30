package me.chen.redux

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import me.chen.redux.core.UnSubscribe
import me.chen.redux.core.launch

class MainActivity : AppCompatActivity() {

    private lateinit var valueTV: TextView
    private lateinit var add: Button
    private lateinit var sub: Button
    private lateinit var unSubscribe: UnSubscribe

    private lateinit var channel: Channel<String>;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        channel = Channel();
        valueTV = findViewById(R.id.value)
        add = findViewById(R.id.add)
        sub = findViewById(R.id.sub)
        add.setOnClickListener {
            AppStore.instance.dispatch(Add)
            AppStore.instance.dispatch(INCREMENT_ASYN)
            launch {
                channel.send("add click")
            }

        }
        sub.setOnClickListener {
            AppStore.instance.dispatch(Sub)
            GlobalScope.launch {
                channel.send("sub click")
            }
        }
        unSubscribe = AppStore.instance.subscription { appState, _ -> valueTV.text = "value = ${appState.count}" }
        GlobalScope.launch(Dispatchers.Main) {
            logChannel()
        }
    }

    suspend fun logChannel() {
        for (i in channel) {
            Log.i("channel", " received : $i")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unSubscribe.invoke()
    }
}
