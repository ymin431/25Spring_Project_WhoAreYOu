package com.example.whoareyou.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.whoareyou.R

enum class BottomTab(
    val label: String,
    val selectedIcon: Int,
    val unselectedIcon: Int
) {
    Home("홈", R.drawable.btn_home_bar_selected, R.drawable.btn_home_bar),
    Contacts("연락처", R.drawable.btn_contacts_bar_selected, R.drawable.btn_contacts_bar),
    MyPage("마이페이지", R.drawable.btn_mypage_bar_selected, R.drawable.btn_mypage_bar)
}

@Composable
fun BottomBar(
    selectedTab: BottomTab,
    onTabSelected: (BottomTab) -> Unit
) {
    Box (
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF2F2F7))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp))
                .background(color = Color.White)
                .navigationBarsPadding(),
        ) {
            BottomTab.values().forEach { tab ->
                BottomBarItem(
                    tab = tab,
                    isSelected = tab == selectedTab,
                    onClick = { onTabSelected(tab) },
                    mModifier = Modifier
                        .weight(1f)
                        .padding(top = 10.dp)
                )
            }
        }
    }
}

@Composable
fun BottomBarItem(
    tab: BottomTab,
    isSelected: Boolean,
    onClick: () -> Unit,
    mModifier: Modifier = Modifier
) {
    val icon = if (isSelected) tab.selectedIcon else tab.unselectedIcon
    val textColor = if (isSelected) Color(0xFF007AFF) else Color(0xFF999999)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = mModifier.noRippleClickable { onClick() }
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = tab.label,
            modifier = Modifier
                .size(30.dp)
                .padding(1.dp)
        )
        Spacer(modifier = Modifier.height(1.dp))
        Text(
            text = tab.label,
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.pretendard_medium)),
            fontWeight = FontWeight(600),
            color = textColor,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview
@Composable
fun PreviewPage() {
    BottomBar(
        selectedTab = BottomTab.Home,
        onTabSelected = {}
    )
}