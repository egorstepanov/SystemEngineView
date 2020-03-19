# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/sosed/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}

# Keep line number table
-keepattributes LineNumberTable

# Keep exceptions
-keepattributes Throwable

# Keep the BuildConfig
-keep class io.example.systemengineview.BuildConfig

# Multidex
-keep class android.support.multidex.**

# AndroidX Multidex
-keepnames class androidx.multidex.**

# WebView
-keep class * extends android.webkit.RenderProcessGoneDetail
-keep class * implements android.webkit.RenderProcessGoneDetail
-keep class android.webkit.RenderProcessGoneDetail

-keep class * extends android.webkit.SafeBrowsingResponse
-keep class * implements android.webkit.SafeBrowsingResponse
-keep class android.webkit.SafeBrowsingResponse

-keepattributes *Annotation*
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
 
# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

# Retrofit 2.X
## https://square.github.io/retrofit/ ##

-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# Retrofit, OkHttp, Gson
-keepattributes *Annotation*
-keepattributes Signature
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**
-dontwarn rx.**
-dontwarn retrofit.**
-keep class retrofit.** { *; }
-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}
-keep class sun.misc.Unsafe { *; }
-dontwarn java.nio.file.*
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# OkHttp3
-keepattributes Signature
-keepattributes *Annotation*
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**

# RxJava 

-dontwarn sun.misc.Unsafe
-keep class rx.schedulers.Schedulers {
    public static <methods>;
}
-keep class rx.schedulers.ImmediateScheduler {
    public <methods>;
}
-keep class rx.schedulers.TestScheduler {
    public <methods>;
}
-keep class rx.schedulers.Schedulers {
    public static ** test();
}
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    long producerNode;
    long consumerNode;
}

# support constraint
-dontwarn android.support.constraint.**
-keep class android.support.constraint.** { *; }
-keep interface android.support.constraint.** { *; }
-keep public class android.support.constraint.R$* { *; }

# Support AppCompat
-keep class android.support.v7.widget.RoundRectDrawable { *; }

-keep public class android.support.v7.widget.** { *; }
-keep public class android.support.v7.internal.widget.** { *; }
-keep public class android.support.v7.internal.view.menu.** { *; }
-keep public class android.support.v7.graphics.drawable.** { *; }
-keep public class android.support.v4.graphics.drawable.** { *; }

-keep public class * extends android.support.v4.view.ActionProvider {
    public <init>(android.content.Context);
}

# Support Design
-dontwarn android.support.design.**
-keep class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }

# When layoutManager xml attribute is used, RecyclerView inflates
#LayoutManagers' constructors using reflection.
-keep public class * extends android.support.v7.widget.RecyclerView$LayoutManager {
    public <init>(...);
}

-keep class * extends android.support.v7.app.AppCompatViewInflater
-keep class * implements android.support.v7.app.AppCompatViewInflater
-keep class android.support.v7.app.AppCompatViewInflater

### Exoplayer2
-dontwarn com.google.android.exoplayer2.**

# JetBrains
-keep class org.jetbrains.** { *; }

# Lambdas
-dontwarn java.lang.invoke.**
-dontwarn **$$Lambda$*