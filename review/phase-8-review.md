# Phase 8 Review - Doi

Branch: `phase-8-team`

## Trang thai

- Da implement flow chuc nang `Doi` theo `feature/Team.md`.
- Da dung chung danh sach `Wheel` va form them/sua banh xe hien co, khong doi schema Room.
- Da noi route `team` vao `AppNavHost`, thay placeholder bang flow that.
- Da build Debug APK thanh cong va chay unit test thanh cong.

## Noi dung da implement

1. Team home:
   - Man `Doi` hien nut `Trinh tao AI` full-width va nut `Tao Banh Xe moi` full-width.
   - Danh sach dung chung du lieu `WheelRepository`.
   - Card danh sach co menu `Sua`, `Nhan ban`, `Xoa` va dialog xac nhan xoa.
   - Trang thai rong hien thong bao `Khong co banh xe. Them moi di!`.

2. Them/sua danh sach:
   - Route Team dung lai `WheelAddEditScreen`.
   - Them moi, sua, AI mock tao form deu luu vao Room qua data layer Wheel hien co.

3. Detail va ghep doi:
   - Man chi tiet hien ten danh sach va cac thanh vien.
   - Setting, reset va nut chinh nam o bottom bar theo mockup.
   - Khi bam `NHAN DE GHEP NOI`, app shuffle danh sach theo duration.
   - Khi het animation, nut chinh doi thanh `Next`; app khong tu chuyen preview.

4. Setting:
   - Cau hinh `Cac muc cua nhom`.
   - Cau hinh `Thoi luong hoat hinh`.
   - Bat/tat `Gieo hat cua nhom`.
   - Cac gia tri duoc luu qua `SettingsRepository` voi feature `TEAM`.

5. Preview ket qua:
   - Man `Ket Qua` co icon Home, khung ket qua cuon ngang va cac bang `Team 1`, `Team 2`, ...
   - Nut `Chia se ket qua` goi Android share sheet.
   - Nut `Thu lai` reset va chay lai animation cho danh sach dang chon.

## File chinh

- `app/src/main/java/com/vga/spinwheel/ui/nav/TeamRoutes.kt`
- `app/src/main/java/com/vga/spinwheel/ui/nav/TeamNavGraph.kt`
- `app/src/main/java/com/vga/spinwheel/ui/screen/team/TeamHomeScreen.kt`
- `app/src/main/java/com/vga/spinwheel/ui/screen/team/TeamDetailScreen.kt`
- `app/src/main/java/com/vga/spinwheel/ui/screen/team/TeamSettingsScreen.kt`
- `app/src/main/java/com/vga/spinwheel/ui/screen/team/TeamPreviewScreen.kt`
- `app/src/main/java/com/vga/spinwheel/ui/screen/team/TeamBoardComponents.kt`
- `app/src/main/java/com/vga/spinwheel/ui/screen/team/TeamViewModel.kt`
- `app/src/main/java/com/vga/spinwheel/ui/screen/team/TeamRoundRules.kt`
- `app/src/test/java/com/vga/spinwheel/ui/screen/team/TeamRoundRulesTest.kt`

## Kiem thu

Lenh da chay:

```bash
.\gradlew.bat :app:testDebugUnitTest :app:assembleDebug --stacktrace
```

Ket qua:

- `BUILD SUCCESSFUL`
- `testDebugUnitTest` pass.
- Tong 14 unit tests pass.
- `TeamRoundRulesTest`: 3 tests, 0 failures, 0 errors.

## Luu y

- AI generate trong Phase 8 van la mock, dung chung dialog/form cua `Banh Xe`.
- Chua test thao tac UI thuc te tren device trong phase nay; da xac minh compile, unit test va debug APK.
