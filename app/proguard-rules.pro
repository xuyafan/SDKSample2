# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\AndroidStudioSDK\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
#}
# 指定代码的压缩级别
-optimizationpasses 5
-dontusemixedcaseclassnames

# 是否混淆第三方jar
-dontwarn com.amap.api.**
-dontwarn org.apache.http.**
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-keepattributes SourceFile,LineNumberTable
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

# 不被混淆的
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.preference.Preference
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.support.v4.**
-keep public class * extends android.support.annotation.**
-keep public class * extends android.support.v7.**

# 第三方接口不混淆
-keep class com.tencent.android.tpush.** { *; }
-keep class com.tencent.mid.** { *; }
-keep class com.jg.** { *; }
-keep class com.qq.** { *; }
-keep class src.com.qq.** { *; }
-keep class com.nineoldandroids.** { *; }
-keep class com.aps.** { *; }
-keep class com.amap.api.** { *; }
-keep class com.google.protobuf.micro.** { *; }

# http client
-keep class org.apache.http.** {*; }

# 保持 native 方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

# 保持 Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

#自定义控件不被混淆

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

##某一变量不混淆
#-keepclasseswithmembers class com.xxx.xxx {
#    private java.io.FileDescriptor mFd;
#}
#
##某一方法不混淆
##注意参数和返回值如果不是基本类型，是类类型都必须写包名；
#-keepclasseswithmembers class com.xxx.xxx {
#    void m1();
#    boolean m2(android.content.Context);
#    com.xxx.xxx.Temp m3(com.xxx.xxx.Temp);
#}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}


# 实体类不混淆==============================
-keep class com.pep.szjc.download.bean.** { *; }
-keep class com.pep.szjc.download.dbbean.** { *; }
-keep class com.pep.szjc.home.bean.** { *; }
-keep class com.pep.szjc.read.bean.** { *; }

-keep class java.nio.** {*;}
-dontwarn java.nio.**


# webView处理，项目中没有使用到webView忽略即可=================================
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
    public *;
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.webView, jav.lang.String);
}


#eventbus=======================
-keepattributes *Annotation*
 -keepclassmembers class ** {
     @org.greenrobot.eventbus.Subscribe <methods>;
 }
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

 # Only required if you use AsyncExecutor
 -keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
     <init>(java.lang.Throwable);
 }

 #butterknife=========================================
 -keep class butterknife.** { *; }
 -dontwarn butterknife.internal.**
 -keep class **$$ViewBinder { *; }

 -keepclasseswithmembernames class * {
     @butterknife.* <fields>;
 }

 -keepclasseswithmembernames class * {
     @butterknife.* <methods>;
 }

 # FastJson==============================================
 -dontwarn com.alibaba.fastjson.**
 -keep class com.alibaba.fastjson.** { *; }
 -keepattributes Signature
 -keepattributes *Annotation*


 # Glide======================================
 -keep public class * implements com.bumptech.glide.module.GlideModule
 -keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
   **[] $VALUES;
   public *;
 }


 # Gson===============================================
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }
# Application classes that will be serialized/deserialized over Gson

 # OkHttp3=============================================
 -dontwarn com.squareup.okhttp3.**
 -keep class com.squareup.okhttp3.** { *;}
 -dontwarn okio.**


 # Retrofit==========================================
 -dontwarn retrofit2.**
 -keep class retrofit2.** { *; }
 -keepattributes Signature
 -keepattributes Exceptions

-keep class com.iflytek.**{*;}


-keepclasseswithmembernames class * {
    native <methods>;
}

-dontwarn com.rjsz.booksdk.**
-keep class com.rjsz.booksdk.** { *; }
# End NetworkBench Lens
-keep class com.chivox.** { *; }

-keepclassmembers class com.rjsz.booksdk.bean.* { *; }
-keepclassmembers class com.rjsz.booksdk.event.* { *; }
-keep class *.R
-keepclasseswithmembers class **.R$* {    public static <fields>;}