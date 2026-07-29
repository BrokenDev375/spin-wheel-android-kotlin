# Báo cáo kế hoạch chỉnh sửa Spin Wheel

Nguồn tổng hợp: `bao-cao-spin-wheel.txt`

## 1. Mục tiêu

Tài liệu này tổng hợp các vấn đề UI/logic hiện tại của app Spin Wheel và đề xuất kế hoạch chỉnh sửa theo từng nhóm chức năng. Mục tiêu là cải thiện độ ổn định, khả năng đọc nội dung, trải nghiệm tương tác và mức độ hoàn thiện của các mini-game trong app.

Trọng tâm xử lý:

- Giảm lỗi chữ tràn, text bị che hoặc bị component khác đè lên.
- Cải thiện giao diện Settings theo hướng mềm mại, sinh động và phù hợp với nhóm người dùng trẻ em.
- Sửa các lỗi logic ảnh hưởng trực tiếp đến kết quả trò chơi.
- Bổ sung một số hiệu ứng quan trọng để trải nghiệm trực quan và dễ hiểu hơn.
- Kiểm tra lại các luồng chính sau khi chỉnh sửa.

## 2. Phạm vi công việc

Phạm vi gồm 8 nhóm chức năng:

| Nhóm | Nội dung cần xử lý |
|---|---|
| Toàn app | Tối ưu phông chữ, xử lý chữ tràn, cải thiện Settings, kiểm tra logic Restore |
| Bánh xe | Xử lý trường hợp item có quá nhiều chữ trong một ô |
| Chọn ngón tay | Thêm hướng dẫn vùng chạm, sửa logic số người thắng |
| Chọn đội | Xử lý text bị đè, chỉnh hiệu ứng chia đội nằm gọn trong màn hình |
| Số ngẫu nhiên | Đổi bóng mặt cười thành bóng số, cân nhắc chỉnh hiệu ứng bóng |
| Rút thăm | Kết quả cần hiển thị tên mục thay vì số thứ tự |
| Xúc xắc | Cân nhắc đổi hiệu ứng rơi xúc xắc |
| Rút thẻ | Thêm hiệu ứng xào bài, sửa thông báo chiến thắng khi có nhiều người thắng |

## 3. Phân loại ưu tiên

### Ưu tiên cao

- Tối ưu phông chữ và xử lý chữ tràn khỏi ô.
- Kiểm tra lại logic nút Restore.
- Xử lý text bị component khác đè lên.
- Sửa logic chọn số người thắng ở màn Chọn ngón tay.
- Rút thăm hiển thị đúng tên mục được chọn.
- Rút thẻ xử lý đúng trường hợp nhiều người thắng.
- Smoke test các luồng chính sau chỉnh sửa.

### Ưu tiên trung bình

- Tối ưu giao diện Settings theo hướng mềm mại, sinh động hơn.
- Thêm hướng dẫn cho người dùng mới ở màn Chọn ngón tay.
- Chỉnh hiệu ứng chia đội nằm gọn trong một màn hình.
- Đổi bóng mặt cười thành bóng số ở màn Số ngẫu nhiên.
- Thêm hiệu ứng xào bài ở màn Rút thẻ.

### Tuỳ chọn

- Đổi hiệu ứng rơi bóng ra khỏi lỗ.
- Đổi hiệu ứng quay bóng.
- Đổi hiệu ứng rơi xúc xắc.

## 4. Ước tính thời gian

Các mốc dưới đây là timebox theo giờ. Một số việc UI nhỏ như font, overflow và spacing sẽ được xử lý theo cụm màn để tiết kiệm thời gian.

| Nhóm | Đầu việc | Ưu tiên | Timebox |
|---|---|---|---:|
| Toàn app | Tối ưu phông chữ, xử lý chữ tràn khỏi ô trên các màn chính | Cao | 3 giờ |
| Toàn app | Tối ưu giao diện Settings mềm mại, lấp lánh, thu hút trẻ em | Trung bình | 3 giờ |
| Toàn app | Kiểm tra lại logic nút Restore | Cao | 1 giờ |
| Bánh xe | Xử lý trường hợp quá nhiều chữ trong ô wheel segment | Cao | 2 giờ |
| Chọn ngón tay | Sửa logic chọn số người thắng và thêm hướng dẫn luật chơi | Cao | 3 giờ |
| Chọn ngón tay | Thêm hiệu ứng hiển thị vùng chạm ngón tay | Trung bình | 2 giờ |
| Chọn đội | Xử lý text bị component khác đè lên | Cao | 1.5 giờ |
| Chọn đội | Sửa hiệu ứng chia đội nằm trong một màn hình | Trung bình | 2 giờ |
| Số ngẫu nhiên | Đổi bóng mặt cười thành bóng số | Trung bình | 2 giờ |
| Số ngẫu nhiên | Đổi hiệu ứng rơi bóng ra khỏi lỗ | Tuỳ chọn | 2 giờ |
| Số ngẫu nhiên | Đổi hiệu ứng quay bóng | Tuỳ chọn | 2 giờ |
| Rút thăm | Kết quả hiển thị tên mục thay vì số thứ tự | Cao | 1 giờ |
| Xúc xắc | Đổi hiệu ứng rơi xúc xắc | Tuỳ chọn | 2 giờ |
| Rút thẻ | Thêm hiệu ứng xào bài | Trung bình | 2 giờ |
| Rút thẻ | Sửa logic thông báo chiến thắng với trường hợp nhiều người thắng | Cao | 2 giờ |
| Kiểm thử | Smoke test toàn bộ flow, sửa lỗi phát sinh, build bản kiểm thử | Cao | 3 - 4 giờ |

Tổng ước tính:

- **Nhóm ưu tiên cao:** khoảng **20 giờ**, tương đương **2 - 2.5 ngày làm việc**.
- **Ưu tiên cao + trung bình:** khoảng **28 giờ**, tương đương **3 - 3.5 ngày làm việc**.
- **Toàn bộ phạm vi, gồm cả phần tuỳ chọn:** khoảng **34 giờ**, tương đương **4 - 4.5 ngày làm việc**.

## 5. Phương án triển khai

### Phương án 1: Xử lý các lỗi trọng yếu

Thời gian: **2 - 3 ngày làm việc**

Phạm vi:

- Font, text overflow, lỗi text bị che.
- Logic Restore.
- Wheel item dài.
- Logic số người thắng ở Chọn ngón tay.
- Kết quả Rút thăm.
- Logic nhiều người thắng ở Rút thẻ.
- Kiểm thử nhanh các luồng chính.

Phương án này phù hợp nếu cần xử lý nhanh các lỗi ảnh hưởng trực tiếp đến chất lượng app.

### Phương án 2: Hoàn thiện trải nghiệm chính

Thời gian: **3 - 4 ngày làm việc**

Phạm vi gồm toàn bộ phương án 1 và bổ sung:

- Cải thiện giao diện Settings.
- Hướng dẫn người dùng mới ở Chọn ngón tay.
- Chỉnh hiệu ứng chia đội.
- Đổi bóng số ở Số ngẫu nhiên.
- Thêm hiệu ứng xào bài.

Phương án này được khuyến nghị vì cân bằng giữa thời gian xử lý và mức độ hoàn thiện sản phẩm.

### Phương án 3: Hoàn thiện đầy đủ

Thời gian: **4 - 5 ngày làm việc**

Phạm vi gồm toàn bộ phương án 2 và bổ sung:

- Hiệu ứng rơi bóng ra khỏi lỗ.
- Hiệu ứng quay bóng.
- Hiệu ứng rơi xúc xắc.
- Rà soát kỹ hơn trên thiết bị thật.

Phương án này phù hợp nếu muốn cải thiện cả các hiệu ứng phụ để tăng độ polish.

## 6. Timeline đề xuất

### Ngày 1: Toàn app, Settings và Bánh xe

- Rà font size, line height, max line, ellipsis ở các màn chính.
- Xử lý các trường hợp chữ tràn khỏi ô.
- Kiểm tra logic Restore.
- Cải thiện giao diện Settings.
- Xử lý item dài trong Bánh xe.
- Kiểm tra nhanh luồng tạo wheel, sửa item, spin và result.

Kết quả cuối ngày: các lỗi hiển thị rõ nhất trên toàn app và Bánh xe được xử lý.

### Ngày 2: Chọn ngón tay, Chọn đội và Rút thăm

- Sửa logic chọn số người thắng.
- Bổ sung hướng dẫn luật chơi cho người dùng mới.
- Thêm hiệu ứng vùng chạm nếu kịp.
- Xử lý text bị đè ở màn Chọn đội.
- Chỉnh hiệu ứng chia đội nằm trong một màn hình.
- Sửa kết quả Rút thăm để hiển thị tên mục.

Kết quả cuối ngày: các lỗi logic và UI chính ở Finger, Team và Rút thăm được xử lý.

### Ngày 3: Số ngẫu nhiên, Xúc xắc, Rút thẻ và kiểm thử

- Đổi bóng mặt cười thành bóng số ở Số ngẫu nhiên.
- Sửa logic thông báo chiến thắng ở Rút thẻ.
- Thêm hiệu ứng xào bài nếu kịp.
- Xem xét hiệu ứng rơi xúc xắc nếu còn thời gian.
- Smoke test toàn bộ flow.
- Fix lỗi phát sinh và build bản kiểm thử.

Kết quả cuối ngày: các nhóm chức năng chính được kiểm tra một lượt và có bản build kiểm thử.

### Ngày 4: Buffer/polish

- Hoàn thiện các hiệu ứng chưa xong.
- Rà lại UI tiếng Việt/tiếng Anh.
- Kiểm tra trên thiết bị thật.
- Sửa các lỗi nhỏ phát sinh trong quá trình kiểm thử.

## 7. Tiêu chí nghiệm thu

- Không còn text tràn nghiêm trọng ở Home, Settings, Wheel, Team và Finger.
- Settings có giao diện mềm mại hơn và phù hợp với đối tượng trẻ em.
- Restore không crash và không làm sai trạng thái premium.
- Wheel xử lý item dài ở mức chấp nhận được.
- Finger chọn đúng số người thắng và có hướng dẫn dễ hiểu.
- Team không còn text bị đè lên component.
- Random Number hiển thị bóng số.
- Rút thăm trả kết quả là tên mục.
- Rút thẻ xử lý đúng trường hợp nhiều người thắng.
- Các hiệu ứng bổ sung không gây lag hoặc crash.
- Bản build kiểm thử chạy ổn trên thiết bị thật.

## 8. Rủi ro và lưu ý

| Rủi ro | Ảnh hưởng | Hướng xử lý |
|---|---|---|
| Text dài ở nhiều ngôn ngữ | Có thể vẫn còn overflow ở màn ít dùng | Ưu tiên xử lý các màn chính trước, sau đó rà bổ sung |
| Hiệu ứng mới gây lag trên máy yếu | Ảnh hưởng trải nghiệm người dùng | Giữ animation đơn giản, có fallback nếu cần |
| Logic winner nhiều người liên quan nhiều màn | Có thể phát sinh case chưa bao phủ | Test riêng các case 1 người thắng, nhiều người thắng, thiếu người chơi |
| Restore phụ thuộc trạng thái IAP | Có thể khó kiểm chứng đủ trên môi trường debug | Kiểm tra flow debug trước, ghi chú case cần test thêm với môi trường thật |

## 9. Đề xuất

Đề xuất chọn **phương án 2: Hoàn thiện trải nghiệm chính**, thời gian khoảng **3 - 4 ngày làm việc**.

Lý do:

- Bao phủ được các lỗi UI/logic quan trọng.
- Có thêm các cải thiện trải nghiệm dễ nhận thấy.
- Không kéo dài sang quá nhiều hiệu ứng tuỳ chọn.
- Phù hợp để tạo một bản build ổn định hơn trong thời gian ngắn.
