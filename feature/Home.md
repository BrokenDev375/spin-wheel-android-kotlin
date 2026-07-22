# Home Flow

Folder screenshot tuong ung: `../screenshot/Home`

Trang thai tai lieu: Draft - cho duyet.

## Muc dich chuc nang

`Home` la diem vao chinh cua ung dung Spin Wheel. Chuc nang nay bao gom luong gioi thieu lan dau, man hinh danh sach tro choi, man hinh cai dat chung va man hinh doi ngon ngu.

## Man hinh lien quan

- `../screenshot/Home/intro1.jpg`: man gioi thieu 1 ve vong quay ngau nhien.
- `../screenshot/Home/intro2.jpg`: man gioi thieu 2 ve chon ngon tay, dong xu, xuc xac.
- `../screenshot/Home/intro3.jpg`: man gioi thieu 3 ve ghep doi/doi va so ngau nhien.
- `../screenshot/Home/intro 4.jpg`: man gioi thieu cuoi, nut bat dau vao app.
- `../screenshot/Home/home.jpg`: man hinh trang chu chua cac chuc nang.
- `../screenshot/Home/Setting.jpg`: man hinh cai dat chung.
- `../screenshot/Home/language.jpg`: man hinh lua chon ngon ngu.
- `../screenshot/Home/Payment.jpg`: man hinh goi Pro/thanh toan.
- `../screenshot/Home/Payment Continue.jpg`: trang thai tiep tuc/chon goi thanh toan.

## Luong hoat dong chinh

1. Nguoi dung mo ung dung.
2. Ung dung hien thi chuoi man hinh gioi thieu.
3. Nguoi dung bam `Tiep tuc`, cham vao dot chi bao, hoac vuot ngang de chuyen qua tung slide intro.
4. O slide cuoi, nguoi dung bam `Bat dau`.
5. Ung dung dieu huong den man hinh `Spin Wheel` trang chu.
6. Trang chu hien thi danh sach cac chuc nang dang co:
   - Banh Xe
   - Chon Ngon Tay
   - Dong Xu
   - Doi
   - So Ngau Nhien
   - Ve
   - Quay Chai
   - Xuc Xac
   - Lat The
7. Nguoi dung bam vao mot card chuc nang bat ky.
8. Ung dung dieu huong sang luong rieng cua chuc nang do.

## Luong cai dat chung

1. Tu trang chu, nguoi dung bam icon cai dat o goc tren trai.
2. Ung dung mo man hinh `Cai dat`.
3. Man hinh cai dat hien thi cac muc:
   - Chia se ung dung.
   - Ngon ngu.
   - Danh gia ung dung.
   - Bat/tat nhac nen.
   - Bat/tat am thanh tro choi.
   - Bat/tat rung.
4. Nguoi dung bam nut quay lai de ve trang chu.

## Luong doi ngon ngu

1. Tu man hinh `Cai dat`, nguoi dung chon muc `Ngon ngu`.
2. Ung dung mo man hinh `Doi ngon ngu`.
3. Nguoi dung co the tim kiem ngon ngu bang o tim kiem.
4. Nguoi dung chon mot ngon ngu trong danh sach.
5. Ngon ngu duoc danh dau bang trang thai selected/radio.
6. Nguoi dung bam `Xong`.
7. Ung dung quay lai man hinh `Cai dat`.

## Luong thanh toan Pro

1. Tu trang chu, nguoi dung bam icon vuong mien o goc tren phai.
2. Ung dung mo man hinh thanh toan Pro.
3. Man hinh hien bang so sanh tinh nang `Pro` va `Basic`.
4. Nguoi dung co the chon goi:
   - Weekly.
   - Annually.
   - Monthly.
5. Goi Annually duoc chon mac dinh va co nhan giam gia.
6. Khi nguoi dung chon goi khac, card goi duoc danh dau selected va text nut hanh dong duoc cap nhat.
7. Nguoi dung bam nut dang ky/tiep tuc de thuc hien thanh toan.
8. Nguoi dung bam `Restore` de khoi phuc giao dich.
9. Nguoi dung bam nut dong de quay lai Home.

## Trang thai va du lieu

- Slide intro hien tai duoc quan ly bang chi so `currentIndex`.
- Ngon ngu dang chon duoc quan ly bang `activeLangCode`.
- Cac toggle cai dat chung gom nhac nen, am thanh tro choi va rung.
- Trang chu chi dieu huong sang chuc nang khac, khong tao ket qua ngau nhien truc tiep.
- Goi thanh toan dang chon duoc the hien bang trang thai `selected` tren package card.

## Dieu kien can luu y

- Nguoi dung co the vao thang Home sau khi hoan thanh intro.
- Tu cac chuc nang con, nut home/quay lai co the dua nguoi dung ve lai Home.
- Man hinh Home la noi tap trung dieu huong, nen khi them chuc nang moi can cap nhat card tren Home va flow tai lieu tuong ung.
