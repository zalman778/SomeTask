# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

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

#app models
-keep class com.hwx.myapplication.network.model.** { *; }

#app models for json serialization
-keep,includedescriptorclasses class com.hwx.myapplication.network.model.**$$serializer { *; } # <-- change package name to your app's
-keepclassmembers class com.hwx.myapplication.network.model.** { # <-- change package name to your app's
    *** Companion;
}
-keepclasseswithmembers class com.hwx.myapplication.network.model.** { # <-- change package name to your app's
    kotlinx.serialization.KSerializer serializer(...);
}

#compose
-keep class androidx.compose.** { *; }

## Kotlinx serialization
-keepattributes *Annotation*, InnerClasses
-keepattributes Signature
-keepattributes Exceptions

-dontnote kotlinx.serialization.SerializationKt


## Keep Ktor client serializers and pipelines
-keep class io.ktor.** { *; }
-keep class io.ktor.**.** { *; }
-keepclassmembers class io.ktor.** { *; }

-keep class io.netty.** { *; }
-keepclassmembers class io.netty.** { *; }
-dontwarn io.netty.**

-dontwarn kotlin.Experimental$Level
-dontwarn kotlin.Experimental
-dontwarn org.slf4j.impl.StaticLoggerBinder

# Keep suspend functions metadata
-keepclassmembers,allowobfuscation class * {
    @kotlin.Metadata *;
}

# Prevent obfuscation of classes using reflection
-keepnames class * {
    @io.ktor.util.reflect.TypeInfo *;
}
-keepclassmembers class * {
    @io.ktor.util.reflect.TypeInfo *;
}


# Gson/Kotlin serialization compatibility (if used)
-keep class kotlinx.serialization.** { *; }

# Avoid obfuscating parameterized types
-keep,allowobfuscation class * implements java.lang.reflect.ParameterizedType { *; }
