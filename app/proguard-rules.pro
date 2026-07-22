# AdMob mediation adapters are loaded by reflection.
-keep class com.google.ads.mediation.** { *; }
-keep class com.facebook.ads.** { *; }
-dontwarn com.facebook.ads.**
-dontwarn com.facebook.ads.internal.**

# Crashlytics deobfuscation support.
-keepattributes SourceFile,LineNumberTable
