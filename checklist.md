# ✅ CHECKLIST — Xây dựng app Android mới (Kotlin + Compose + base-application)

> Checklist tổng hợp áp dụng được cho **mọi app mới**. Copy file này, đổi giá trị cụ thể theo app rồi check từng mục.
> Tham chiếu chi tiết: `architecture.md`

---

## A. TOOLCHAIN & PROJECT SETUP

- [ ] Gradle ≥ 8.13, AGP 8.12.0, Kotlin 2.1.20, KSP 2.1.20-1.0.32
- [ ] Plugin compose 2.1.20, `google-services` 4.4.3, `firebase-crashlytics` 3.0.6
- [ ] `settings.gradle`: `include(":base-application")` + repos (google/mavenCentral/jitpack/dl.google)
- [ ] Module `base-application/`: AAR + `build.gradle` khai **đủ transitive deps**
- [ ] `app/build.gradle`: `minSdk 28`, Java/Kotlin 17, `multiDexEnabled`, `viewBinding=true`, `buildConfig=true`
- [ ] Firebase BoM **34.1.0**, dùng artifact **không** `-ktx` (analytics, crashlytics, config, messaging)
- [ ] `api(project(":base-application"))` — **GỠ** mọi dep trùng lib
- [ ] `google-services.json` đúng project Firebase

## B. HOST APPLICATION

- [ ] `MyApplication : BaseApplication()` — giữ `@HiltAndroidApp` nếu dùng Hilt
- [ ] **GỌI `super.onCreate()`** — lib init trong đó
- [ ] Implement **đủ hàm abstract** (dùng `javap` kiểm tra chữ ký):
  - [ ] `getAppNameRes()`, `getIconSplashRes()`, `getSplashLoadingRes()`
  - [ ] `getHomeActivity()` → trả `MainActivity::class.java` (hoặc IntroActivity nếu có onboarding)
  - [ ] `hasForegroundServicePermission()`
  - [ ] `initAppFlyerId()`, `setupKoin()`
  - [ ] `notifyLanguageSaved(code)` — lưu vào SharedPreferences
  - [ ] `iapPremiumKey()`, `iapPremiumWeeklyKey/Monthly/Yearly()`, `iapPublicKey()`
  - [ ] `getFeature1IconRes()` … `getFeature5IconRes()` + text đi cặp
  - [ ] ~20 hàm `getNotification...Res()`, `getNotificationImages()` (**đúng 5** icon)
- [ ] Override tuỳ chọn: `isPurchased()` → `IAPUtils.isPremium()`, `enableAdsResume()` → `!IAPUtils.isPremium()`
- [ ] `getListTestDeviceId()` — khai test device id (lấy từ logcat)
- [ ] Nếu `getHomeActivity()` đọc SharedPreferences → init **TRƯỚC** `super.onCreate()`

## C. MANIFEST

- [ ] `android:name=".MyApplication"` trong `<application>` ⚠️ (thiếu → lib chết im không báo lỗi)
- [ ] **4 quyền BẮT BUỘC**: `FOREGROUND_SERVICE`, `FOREGROUND_SERVICE_SPECIAL_USE`, `POST_NOTIFICATIONS`, `AD_ID`
- [ ] Meta-data: `admob_app_id`, `facebook_app_id`, `facebook_client_token` (dùng `@string`)
- [ ] **KHÔNG** có `MAIN/LAUNCHER` ở bất kỳ Activity nào của app
- [ ] Cold start vào SplashActivity của lib (không màn trắng/crash)

## D. TÀI NGUYÊN

### Colors
- [ ] `primaryColor` == `accentTone` (giống nhau)
- [ ] `text1` — giống nhau ở `values/` và `values-night/`

### Drawable
- [ ] `icon_app.xml` hoặc `.png` — icon app cho splash
- [ ] `icon_notification.png` — 5 mật độ (m/h/xh/xxh/xxxh), 24×24dp, alpha only
- [ ] `img_document_preview.png` — 365 × 174 dp
- [ ] `icon_1_iap` … `icon_5_iap` — 32×32dp, 5 icon IAP
- [ ] `icon_noti_1` … `icon_noti_5` — **đúng 5**, hợp chủ đề app

### Raw
- [ ] `res/raw/splash_loading.json` — Lottie, màu khớp `primaryColor`

### Strings
- [ ] `app_name`, `app_flyer_id`, `admob_app_id`, `facebook_app_id`, `facebook_client_token`, `public_license_key`
- [ ] `notification_title2`, `notification_message2`, `notification_button2` — mỗi mảng **5 phần tử**, hợp chủ đề app
- [ ] Nội dung thông báo phải hợp chủ đề app (KHÔNG copy nguyên mẫu app khác)

### Assets
- [ ] `assets/default_ads_config.json` — **8 placement lib bắt buộc** + placement riêng app:
  - `open_splash`, `inter_splash`, `splash_uninstall`, `native_language`
  - `open_all`, `native_keep_user`, `native_survey_user`, `native_exit_app`
  - + placement riêng app (native_home, inter_home, ...)

### Bản dịch
- [ ] `strings_i18n.xml` đa ngôn ngữ (en-US, vi, pt, ru, hi, ja, ko, fr, es, zh, de, ...)

## E. GỠ VỎ TRÙNG

- [ ] Gỡ màn Splash của app (lib là launcher)
- [ ] Gỡ Language startup tự viết (lib có sẵn)
- [ ] Gỡ Consent/UMP tự viết (lib tự lo)
- [ ] Gỡ `MobileAds.initialize(...)` (lib tự lo)
- [ ] Gỡ AppOpen/Resume ad tự viết (lib tự lo)
- [ ] Gỡ FCM service + scheduler + FGS + receiver cũ (nếu không cần push riêng)
- [ ] `startDestination` Compose đổi sang Home (hoặc Intro nếu first-open)
- [ ] **Giữ lại** màn IAP — mở được từ Home & Settings

## F. QUẢNG CÁO (ADS)

### Remote Config
- [ ] KHÔNG gọi `FirebaseRemoteConfig.setDefaultsAsync(...)` trực tiếp ⚠️
- [ ] Dùng `FirebaseRemoteConfigUtil.getInstance().setAppDefaults(...)` hoặc `setAppDefaultsFromXml(R.xml.config)` **SAU** `super.onCreate()`
- [ ] KHÔNG subclass `FirebaseRemoteConfigUtil` (tạo singleton song song → ad rỗng)
- [ ] KHÔNG còn `import GMA` (`AdLoader/InterstitialAd/AppOpenAd/MobileAds.initialize`) trong code app

### Ads tự gọi
- [ ] Native load qua `Admob.getInstance().loadNativeAd(...)`
- [ ] Native show qua `pushAdsToViewCustom(ad, view)` + layout lib (hoặc `setNativeAd` cho fullscreen custom)
- [ ] Inter qua `Admob.getInstance().loadAndShowInter(...)`
- [ ] `onNext()` của `showInter` luôn gọi đúng 1 lần (dù ad có hiện hay không)

### Thành phần ads tái sử dụng
- [ ] Copy `advertisement/*.kt` + `firebase/Remote.kt` + `core/InstallReferrerHelper.kt` — đổi `package`
- [ ] Copy `res/layout/ad_native_full.xml` — đổi màu CTA
- [ ] `config.xml`: đủ `{placement}_enable` + ratio/max cho mỗi placement
- [ ] `NativeInterHost()` đặt ở root `setContent` của MainActivity (gọi qua import, KHÔNG FQN inline)

### Log ads
- [ ] Log ads dùng `println(...)` — KHÔNG `android.util.Log` ⚠️ (lib strip ở release)
- [ ] Đọc: `adb logcat -s System.out | grep -E "ADSLOT|ADS"`

### Xử lý lỗi
- [ ] NativeAdSlot khi fail: `slot.onError()` + `suppressedAfterError = true` (kẻo shimmer kẹt >30s)
- [ ] Native trong `LazyColumn`: dùng `item(key = "ad_$placement")` (tránh recompose reload)

### Premium
- [ ] `IAPUtils.isPremium()` thay mọi cờ premium tự chế
- [ ] Premium → tắt TẤT CẢ ads (native, inter, banner, open/resume)
- [ ] `enableAdsResume() = !IAPUtils.isPremium()`

## G. IAP

- [ ] `iapPremiumKey` = id gói mua 1 lần (KHÔNG dùng `defaultIap*Key()` nếu app đã có key thật)
- [ ] `iapPremiumMonthlyKey/YearlyKey` = id gói subscription
- [ ] `iapPremiumWeeklyKey` = `"weekly"` nếu không có gói tuần
- [ ] `public_license_key` THẬT (Play Console)
- [ ] Màn IAP mở được từ Home **và** Settings
- [ ] Paywall gate: mở IAP → đóng → lần sau `isPremium()=true` → tính năng chạy thẳng

## H. INTRO / ONBOARDING (nếu có)

- [ ] `resolveStartRoute()` tính route bắt đầu (Intro hay Home) dựa trên `goToHomeNumber` + `isAdsCampaign`
- [ ] `InstallReferrerHelper.resolve(this)` trong `MyApplication.onCreate()` (async, không block splash)
- [ ] `goToHomeNumber` +1 mỗi lần mở vào Home
- [ ] `count_app_open` + `organic_number_not_guide` đọc từ Remote Config
- [ ] Fallback `isAdsCampaign = true` khi chưa resolve
- [ ] Nút Next/Continue trong Intro: bấm được khi ad **FILL *hoặc* LỖI** (có timeout fallback 5s + `onResolved`)

## I. THEME / UI

- [ ] `AppTheme(darkTheme = false)` — khoá cứng nếu chưa thiết kế dark mode
- [ ] Mọi `xxxColorScheme(background=...)` phải set kèm `onBackground`/`onSurface` tương phản
- [ ] Đổi ngôn ngữ / bật-tắt night mode **KHÔNG** restart từ Splash

## J. BUILD / RELEASE

### R8 / Minify
- [ ] `isMinifyEnabled = true` **VÀ** `isShrinkResources = true` ⚠️ (thiếu 1 → lỗi)
- [ ] `crashlyticsCollectionEnabled` placeholder: debug=`"false"`, release=`"true"`
- [ ] `proguard-rules.pro`:
  - [ ] Keep `com.google.ads.mediation.**`
  - [ ] Keep `com.facebook.ads.**`
  - [ ] Keep model app (Room entity + DTO Gson/Retrofit)
  - [ ] Crashlytics attrs: `SourceFile,LineNumberTable`
  - [ ] KHÔNG thêm `-dontoptimize`
- [ ] Không có string mở đầu `/` trong `build.gradle.kts` (vd `excludes += "META-INF/{...}"`)
- [ ] `packagingOptions.excludes`: `META-INF/{AL2.0,LGPL2.1}` + `META-INF/atomicfu.kotlin_module`

### Ký (signing)
- [ ] Keystore THẬT trong `signingConfig` release
- [ ] Build release đúng keystore (khác keystore = khác chữ ký = không update app cũ)

### Test trên máy
- [ ] Build debug XANH + cài máy test (`adb install -r`)
- [ ] Build release(R8) XANH + cài máy test (uninstall debug trước)
- [ ] Luồng `Splash → Inter → Language → IAP → Home` chạy đúng (debug + release)
- [ ] Process sống, không FATAL EXCEPTION
- [ ] Log ads (`println`): `ADSLOT loaded ... ad=true` hoặc `ADSLOT ERR ... → ẩn slot`
- [ ] Slot fail → ẩn (không kẹt shimmer)

## K. NGHIỆM THU ADS KÉP (CHAIN)

- [ ] Tap tile → inter → (đóng) → native-inter fullscreen → điều hướng
- [ ] Fallback: inter không hiện → native-inter ngay → điều hướng
- [ ] `onNext()` luôn gọi — app KHÔNG kẹt điều hướng

## L. TRƯỚC PHÁT HÀNH (TODO cuối cùng)

- [ ] `facebook_app_id` + `facebook_client_token` THẬT (không placeholder)
- [ ] `appsflyer_dev_key` THẬT
- [ ] i18n: rà đủ ngôn ngữ đã hỗ trợ
- [ ] Icon notification (5 density) / icon splash / ảnh preview IAP — **ảnh THẬT**, không placeholder
- [ ] Ad unit THẬT trong `default_ads_config.json`
- [ ] Gói IAP/subscription THẬT trên Play Console
- [ ] `public_license_key` THẬT
- [ ] Publish Play Store (đúng package + keystore)
- [ ] Sau publish: ads THẬT fill (hiện NO_FILL = chưa publish — KHÔNG phải lỗi)

---

## M. ⚠️ MỤC HAY QUÊN — KIỂM MỖI LẦN BUILD

> Check lại sau **MỖI lần sửa code**, đặc biệt phần ads:

- [ ] **Log**: dùng `println`, KHÔNG `android.util.Log`
- [ ] **NativeAdSlot fail**: `slot.onError()` + `suppressedAfterError` (kẹt shimmer)
- [ ] **Intro nút Next**: bấm được khi ad fill HOẶC lỗi (timeout 5s)
- [ ] **onNext() inter**: luôn gọi đúng 1 lần
- [ ] **Build release xong**: cài máy test + đọc log
- [ ] **NO_FILL release**: bình thường nếu chưa publish
- [ ] **Tên hàm**: không trùng stdlib (`parse` → `parsePositions`)
- [ ] **`NativeInterHost()`**: gọi qua import, không FQN inline
- [ ] **`RemoteConfigManager`**: KHÔNG gọi `setDefaultsAsync` trực tiếp
- [ ] **`android:name`**: phải trỏ HostApplication trong manifest
- [ ] **Background build**: grep `BUILD SUCCESSFUL/FAILED`, đừng tin exit code

---

## N. HƯỚNG DẪN SỬ DỤNG CHECKLIST NÀY

1. **App mới**: Copy file này vào project, đổi tên file nếu cần
2. **Đánh dấu**: Check `[x]` khi hoàn thành, `[/]` khi đang làm
3. **Bỏ qua**: Nếu app không có feature nào (vd không có Intro) → ghi chú "N/A" và bỏ qua section
4. **Tham chiếu**: Xem `architecture.md` để có hướng dẫn chi tiết + code mẫu cho từng mục
