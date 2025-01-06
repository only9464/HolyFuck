package tql.holy.fuck.hook


import com.highcapable.yukihookapi.annotation.xposed.InjectYukiHookWithXposed
import com.highcapable.yukihookapi.hook.factory.configs
import com.highcapable.yukihookapi.hook.factory.encase
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.xposed.proxy.IYukiHookXposedInit
import com.highcapable.yukihookapi.hook.factory.registerModuleAppActivities
import tql.holy.fuck.BuildConfig
import android.os.Build
import tql.holy.fuck.R
import kotlin.math.log

@InjectYukiHookWithXposed(isUsingResourcesHook = true)
class HookEntry : IYukiHookXposedInit {

    override fun onInit() = configs {
        debugLog {
            tag = "YukiHookAPI"
            isEnable = true
            isRecord = false
            elements(TAG, PRIORITY, PACKAGE_NAME, USER_ID)
        }
        isDebug = BuildConfig.DEBUG
        isEnableModuleAppResourcesCache = true
        isEnableHookModuleStatus = true
        isEnableHookSharedPreferences = false
        isEnableDataChannel = true

        // Your code here.
    }

    override fun onHook() = encase {


        loadApp(name = "com.zj.wuaipojie") {
//            onAppLifecycle {
//                onCreate { if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) registerModuleAppActivities() }
//            }
            // Member Hook
            "$packageName.ui.ChallengeSecond".toClass().apply {
            method {
            name = "isvip"
            emptyParam()
            returnType = BooleanType
            }.hook {
                replaceToTrue()
                android.util.Log.d("Hook", "isvip方法执行完成,修改也好了")

                }
            }
        }

    }
}