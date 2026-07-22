# Phase 12 Review - Xúc Xắc (Dice)

## Trạng thái: ✅ Hoàn thành

## Tổng quan

Tính năng Xúc Xắc cho phép người dùng tung từ 1-6 viên xúc xắc với hiệu ứng rung lắc, tính tổng điểm, đổi skin màu, và chia sẻ kết quả. Xúc xắc được vẽ 100% bằng Compose UI (không dùng hình ảnh).

## Các file đã tạo

### Navigation
- `ui/nav/DiceNavGraph.kt` - Điều hướng 4 màn hình Dice (Home, Settings, Label, Preview).

### Components
- `ui/screen/dice/DiceComponents.kt` - `DiceFace`, `Dot`, `DiceTile` composables + 4 `DiceStyle` themes.

### Screens
- `ui/screen/dice/DiceHomeScreen.kt` - Chọn số lượng viên (1-6), nút CHƠI, hiển thị tổng điểm.
- `ui/screen/dice/DiceSettingsScreen.kt` - Cài đặt thời lượng animation (1-15s) + stepper.
- `ui/screen/dice/DiceLabelScreen.kt` - Chọn skin xúc xắc (4 mẫu màu).
- `ui/screen/dice/DicePreviewScreen.kt` - Hiển thị kết quả + chia sẻ.

### ViewModel
- `ui/screen/dice/DiceViewModel.kt` - Quản lý state, random logic, lưu settings vào `SettingsRepository`.

## Tính năng đã implement

1. Dice count selector 1-6 viên.
2. Dice face component vẽ chấm (dots) đúng vị trí theo giá trị 1-6.
3. Rolling animation với hiệu ứng `rotate()` rung lắc.
4. Random result từng viên (`Random.nextInt(1, 7)`).
5. Tính tổng điểm (total).
6. 4 skin/theme màu đúng theo `test.html` (DICE_STYLES).
7. Settings: duration, skin selection.
8. Preview/Result screen với nút chia sẻ và thử lại.

## Kiểm tra

- [x] Build debug thành công.
- [x] Dice count 1-6 hoạt động.
- [x] Mỗi kết quả từ 1 đến 6.
- [x] Total đúng.
- [x] Theme dice cập nhật đúng.
- [x] Push lên remote `feature/phase-12-dice`.
