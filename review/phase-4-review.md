# Phase 4 Review - Home, Intro, Setting, Language, Payment

Branch: `phase-4-skeleton`

## Trang thai

- Da implement day du phan Phase 4 tren nen skeleton `4c2266e`.
- Da tick `[x]` Phase 4 trong `implementation-plan.md`.
- Da giu route skeleton `Wheel` de agent Phase 5 co the tiep tuc lam man Banh Xe.
- Chua push branch theo yeu cau cua user; chi commit local va bao lenh push sau khi chot.

## Scope da implement

1. Startup sau lib:
   - `MainActivity` inject `SettingsRepository`.
   - Neu `APP.intro_done = false` thi start `Screen.Intro`.
   - Neu `APP.intro_done = true` thi start `Screen.Home`.
   - Luong truoc `MainActivity` van giu theo lib/base: Splash -> Ads/Language/IAP -> app route.

2. Intro:
   - Them `IntroScreen`.
   - Them `IntroViewModel`.
   - 4 slide gioi thieu ung dung.
   - Nut `TIEP TUC` chuyen slide.
   - Nut `BAT DAU` luu `intro_done = true` roi vao Home.

3. Home:
   - Giu Home grid 9 chuc nang da co tu Phase 2.
   - Home route sang cac feature placeholder/skeleton.
   - Nut Setting vao Setting.
   - Nut Pro vao Payment mock.

4. Setting:
   - Doi `SettingsSkeletonScreen` thanh `SettingsScreen`.
   - Them `SettingsViewModel`.
   - Cac row: Share, Language, Rate, Music, Game sound, Vibration.
   - Music/Game sound/Vibration luu qua `SettingsRepository`.
   - Share dung Android share sheet.
   - Rate hien toast mock.

5. Language:
   - Them `LanguageScreen`.
   - Them `LanguageViewModel`.
   - Co search, list ngon ngu, select va nut Done.
   - Luu `APP.language_code` qua `SettingsRepository`.

6. Payment/Pro:
   - Them `PaymentScreen`.
   - Them `PaymentViewModel`.
   - Co feature comparison, Weekly/Annual/Monthly plan.
   - Chon plan va luu `APP.payment_plan`.
   - Restore va subscribe dang la mock UI.

7. Shared setting keys:
   - Them `AppSettingKeys` de gom key `intro_done`, `language_code`, `music_enabled`, `game_sound_enabled`, `vibration_enabled`, `payment_plan`.

8. Dependency:
   - Them `androidx.hilt:hilt-navigation-compose:1.2.0` de dung `hiltViewModel()` trong Compose.
   - Chuyen Room tu `2.8.4` ve `2.6.1` vi Room KSP `2.8.4` bi loi `AbstractMethodError` khi doc schema export cu tren toolchain hien tai.
   - Khong doi database schema version 1.

## Kiem thu da chay

Lenh:

```powershell
.\gradlew.bat :app:testDebugUnitTest :app:assembleDebug --stacktrace
```

Ket qua:

- `BUILD SUCCESSFUL`
- Unit test pass.
- APK debug tao tai `app/build/outputs/apk/debug/app-debug.apk`.

Runtime smoke test:

- Device `Y98TKVQKTGJ7EI49` online.
- Install debug APK bang `adb install -r`.
- Mo app bang `adb shell monkey -p com.vga.spinwheel 1`.
- Process `com.vga.spinwheel` con song sau launch.
- `AndroidRuntime` logcat khong co crash.
- UI dump thay app vao duoc man skeleton `Banh Xe`.

## Mock/test con lai

- Payment/Pro trong app route la mock, chua dau IAP product that.
- Restore/Subscribe/Rate dang la mock action.
- Language screen moi luu selection, chua doi locale runtime/toan app.
- Cac feature ngoai Home/Intro/Setting/Language/Payment van la placeholder hoac skeleton theo dung plan.

## Luu y cho Phase 5

- Route Wheel skeleton da co san trong `ui/nav/WheelNavGraph.kt`.
- Khi implement Phase 5, tiep tuc dung `WheelRoutes` hien co de tranh conflict navigation.
- Neu can thay doi schema `Wheel`, `WheelItem` hoac `random_history`, phai hoi user truoc theo `agent.md`.
