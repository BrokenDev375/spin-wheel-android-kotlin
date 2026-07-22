# Phase 1 Review - Khoi tao project Android

Trang thai: Hoan thanh - build debug thanh cong.

## Muc tieu Phase 1

Tao khung project Android Kotlin + Compose co the build/run, tich hop `base-application` tu dau, va dam bao launcher cua app di qua Splash cua lib truoc khi vao `MainActivity`.

## Viec da lam

1. Tao root Gradle project:
   - `settings.gradle`
   - `build.gradle`
   - `gradle.properties`
   - `local.properties` de tro Android SDK tren may hien tai

2. Tao module Android app:
   - `app/build.gradle`
   - `app/proguard-rules.pro`
   - `app/src/main/AndroidManifest.xml`

3. Tich hop `base-application`:
   - Include `:base-application` trong `settings.gradle`.
   - App dung `api project(':base-application')`.
   - Manifest khai bao `android:name=".MyApplication"`.
   - App khong khai bao `MAIN/LAUNCHER`; launcher den tu `com.brian.base_application.start.SplashActivity` cua AAR.

4. Tao code shell:
   - `app/src/main/java/com/vga/spinwheel/MyApplication.kt`
   - `app/src/main/java/com/vga/spinwheel/core/MainActivity.kt`
   - `app/src/main/java/com/vga/spinwheel/ui/nav/Screen.kt`
   - `app/src/main/java/com/vga/spinwheel/ui/nav/AppNavHost.kt`
   - `app/src/main/java/com/vga/spinwheel/ui/screen/home/HomeScreen.kt`
   - `app/src/main/java/com/vga/spinwheel/ui/screen/placeholder/PlaceholderScreen.kt`
   - `app/src/main/java/com/vga/spinwheel/ui/theme/Theme.kt`

5. Tao resource bat buoc toi thieu cho lib:
   - `app/src/main/res/values/colors.xml`
   - `app/src/main/res/values-night/colors.xml`
   - `app/src/main/res/values/styles.xml`
   - `app/src/main/res/values/strings.xml`
   - `app/src/main/res/raw/splash_loading.json`
   - `app/src/main/assets/default_ads_config.json`
   - Cac icon placeholder trong `app/src/main/res/drawable/`
   - Launcher icon trong `app/src/main/res/mipmap-anydpi-v26/`

## Mock/test dang dung

Nhung phan sau chi de build va test Phase 1, khong dung cho release:

1. `app/google-services.json`
   - La Firebase config mock.
   - Can thay bang file that cua Firebase truoc Phase 14/release.

2. `app/src/main/assets/default_ads_config.json`
   - Dang dung Google test ad unit id:
     - App Open: `ca-app-pub-3940256099942544/9257395921`
     - Interstitial: `ca-app-pub-3940256099942544/1033173712`
     - Native: `ca-app-pub-3940256099942544/2247696110`
   - Can thay bang ad unit id that tu AdMob truoc release.

3. IAP/payment config
   - `public_license_key` dang la `MOCK_PUBLIC_LICENSE_KEY`.
   - Cac product id dang dung default key cua `base-application`.
   - Can thay bang Play Console product id va license key that o Phase 14.

4. AppsFlyer/Facebook config
   - `app_flyer_id`, `facebook_app_id`, `facebook_client_token` dang la mock.
   - Can thay bang key/token that neu release can tracking/mediation that.

5. Visual assets
   - `icon_app`, `icon_notification`, `icon_1_iap` ... `icon_5_iap`, `icon_noti_1` ... `icon_noti_5`, `img_document_preview`, `splash_loading.json` dang la placeholder theo chu de Spin Wheel.
   - Can thay bang asset thiet ke that truoc Phase 15/release polish.

## Ket qua build

Lenh da chay:

```powershell
& 'C:\Users\Admin\.gradle\wrapper\dists\gradle-8.13-bin\5xuhj0ry160q40clulazy9h7d\gradle-8.13\bin\gradle.bat' :app:assembleDebug --stacktrace
```

Ket qua:

- Build debug thanh cong.
- APK tao tai: `app/build/outputs/apk/debug/app-debug.apk`.
- Kich thuoc APK debug: khoang 76.4 MB.

Luu y:

- Lan build trong sandbox fail do bi chan quyen doc Android SDK/Kotlin daemon ngoai workspace.
- Chay lai ngoai sandbox thanh cong.
- Co warning SDK XML version va warning namespace Appsflyer trung nhau tu dependency cua lib; chua chan build.

## Hotfix sau khi test tren device

Van de gap:

- Khi mo app tren device, app crash sau man Splash cua lib.
- Logcat bao loi: `You need to use a Theme.AppCompat theme (or descendant) with this activity.`

Nguyen nhan:

- `SplashActivity` cua `base-application` la AppCompat activity.
- Theme host ban dau dung `@android:style/Theme.Material.Light.NoActionBar`, khong phai AppCompat descendant.

Da sua:

- Doi `Theme.SpinWheel` sang `Theme.AppCompat.Light.NoActionBar` trong `app/src/main/res/values/styles.xml`.
- Doi ten tham so `notifyLanguageSaved(code)` thanh `notifyLanguageSaved(languageCode)` de khop signature tai lieu/AAR.

Kiem tra lai:

- Build debug lai thanh cong.
- Cai APK len device thanh cong.
- Mo app bang ADB khong con `FATAL EXCEPTION`.
- `dumpsys activity` xac nhan app vao duoc `com.vga.spinwheel/.core.MainActivity`.

## Kiem tra manifest merge

Da xac nhan trong merged manifest debug:

- `application android:name="com.vga.spinwheel.MyApplication"`.
- `MainActivity` co `android:exported="false"`.
- `SplashActivity` cua `base-application` co `MAIN/LAUNCHER`.
- Co cac permission bat buoc:
  - `FOREGROUND_SERVICE`
  - `FOREGROUND_SERVICE_SPECIAL_USE`
  - `POST_NOTIFICATIONS`
  - `AD_ID`

## Chua lam trong Phase 1

- Da cai va run APK tren device attached sau hotfix theme AppCompat.
- Chua implement UI chi tiet theo screenshot.
- Chua implement Room, ViewModel feature, data persistence.
- Chua thay key/asset that.

## Ket luan

Phase 1 dat checkpoint build debug va khung app da vao dung kien truc Android Kotlin + Compose + `base-application`.

Da tick `[x]` Phase 1 trong `implementation-plan.md`.
