# touchImageView
android 拖拽imageView ，停留在四边框。


使用规则

 ![image](https://github.com/jansonqiang/touchImageView/blob/master/images/test1.gif?raw=true)


To get a Git project into your build:

Step 1. Add the JitPack repository to your build file

gradle

Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.jansonqiang:touchImageView:v1.0.1'
	}


使用方式：

```xml

    <com.janson.touchView.TouchImageView
        android:id="@+id/imageView"
        android:layout_width="50dp"
        android:layout_height="50dp"
        android:text="asdfa"
        android:background="@mipmap/ic_launcher"
        app:tiv_percentX="50"
        />

```

xml属性说明

|属性           | 说明                           |
| ------------- |:-------------:                 |
| tiv_marginY   | imageView可以移动上下边距      |
| tiv_marginX   | imageView可以移动左右边距      |
| tiv_percentX  | imageView缩回四边自身的百分比                      |
| tiv_delayMillis  | imageView 靠边后多少延迟后缩回自身                       |




