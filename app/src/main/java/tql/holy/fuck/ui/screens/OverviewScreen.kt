package tql.holy.fuck.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.highcapable.yukihookapi.YukiHookAPI
import tql.holy.fuck.BuildConfig
import tql.holy.fuck.R

@Composable
fun OverviewScreen(onNavigateToAppList: () -> Unit) {
    val context = LocalContext.current
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(15.dp)
    ) {
        // 标题栏
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "正义之师",
                style = MaterialTheme.typography.titleLarge,
                color = Color.Gray
            )
            Text(
                text = "Holy Fuck",
                style = MaterialTheme.typography.titleLarge,
                color = Color.Gray
            )
        }
        
        ModuleStatusCard()
        
        // 蓝色渐变卡片
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            onClick = onNavigateToAppList
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF2196F3),
                                Color(0xFFFFB74D)
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
                        text = "查看Xposed模块支持的应用及版本 ( ${context.resources.getStringArray(R.array.module_scope).size} 个)",
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
        
        // 系统信息卡片
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
                    label = "APP版本",
                    value = BuildConfig.VERSION_NAME,
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

@Composable
private fun ModuleStatusCard() {
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
            Text(
                text = if (isModuleActive) "✓" else "!",
                color = if (isModuleActive) Color.Yellow else Color.Gray,
                fontSize = 25.sp,
                modifier = Modifier.size(25.dp)
            )
            
            Spacer(modifier = Modifier.width(20.dp))
            
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (isModuleActive) "模块已激活" else "模块未激活",
                        color = if (isModuleActive) Color.Yellow else Color.Gray,
                        fontSize = 18.sp
                    )
                    Text(
                        text = buildAnnotatedString {
                            append("--- 让科技真正服务于")
                            withStyle(SpanStyle(color = Color.Red)) {
                                append("无产阶级")
                            }
                            append("！！！ ---")
                        },
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 13.sp,
                        modifier = Modifier.padding(top = 5.dp)
                    )
                    
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
}

@Composable
private fun InfoItem(
    label: String,
    value: String,
    showArrow: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = onClick != null) { onClick?.invoke() }
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
            if (showArrow) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color.Gray
                )
            }
        }
    }
} 