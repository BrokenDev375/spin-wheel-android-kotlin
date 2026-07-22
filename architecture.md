# KIẾN TRÚC APP ANDROID (Kotlin + Compose) — Tài liệu tái sử dụng

> Tổng hợp từ kinh nghiệm thực chiến nhiều dự án. Dùng làm **blueprint** khi bắt đầu app Android mới.
> Mọi phần đặc thù từng app đã được lược bỏ — chỉ giữ kiến trúc, pattern, thành phần tái sử dụng, bug/bài học cần nhớ.

---

## MỤC LỤC

1. [Tổng quan kiến trúc](#1-tổng-quan-kiến-trúc)
2. [Cấu trúc thư mục/package chuẩn](#2-cấu-trúc-thư-mụcpackage-chuẩn)
3. [Tích hợp thư viện `base-application` AAR](#3-tích-hợp-thư-viện-base-application-aar)
4. [Tầng quảng cáo — Thành phần tái sử dụng](#4-tầng-quảng-cáo--thành-phần-tái-sử-dụng)
5. [Logic Intro/Onboarding (gate theo nguồn cài)](#5-logic-introonboarding-gate-theo-nguồn-cài)
6. [Điều hướng & NavHost](#6-điều-hướng--navhost)
7. [Tầng dữ liệu & Pattern](#7-tầng-dữ-liệu--pattern)
8. [Theme & UI System](#8-theme--ui-system)
9. [Đa ngôn ngữ (i18n)](#9-đa-ngôn-ngữ-i18n)
10. [Catalog bug & bài học thực chiến](#10-catalog-bug--bài-học-thực-chiến)
11. [Quy trình build, ký & test](#11-quy-trình-build-ký--test)
12. [Pattern xây feature mới (từ A→Z)](#12-pattern-xây-feature-mới-từ-az)
13. [Chuyển Ads từ GMA SDK sang lib](#13-chuyển-ads-từ-gma-sdk-sang-lib)

---

## 1. TỔNG QUAN KIẾN TRÚC

### 1.1 Các khối chính

| Khối | Công nghệ | Vai trò |
|---|---|---|
| UI | Jetpack Compose | Toàn bộ màn hình là `@Composable` |
| Điều hướng | Compose Navigation (`NavHost`) | 1 `NavHost` duy nhất, route dạng enum |
| DI | Hilt (`@HiltAndroidApp`, `@AndroidEntryPoint`, `@HiltViewModel`) | Cung cấp Room DB, Repository, ViewModel |
| Kiến trúc tầng | MVVM nhẹ | Composable → StateFlow ← ViewModel → Repository |
| Dữ liệu | Room (SQLite), SharedPreferences | Room lưu dữ liệu; SharedPreferences lưu cấu hình |
| Ads/IAP/Splash/Consent/Notification | Thư viện AAR `base-application` | App **không tự viết** các phần này |

### 1.2 Single-Activity

App chỉ có **một `Activity`**: `MainActivity`. Toàn bộ UI là các **route** trong `NavHost`. Thư viện `base-application` sở hữu các Activity riêng (Splash, Language, IAP) — app không cần biết code bên trong.

### 1.3 Luồng khởi động

```
Hệ điều hành → MyApplication.onCreate()
   1. super.onCreate()              ← lib init Ads/Consent/IAP/Notification, fetch Remote Config
   2. Đăng ký Remote Config default riêng app
   3. InstallReferrerHelper.resolve()  ← xác định nguồn cài (ads/organic)
         │
         ▼
Lib: Splash → Interstitial → Language → IAP
         │  lib gọi getHomeActivity()
         ▼
MainActivity.onCreate()
   1. Áp lại ngôn ngữ đã lưu
   2. resolveStartRoute() → Intro hay Home
   3. setContent { AppTheme { AppNavHost(startRoute); NativeInterHost() } }
```

> ⚠️ **MainActivity KHÔNG khai `MAIN/LAUNCHER`** — launcher là `SplashActivity` của thư viện.

---

## 2. CẤU TRÚC THƯ MỤC/PACKAGE CHUẨN

```
app/src/main/java/<pkg>/
├── MyApplication.kt          ← Tích hợp base-application
├── core/
│   ├── MainActivity.kt       ← Activity duy nhất
│   ├── AppStorage.kt         ← SharedPreferences wrapper (first_open, language, go_to_home_number...)
│   ├── InstallReferrerHelper.kt  ← Xác định nguồn cài (ads/organic), cache 3 tầng
│   ├── LocaleHelper.kt       ← Áp per-app locale
│   └── IapOpener.kt          ← Mở màn IAP
├── di/AppModule.kt            ← Hilt module (Room, ...)
├── data/
│   ├── db/                    ← Room Entity + DAO
│   ├── model/                 ← Data class dùng chung
│   └── repo/                  ← Repository (gọi Android API, network)
├── firebase/Remote.kt         ← Facade Remote Config (delegate sang lib)
├── advertisement/
│   ├── AdsViewModel.kt        ← Cache native ad theo Activity
│   ├── NativeAdSlot.kt        ← Composable nhúng native ad
│   ├── AdManager.kt           ← Điều phối inter + chain native-inter
│   ├── AdScenario.kt          ← Tần suất (ratio + maxPerDay + dọn key >7 ngày)
│   ├── NativeInter.kt         ← Native-interstitial fullscreen
│   ├── NativeAdsFull.kt       ← Native fullscreen cho modal Intro
│   └── AdPositions.kt         ← Parse positionIntrol (A/B test Intro)
├── ui/
│   ├── theme/Theme.kt
│   ├── components/            ← Composable dùng lại (Card, Header, SearchBar...)
│   ├── nav/
│   │   ├── Screen.kt          ← Enum route
│   │   └── AppNavHost.kt      ← NavHost
│   └── screen/<feature>/      ← Mỗi feature 1 package (ViewModel + Screen)
└── res/
    ├── layout/ad_native_full.xml  ← Layout native fullscreen tự dựng
    └── xml/config.xml             ← Remote Config default (placement enable/ratio/max)
```

---

## 3. TÍCH HỢP THƯ VIỆN `base-application` AAR

### 3.1 Bản chất

Thư viện sở hữu **lớp vỏ khởi động/monetize**: là **launcher** (SplashActivity), chạy `Splash → Inter → Language → IAP → getHomeActivity()`, tự lo **ads + consent(UMP) + init SDK + FCM + notification + foreground service + app-open/resume**, đọc mọi ad-unit từ **một** key Remote Config `ads_config`.

**Tích hợp = thay vỏ khởi động/monetize của app bằng vỏ lib, giữ các màn tính năng.**

### 3.2 Yêu cầu môi trường

| Mục | Giá trị |
|---|---|
| `minSdk` | 28 |
| `compileSdk` / `targetSdk` | 35 |
| Java / Kotlin JVM target | 17 |
| AGP | 8.12.0 |
| Kotlin | 2.1.20 |
| KSP | 2.1.20-1.0.32 |
| Plugin | `google-services` 4.4.3, `firebase-crashlytics` 3.0.6 |

### 3.3 Quy trình tích hợp (5 phase)

> **Quan trọng:** Phase 1→4 làm **một thể, KHÔNG build từng phase**. Xong hết rồi mới build debug + sửa lỗi, cài máy test, rồi Phase 5 (R8/release).

#### Phase 1 — Toolchain + module AAR

- `settings.gradle`: `include(":base-application")` + repos
- Tạo module `base-application/`: AAR + `build.gradle` khai **đủ transitive deps** (AAR không kèm POM → thiếu = `NoClassDefFoundError`)
- `app/build.gradle`: `minSdk 28`, Java/Kotlin 17, `multiDexEnabled`, `viewBinding=true`, `buildConfig=true`, Firebase BoM 34.1.0, `api(project(":base-application"))`
- **GỠ** mọi dep trùng lib (play-services-ads, ump, facebook mediation/sdk, appsflyer, glide…)

**Dependency đầy đủ cho module `base-application/build.gradle`:**

```groovy
configurations.maybeCreate("default")
artifacts.add("default", file('base-application-1.0.0.aar'))

dependencies {
    "default"("androidx.core:core-ktx:1.16.0")
    "default"("androidx.appcompat:appcompat:1.7.1")
    "default"("com.google.android.material:material:1.13.0")
    "default"("androidx.preference:preference:1.2.1")
    "default"("androidx.work:work-runtime:2.10.3")
    "default"("com.android.billingclient:billing:7.1.0")
    "default"("com.google.android.gms:play-services-ads:25.2.0")
    "default"('com.google.android.ump:user-messaging-platform:4.0.0')
    "default"("com.google.ads.mediation:facebook:6.21.0.2")
    "default"(platform("com.google.firebase:firebase-bom:34.1.0"))
    "default"("com.google.firebase:firebase-analytics")
    "default"("com.google.firebase:firebase-crashlytics")
    "default"("com.google.firebase:firebase-messaging-ktx:24.1.2")
    "default"("com.google.firebase:firebase-firestore:26.0.2")
    "default"("com.google.firebase:firebase-appcheck-playintegrity:19.0.0")
    "default"("com.google.firebase:firebase-config:23.0.0")
    "default"("com.intuit.sdp:sdp-android:1.1.1")
    "default"("com.intuit.ssp:ssp-android:1.1.1")
    "default"("com.facebook.shimmer:shimmer:0.5.0")
    "default"("com.airbnb.android:lottie:6.6.7")
    "default"("androidx.media:media:1.0.0")
    "default"('com.appsflyer:purchase-connector:2.1.2')
    "default"('de.hdodenhof:circleimageview:3.1.0')
    "default"('com.github.castorflex.smoothprogressbar:library-circular:1.3.0')
    "default"("org.jsoup:jsoup:1.21.2")
    "default"("com.squareup.okhttp3:okhttp:5.2.1")
    "default"('com.akexorcist:localization:1.2.11')
    "default"('androidx.lifecycle:lifecycle-process:2.9.4')
    "default"('com.appsflyer:af-android-sdk:6.18.0')
    "default"('com.adjust.sdk:adjust-android:5.6.1')
    "default"('com.android.installreferrer:installreferrer:2.2')
    "default"('com.facebook.android:facebook-android-sdk:18.1.3')
    "default"('com.github.bumptech.glide:glide:4.16.0')
    "default"('com.github.ybq:Android-SpinKit:1.4.0')
    "default"(platform("androidx.compose:compose-bom:2024.12.01"))
    "default"("androidx.compose.ui:ui")
    "default"("androidx.compose.foundation:foundation")
    "default"("androidx.compose.material3:material3")
    "default"("androidx.activity:activity-compose:1.9.3")
    "default"("com.airbnb.android:lottie-compose:6.6.7")
}
```

#### Phase 2 — HostApplication + Manifest + Resources

- `MyApplication : BaseApplication()` **giữ `@HiltAndroidApp`** nếu dùng Hilt. Implement **đủ hàm abstract** (dùng `javap` lấy chữ ký chính xác).
- Manifest: **gỡ `MAIN/LAUNCHER`** khỏi MainActivity; thêm meta-data `admob_app_id`/`facebook_app_id`/`facebook_client_token`
- **4 quyền BẮT BUỘC khai báo** (AAR không ship sẵn):

```xml
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE_SPECIAL_USE" />
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
<uses-permission android:name="com.google.android.gms.permission.AD_ID" />
```

- Tài nguyên: `assets/default_ads_config.json`, `res/raw/splash_loading.json`, colors (`primaryColor` == `accentTone`, `text1`), drawable (icon_app, icon_notification 5 density, icon IAP, icon noti), string-array `notification_title2/message2/button2` (**đúng 5 phần tử mỗi mảng**, ghép theo index)

#### Phase 3 — Gỡ vỏ trùng + Ads migration + RemoteConfig + Premium

- Gỡ Splash/Language/Consent startup của app (lib lo)
- **RemoteConfig**: KHÔNG gọi `FirebaseRemoteConfig.setDefaultsAsync(...)` trực tiếp (xoá `ads_config`). Dùng `FirebaseRemoteConfigUtil.getInstance().setAppDefaults(...)` hoặc `setAppDefaultsFromXml(R.xml.config)` **SAU** `super.onCreate()`
- **Ads**: xóa consent/MobileAds.init/AppOpen tự viết; native→`Admob.loadNativeAd`; inter→`Admob.loadAndShowInter`
- **Premium**: `GlobalAds.isPremiumUser` → `IAPUtils.isPremium()`

#### Phase 4 — Notification (nếu gỡ hệ app cũ)

- Xóa FCM service + scheduler + FGS + receiver của app cũ; gỡ service/receiver/meta-data trong manifest → còn 1 FCM + 1 FGS của lib

#### Phase 5 — R8 / Release

- `isMinifyEnabled = true` **và** `isShrinkResources = true` (thiếu 1 trong 2 → AGP báo lỗi)
- `manifestPlaceholders["crashlyticsCollectionEnabled"]` = `"false"` (debug) / `"true"` (release)
- `proguard-rules.pro`:

```proguard
# AdMob mediation (nạp bằng reflection)
-keep class com.google.ads.mediation.** { *; }
-keep class com.facebook.ads.** { *; }
-dontwarn com.facebook.ads.**

# Crashlytics deobfuscate
-keepattributes SourceFile,LineNumberTable

# Model JSON/Gson riêng app
-keep class <package.model.cua.app>.** { *; }
-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
}
```

> ⚠️ KHÔNG cần thêm `-keep` cho `com.nlbn.**` / `com.brian.**` — AAR đã đóng gói consumer rules.
> ⚠️ KHÔNG thêm `-dontoptimize` — rule strip log của AAR cần optimization bật.

### 3.4 Các hàm BẮT BUỘC override (`abstract`) trong HostApplication

| Nhóm | Hàm | Ví dụ |
|---|---|---|
| Splash & thương hiệu | `getAppNameRes()`, `getIconSplashRes()`, `getSplashLoadingRes()` | `R.string.app_name`, `R.drawable.icon_app`, `R.raw.splash_loading` |
| Định tuyến | `getHomeActivity(): Class<out Activity>` | `MainActivity::class.java` |
| Năng lực | `hasForegroundServicePermission(): Boolean` | |
| Khởi tạo | `initAppFlyerId()`, `setupKoin()` | |
| Ngôn ngữ | `notifyLanguageSaved(code: String)` | Lưu vào SharedPreferences |
| IAP | `iapPremiumKey()`, `iapPremiumWeeklyKey/Monthly/Yearly()`, `iapPublicKey()` | |
| Icon IAP | `getFeature1IconRes()` … `getFeature5IconRes()` | Đi cặp với text |
| Notification | ~20 hàm `getNotification...Res()`, `getNotificationImages(): IntArray` (5 icon) | Nội dung phải hợp chủ đề app |

### 3.5 Các hàm TUỲ CHỌN override (`open`)

| Hàm | Mặc định | Khi nào override |
|---|---|---|
| `isPurchased()` | `false` | `= IAPUtils.isPremium()` |
| `enableAdsResume()` | `true` | `= !IAPUtils.isPremium()` |
| `getListTestDeviceId()` | rỗng | Test device id |
| `buildDebug()` | `false` | `= BuildConfig.DEBUG` |

### 3.6 Mẹo: lấy chữ ký chính xác hàm abstract

```bash
unzip -o classes.jar -d jardir   # classes.jar lấy từ trong AAR
javap -p -classpath jardir com.brian.base_application.BaseApplication
javap -p -classpath jardir com.nlbn.ads.util.Admob
javap -p -classpath jardir com.nlbn.ads.callback.AdCallback
```

### 3.7 Remote Config cho key riêng app

- ✅ Gọi qua `FirebaseRemoteConfigUtil.getInstance()` — **KHÔNG** dùng `FirebaseRemoteConfig.getInstance()` trực tiếp
- ✅ Đăng ký ở `HostApplication.onCreate()` **sau** `super.onCreate()`
- ✅ Truyền **toàn bộ** key trong **một** lần gọi `setAppDefaults`
- ✅ Đặt tiền tố riêng cho key app (vd. `myapp_…`)
- ❌ KHÔNG subclass `FirebaseRemoteConfigUtil` (sẽ tạo singleton song song → ad ID rỗng)
- ❌ KHÔNG gọi `setDefaultsAsync(...)` trực tiếp (xoá default lib)

```kotlin
// ✓ ĐÚNG — alias mượn singleton lib
object MyConfigHelper {
    @JvmStatic
    fun getInstance(): FirebaseRemoteConfigUtil = FirebaseRemoteConfigUtil.getInstance()
}

// ✗ SAI — singleton song song
class MyConfigPrime : FirebaseRemoteConfigUtil() { ... }
```

### 3.8 Mở màn IAP

```kotlin
import com.brian.base_iap.utils.NativeCodecSnowFlakeCortexAI
NativeCodecSnowFlakeCortexAI.nativeAiStartIapActivity(activity)
```

Pattern paywall gate:
```kotlin
fun onPremiumFeatureClick() {
    if (IAPUtils.isPremium()) useFeature()
    else NativeCodecSnowFlakeCortexAI.nativeAiStartIapActivity(this)
}
```

### 3.9 Dùng màn Language riêng (tuỳ chọn)

**Cách A (khuyên dùng):**
```kotlin
LanguageRouter.customActivityClass = MyLanguageActivity::class.java  // trong HostApplication.onCreate()
```
Trong MyLanguageActivity:
```kotlin
LanguageRouter.confirmLanguageSelection(this, selectedCode)  // lib tự chuyển sang IAP
```

**Cách B (kiểm soát toàn quyền):**
```kotlin
LanguageRouter.launcher = LanguageScreenLauncher { activity, nextScreen -> ... }
```
Phải tự truyền `nextScreen` vào `confirmLanguageSelection`.

**Preload native ad cho Language:** Lib tự preload vào `TemporaryStorage.preloadedLanguageNativeAd`. Custom activity chỉ cần đọc (read-and-clear):
```kotlin
val preloaded = TemporaryStorage.preloadedLanguageNativeAd
TemporaryStorage.preloadedLanguageNativeAd = null
if (preloaded != null) bindInstantly(preloaded) else loadInlineWithShimmer()
```

### 3.10 Màn Intro/Onboarding riêng app

```kotlin
override fun getHomeActivity(): Class<out Activity> {
    return if (MyAppPrefs.isOnboardingDone()) MainActivity::class.java
    else IntroActivity::class.java
}
```
- Init storage **TRƯỚC** `super.onCreate()` (vì `getHomeActivity()` được gọi rất sớm)
- IntroActivity tự chuyển sang MainActivity (dùng `FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK`)

### 3.11 FCM service riêng (tuỳ chọn)

Chỉ cần khi app **có push riêng**. Kế thừa `BaseFirebaseMessagingService`, gọi `super.onMessageReceived(...)` đầu tiên, rồi gỡ service mặc định của lib trong manifest bằng `tools:node="remove"`.

### 3.12 Hiển thị quảng cáo riêng app (API của lib)

| Loại | API |
|---|---|
| Banner | `Admob.getInstance().loadBanner(activity, unitId, container, BannerCallBack())` |
| Interstitial | `Admob.getInstance().loadAndShowInter(activity, unitId, true, AdCallback())` |
| Native | `Admob.getInstance().loadNativeAd(ctx, unitId, NativeCallback())` + `pushAdsToViewCustom(ad, view)` |
| Rewarded | `Admob.getInstance().loadAndShowReward(activity, unitId, true, RewardCallback())` |
| App-Open/Resume | `AppOpenManager.getInstance().setAppResumeAdId(unitId)` |

**Ad unit test Google:**

| Loại | Test ID |
|---|---|
| App Open | ca-app-pub-3940256099942544/9257395921 |
| Banner | ca-app-pub-3940256099942544/6300978111 |
| Interstitial | ca-app-pub-3940256099942544/1033173712 |
| Rewarded | ca-app-pub-3940256099942544/5224354917 |
| Native | ca-app-pub-3940256099942544/2247696110 |

---

## 4. TẦNG QUẢNG CÁO — THÀNH PHẦN TÁI SỬ DỤNG

### 4.1 File copy nguyên (chỉ đổi `package` + `R`/`BuildConfig`)

| File | Vai trò | Đổi gì |
|---|---|---|
| `AdsViewModel.kt` | Cache native theo Activity | chỉ `package` |
| `NativeAdSlot.kt` | Slot native (cache + shimmer + bind 1 lần + tự ẩn khi fail) | `package`; layout lib giữ nguyên |
| `AdManager.kt` | Điều phối inter + chain native-inter + tần suất | chỉ `package` |
| `AdScenario.kt` | Tần suất `showCount % ratio` + `maxPerDay` + dọn key >7 ngày | chỉ `package` |
| `NativeInter.kt` | NativeInterController + NativeInterHost (Dialog fullscreen) | `package` + import `R` |
| `NativeAdsFull.kt` | Native fullscreen cho modal Intro | `package` + import `R` |
| `AdPositions.kt` | Parse positionIntrol A/B | chỉ `package` |
| `firebase/Remote.kt` | Facade Remote Config + debug test-unit | `package` + import `BuildConfig` |
| `core/InstallReferrerHelper.kt` | isAdsCampaign (gate Intro) — cache 3 tầng | chỉ `package` |
| `res/layout/ad_native_full.xml` | Layout native fullscreen | đổi màu CTA theo app |

**Copy nhanh:**
```bash
SA=<app-mẫu>/app/src/main/java/<pkg-mẫu>
SB=<app-mới>/app/src/main/java/<pkg-mới-path>
for f in advertisement/*.kt firebase/Remote.kt core/InstallReferrerHelper.kt; do
  sed 's/<pkg.mẫu>/<pkg.mới>/g' "$SA/$f" > "$SB/$f"
done
cp .../res/layout/ad_native_full.xml <app-mới>/.../res/layout/
```

### 4.2 File copy rồi chỉnh theo app

| File | Chỉnh gì |
|---|---|
| `res/xml/config.xml` | Danh sách placement `*_enable` + ratio/max + `positionIntrol` |
| `assets/default_ads_config.json` | Ad unit THẬT (8 placement lib + placement app) |
| `ui/theme/Theme.kt` | Màu Primary/Secondary |
| Intro/Onboarding | Chỉ đổi nội dung slide. Logic positionIntrol + nút Next GIỮ NGUYÊN |
| `MainActivity.kt` | Giữ `NativeInterHost()` ở root + `resolveStartRoute` |
| `MyApplication.kt` | IAP keys, test device id, notification resources |

### 4.3 Nơi gọi (điểm ráp vào màn hình)

```kotlin
// Native slot
NativeAdSlot("native_home")
NativeAdSlot("native_xxx", isSmall = true)  // màn list

// Inter + native-inter (ads kép)
AdManager.showInter(activity, "inter_home") { onNavigate() }

// Native-inter host — BẮT BUỘC đặt ở root setContent
NativeInterHost()   // gọi qua import, KHÔNG FQN inline

// Intro modal
NativeAdsFull(unitId, onClose, onError)
```

### 4.4 Kiến trúc ads — 6 điểm quan trọng

| # | Điểm | File |
|---|---|---|
| 1 | Cache native theo Activity (không reload mỗi lần vào lại) | `AdsViewModel.kt`, `NativeAdSlot.kt` |
| 2 | Bind native 1 lần `key(ad)` (KHÔNG cả `update`) | `NativeAdSlot.kt` |
| 3 | Dọn key đếm >7 ngày (SharedPreferences không phình) | `AdScenario.kt` |
| 4 | Native-inter tính tần suất SAU khi inter đóng | `AdManager.kt` |
| 5 | Preload native-inter lúc inter đang hiện | `AdManager.kt` + `NativeInter.kt` |
| 6 | positionIntrol A/B ở Intro | `AdPositions.kt`, `NativeAdsFull.kt` |

### 4.5 Kịch bản inter (chain 2 quảng cáo)

```
tap tile → AdManager.showInter
  → inter HIỆN (ratio/max qua) → đóng → native-inter (ratio/max riêng) → điều hướng
  → inter KHÔNG → native-inter ngay (fallback)
→ onNext() LUÔN gọi đúng 1 lần
```

### 4.6 Layout native

| Context | Layout |
|---|---|
| Slot thường (Home/Intro/detail) | `com.brian.base_application.R.layout.ads_native_bot_2` + shimmer `ads_native_bot_loading_2` |
| Slot màn LIST | `ads_native_bot_no_media_short_main` + shimmer `ads_native_loading_short_main` |
| Native-inter / modal Intro | Layout tự dựng `res/layout/ad_native_full.xml` + bind thủ công `setNativeAd` |

---

## 5. LOGIC INTRO/ONBOARDING (gate theo nguồn cài)

### 5.1 Quy tắc

```kotlin
val goToHomeStatus = if (isAdsCampaign) n >= countAppOpen
                     else n < organic || n >= countAppOpen + organic
// true → Home, false → Intro
```

- `goToHomeNumber` — đếm số lần vào Home, bắt đầu **1**, +1 mỗi lần
- `count_app_open` (default 3), `organic_number_not_guide` (default 3) — Remote Config
- `isAdsCampaign` — từ InstallReferrer (cache 3 tầng, async, không block splash)

### 5.2 InstallReferrerHelper

- Query 1 lần duy nhất → cache (in-memory → SharedPreferences → InstallReferrer)
- Phân loại: có `gclid` → ads; `utm_medium == "organic"` → organic; mọi nhánh lỗi → **ads=true**
- Chạy async fire-and-forget trong `Application.onCreate`

---

## 6. ĐIỀU HƯỚNG & NAVHOST

### 6.1 Khai báo route

```kotlin
enum class Screen(val route: String) {
    Intro("intro"), Home("home"),
    // ... thêm route theo feature
    ;
    companion object {
        const val ARG_PACKAGE = "packageName"
        fun detailXxx(pkg: String) = "${DetailXxx.route}/$pkg"
    }
}
```

### 6.2 NavHost

```kotlin
@Composable
fun AppNavHost(startRoute: String, navController = rememberNavController()) {
    NavHost(navController, startRoute) {
        composable(Screen.Home.route) { HomeScreen(...) }
        composable("${Screen.DetailXxx.route}/{${Screen.ARG_PACKAGE}}",
            arguments = listOf(navArgument(Screen.ARG_PACKAGE) { type = NavType.StringType })
        ) { DetailXxxScreen(onBack = ::back) }
    }
}
```

### 6.3 Pattern điều hướng từ Home (có ads)

```kotlin
fun go(route: String) {
    if (activity != null) AdManager.showInter(activity, "inter_home") { onNavigate(route) }
    else onNavigate(route)
}
```

---

## 7. TẦNG DỮ LIỆU & PATTERN

### 7.1 Repository pattern

- Composable/ViewModel **KHÔNG gọi trực tiếp** Android SDK / network
- Repository: `@Singleton` + `@Inject constructor`, chạy `Dispatchers.IO`
- Trả model đơn giản, không leak kiểu Android

### 7.2 ViewModel

```kotlin
@HiltViewModel
class XxxViewModel @Inject constructor(private val repo: SomeRepository) : ViewModel() {
    private val _state = MutableStateFlow(XxxState())
    val state: StateFlow<XxxState> = _state.asStateFlow()

    fun doSomething() {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true)
            val result = repo.fetch()
            _state.value = _state.value.copy(loading = false, data = result)
        }
    }
}
```

### 7.3 Composable lấy ViewModel

```kotlin
@Composable
fun XxxScreen(vm: XxxViewModel = hiltViewModel()) {
    val state by vm.state.collectAsState()
    ...
}
```

> Dùng `StateFlow`, KHÔNG dùng `LiveData`.

### 7.4 SharedPreferences

- `AppStorage` — cấu hình/trạng thái app
- `AdScenario` — đếm ads theo ngày

### 7.5 Chuyển dữ liệu giữa 2 màn

- Dữ liệu nhỏ (1 id): nav argument
- Dữ liệu lớn (list): singleton RAM (ví dụ `ScanResultStore`)

---

## 8. THEME & UI SYSTEM

### 8.1 Cấu trúc Theme

```kotlin
@Composable
fun AppTheme(
    darkTheme: Boolean = false,  // khoá cứng — xem giải thích bên dưới
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography(),
        content = content,
    )
}
```

### 8.2 Về Dark Mode

> ⚠️ **BUG đã gặp:** Nếu `AppTheme(darkTheme = isSystemInDarkTheme())` mà app KHÔNG có bản dark riêng → máy bật dark mode → chữ/icon xám mờ trên nền trắng → gần như không đọc được.

**Fix chuẩn:** Khoá cứng `darkTheme = false`, giữ nhánh `if (darkTheme) DarkColors else LightColors` để dự phòng. Muốn dark mode thật → phải thiết kế lại đồng bộ `DarkColors` + mọi hardcode `Color.White`/`Color.Black`.

**Quy tắc:** Bất kỳ `xxxColorScheme(...)` nào set `background` phải luôn set kèm `onBackground`/`onSurface` tương phản.

---

## 9. ĐA NGÔN NGỮ (i18n)

### 9.1 Cấu trúc file

| File | Nội dung | Dịch? |
|---|---|---|
| `values/strings.xml` | Khoá cấu hình SDK (admob id, fb id, license key...) | **Không** |
| `values*/strings_i18n.xml` | Chuỗi hiển thị UI | **Có** |

### 9.2 Đổi ngôn ngữ

- Qua màn Language của lib: `LanguageActivity.start(activity, MainActivity::class.java)`
- Qua màn tự vẽ: `LanguageRouter.confirmLanguageSelection(activity, code, navigate = false)` rồi `activity.recreate()`
- Lib luôn gọi `MyApplication.notifyLanguageSaved(code)` → app lưu + áp per-app locale

---

## 10. CATALOG BUG & BÀI HỌC THỰC CHIẾN

### 10.1 Log

| ❌ Sai | ✅ Đúng |
|---|---|
| `android.util.Log.d(...)` | `println("ADSLOT ...")` |

**Lý do:** Lib có `-assumenosideeffects android.util.Log` → release **XÓA SẠCH** mọi `android.util.Log`.

**Đọc log production:**
```bash
adb logcat -s System.out | grep -E "ADSLOT|ADS"
```

### 10.2 NativeAdSlot fail → kẹt shimmer

Khi native fail **PHẢI** gọi `slot.onError()` + `suppressedAfterError = true` → ẩn slot. Thiếu → shimmer kẹt >30s.

### 10.3 Ads NO_FILL ở release

App **chưa publish** + unit **THẬT** → AdMob không phục vụ → KHÔNG phải lỗi code. Debug dùng unit test (fill OK), release dùng unit thật (chờ publish).

```kotlin
fun adUnit(p) = if (BuildConfig.DEBUG) debugTestUnit(p) else frc.getAdsConfigValue(p)
```

### 10.4 Catalog lỗi build/tích hợp

| # | Lỗi | Nguyên nhân | Sửa |
|---|---|---|---|
| 1 | `Could not find firebase-analytics-ktx` | Firebase BoM 34.x bỏ `-ktx` | Đổi sang bản không `-ktx` |
| 2 | R8: `Type ... defined multiple times` | Fat-AAR đã gói sẵn lib, module khai lại | Gỡ dep trùng khỏi `base-application/build.gradle` |
| 3 | Linter: `Hardcoded absolute path /` | Mọi string mở đầu `/` trong build.gradle | Bỏ `/` đầu: `META-INF/{...}` |
| 4 | Build fail: thiếu `crashlyticsCollectionEnabled` | Lib có `${crashlyticsCollectionEnabled}` trong manifest | Thêm `manifestPlaceholders` |
| 5 | `ClassNotFoundException` mediation | Adapter nạp bằng reflection, R8 xoá | Keep `com.google.ads.mediation.**` |
| 6 | Ads lib không load (rỗng) | App gọi `setDefaultsAsync` → xoá `ads_config` | Dùng `FirebaseRemoteConfigUtil` |
| 7 | Mọi tính năng lib chết im | Quên `android:name=".MyApplication"` | Khai trong manifest |
| 8 | Cold start màn trắng / crash | Bỏ `MAIN/LAUNCHER` nhưng quên đổi `startDestination` | Đổi startDestination sang Home |
| 9 | `Cannot infer type` / unresolved | Xóa file chứa data class dùng chung | Grep tham chiếu trước khi xóa |
| 10 | okhttp/retrofit mismatch | Lib kéo okhttp 5.x, app pin 4.x | Đồng bộ version |
| 11 | Background build báo exit 0 nhưng FAILED | Lệnh có `;` / `| tail` → exit code sai | Grep `BUILD SUCCESSFUL/FAILED` trong log |

### 10.5 Đặt tên hàm

- **KHÔNG** đặt tên trùng API chuẩn (vd `parse` → nhầm `Uri.parse`). Đặt tên riêng: `parsePositions()`
- `NativeInterHost()` gọi qua `import`, KHÔNG viết FQN inline

### 10.6 IAP

- Dùng `iapPremiumKey()` với key sản phẩm thật, **KHÔNG** dùng `defaultIap*Key()` nếu app đã có key thật
- Thiếu gói tuần → `iapPremiumWeeklyKey()` để mặc định `"weekly"`

### 10.7 Native trong LazyColumn

| Triệu chứng | Sửa |
|---|---|
| Load rồi biến mất, load lại 3 lần | `item(key = "ad_$placement")` cho slot ads |

---

## 11. QUY TRÌNH BUILD, KÝ & TEST

### 11.1 Build & Test trên máy (adb)

```bash
adb devices
adb install -r app/build/outputs/apk/debug/app-debug.apk
adb logcat -c
adb shell monkey -p <applicationId> -c android.intent.category.LAUNCHER 1
adb shell pm grant <applicationId> android.permission.POST_NOTIFICATIONS

# Kiểm tra activity hiện tại:
adb shell dumpsys activity activities | grep -i ResumedActivity
adb shell pidof <applicationId>

# Lọc crash:
adb logcat -d | grep -iE "FATAL EXCEPTION|AndroidRuntime|NoClassDefFound|ClassNotFound"

# Log ads:
adb logcat -s System.out | grep -E "ADSLOT|ADS"
adb logcat -d | grep -E " Ads +:" | grep -iE "no fill|too many|400|timeout"

# Reset onboarding/counter:
adb shell pm clear <applicationId>

# Xem InstallReferrer:
adb logcat -d -s InstallReferrer

# Xem SharedPreferences:
adb shell run-as <applicationId> cat /data/data/<applicationId>/shared_prefs/AppStorage.xml
```

### 11.2 Nghiệm thu

- Luồng `SplashActivity (lib) → Inter → Language → IAP → MainActivity`, process sống, không FATAL
- Test **cả debug lẫn release(R8)** — R8 chỉ lộ lỗi lúc chạy
- Cài release phải `adb uninstall` bản debug trước (khác chữ ký)

### 11.3 Ký

- Keystore THẬT phải đúng app, build release đúng keystore trước publish
- Khác keystore = khác chữ ký = không update được app cũ

### 11.4 Test ads trên release (chưa publish)

Thêm test device id vào `getListTestDeviceId()` (id lấy từ log `Use RequestConfiguration...setTestDeviceIds("<id>")`).

---

## 12. PATTERN XÂY FEATURE MỚI (từ A→Z)

1. **Model** — data class mô tả dữ liệu
2. **Repository** — hàm `suspend` gọi Android API/network, chạy `Dispatchers.IO`, trả model. `@Singleton @Inject constructor`
3. **(Tuỳ chọn) Room/Store** — lưu lâu dài (Room) hoặc chuyển tạm giữa 2 màn (Store singleton RAM)
4. **ViewModel** — `@HiltViewModel`, giữ `MutableStateFlow<State>`, gọi Repository trong `viewModelScope.launch`
5. **UI Composable** — `hiltViewModel()`, `collectAsState()`, chèn `NativeAdSlot`/`AdManager.showInter` nếu cần
6. **Route** — thêm entry `Screen`, thêm `composable(...)` trong `AppNavHost`, nối `onClick`/`onNavigate`

---

## 13. CHUYỂN ADS TỪ GMA SDK SANG LIB

### 13.1 Bảng chuyển đổi

| Bản GỐC (GMA SDK) | Bản LIB (`com.nlbn.ads`) |
|---|---|
| `AdLoader.Builder(...).forNativeAd{}.build().loadAd(...)` | `Admob.getInstance().loadNativeAd(ctx, id, NativeCallback())` |
| Dựng `NativeAdView` + `setNativeAd(ad)` | Inflate layout lib + `pushAdsToViewCustom(ad, view)` |
| `InterstitialAd.load(...).show()` | `Admob.getInstance().loadAndShowInter(activity, id, true, AdCallback())` |
| `AdView + loadAd(...)` | `Admob.getInstance().loadBanner(activity, id, container, BannerCallBack())` |
| `AppOpenAd.load/show` tự quản | **Lib tự lo** |
| `GoogleMobileAdsConsentManager` | **Lib tự lo** |
| `MobileAds.initialize(...)` | **Lib tự lo** |
| `Global.isPremiumUser` | `IAPUtils.isPremium()` |

### 13.2 Quy tắc

1. **XÓA** mọi khởi tạo tự viết: `MobileAds.initialize`, `ConsentManager`, `AppOpenManager`
2. **THAY** load/show GMA → `Admob.getInstance()`
3. **GIỮ NGUYÊN** kịch bản/logic điều phối ads — chỉ đổi primitive load/show
4. **BỎ loading dialog khi gọi inter** — lib tự hiện loading
5. Unit id qua `getAdsConfigValue(key)`
6. Khi lib CHƯA có layout phù hợp (vd native fullscreen): giữ view custom + `setNativeAd`, chỉ đổi loader

### 13.3 Remote Config migration

```
RemoteConfigManager.instance.getBoolean(k) → FirebaseRemoteConfigUtil.getInstance().getBoolean(k)
RemoteConfigManager.instance.getString(k)  → FirebaseRemoteConfigUtil.getInstance().getString(k)
getString("<unit_id>")                     → getAdsConfigValue("<unit_id>")  ⚠️
```

**Mẹo ít rủi ro:** Giữ facade `RemoteConfigManager` cũ nhưng đổi ruột ủy quyền sang `FirebaseRemoteConfigUtil`.

---

## CẦN CHUẨN BỊ TRƯỚC KHI BẮT ĐẦU APP MỚI (chỉ cần khi release)

- `public_license_key` (Google Play)
- `facebook_app_id` / `client_token` (nếu dùng Meta)
- Keystore folder + pass
- Ad unit thật (AdMob), gói IAP (subscription id) của app
- `google-services.json`
