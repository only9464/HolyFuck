package tql.holy.fuck.hook.apps

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.type.java.BooleanType

class WuaiPojie : YukiBaseHooker() {
    override fun onHook() {
        "$packageName.ui.ChallengeSecond".toClass()
            .method {
                name = "isvip"
                emptyParam()
                returnType = BooleanType
            }.hook {
//                这里是找到的时候执行的代码
                replaceToTrue()
                android.util.Log.d("Hook", "isvip方法hook执行完成，返回值111111")

            }
    }
} 