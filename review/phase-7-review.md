# Phase 7 Review - Đồng Xu (Coin)

## Trạng thái: ✅ Hoàn thành

## Tổng quan

Tính năng Đồng Xu cho phép người dùng tung đồng xu với hiệu ứng lật (flip animation), hiển thị kết quả Sấp/Ngửa, đổi skin đồng xu, và chia sẻ kết quả.

## Các file đã tạo

### Navigation
- `ui/nav/CoinNavGraph.kt` - Điều hướng các màn hình Coin.
- `ui/nav/CoinRoutes.kt` - Định nghĩa route constants.

### Data
- `data/repo/CoinSettingsRepository.kt` - Lưu trữ cài đặt riêng cho Coin.

### Screens
- `ui/screen/coin/CoinHomeScreen.kt` - Màn hình chính với đồng xu và nút tung.
- `ui/screen/coin/CoinResultScreen.kt` - Hiển thị kết quả Sấp/Ngửa + chia sẻ.
- `ui/screen/coin/CoinSettingsScreen.kt` - Cài đặt thời lượng animation.
- `ui/screen/coin/CoinLabelScreen.kt` - Chọn skin/theme đồng xu.

### Models & ViewModel
- `ui/screen/coin/CoinModels.kt` - Data models cho Coin (styles, themes).
- `ui/screen/coin/CoinViewModel.kt` - Quản lý state và random logic.

### Resources
- `res/drawable/ic_coin_head.xml` - Icon mặt Ngửa.
- `res/drawable/ic_coin_tail.xml` - Icon mặt Sấp.

## Tính năng đã implement

1. Flip animation đồng xu.
2. Random Sấp/Ngửa (heads/tails).
3. Hiển thị kết quả với điểm số.
4. Setting thời lượng animation.
5. Coin label/theme (đổi skin đồng xu).
6. Reset/Share/Retry.

## Kiểm tra

- [x] Build debug thành công.
- [x] Flip animation hoạt động.
- [x] Kết quả random đúng Sấp/Ngửa.
- [x] Đổi coin theme cập nhật đúng.
- [x] Duration thay đổi thời gian animation.
- [x] Push lên remote `phase-7-coin`.
