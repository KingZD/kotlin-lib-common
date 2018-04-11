# kotlin-lib  

>#### 目前1.1.1版本的配置描述
内置多服务器地址 在网络不佳情况下自动切换地址 可以动态添加并无需再次打开app

>#### 目前V1~1.1版本的配置描述
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
