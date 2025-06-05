package com.example.whoareyou.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.whoareyou.R

@Composable
fun TopTab() {
    Row(
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(Color(0xFFF2F2F7))
    ) {
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.btn_search_bar),
            contentDescription = "검색"
        )
        Spacer(Modifier.width(30.dp))
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.btn_setting_bar),
            contentDescription = "설정"
        )
        Spacer(Modifier.width(10.dp))
    }
}

@Composable
@Preview
fun TobTabPreview() {
    TopTab()
}