package tql.holy.fuck.ui.activity

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Android
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import android.graphics.drawable.BitmapDrawable
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import tql.holy.fuck.R
import java.text.SimpleDateFormat
import java.util.*

class AppListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppListScreen()
        }
    }
}

@Composable
fun AppListScreen() {
    val context = LocalContext.current
    val pm = context.packageManager

    // 从 XML 文件中读取包名和应用名的映射
    val packageToAppName = remember {
        val packageNames = context.resources.getStringArray(R.array.module_scope)
        val appNames = context.resources.getStringArray(R.array.module_scope_name)
        packageNames.zip(appNames).toMap()
    }

    // 获取已安装应用的信息
    val installedApps = remember {
        pm.getInstalledApplications(PackageManager.GET_META_DATA)
            .associateBy { it.packageName }
    }

    // 构建应用列表数据
    val appsList = remember {
        packageToAppName.keys.map { packageName ->
            installedApps[packageName]?.let { appInfo ->
                // 如果应用已安装，返回已安装应用的信息
                AppInfo(appInfo, pm)
            } ?: AppInfo(packageName, packageToAppName[packageName] ?: packageName) // 如果应用未安装，从映射中获取应用名
        }.sortedByDescending { it.lastUpdateTime } // 按照更新时间对这些应用进行降序排序
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // 顶部栏
        TopBar()

        // 应用列表
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(appsList) { appInfo ->
                AppItem(appInfo, pm)
            }
        }
    }
}

@Composable
fun TopBar() {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "返回",
            modifier = Modifier
                .size(24.dp)
                .clickable { (context as? ComponentActivity)?.finish() }
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = "已安装应用",
            style = MaterialTheme.typography.titleLarge
        )
    }
}

// 数据类，用于表示应用信息
data class AppInfo(
    val appInfo: ApplicationInfo? = null,
    val pm: PackageManager? = null,
    val packageName: String = "",
    val label: String = "",
    val firstInstallTime: Long = 0,
    val lastUpdateTime: Long = 0,
    val icon: BitmapDrawable? = null
) {
    // 构造函数，用于已安装的应用
    constructor(appInfo: ApplicationInfo, pm: PackageManager) : this(
        appInfo = appInfo,
        pm = pm,
        packageName = appInfo.packageName,
        label = appInfo.loadLabel(pm).toString(), // 从 ApplicationInfo 获取应用名称
        firstInstallTime = try {
            pm.getPackageInfo(appInfo.packageName, PackageManager.GET_META_DATA).firstInstallTime
        } catch (e: PackageManager.NameNotFoundException) {
            0
        },
        lastUpdateTime = try {
            pm.getPackageInfo(appInfo.packageName, PackageManager.GET_META_DATA).lastUpdateTime
        } catch (e: PackageManager.NameNotFoundException) {
            0
        },
        icon = appInfo.loadIcon(pm) as? BitmapDrawable
    )

    // 构造函数，用于未安装的应用
    constructor(packageName: String, appName: String) : this(
        packageName = packageName,
        label = appName, // 使用传入的应用名称
        firstInstallTime = 0,
        lastUpdateTime = 0,
        icon = null
    )
}

@Composable
fun AppItem(appInfo: AppInfo, pm: PackageManager) {
    // 安装时间和更新时间的格式化
    val installTimeFormatted = remember(appInfo.firstInstallTime) {
        if (appInfo.firstInstallTime > 0) {
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date(appInfo.firstInstallTime))
        } else {
            ""
        }
    }
    val updateTimeFormatted = remember(appInfo.lastUpdateTime) {
        if (appInfo.lastUpdateTime > 0) {
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date(appInfo.lastUpdateTime))
        } else {
            ""
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* 点击处理 */ },
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 应用图标
            if (appInfo.icon != null) {
                Image(
                    bitmap = appInfo.icon.bitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
            } else {
                // 使用默认图标
                Icon(
                    imageVector = Icons.Filled.Android,
                    contentDescription = "Default Icon",
                    modifier = Modifier.size(40.dp),
                    tint = Color.Gray
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // 应用信息
            Column {
                Text(
                    text = appInfo.label,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = appInfo.packageName,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                if (installTimeFormatted.isNotEmpty()) {
                    Text(
                        text = "安装时间: $installTimeFormatted",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
                if (updateTimeFormatted.isNotEmpty()) {
                    Text(
                        text = "更新时间: $updateTimeFormatted",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        }
    }
} 