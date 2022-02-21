package com.jh.dailypoem.ui.activity

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jh.dailypoem.R
import com.jh.dailypoem.network.PoemBean
import com.jh.dailypoem.ui.view.SongFamilyText

class PoemDetailActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        val poemData = intent.getSerializableExtra("poemData") as PoemBean
        setContent { Layout(poemData) { finish() } }
    }
}

@Composable
fun Layout(poemData: PoemBean?, finishActivity: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        Image(
            painter = painterResource(id = R.mipmap.ic_back),
            contentDescription = "back",
            modifier = Modifier.padding(start = 18.dp, top = 56.dp).clickable { finishActivity.invoke() }
        )
        SongFamilyText(
            text = "《${poemData?.origin?.title}》",
            fontSize = 24.sp,
            color = Color(0xFF303437),
            modifier = Modifier.padding(top = 48.dp, start = 28.dp, end = 28.dp).fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
        SongFamilyText(
            text = "【${poemData?.origin?.dynasty?.get(0).toString()}】${poemData?.origin?.author}",
            fontSize = 16.sp,
            color = Color(0xFF303437),
            modifier = Modifier.padding(top = 24.dp, start = 28.dp, end = 28.dp).fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(24.dp))
        if (poemData?.origin?.content != null){
            for (item in poemData.origin!!.content!!){
                SongFamilyText(
                    text = item,
                    fontSize = 20.sp,
                    color = Color(0xFF303437),
                    isBold = item == poemData.content,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(start = 28.dp, end = 28.dp).fillMaxWidth(),
                )
            }
            if (poemData.origin?.translate != null){
                Box(
                    Modifier
                        .padding(top = 32.dp, start = 31.dp, end = 31.dp)
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color(color = 0x1A000000))
                )
                SongFamilyText(
                    text = "【译文】",
                    fontSize = 16.sp,
                    color = Color(0xFF303437),
                    modifier = Modifier.padding(top = 31.dp).align(Alignment.CenterHorizontally)
                )
                SongFamilyText(
                    text = poemData.origin!!.translate!!.reduce { acc, s -> acc + s },
                    fontSize = 16.sp,
                    color = Color(0xFF303437),
                    modifier = Modifier
                        .padding(top = 26.dp, start = 31.dp, end = 31.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}