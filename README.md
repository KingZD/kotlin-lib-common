# kotlin-lib  

>#### 目前1.1.1版本的配置描述
>内置多服务器地址 在网络不佳情况下自动切换地址 可以动态添加并无需再次打开  
app配置方式请看[ZDApplication.kt](https://github.com/KingZD/kotlin-lib-common/blob/master/app/src/main/java/com/zed/example/ZDApplication.kt "ZDApplication")的initHttpParam()  
这里initHttpParam() httpCache的数据是 [HmbbPresenter.kt](https://github.com/KingZD/kotlin-lib-common/blob/master/app/src/main/java/com/zed/example/presenter/HmbbPresenter.kt "HmbbPresenter") 的getAllData()返回的服务器地址列表  
以上模式需要 下次打开app新的服务器地址才会加入轮询  
***如果需要即时生效***  
请在 [HmbbPresenter.kt](https://github.com/KingZD/kotlin-lib-common/blob/master/app/src/main/java/com/zed/example/presenter/HmbbPresenter.kt "HmbbPresenter") 的getAllData()按照 [ZDApplication.kt](https://github.com/KingZD/kotlin-lib-common/blob/master/app/src/main/java/com/zed/example/ZDApplication.kt "ZDApplication")的 ***initHttpParam()*** 方式进行添加

切换服务器时打印日志如下 可以看到每次切换<br/>
originalRequest url(原始地址) is http://127.0.0.1/xxx<br/>
current param url(地址列表里面的地址) is http://127.0.0.1:80/xxx<br/>
switch url(真实请求的地址) is http://127.0.0.1:80/xxx<br/>


<br/><br/>
>#### V1~1.1版本的配置描述
>[README_V1.md](https://github.com/KingZD/kotlin-lib-common/blob/master/README_V1.md "README_V1")

ps 以下的依赖只是common目录下的常用组建 不包含上面的自定义view

依赖方式
==
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
  	dependencies {
	        compile 'com.github.KingZD:kotlin-lib-common:1.1.1'
	}
