# Phase 10 Review - Vẽ (Drawing)

## Trạng thái: ✅ Hoàn thành

## Tổng quan

Tính năng Vẽ (Drawing) cho phép người dùng sử dụng lại danh sách Wheel có sẵn để rút thăm ngẫu nhiên dưới dạng thẻ bài (card stack). Hỗ trợ CRUD danh sách, AI generate mock, đổi theme/palette, và chia sẻ kết quả.

## Các file đã tạo

### Navigation
- `ui/nav/DrawingNavGraph.kt` - Điều hướng 8 màn hình Drawing.
- `ui/nav/DrawingRoutes.kt` - Định nghĩa route constants.

### Screens
- `ui/screen/drawing/DrawingHomeScreen.kt` - Danh sách wheel dùng cho Drawing.
- `ui/screen/drawing/DrawingSpinScreen.kt` - Màn hình rút thăm với hiệu ứng rung lắc xen kẽ.
- `ui/screen/drawing/DrawingResultScreen.kt` - Hiển thị kết quả rút thăm.
- `ui/screen/drawing/DrawingSettingsScreen.kt` - Cài đặt thời lượng animation.
- `ui/screen/drawing/DrawingPaletteScreen.kt` - Chọn theme màu cho thẻ bài.
- `ui/screen/drawing/DrawingAddEditScreen.kt` - Thêm/sửa danh sách.
- `ui/screen/drawing/DrawingAiFormScreen.kt` - AI generate mock danh sách.

### ViewModel
- `ui/screen/drawing/DrawingViewModel.kt` - Quản lý state, random logic, CRUD.

## Tính năng đã implement

1. Dùng chung danh sách `Wheel` từ Room database.
2. Fallback demo wheel khi không có danh sách nào.
3. CRUD: Tạo/Sửa/Xóa/Nhân bản danh sách.
4. AI generate mock danh sách.
5. Stack card UI với hiệu ứng rung lắc **xen kẽ** (index-based alternating shake).
6. Random animation theo duration setting.
7. Result screen với chia sẻ kết quả.
8. Palette/theme cho thẻ bài.

## Kiểm tra

- [x] Build debug thành công.
- [x] Hiệu ứng rung lắc xen kẽ (không đồng bộ).
- [x] Random hiển thị đúng item.
- [x] Theme đổi màu stack card.
- [x] Push lên remote `feature/phase-10-drawing`.
