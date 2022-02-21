package com.jh.dailypoem.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jh.dailypoem.network.ApiService
import com.jh.dailypoem.network.PoemBean
import com.jh.dailypoem.network.RetrofitHelper
import com.jh.dailypoem.ui.view.SongFamilyText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    private val mainScope = CoroutineScope(Dispatchers.Main)
    private val sp by lazy { getSharedPreferences("config", Context.MODE_PRIVATE) }

    private var token: String? = null
    private var poemData: PoemBean? by mutableStateOf(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        setContent { Layout(poemData, { getPoemData() }, { toPoemDetail(it) }) }

        token = sp.getString("token", null)
        if (token == null){
            getPoemToken()
        }else{
            getPoemData()
        }
    }

    private fun toPoemDetail(poemData: PoemBean) {
        startActivity(Intent(this, PoemDetailActivity::class.java).apply {
            putExtra("poemData", poemData)
        })
    }

    private fun getPoemToken() {
        mainScope.launch {
            val token = RetrofitHelper.requestAsync({ retrofit ->
                retrofit.create(ApiService::class.java).getPoemToken()
            }).await()?.data
            Log.d("result token", token ?: "null")
            sp.edit().putString("token", token).apply()
            this@MainActivity.token = token
            getPoemData()
        }
    }

    private fun getPoemData() {
        mainScope.launch {
            val poemData = RetrofitHelper.requestAsync({ retrofit ->
                retrofit.create(ApiService::class.java).getPoemData(token ?: "")
            }).await()?.data
            this@MainActivity.poemData = poemData
        }
    }
}

@Composable
fun Layout(poemData: PoemBean?, onSwitchClick: () -> Unit, toPoemDetail: (PoemBean) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(arrayListOf(Color(0xFFF3F3F3), Color(0xFFE8E8E8))))
            .padding(top = 76.dp, bottom = 58.dp, start = 32.dp, end = 32.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            val date = Date(System.currentTimeMillis())
            SongFamilyText(
                text = SimpleDateFormat("dd").format(date),
                fontSize = 40.sp,
                color = Color(0xFF303437),
                isBold = true
            )
            Column(
                modifier = Modifier.padding(start = 9.dp)
            ) {
                SongFamilyText(
                    text = SimpleDateFormat("E").format(date),
                    fontSize = 14.sp,
                    color = Color(0xFF72777A)
                )
                SongFamilyText(
                    text = SimpleDateFormat("yyyy年MM月").format(date),
                    fontSize = 14.sp,
                    color = Color(0xFF72777A)
                )
            }
        }
        Box(
            modifier = Modifier
                .padding(top = 27.dp)
                .fillMaxWidth()
                .weight(weight = 1f, fill = true)
                .background(color = Color(0xFFFFFFFF), shape = RoundedCornerShape(2.dp))
                .clickable {
                    if (poemData != null) {
                        toPoemDetail.invoke(poemData)
                    }
                }
                .padding(start = 24.dp, end = 36.dp, top = 38.dp, bottom = 24.dp)
        ){
            if (poemData != null){
                Row(modifier = Modifier.align(Alignment.TopEnd), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    for (item in poemData.content!!.split("，", "。", "？", "、").reversed()){
                        SongFamilyText(
                            text = item,
                            fontSize = 28.sp,
                            color = Color(0xFF303437),
                            modifier = Modifier.width(28.dp)
                        )
                    }
                }
                Column(
                    Modifier
                        .width(24.dp)
                        .align(Alignment.BottomStart)) {
                    SongFamilyText(
                        text = poemData.origin?.author ?: "",
                        fontSize = 18.sp,
                        color = Color(0xFF303437),
                        modifier = Modifier.width(24.dp),
                        textAlign = TextAlign.Center,
                    )
                    Box(
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .size(width = 24.dp, height = 24.dp)
                            .background(color = Color(0xFF303437), shape = RoundedCornerShape(2.dp)),
                    ) {
                        SongFamilyText(
                            text = poemData.origin?.dynasty?.get(0).toString(),
                            fontSize = 16.sp,
                            color = Color(0xFFFFFFFF),
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                }
            }
        }
        Box(
            modifier = Modifier
                .padding(top = 24.dp)
                .size(width = 111.dp, 40.dp)
                .align(Alignment.CenterHorizontally)
                .border(1.dp, Color(0xFFC9CFDA), RoundedCornerShape(22.dp))
                .clickable { onSwitchClick.invoke() }
        ){
            SongFamilyText(
                text = "换一句",
                fontSize = 16.sp,
                color = Color(0xFF303437),
                modifier = Modifier.align(Alignment.Center),
            )
        }
    }
}