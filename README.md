# Android Intent Flags 使用

[TOC]

## 一、基本使用

>   `setFlags`, 直接设置flags, 之前设置的flags会被覆盖

```java
intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
```

内部实现如下: 

```java
public @NonNull Intent setFlags(@Flags int flags) {
    mFlags = flags;
    return this;
}
```

>   `addFlags`, 追加flags, 在之前设置的flags的基础上添加

```java
intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
```

内部实现如下:

```java
public @NonNull Intent addFlags(@Flags int flags) {
    mFlags |= flags;
    return this;
}
```



## 二、Flags介绍

### 1. 系统定义的Flags

* FLAG_GRANT_READ_URI_PERMISSION
* FLAG_GRANT_WRITE_URI_PERMISSION
* FLAG_GRANT_PERSISTABLE_URI_PERMISSION
* FLAG_GRANT_PREFIX_URI_PERMISSION
* FLAG_FROM_BACKGROUND
* FLAG_DEBUG_LOG_RESOLUTION
* FLAG_EXCLUDE_STOPPED_PACKAGES
* FLAG_INCLUDE_STOPPED_PACKAGES
* FLAG_DEBUG_TRIAGED_MISSING
* FLAG_IGNORE_EPHEMERAL
* FLAG_ACTIVITY_MATCH_EXTERNAL
* FLAG_ACTIVITY_NO_HISTORY
* FLAG_ACTIVITY_SINGLE_TOP
* FLAG_ACTIVITY_NEW_TASK
* FLAG_ACTIVITY_MULTIPLE_TASK
* FLAG_ACTIVITY_CLEAR_TOP
* FLAG_ACTIVITY_FORWARD_RESULT
* FLAG_ACTIVITY_PREVIOUS_IS_TOP
* FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
* FLAG_ACTIVITY_BROUGHT_TO_FRONT
* FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
* FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY
* FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET
* FLAG_ACTIVITY_NEW_DOCUMENT
* FLAG_ACTIVITY_NO_USER_ACTION
* FLAG_ACTIVITY_REORDER_TO_FRONT
* FLAG_ACTIVITY_NO_ANIMATION
* FLAG_ACTIVITY_CLEAR_TASK
* FLAG_ACTIVITY_TASK_ON_HOME
* FLAG_ACTIVITY_RETAIN_IN_RECENTS
* FLAG_ACTIVITY_LAUNCH_ADJACENT
* FLAG_RECEIVER_REGISTERED_ONLY
* FLAG_RECEIVER_REPLACE_PENDING
* FLAG_RECEIVER_FOREGROUND
* FLAG_RECEIVER_NO_ABORT
* FLAG_RECEIVER_REGISTERED_ONLY_BEFORE_BOOT
* FLAG_RECEIVER_BOOT_UPGRADE
* FLAG_RECEIVER_INCLUDE_BACKGROUND
* FLAG_RECEIVER_EXCLUDE_BACKGROUND
* FLAG_RECEIVER_FROM_SHELL
* FLAG_RECEIVER_VISIBLE_TO_INSTANT_APPS

### 2. Actvity Flags

#### 测试工具

主要利用[IntentTest](https://github.com/Jeasoon/IntentFlagsTest)App进行测试验证



#### FLAG_ACTIVITY_NEW_TASK

>   要点

*   很重要的一个标识
*   启动Activity时, 如果符合Activity TaskAffinity的栈没有创建, 就会创建一个, 然后再启动Activity放置其中
*   当App从Launcher启动时, Intent会添加这个标志位
*   如果使用Application启动Activity, 必须添加这个标志位
*   通常配合的其他标志位有: 
    *   `Intent.FLAG_ACTIVITY_CLEAR_TASK`
    *   `Intent.FLAG_ACTIVITY_CLEAR_TOP`
    *   `Intent.FLAG_ACTIVITY_MULTIPLE_TASK`
    *   `Intent.FLAG_ACTIVITY_NEW_DOCUMENT`



#### FLAG_ACTIVITY_CLEAR_TOP

> 要点

* 没有指定Activity TaskAffinity栈的情况下
  * 如果目标Activity还未启动, 直接在默认栈创建一个新的实例
  * 如果目标Activity已经启动, 并且已处于栈顶, 则会先启动一个新的实例, 再Destroy掉之前的实例
  * 如果目标Activity已经启动, 并且不处于栈顶, 则会先Destroy掉之前的实例, 再启动一个新的实例, 最后Destroy掉之前实例之上的其他Activity实例
* 已经指定Activity TaskAffinity栈的情况下
  * 如果目标Activity还未启动, 不管是否有栈名称指定的Affinity匹配, 直接创建一个新栈和新的实例
  * 如果目标Activity已经启动, 直接将所在栈拉到前台
  * 如果只指定`FLAG_ACTIVITY_CLEAR_TOP`这一个标志位, 启动时系统自动添加`FLAG_ACTIVITY_NEW_TASK`标志位, 效果等同于SingleInstance



#### FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS

> 要点

* 设置后Activity不会展示到最近任务页面中, 建议加上TaskAffinity配合使用
  * 例子: A启动B时, 添加`FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS`标志, 按Home回到桌面后再按最近任务页面, 显示的为A, 而不是B
* 通过点击最近页面或者图标再次进入时, 会进入其他Activity页面
* 尝试配合`FLAG_ACTIVITY_NEW_TASK`和`FLAG_ACTIVITY_MULTIPLE_TASK`使用时, 效果很奇怪, 整个App都不在最近页面了



#### FLAG_ACTIVITY_FORWARD_RESULT

> 要点

* 委托请求结果
  * 例子: A通过`startActivityForResult`启动B, B不知如何`setResult`, B就启动C, 由C设置`setResult`
  * 当C返回结果后, B设置`setResult`并没有任何效果, A收到的值是C的结果
  * 再由B启动C时需要调用`startActivity`, 使用`startActivityForResult`会抛出`android.util.AndroidRuntimeException: FORWARD_RESULT_FLAG used while also requesting a result`异常



#### FLAG_ACTIVITY_NEW_DOCUMENT

> 要点

* 在最近任务页面里面添加同一个App的不同页面记录
* 如果之前没有启动过Activity, 会创建一个新的任务栈, 并启动一个新的Activity放入, 否则直接将之前的任务栈拉到前台
* 系统会自动添加`FLAG_ACTIVITY_NEW_TASK`标志位
* 如果单独使用的话, 效果果同在Manifest设置`documentLaunchMode="intoExisting"`
* 可以配合`FLAG_ACTIVITY_MULTIPLE_TASK`使用, 这样就总会创建新的任务栈和Activity实例, 效果同在Manifest设置`documentLaunchMode="always"`
* 在手机上测试时发现, 不指定这个标志位, 只设置TaskAffinity, 也能达到这种效果, 但只有一个实例, 设置`FLAG_ACTIVITY_MULTIPLE_TASK`也没有效果



#### FLAG_ACTIVITY_NO_ANIMATION

> 要点

* 去除动画效果
* 仅这一次有效, 如果A启动B, B再启动C, 需要再次设置这个标志位



#### FLAG_ACTIVITY_NO_HISTORY

> 要点

* 通过设置`FLAG_ACTIVITY_NO_HISTORY`启动的Activity在失去前台后会被销毁掉
* 并不是立即销毁掉, 实际是这个Activity上面没有其他Activity时才会开始销毁
* 感觉就是: 先失去前台, 再获取到前台, 发现设置了`FLAG_ACTIVITY_NO_HISTORY`标志位, 就直接销毁了



#### FLAG_ACTIVITY_NO_USER_ACTION

> 要点

* Intent设置`FLAG_ACTIVITY_NO_USER_ACTION`后, 调用startActivity所在的Activity, 不会调用`onUserLeaveHint`, 不是被调用者
* 不设置`FLAG_ACTIVITY_NO_USER_ACTION`情况下
  * `onUserLeaveHint`在失去前台时会被调用, 但在Activity销毁时不会被调用
  * 不知道是不是手机问题, 通过最近任务页面跳转到其他App时, `onUserLeaveHint`也不会被调用



#### FLAG_ACTIVITY_LAUNCH_ADJACENT

> 要点

* 仅在分屏多窗口模式下起作用
* 新Activity将显示在启动它的Activity的旁边
* 需要与`FLAG_ACTIVITY_NEW_TASK`搭配使用, 如果需要创建现有Activity的新实例, 可以添加`FLAG_ACTIVITY_MULTIPLE_TASK`
* 这个没有在手机上实际验证



#### FLAG_ACTIVITY_REORDER_TO_FRONT

> 要点

* 效果为在不销毁Activity的前提下, 更改当前栈Activity的顺序
* 例子:
  * 假设当前栈有Activity A、B、C、D, D在栈顶
  * D 启动Activity B, 并添加`FLAG_ACTIVITY_REORDER_TO_FRONT`标志位
  * 当前栈的顺序改为: A、C、D、B
  * B 会调用`onNewIntent`
* 如果设置了`FLAG_ACTIVITY_CLEAR_TOP`, 忽略本标志位, 走`FLAG_ACTIVITY_CLEAR_TOP`这一套
* 与`FLAG_ACTIVITY_CLEAR_TOP`的区别就在于, 是否会销毁上面的Activity



#### FLAG_ACTIVITY_RESET_TASK_IF_NEEDED

> 要点

* 转移Activity到其他栈, 一般用于不同App之间
* 需要在清单文件配置如下两个属性:
  * `android:export="true"`允许其他App启动
  * `android:allowTaskReparenting=true`允许Activity所在栈可以更换
* 参考网络资源, 主要工作流程如下:
  * A 应用有主Activity A 和Activity A_2, A_2 设置`android:export="true"`和`android:allowTaskReparenting=true`
  * B 应用有主Activity B
  * Activity B 启动 Activity A_2, 当前Activity A_2 在应用B的栈内
  * 回到桌面启动Activity A, 启动时, 系统会自动添加`FLAG_ACTIVITY_RESET_TASK_IF_NEEDED`和`FLAG_ACTIVITY_NEW_TASK`标志位
  * 此时系统会从所有App栈记录中查找和 Activity A taskAffinity相同的Activity, 并将找到的Activity放入这个A应用的新栈中



#### FLAG_ACTIVITY_RETAIN_IN_RECENTS

> 要点

* 需要配合`FLAG_ACTIVITY_NEW_DOCUMENT`使用, 系统会自动添加`FLAG_ACTIVITY_NEW_TASK`标志位
* 使用 `FLAG_ACTIVITY_NEW_DOCUMENT`时, 加不加`FLAG_ACTIVITY_RETAIN_IN_RECENTS`并没明显作用



#### FLAG_ACTIVITY_SINGLE_TOP

> 要点

* 如果Activity已经在栈顶部, 则不会启动新实例, 仅调用`onNewIntent`
* 可以搭配`FLAG_ACTIVITY_CLEAR_TOP`使用, 如果只使用`FLAG_ACTIVITY_CLEAR_TOP`, 会销毁本Activity和上面的其他Activity; 如果加上`FLAG_ACTIVITY_SINGLE_TOP`, 就只销毁本Activity上面的其他Activity, 本Activity会保留, 并调用`onNewIntent`



#### FLAG_DEBUG_LOG_RESOLUTION

> 要点

* 启动Debug的标志, 在解析本Intent时打印日志消息
* 可以在logcat中过滤`IntentResolver`关键字, 主要是`queryIntent`方法



#### FLAG_EXCLUDE_STOPPED_PACKAGES / FLAG_INCLUDE_STOPPED_PACKAGES

> 要点

* `FLAG_EXCLUDE_STOPPED_PACKAGES`
  * 此Intent不去匹配没有运行的App, 防止唤醒
* `FLAG_INCLUDE_STOPPED_PACKAGES`
  * 此Intent去匹配所有App, 包括没有运行的App
* 这两个Flag都不设置或者都设置, 效果同`FLAG_EXCLUDE_STOPPED_PACKAGES`



#### FLAG_FROM_BACKGROUND

> 要点

* 表示Intent来自后台操作, 而不是直接来自用户操作
* 手机测试时, 并没有发现什么特殊效果



#### FLAG_ACTIVITY_TASK_ON_HOME

> 要点

* 当Actvity返回时, 直接回到桌面, 需要搭配`FLAG_ACTIVITY_NEW_TASK`使用
* 需要让Activity在栈底, 可选下列其一
    *   指定taskAffinity
    *   配合`FLAG_ACTIVITY_NEW_DOCUMENT`


### 2. Uri Flags

#### FLAG_GRANT_READ_URI_PERMISSION / FLAG_GRANT_WRITE_URI_PERMISSION

> 要点

* 临时对目标授予Uri的读/写权限
* 只使用这两个的话, 设备重启权限就消失了, 需要重新授予

#### FLAG_GRANT_PERSISTABLE_URI_PERMISSION

> 要点

* 授予目标的权限一直保持, 重启设备后依然存在, 这个只是提供可能持久授权
* 需要`ContentResolver.takePersistableUriPermission(Uri, modeFlag)`实现

#### FLAG_GRANT_PREFIX_URI_PERMISSION

> 要点

* 添加`FLAG_GRANT_PREFIX_URI_PERMISSION`标志位后, 只要Uri的前缀相匹配就有相应的权限
* 如果没有这个标志位,  需要Uri完全匹配才有相应的权限

#### 使用案例

> 授予目标权限

```java
Intent intent = new Intent();
// 这里指定读和写权限
intent.addFlags(
    Intent.FLAG_GRANT_READ_URI_PERMISSION 
	| Intent.FLAG_GRANT_WRITE_URI_PERMISSION
	| Intent.FLAG_GRANT_PREFIX_URI_PERMISSION
	| Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
);
// 指定赋予权限的Uri
intent.setData = Uri.parse("content://com.jeasoon.learnintent/hello/world");
intent.setComponent = new ComponentName("com.jeasoon.learnintent_1", "com.jeasoon.learnintent.MainActivity");
// 启动目标, 启动后的目标就有对指定Uri的读写权限了
startActivity(intent);
```

>   回收权限

```java
// 回收权限后, 目标就不能对Uri的读写做操作了
Context.revokeUriPermission(
    Uri.parse("content://com.jeasoon.learnintent/hello/world"),
    Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
)
```

### 3. Receiver Flags

#### FLAG_RECEIVER_REGISTERED_ONLY

> 要点

* 只有动态注册的BroadcastReceiver可以收到消息
* 在AndroidManifest.xml注册的BroadcastReceiver无法收到



#### FLAG_RECEIVER_REPLACE_PENDING

> 要点

* 如果你的广播发送速度过快, 系统还没来得及处理之前的广播, 这些广播就会被最新的广播替换掉
* 确保让程序处理的广播是最新的



#### FLAG_RECEIVER_FOREGROUND

> 要点

* 以前台优先级运行广播接受者
* 优先运行, 运行间隔会变小



#### FLAG_RECEIVER_NO_ABORT

> 要点

* 发送有序广播时, 不允许中断广播
* 后续广播接受者可以收到广播并继续修改传递结果



#### FLAG_RECEIVER_VISIBLE_TO_INSTANT_APPS

> 要点

* 广播可被Instant App接收
* 默认Instant App不接收任何广播


