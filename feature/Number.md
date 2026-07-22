# Number Flow

Folder screenshot tuong ung: `../screenshot/Number`

Trang thai tai lieu: Draft - cho duyet.

## Muc dich chuc nang

`Number` dai dien cho chuc nang `So Ngau Nhien`. Chuc nang nay cho phep nguoi dung tao mot hoac nhieu so ngau nhien trong khoang gia tri cau hinh, tuy chinh so luong ket qua, cho phep/tranh trung lap, xem lich su va chia se ket qua.

## Man hinh lien quan

- `../screenshot/Number/home.jpg`: man hinh tao so ngau nhien.
- `../screenshot/Number/setting.jpg`: man hinh tuy chinh khoang so va cach tao ket qua.
- `../screenshot/Number/history.jpg`: man hinh lich su ket qua.
- `../screenshot/Number/afterplay.jpg`: trang thai sau khi quay xong tren man hinh chinh.
- `../screenshot/Number/Screenshot_2026-07-21-16-15-43-74_b2d028cec5e18a2dfe1601d531fe4f7f.jpg`: man hinh/trang thai lien quan ket qua so ngau nhien.
- `../screenshot/Number/Screenshot_2026-07-21-16-15-53-54_b2d028cec5e18a2dfe1601d531fe4f7f.jpg`: man hinh/trang thai lien quan ket qua so ngau nhien.

## Luong vao chuc nang

1. Tu `Home`, nguoi dung bam card `So Ngau Nhien`.
2. Ung dung mo man hinh `So Ngau Nhien`.
3. Man hinh hien khoang so dang ap dung, khu vuc may quay so, nut lich su va thanh thao tac duoi.
4. Nguoi dung co the bam nut quay lai de ve `Home`.

## Luong tao so ngau nhien

1. Tu man hinh `So Ngau Nhien`, nguoi dung bam `NHAN DE NGAU NHIEN`.
2. Ung dung chuyen may quay so sang trang thai hoat hinh.
3. Trong khi dang quay, cac nut thao tac can duoc khoa de tranh thay doi cau hinh giua phien.
4. Sau thoi luong hoat hinh, ung dung tao ket qua dua theo cau hinh hien tai.
5. Ket qua duoc hien nhanh tren man hinh chinh.
6. Ket qua duoc them vao lich su.
7. Ung dung chuyen sang man hinh `Ket Qua`.

## Cach tao ket qua

1. Ung dung doc cac cau hinh:
   - Gia tri nho nhat.
   - Gia tri lon nhat.
   - So luong ket qua can tao.
   - Co cho phep trung lap hay khong.
2. Neu cho phep trung lap, moi ket qua duoc random doc lap trong khoang min-max.
3. Neu khong cho phep trung lap, ung dung tao pool so trong khoang min-max, xao tron pool, roi lay so luong ket qua can thiet.
4. Neu min lon hon max, ung dung hoan doi lai de dam bao khoang hop le.
5. Neu so luong ket qua lon hon kich thuoc khoang va khong cho phep trung lap, ung dung gioi han so luong theo kich thuoc khoang.

## Luong ket qua

1. Man hinh `Ket Qua` hien may quay so va chip ket qua.
2. Neu co nhieu so, ket qua duoc hien dang danh sach ngan cach bang dau phay.
3. Nguoi dung co the bam `Chia se ket qua`.
4. Neu thiet bi ho tro native share, ung dung mo share sheet.
5. Neu khong ho tro native share, ung dung copy ket qua vao clipboard hoac hien thong bao.
6. Nguoi dung bam `Thu lai` de quay lai man hinh tao so.
7. Nguoi dung bam icon Home de ve trang chu.

## Luong tuy chinh

1. Tu man hinh `So Ngau Nhien`, nguoi dung bam nut tuy chinh.
2. Ung dung mo man hinh `Tuy chinh`.
3. Nguoi dung co the nhap `Minimum Value (From)`.
4. Nguoi dung co the nhap `Maximum Value (To)`.
5. Nguoi dung co the tang/giam `Generate Count`.
6. Nguoi dung co the bat/tat `Allow Duplicates`.
7. Nguoi dung co the tang/giam `Thoi luong hoat hinh`.
8. Nguoi dung bam `Luu`.
9. Ung dung chuan hoa du lieu, luu cau hinh va quay lai man hinh `So Ngau Nhien`.

## Luong lich su

1. Tu man hinh `So Ngau Nhien`, nguoi dung bam icon lich su.
2. Ung dung mo man hinh `Lich su`.
3. Neu chua co ket qua nao, ung dung hien trang thai rong.
4. Neu da co ket qua, ung dung hien danh sach ket qua moi nhat truoc.
5. Tu man hinh chinh, nguoi dung co the thay nhanh mot so ket qua gan day.
6. Nguoi dung bam nut quay lai de ve man hinh `So Ngau Nhien`.

## Luong xoa lich su/reset

1. Tu man hinh `So Ngau Nhien`, nguoi dung bam nut reset/xoa lich su.
2. Ung dung xoa danh sach lich su ket qua.
3. Ket qua cuoi cung hien tai duoc lam moi.
4. Man hinh quay ve trang thai chua co ket qua gan day.

## Trang thai va du lieu

- `numberSettings`: cau hinh tao so, gom:
  - `min`: gia tri nho nhat.
  - `max`: gia tri lon nhat.
  - `count`: so luong ket qua.
  - `allowDuplicates`: cho phep trung lap hay khong.
  - `duration`: thoi luong hoat hinh.
- `numberHistory`: danh sach ket qua da tao, luu toi da 30 ket qua moi nhat.
- `numberLastResult`: ket qua cuoi cung.
- `numberRunId`: ma phien chay, dung de huy callback cu khi nguoi dung thoat/chuyen man hinh.

## Dieu kien can luu y

- Can chuan hoa min/max truoc khi tao ket qua.
- Neu khong cho phep trung lap, so luong ket qua khong duoc vuot qua kich thuoc khoang so.
- Khi dang chay animation, nen khoa cac nut setting/reset/start de tranh sinh ket qua sai cau hinh.
- Lich su can duoc luu lai de nguoi dung xem sau khi roi khoi man hinh.
