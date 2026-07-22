# Review Phase 9 - Số Ngẫu Nhiên

## 1. Trạng thái
- **Trạng thái**: [x] Hoàn thành
- **Branch**: `phase-9-number`

## 2. Công việc đã thực hiện
1. **NumberSettingsRepository**: 
   - Đã tạo repository lưu trữ SharedPreferences cho các cấu hình: Min, Max, Count, Allow Duplicates, Duration.
2. **NumberViewModel**:
   - Đã tạo ViewModel để xử lý logic lấy số ngẫu nhiên.
   - Thêm xử lý đảo Min/Max nếu người dùng nhập Min > Max.
   - Xử lý chặn số lượng random (count) không vượt quá kích thước dải số khi không cho phép trùng lặp (Allow Duplicates = false).
3. **NumberNavGraph & AppNavHost**:
   - Đã tạo graph điều hướng cho 4 màn hình của Số Ngẫu Nhiên (Home, Settings, Result, History).
   - Đã tích hợp vào `AppNavHost` và thay thế cho placeholder.
4. **NumberHomeScreen**:
   - Đã tạo giao diện máy quay với hiệu ứng rung (shake).
   - Đã tạo hiệu ứng rớt các viên bi chứa số ngẫu nhiên sau khi hết thời lượng quay.
5. **NumberSettingsScreen**:
   - Đã tạo màn hình cài đặt sử dụng các components chung (`SpinStepper`, `SpinToggle`).
6. **NumberResultScreen & NumberHistoryScreen**:
   - Đã tạo màn hình kết quả và màn hình lịch sử (hiển thị danh sách kết quả phân cách bằng dấu phẩy).
   - Thêm nút chia sẻ kết quả native.
   - Gọi hàm clearHistory() cho tính năng xoá lịch sử.

## 3. Kết quả test
- Build debug bằng lệnh `.\gradlew.bat :app:assembleDebug` thành công.
- Không có lỗi compile.

## 4. Các điểm cần chú ý
- Animation drop balls hiện tại được thiết kế chạy từ trong Box của cỗ máy đổ xuống random (mỗi bóng chứa số của kết quả).
- Chưa test trực tiếp bằng thiết bị vật lý. Người dùng nên chạy trên emulator/device để tinh chỉnh thêm về mặt UI/Animation nếu cần thiết.
