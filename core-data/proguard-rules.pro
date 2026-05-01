# Keep Hilt generated classes
-keep class dagger.hilt.** { *; }
-keep class * extends dagger.hilt.android.lifecycle.HiltViewModel { *; }

# Keep Retrofit and Moshi
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.squareup.moshi.** { *; }

# Keep generated Moshi adapters
-keepclasseswithmembers class * {
    @com.squareup.moshi.FromJson <methods>;
}
-keepclasseswithmembers class * {
    @com.squareup.moshi.ToJson <methods>;
}

# Keep DataStore
-keep class androidx.datastore.** { *; }

# Keep annotations
-keepattributes RuntimeVisibleAnnotations
-keepattributes RuntimeInvisibleAnnotations
-keepattributes Exceptions

