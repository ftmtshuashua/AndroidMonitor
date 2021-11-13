AndroidMonitor
=====
[![](https://jitpack.io/v/ftmtshuashua/AndroidMonitor.svg)](https://jitpack.io/#ftmtshuashua/AndroidMonitor)
[![](https://img.shields.io/badge/jdk-1.8%2B-blue)]()
[![License Apache2.0](http://img.shields.io/badge/license-Apache2.0-brightgreen.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0.html)

各种实用的 Andorid 检测工具


#### Who Finish My Activity?
-----
[![](https://img.shields.io/badge/android-2.3%2B-blue)]()

Android 的 Handler 机制会导致调用栈断裂,就无法有效追踪导致 Activity.onDestroy() 被执行的原因. WFMA 可以绕过 Handler 机制,直达导致 Activity.onDestroy() 被执行的原因.

```
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //Activity关闭监听。日志TAG:WFMA
        WFMA.start();
    }
}
```

```
repositories {
    maven { url 'https://www.jitpack.io' }
    jcenter()
}

dependencies {
    implementation 'com.github.ftmtshuashua:AndroidMonitor:1.0.0-beta'
}
```

#### 计划

1.用户隐私信息合规检测