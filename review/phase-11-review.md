# Phase 11 Review - Quay Chai

Branch: `phase-11-bottle`

## Trang thai

- Da implement flow chuc nang `Quay Chai` theo `feature/bottle.md`.
- Da thay placeholder `bottle` trong `AppNavHost` bang nav graph that.
- Da build Debug APK thanh cong va chay unit test thanh cong.

## Noi dung da implement

1. Bottle home:
   - Man `Quay Chai` co header back, chai lon o giua va bottom bar 3 nut.
   - Nut tuy chinh, start va reset bi khoa khi chai dang quay.
   - Reset huy phien quay hien tai va dua goc chai ve 0 do.

2. Spin logic:
   - Duration doc/luu qua `SettingsRepository` voi feature `BOTTLE`.
   - Duration duoc clamp trong khoang 1..15 giay theo mockup HTML.
   - Khi bam `BAT DAU`, chai quay theo animation lap, het duration random goc 0..359.
   - Sau khi dung o goc cuoi, app doi 650ms roi hien man ket qua.
   - `runId` duoc dung de huy timer cu khi reset/back.

3. Bottle drawing/theme:
   - Chai duoc ve bang Compose Canvas, khong them asset ngoai.
   - Co 6 mau chai/card background theo `BOTTLE_LABELS` va CSS trong `test.html`.
   - Style dang chon duoc luu qua `SettingsRepository`.

4. Settings va label:
   - Man `Tuy chinh` co stepper `Thoi luong hoat hinh` va row `Quay Chai`.
   - Man `Mau Chai` co grid 2 cot, selected border mau cam va check xanh.
   - Chon mau la tam thoi; bam `Luu` moi ap dung va quay ve settings.

5. Result:
   - Man `Ket Qua` co khung preview chai tai goc cuoi.
   - Nut `Chia se ket qua` mo Android share sheet; fallback copy clipboard neu khong mo duoc share sheet.
   - Nut `Thu lai` quay ve man quay chai.
   - Icon Home ve trang chu va reset state Bottle.

## File chinh

- `app/src/main/java/com/vga/spinwheel/ui/nav/BottleRoutes.kt`
- `app/src/main/java/com/vga/spinwheel/ui/nav/BottleNavGraph.kt`
- `app/src/main/java/com/vga/spinwheel/ui/nav/AppNavHost.kt`
- `app/src/main/java/com/vga/spinwheel/ui/screen/bottle/BottleViewModel.kt`
- `app/src/main/java/com/vga/spinwheel/ui/screen/bottle/BottleRoundRules.kt`
- `app/src/main/java/com/vga/spinwheel/ui/screen/bottle/BottleStyles.kt`
- `app/src/main/java/com/vga/spinwheel/ui/screen/bottle/BottleScreen.kt`
- `app/src/test/java/com/vga/spinwheel/ui/screen/bottle/BottleRoundRulesTest.kt`

## Kiem thu

Lenh da chay:

```bash
.\gradlew.bat :app:testDebugUnitTest :app:assembleDebug --stacktrace
```

Ket qua:

- `BUILD SUCCESSFUL`
- `testDebugUnitTest` pass.
- `assembleDebug` pass.
- Tong 17 unit tests pass.
- `BottleRoundRulesTest`: 3 tests, 0 failures, 0 errors.

## Luu y

- Lan build dau tien trong sandbox bi chan boi `AccessDeniedException` khi Kotlin daemon ghi vao `C:\Users\Admin\AppData\Local\kotlin\daemon`; da chay lai voi quyen escalated va build pass.
- Chua run duoc tren device vi `adb` khong co trong PATH cua terminal hien tai.
- Chai su dung Canvas/vector Compose thay vi anh raster; muc tieu Phase 11 uu tien flow dung va UI gan screenshot, pixel polish neu can de Phase 15.
