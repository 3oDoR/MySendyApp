# MySendyApp

# 1. Splash screen
Сделал Splash-экран с помощью темы + заменил тему активити на MySplashScreen.

```xml
<style name="Theme.SplashScreen.MySplashScreen" parent="Theme.MySendyApp" >  
 <item name="android:windowSplashScreenBackground">@color/white</item>  
 <item name="android:windowSplashScreenAnimatedIcon">@drawable/ic_logo_foreground</item>  
 <item name="android:windowSplashScreenAnimationDuration">3000</item>  
 <item name="android:windowSplashScreenIconBackgroundColor">@color/fineBlue</item>  
 <item name="postSplashScreenTheme">@style/Theme.MySendyApp</item>  
</style>
```

```xml
<activity  
  android:name=".MainActivity"  
  android:exported="true"  
  android:theme="@style/Theme.SplashScreen.MySplashScreen"> 
```
