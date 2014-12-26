# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\Leonardo\androidsdk/tools/proguard/proguard-android.txt
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
# -- Android Annotations --
-dontwarn org.springframework.**
-dontwarn com.squareup.okhttp.**
-dontwarn okio.**

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.preference.Preference

-keep public class * extends android.app.Fragment
-keep public class * extends android.support.v4.Fragment

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * implements android.os.Parcelable {
    static android.os.Parcelable$Creator CREATOR;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

-keepclassmembernames class * {
	public protected <methods>;
}

-keepclasseswithmembernames class * {
	native <methods>;
}

-dontwarn android.support.**
-keepattributes *Annotation*

-keep public class com.leonardo.findya.**
-keep public class com.leonardo.findya.acoes.**
-keep public class com.leonardo.findya.adapters.**
-keep public class com.leonardo.findya.comparators.**
-keep public class com.leonardo.findya.fragments.**
-keep public class com.leonardo.findya.outros.**