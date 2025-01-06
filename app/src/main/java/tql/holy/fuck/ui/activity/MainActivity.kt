@file:Suppress("SetTextI18n")

package tql.holy.fuck.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.highcapable.yukihookapi.YukiHookAPI
import tql.holy.fuck.BuildConfig
import tql.holy.fuck.R
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.ui.graphics.Brush
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.mutableStateOf
import androidx.compose.material.icons.filled.Home

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf(1) }
    
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(250.dp)
                        .padding(16.dp)
                ) {
                    Text("菜单项 1", fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("菜单项 2", fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("菜单项 3", fontSize = 18.sp)
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu"
                        )
                    }
                    Text(
                        text = stringResource(R.string.app_name),
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            },
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.TouchApp, contentDescription = "应用列表") },
                        label = { Text("应用列表") },
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Home, contentDescription = "概览") },
                        label = { Text("概览") },
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Settings, contentDescription = "设置") },
                        label = { Text("设置") },
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 }
                    )
                }
            }
        ) { paddingValues ->
            when (selectedTab) {
                0 -> {
                    AppListScreen()
                }
                1 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFFF5F5F5))
                            .padding(paddingValues)
                            .padding(15.dp)
                    ) {
                        ModuleStatusCard()
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 5.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                            onClick = {
                                selectedTab = 0
                            }
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        brush = Brush.horizontalGradient(
                                            colors = listOf(
                                                Color(0xFF2196F3),  // 蓝色起始
                                                Color(0xFFFFB74D)   // 浅蓝色结束
                                            )
                                        )
                                    )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(15.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.TouchApp,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    
                                    Spacer(modifier = Modifier.width(12.dp))
                                    
                                    Text(
                                        text = "查看支持的的应用及版本 (${context.resources.getStringArray(R.array.module_scope).size} 个)",
                                        fontSize = 16.sp,
                                        color = Color.White
                                    )
                                    
                                    Spacer(modifier = Modifier.weight(1f))
                                    
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowRight,
                                        contentDescription = null,
                                        tint = Color.White
                                    )
                                }
                            }
                        }
                        
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 5.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFECE8))
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(15.dp)
                                    .fillMaxWidth()
                                    .verticalScroll(rememberScrollState())
                            ) {
                                InfoItem(
                                    label = "构建时间",
                                    value = BuildConfig.BUILD_TIME,
                                    showArrow = false
                                )
                                InfoItem(
                                    label = "适配应用",
                                    value = "${context.resources.getStringArray(R.array.module_scope).size} 个",
                                    showArrow = false
                                )
                                InfoItem(
                                    label = "硬件平台",
                                    value = "${android.os.Build.HARDWARE}",
                                    showArrow = false
                                )
                                InfoItem(
                                    label = "系统标识",
                                    value = "${android.os.Build.ID}",
                                    showArrow = false
                                )
                                InfoItem(
                                    label = "系统版本",
                                    value = "Android ${android.os.Build.VERSION.RELEASE} (SDK: ${android.os.Build.VERSION.SDK})",
                                    showArrow = false
                                )
                                InfoItem(
                                    label = "增量版本",
                                    value = "${android.os.Build.VERSION.INCREMENTAL}",
                                    showArrow = false
                                )

                                InfoItem(
                                    label = "系统架构",
                                    value = android.os.Build.SUPPORTED_ABIS.joinToString(" "),
                                    showArrow = false
                                )
                                InfoItem(
                                    label = "设备型号",
                                    value = "【${android.os.Build.MANUFACTURER}】${android.os.Build.BRAND} ${android.os.Build.MODEL} (${android.os.Build.BOARD})",
                                    showArrow = false
                                )
                            }
                        }
                    }
                }
                2 -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        Text("设置页面", modifier = Modifier.align(Alignment.Center))
                    }
                }
            }
        }
    }
}

@Composable
fun ModuleStatusCard() {
    val isModuleActive = YukiHookAPI.Status.isModuleActive
    val backgroundColor = if (isModuleActive) Color(0xFF4CAF50) else Color(0xFF424242)
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 状态图标
            Text(
                text = if (isModuleActive) "✓" else "!",
                color = if (isModuleActive) Color.Yellow else Color.Gray,
                fontSize = 25.sp,
                modifier = Modifier.size(25.dp)
            )
            
            Spacer(modifier = Modifier.width(20.dp))
            
            Column {
                // 状态文本
                Text(
                    text = if (isModuleActive) "模块已激活" else "模块未激活",
                    color = if (isModuleActive) Color.Yellow else Color.Gray,
                    fontSize = 18.sp
                )
                
                // 版本信息
                Text(
                    text = stringResource(R.string.module_version, BuildConfig.VERSION_NAME),
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 5.dp)
                )
                
                // 自定义文本
                Text(
                    text = "- 正义之师 -",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 5.dp)
                )
                
                // API 信息
                if (isModuleActive) {
                    Text(
                        text = if (YukiHookAPI.Status.Executor.apiLevel > 0)
                            "Activated by ${YukiHookAPI.Status.Executor.name} API ${YukiHookAPI.Status.Executor.apiLevel}"
                        else 
                            "Activated by ${YukiHookAPI.Status.Executor.name}",
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 11.sp,
                        modifier = Modifier.padding(top = 5.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoItem(
    label: String, 
    value: String, 
    showArrow: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 15.sp,
            color = Color.Black.copy(alpha = 0.7f)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = value,
                fontSize = 15.sp,
                color = Color.Black.copy(alpha = 0.9f)
            )
            if (showArrow) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = null,
                    modifier = Modifier.padding(start = 4.dp),
                    tint = Color.Gray
                )
            }
        }
    }
}