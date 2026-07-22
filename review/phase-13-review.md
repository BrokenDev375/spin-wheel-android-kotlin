# Phase 13 Review - Lat The

Branch: `phase-13-card`

## Trang thai

- Da implement flow chuc nang `Lat The` theo `feature/Card.md`.
- Da thay placeholder `card` trong `AppNavHost` bang nav graph that.
- Da build Debug APK thanh cong va chay unit test thanh cong.

## Noi dung da implement

1. Card home:
   - Man `Lat The` co header back, khu preview `The Thang`, grid card va bottom bar 3 nut.
   - Mac dinh theo mockup: duration 2s, total cards 4, winners 1, theme index 0.
   - Card ban dau up mat sau; bam tung card de flip sang trang thai thang/thua.

2. Card logic:
   - Total cards clamp trong khoang 2..12.
   - Winners clamp trong khoang 1..totalCards-1 de luon con it nhat 1 the thua.
   - Moi round random vi tri winners bang danh sach boolean khong trung lap.
   - Shuffle/reset tao round moi, tat ca card quay ve mat sau.
   - Khi tat ca card da lat, ViewModel doi ngan roi chuyen sang man result.

3. Settings va theme label:
   - Man `Tuy chinh` co stepper duration, Total Cards, winners va row Card Deck Theme.
   - Settings duoc doc/luu qua `SettingsRepository` voi feature `CARD`.
   - Man `The mau` co 4 theme theo mockup HTML; chon theme tam thoi, bam `Luu` moi ap dung.

4. Result:
   - Man `Ket Qua` hien tat ca card da lat trong khung preview.
   - Nut `Chia se ket qua` mo Android share sheet; fallback copy clipboard neu khong mo duoc share sheet.
   - Nut `Thu lai` tao round moi.
   - Icon Home quay ve trang chu.

## File chinh

- `app/src/main/java/com/vga/spinwheel/ui/nav/CardRoutes.kt`
- `app/src/main/java/com/vga/spinwheel/ui/nav/CardNavGraph.kt`
- `app/src/main/java/com/vga/spinwheel/ui/nav/AppNavHost.kt`
- `app/src/main/java/com/vga/spinwheel/ui/screen/card/CardViewModel.kt`
- `app/src/main/java/com/vga/spinwheel/ui/screen/card/CardRoundRules.kt`
- `app/src/main/java/com/vga/spinwheel/ui/screen/card/CardThemes.kt`
- `app/src/main/java/com/vga/spinwheel/ui/screen/card/CardScreen.kt`
- `app/src/test/java/com/vga/spinwheel/ui/screen/card/CardRoundRulesTest.kt`

## Kiem thu

Lenh da chay:

```bash
.\gradlew.bat clean :app:testDebugUnitTest :app:assembleDebug --stacktrace
```

Ket qua:

- `BUILD SUCCESSFUL`
- `testDebugUnitTest` pass.
- `assembleDebug` pass.

## Luu y

- Lan build dau tien gap loi unresolved reference do Kotlin incremental cache; sau `clean` thi build/test pass.
- Chua run duoc tren device vi `adb` khong co trong PATH cua terminal hien tai.
- Card art duoc ve bang Compose thay vi dung emoji trong mock HTML de tranh phu thuoc font emoji tren may Android.
