package tql.holy.fuck.hook

import com.highcapable.yukihookapi.annotation.xposed.InjectYukiHookWithXposed
import com.highcapable.yukihookapi.hook.factory.configs
import com.highcapable.yukihookapi.hook.factory.encase
import com.highcapable.yukihookapi.hook.xposed.proxy.IYukiHookXposedInit
import tql.holy.fuck.BuildConfig
import tql.holy.fuck.hook.apps.WuaiPojie

@InjectYukiHookWithXposed(isUsingResourcesHook = true)
class HookEntry : IYukiHookXposedInit {

    override fun onInit() = configs {
        debugLog {
            tag = "HolyFuck"
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
        // 无爱破解
        loadApp("com.zj.wuaipojie") {
            loadHooker(WuaiPojie())
        }
        
        // 其他应用的 Hook 示例
        // loadApp("其他包名") {
        //     loadHooker(AnotherApp())
        // }
    }
}