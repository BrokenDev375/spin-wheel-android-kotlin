# Implementation Plan: chỉnh sửa theo `bao-cao-spin-wheel.txt`

Nguồn duy nhất: `D:\Study\thuc tap mobile vga\Báo cáo\bao-cao-spin-wheel.txt`

## 1. Mục tiêu

Triển khai các chỉnh sửa được liệt kê trong file `bao-cao-spin-wheel.txt`, tập trung vào 8 nhóm việc:

1. Công việc toàn app
2. Bánh xe
3. Chọn ngón tay
4. Chọn đội
5. Số ngẫu nhiên
6. Rút thăm
7. Xúc xắc
8. Rút thẻ

Plan này chỉ bám theo các đầu việc trong file txt. Các phần đánh dấu **tuỳ chọn** sẽ triển khai sau khi xong nhóm bắt buộc.

## 2. Nguyên tắc triển khai

- Sửa lỗi UI/logic trước, làm hiệu ứng sau.
- Với text bị tràn hoặc bị đè, ưu tiên xử lý bằng constraint/layout, `maxLines`, `overflow`, kích thước font và khoảng cách component.
- Với logic kết quả, sửa ở ViewModel/rules nếu có thể, UI chỉ hiển thị state đã đúng.
- Các hiệu ứng mới phải nhẹ, không làm màn hình bị rối hoặc lag.
- Sau mỗi nhóm chức năng phải smoke test ngay trên màn liên quan.

## 3. Phase 1: Công việc toàn app

Đầu việc từ txt:

- Tối ưu phông chữ.
- Xử lí trường hợp ngoại lệ chữ tràn khỏi ô.
- Tối ưu giao diện cài đặt mềm mại, lấp lánh, thu hút trẻ em.
- Check lại logic nút Restore.

### File dự kiến

- `app/src/main/java/com/vga/spinwheel/ui/components/SpinControls.kt`
- `app/src/main/java/com/vga/spinwheel/ui/components/SpinFeatureCard.kt`
- `app/src/main/java/com/vga/spinwheel/ui/components/SpinTopBar.kt`
- `app/src/main/java/com/vga/spinwheel/ui/screen/settings/SettingsRoute.kt`
- `app/src/main/java/com/vga/spinwheel/ui/screen/settings/SettingsScreen.kt`
- `app/src/main/java/com/vga/spinwheel/ui/screen/settings/SettingsViewModel.kt`
- `app/src/main/java/com/vga/spinwheel/core/IapOpener.kt`
- `app/src/main/res/values/strings_i18n.xml`
- `app/src/main/res/values-vi/strings_i18n.xml`

### Cách làm

1. Rà các component text dùng chung:
   - title card;
   - setting row;
   - top bar;
   - button text;
   - label trong các card nhỏ.
2. Thêm xử lý chữ dài:
   - `maxLines = 1` hoặc `maxLines = 2`;
   - `overflow = TextOverflow.Ellipsis`;
   - giảm font ở component nhỏ;
   - đảm bảo text không đẩy icon/button ra khỏi container.
3. Settings:
   - giữ cấu trúc màn hiện tại;
   - làm mềm row/background/icon;
   - thêm điểm nhấn màu nhẹ, phù hợp app trẻ em;
   - không thêm animation nặng.
4. Restore:
   - kiểm tra nút Restore đang gọi flow nào;
   - đảm bảo bấm Restore không crash;
   - đảm bảo không tự set sai premium state.

### Test nhanh

- Mở Home, Settings, các màn tool chính bằng EN/VI.
- Bật/tắt các setting hiện có.
- Bấm Restore ở trạng thái chưa premium.
- Kiểm tra chữ dài không tràn khỏi card/button.

### Done

- Font đồng đều hơn.
- Không còn text tràn rõ ràng ở các màn chính.
- Settings nhìn mềm mại hơn.
- Restore không crash và không làm sai state.

## [x] 4. Phase 2: Bánh xe

Đầu việc từ txt:

- Xử lí trường hợp quá nhiều chữ trong ô.

### File dự kiến

- `app/src/main/java/com/vga/spinwheel/ui/screen/wheel/WheelCanvas.kt`
- `app/src/main/java/com/vga/spinwheel/ui/screen/wheel/WheelSpinScreen.kt`
- `app/src/main/java/com/vga/spinwheel/ui/screen/wheel/WheelResultScreen.kt`
- `app/src/main/java/com/vga/spinwheel/ui/screen/wheel/WheelAddEditScreen.kt`

### Cách làm

1. Xử lý text item dài trong `WheelCanvas`.
2. Trước khi vẽ text lên canvas:
   - trim khoảng trắng;
   - giới hạn số ký tự theo số lượng item;
   - thêm `...` nếu text quá dài.
3. Nếu wheel có nhiều item, giảm nhẹ font size label.
4. Không thay đổi dữ liệu gốc của item, chỉ rút gọn phần hiển thị.

### Test nhanh

- Tạo wheel có item 30-50 ký tự.
- Tạo wheel nhiều item, mỗi item dài.
- Spin và xem Result.
- Đảm bảo winner vẫn là tên đầy đủ trong result/share nếu UI có chỗ đủ rộng.

### Done

- Text trong ô bánh xe không bị tràn quá mức.
- Spin/result vẫn hoạt động đúng.
- ✅ Thay đởi thực tế (branch `phase-2-banh-xe`):
  - `fitLabelToWidth()` — cắt label dựa trên pixel thực tế (`Paint.measureText()`) thay vì đếm ký tự.
  - `availableRadialPx` — chiều dài tia khả dụng = `(rimEdge - capEdge) * radius * 0.85`.
  - Text xoay **radial** (`rotate(midAngle)`) — đọc dọc theo tia từ tâm ra rim, chứa được nhiều text hơn tangential khi nhiều item.
  - `textRadius` cố định tại giữa tia (~0.545r), độc lập với số item.
  - Build debug: ✅ SUCCESSFUL.

## [x] 5. Phase 3: Chọn ngón tay

Đầu việc từ txt:

- Thêm hiệu ứng hiển thị vùng chạm ngón tay hướng dẫn cho người chơi đầu.
- Sửa lại logic chọn số là số người thắng kèm hướng dẫn luật chơi cho newbie.

### File dự kiến

- `app/src/main/java/com/vga/spinwheel/ui/screen/finger/FingerScreen.kt`
- `app/src/main/java/com/vga/spinwheel/ui/screen/finger/FingerViewModel.kt`
- `app/src/main/java/com/vga/spinwheel/ui/screen/finger/FingerRoundRules.kt`
- `app/src/main/res/values/strings_i18n.xml`
- `app/src/main/res/values-vi/strings_i18n.xml`

### Cách làm

1. Đổi ý nghĩa phần chọn số:
   - số người dùng chọn là **số người thắng**;
   - không hiểu nhầm là số ngón tay cần đặt.
2. Cập nhật label/hint trên UI để newbie hiểu luật chơi.
3. Logic chọn winner:
   - nếu chỉ hỗ trợ 1 winner hiện tại, mở rộng sang nhiều winner;
   - tạo danh sách winner ids thay vì một winner id;
   - clamp số winner không vượt số người chơi thực tế.
4. Hiệu ứng vùng chạm:
   - khi chưa có touch, hiển thị vùng hướng dẫn đặt ngón tay;
   - khi người chơi chạm, hiển thị pulse/halo tại vị trí chạm;
   - winner được highlight rõ hơn.

### Test nhanh

- Chọn 1 người thắng.
- Chọn 2 người thắng với 4 ngón tay.
- Chọn số winner lớn hơn số người đặt tay, app không crash.
- Người chơi mới nhìn màn hình biết cần đặt ngón tay vào đâu.

### Done

- [x] Số được chọn trên icon bàn tay là số người chơi.
- [x] Thêm một dropdown menu kế bên để chọn số người thắng.
- [x] Có hướng dẫn luật chơi cơ bản cho người mới ngay trên giao diện (text thay đổi tuỳ theo cấu hình).
- [x] Kết quả highlight đúng số winner theo thiết lập.
- [x] Logic `FingerRoundRules` và `FingerViewModel` hỗ trợ lưu multi-winners (`Set<Long>`).

## 6. Phase 4: Chọn đội

Đầu việc từ txt:

- Xử lí phần text bị các component khác đè lên.
- Sửa đổi hiệu ứng chia đội nằm trong 1 màn hình.

### File dự kiến

- `app/src/main/java/com/vga/spinwheel/ui/screen/team/TeamHomeScreen.kt`
- `app/src/main/java/com/vga/spinwheel/ui/screen/team/TeamDetailScreen.kt`
- `app/src/main/java/com/vga/spinwheel/ui/screen/team/TeamPreviewScreen.kt`
- `app/src/main/java/com/vga/spinwheel/ui/screen/team/TeamSettingsScreen.kt`
- `app/src/main/java/com/vga/spinwheel/ui/screen/team/TeamBoardComponents.kt`

### Cách làm

1. Rà các màn Team có text bị đè:
   - title;
   - tên thành viên;
   - tên đội;
   - nút action;
   - vùng result/preview.
2. Sửa layout:
   - tránh height cố định quá thấp;
   - dùng scroll nếu danh sách dài;
   - dùng `weight` hợp lý để header/footer không đè nội dung.
3. Hiệu ứng chia đội:
   - đưa animation vào container có kích thước ổn định;
   - nếu nhiều đội/thành viên thì cho content scroll hoặc scale hợp lý;
   - không để animation vượt khỏi màn hình.

### Test nhanh

- Chia 6 người thành 2 đội.
- Chia 12 người thành 3-4 đội.
- Tên người chơi dài.
- Xoay qua lại giữa settings/detail/preview.

### Done

- Text không bị component khác che.
- Hiệu ứng chia đội nằm gọn trong màn hình hoặc trong vùng scroll có kiểm soát.

## 7. Phase 5: Số ngẫu nhiên

Đầu việc từ txt:

- Đổi các bóng mặt cười thành các bóng số.
- Đổi hiệu ứng rơi bóng ra khỏi lỗ (tuỳ chọn).
- Đổi hiệu ứng quay bóng (tuỳ chọn).

### File dự kiến

- `app/src/main/java/com/vga/spinwheel/ui/screen/number/NumberMachine.kt`
- `app/src/main/java/com/vga/spinwheel/ui/screen/number/NumberHomeScreen.kt`
- `app/src/main/java/com/vga/spinwheel/ui/screen/number/NumberResultScreen.kt`
- `app/src/main/java/com/vga/spinwheel/ui/screen/number/NumberViewModel.kt`
- `app/src/main/res/drawable/number_ball.*`

### Cách làm

1. Bóng số:
   - giữ nền bóng hiện tại nếu dùng được;
   - overlay số lên bóng bằng Compose `Text`;
   - nếu asset có mặt cười cố định, tạo bóng mới hoặc vẽ bóng bằng Compose shape.
2. Hiệu ứng rơi bóng ra khỏi lỗ (tuỳ chọn):
   - thêm translateY/alpha khi result xuất hiện;
   - chỉ chạy sau khi random xong.
3. Hiệu ứng quay bóng (tuỳ chọn):
   - thêm rotation nhẹ trong lúc random;
   - không làm bóng quay quá nhanh gây rối mắt.

### Test nhanh

- Random 1 số.
- Random nhiều số.
- Có duplicate và không duplicate.
- Result hiển thị đúng số.

### Done

- Bóng hiển thị số rõ.
- Hiệu ứng tuỳ chọn nếu làm thì không gây lag/crash.

## 8. Phase 6: Rút thăm

Đầu việc từ txt:

- Phần thông báo kết quả là tên mục thay vì là số thứ tự của mục.

### File dự kiến

- `app/src/main/java/com/vga/spinwheel/ui/screen/drawing/DrawingSpinScreen.kt`
- `app/src/main/java/com/vga/spinwheel/ui/screen/drawing/DrawingResultScreen.kt`
- `app/src/main/java/com/vga/spinwheel/ui/screen/drawing/DrawingCardStack.kt`
- `app/src/main/java/com/vga/spinwheel/ui/screen/drawing/DrawingViewModel.kt`

### Cách làm

1. Dùng `winner.name` làm kết quả chính.
2. Nếu vẫn cần số thứ tự, chuyển thành subtitle nhỏ.
3. Update share text nếu hiện tại vẫn dùng index.
4. Đảm bảo khi winner null thì UI có fallback an toàn.

### Test nhanh

- Rút thăm list 3 mục.
- Rút thăm list có tên mục dài.
- Xem result và share.

### Done

- Kết quả chính hiển thị tên mục được chọn, không phải số thứ tự.

## 9. Phase 7: Xúc xắc

Đầu việc từ txt:

- Đổi hiệu ứng rơi xúc xắc (tuỳ chọn).

### File dự kiến

- `app/src/main/java/com/vga/spinwheel/ui/screen/dice/DiceComponents.kt`
- `app/src/main/java/com/vga/spinwheel/ui/screen/dice/DiceHomeScreen.kt`
- `app/src/main/java/com/vga/spinwheel/ui/screen/dice/DicePreviewScreen.kt`
- `app/src/main/java/com/vga/spinwheel/ui/screen/dice/DiceViewModel.kt`

### Cách làm

1. Không đổi logic random dice.
2. Chỉ sửa animation hiển thị:
   - translateY nhẹ;
   - scale/rotation nhẹ;
   - stagger nếu có nhiều xúc xắc.
3. Giữ layout grid ổn định, tránh dice nhảy làm vỡ màn.

### Test nhanh

- Roll 1 xúc xắc.
- Roll nhiều xúc xắc.
- Roll liên tục.
- Mở preview sau khi roll.

### Done

- Hiệu ứng rơi xúc xắc chạy mượt nếu được triển khai.
- Kết quả dice không bị ảnh hưởng.

## 10. Phase 8: Rút thẻ

Đầu việc từ txt:

- Thêm hiệu ứng xào bài.
- Sửa logic thông báo chiến thắng với trường hợp nhiều người thắng.

### File dự kiến

- `app/src/main/java/com/vga/spinwheel/ui/screen/card/CardScreen.kt`
- `app/src/main/java/com/vga/spinwheel/ui/screen/card/CardViewModel.kt`
- `app/src/main/java/com/vga/spinwheel/ui/screen/card/CardRoundRules.kt`
- `app/src/main/java/com/vga/spinwheel/ui/screen/card/CardThemes.kt`

### Cách làm

1. Hiệu ứng xào bài:
   - chạy khi bấm shuffle;
   - card dịch ngang/rotate nhẹ;
   - không shuffle data liên tục trong animation.
2. Logic nhiều người thắng:
   - kiểm tra điều kiện chuyển sang màn kết quả;
   - nếu số winner > 1, không thông báo thắng quá sớm khi mới lật 1 winner;
   - kết quả phải liệt kê đủ các thẻ/người thắng theo rule.
3. Result/share text:
   - hiển thị danh sách winner rõ ràng;
   - không chỉ nói chung chung “win” khi có nhiều người thắng.

### Test nhanh

- 4 thẻ, 1 winner.
- 6 thẻ, 2 winner.
- Lật trúng winner đầu tiên khi còn winner khác.
- Shuffle lại sau một lượt chơi.

### Done

- Có hiệu ứng xào bài.
- Thông báo chiến thắng đúng với trường hợp nhiều người thắng.

## 11. Thứ tự commit đề xuất

1. `fix(app): handle shared text overflow and settings polish`
2. `fix(wheel): clamp long labels in wheel segments`
3. `fix(finger): use selected number as winner count`
4. `fix(team): prevent text overlap in team screens`
5. `polish(number): replace smile balls with number balls`
6. `fix(drawing): show result item name`
7. `polish(dice): adjust dice drop animation`
8. `fix(card): add shuffle animation and multi-winner result`
9. `test: smoke test updated random tools`

## 12. Checklist kiểm thử cuối

- Toàn app không còn lỗi chữ tràn rõ ở các màn chính.
- Settings nhìn mềm mại hơn.
- Restore không crash.
- Bánh xe xử lý được item dài.
- Chọn ngón tay dùng số chọn là số người thắng.
- Chọn đội không còn text bị che.
- Số ngẫu nhiên hiển thị bóng số.
- Rút thăm trả kết quả bằng tên mục.
- Xúc xắc không bị sai kết quả sau khi đổi hiệu ứng.
- Rút thẻ có hiệu ứng xào bài và xử lý đúng nhiều winner.
- Build debug thành công.

## 13. Command kiểm tra

Chạy tối thiểu:

```powershell
./gradlew.bat :app:assembleDebug
```

Nếu có thời gian:

```powershell
./gradlew.bat :app:testDebugUnitTest
```
