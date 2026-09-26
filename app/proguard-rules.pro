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
# MSAL (com.microsoft.identity) references FindBugs annotations that are compile-only.
-dontwarn edu.umd.cs.findbugs.annotations.NonNull
-dontwarn edu.umd.cs.findbugs.annotations.Nullable
-dontwarn edu.umd.cs.findbugs.annotations.SuppressFBWarnings

# Navigation routes + DTOs. Routes are type-safe (`composable<T>`, whose route is the
# serializer's serialName = original FQN) AND string-based (`Any.route` =
# `this::class.qualifiedName`). If R8 renames these classes the two stop matching and the
# serializer lookup fails at launch ("Serializer for class 'f' is not found"). Keep the
# names and generated serializers of every @Serializable class in the app.
-keepattributes *Annotation*, InnerClasses, EnclosingMethod, Signature
-keep @kotlinx.serialization.Serializable class com.bonjur.** { *; }
-keep class com.bonjur.**$$serializer { *; }
-keepclassmembers class com.bonjur.** {
    *** Companion;
    kotlinx.serialization.KSerializer serializer(...);
}
# The sealed route containers (AppScreens, ClubsScreens, MainScreen, ...) must survive too:
# if R8 drops/merges the outer interface, `qualifiedName` of the nested route loses its
# enclosing class and comes back as "AppScreens$Auth" instead of "AppScreens.Auth", which
# matches no destination. SharedNavArgs also hardcodes these dotted FQNs.
-keep interface com.bonjur.**.*Screens
-keep interface com.bonjur.**.*Screen
