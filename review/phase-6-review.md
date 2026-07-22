# Phase 6 Review - Chon Ngon Tay

Branch: `phase-6-finger-random`

## Trang thai

- Da merge nen moi nhat tu `origin/phase-5-wheel` truoc khi implement Phase 6.
- Da thay placeholder `Finger` bang man hinh chuc nang that.
- Da build Debug APK va unit test thanh cong.
- Da install APK debug len thiet bi `Y98TKVQKTGJ7EI49` va launch app khong thay log crash `AndroidRuntime`.

## Chuc nang da implement

1. Man hinh `Chon`:
   - Header co nut quay lai, title `Chon`, nut dropdown tron mau xanh.
   - Huong dan dat ngon tay len man hinh va giu 2 giay.

2. Dropdown so ngon tay:
   - Cho phep chon 1 den 5 ngon tay.
   - Gia tri duoc luu qua `SettingsRepository` voi feature `FINGER`.

3. Multi-touch:
   - Bat cac pointer dang pressed tren vung choi.
   - Gioi han so diem cham theo so ngon tay dang chon.
   - Neu rut ngon tay trong countdown thi huy phien hien tai.

4. Countdown va ket qua:
   - Dem nguoc `2S` -> `1S`.
   - Chon winner ngau nhien tu danh sach diem cham hop le.
   - Hien quick result voi badge `THANG`, emoji thang/thua.
   - Tu dong chuyen sang man hinh ket qua cuoi.

5. Man hinh ket qua cuoi:
   - Hien lai cac diem cham trong khung ket qua.
   - Co nut Home, `Chia se ket qua`, va `Thu lai`.

## File thay doi chinh

- `app/src/main/java/com/vga/spinwheel/ui/screen/finger/FingerScreen.kt`
- `app/src/main/java/com/vga/spinwheel/ui/screen/finger/FingerViewModel.kt`
- `app/src/main/java/com/vga/spinwheel/ui/screen/finger/FingerRoundRules.kt`
- `app/src/main/java/com/vga/spinwheel/ui/nav/AppNavHost.kt`
- `app/src/main/java/com/vga/spinwheel/ui/components/SpinIcons.kt`
- `app/src/test/java/com/vga/spinwheel/ui/screen/finger/FingerRoundRulesTest.kt`

## Kiem thu da chay

- `.\gradlew.bat :app:testDebugUnitTest :app:assembleDebug --stacktrace`
- Ket qua: `BUILD SUCCESSFUL`
- Device smoke:
  - `adb install -r app/build/outputs/apk/debug/app-debug.apk`: `Success`
  - `adb shell monkey -p com.vga.spinwheel 1`: launch thanh cong
  - `adb logcat -d -v brief AndroidRuntime:E System.err:E *:S`: khong co crash log

## Luu y

- Startup hien tai van di qua luong lib/ads; luc smoke test app dang o `AdActivity`, nen can test tay tren thiet bi sau khi dong quang cao de xac nhan multi-touch 2-5 ngon tay theo dung cam giac UI.
- Phase 6 khong thay doi Room schema va khong sua `base-application`.
