# WheelChair Flow

Folder screenshot tuong ung: `../screenshot/WheelChair`

Trang thai tai lieu: Draft - cho duyet.

## Muc dich chuc nang

`WheelChair` trong screenshot dang dai dien cho chuc nang `Banh Xe`. Chuc nang nay cho phep nguoi dung tao danh sach lua chon, luu thanh banh xe, quay de lay ket qua ngau nhien, tuy chinh thoi gian/quy tac/mau sac va chia se ket qua.

## Man hinh lien quan

- `../screenshot/WheelChair/WheelSpinHome.jpg`: man hinh danh sach banh xe khi chua co du lieu hoac trang thai mac dinh.
- `../screenshot/WheelChair/wheelSpinHomeWithStorage.jpg`: man hinh danh sach banh xe khi da co banh xe duoc luu.
- `../screenshot/WheelChair/AddWheel.jpg`: man hinh them/sua banh xe.
- `../screenshot/WheelChair/AddManyOptions.jpg`: popup them nhieu lua chon cung luc.
- `../screenshot/WheelChair/AddWheelChairException.jpg`: trang thai validate loi khi thieu ten banh xe hoac thieu muc.
- `../screenshot/WheelChair/AIGenerate.jpg`: man hinh tao tuy chon bang AI.
- `../screenshot/WheelChair/WheelChair.jpg`: man hinh quay banh xe.
- `../screenshot/WheelChair/WheelSetting.jpg`: man hinh tuy chinh banh xe.
- `../screenshot/WheelChair/WheelSettingColor.jpg`: man hinh chon bang mau.
- `../screenshot/WheelChair/WheelResult.jpg`: man hinh ket qua sau khi quay.

## Luong vao chuc nang

1. Tu `Home`, nguoi dung bam card `Banh Xe`.
2. Ung dung mo man hinh danh sach banh xe.
3. Neu chua co banh xe nao, hien thong bao rong va nut tao moi.
4. Neu da co banh xe trong bo nho, hien danh sach cac banh xe da luu kem so luong option.

## Luong tao banh xe moi

1. Tu man hinh danh sach banh xe, nguoi dung bam `Tao Banh xe moi`.
2. Ung dung mo man hinh `Them banh xe`.
3. Nguoi dung nhap `Ten banh xe`.
4. Nguoi dung nhap toi thieu 2 muc trong danh sach `Muc`.
5. Voi moi muc, nguoi dung co the tang/giam `Uu tien`.
6. Hai muc dau tien la bat buoc; khi `Uu tien` dang la 1, bam giam tiep khong dua ve 0 va khong xoa muc.
7. Cac muc them phia duoi hai muc dau, neu `Uu tien` dang la 1 ma bam giam tiep thi xem nhu giam ve 0 va ung dung xoa muc do khoi danh sach.
8. Neu can them dong rieng le, nguoi dung bam `Them muc`.
9. Neu can nhap nhieu muc nhanh, nguoi dung bam `Them nhieu`.
10. Popup `Them nhieu tuy chon` hien textarea.
11. Nguoi dung nhap moi tuy chon tren mot dong, bam `Xong`.
12. Ung dung them cac dong hop le vao danh sach muc.
13. Nguoi dung bam `Luu`.
14. Ung dung validate du lieu va luu banh xe vao storage.
15. Ung dung quay lai man hinh danh sach banh xe.

## Validate khi tao/sua banh xe

1. Neu `Ten banh xe` dang rong, ung dung bao loi yeu cau nhap ten.
2. Neu khong co muc nao co ten, ung dung bao loi yeu cau nhap ten muc.
3. Neu chi co 1 muc co ten, ung dung bao loi can it nhat 2 muc.
4. Khi nguoi dung sua input hop le, trang thai loi duoc xoa.

## Luong quan ly banh xe da luu

1. Tu danh sach banh xe, nguoi dung bam vao mot banh xe de vao man hinh quay.
2. Nguoi dung bam menu ba cham tren card banh xe de mo tuy chon.
3. Cac tuy chon gom:
   - `Sua`: mo lai form them/sua voi du lieu hien tai.
   - `Nhan ban`: tao ban sao cua banh xe hien tai.
   - `Xoa`: hoi xac nhan truoc khi xoa khoi storage.
4. Sau khi sua, nhan ban hoac xoa, danh sach banh xe duoc cap nhat.

## Luong tao tuy chon bang AI

1. Tu man hinh danh sach banh xe, nguoi dung bam `Trinh tao AI`.
2. Ung dung mo man hinh nhap chu de.
3. Nguoi dung nhap chu de cho banh xe.
4. Nguoi dung bam `Generate options`/`Tao tuy chon`.
5. Neu chu de rong, ung dung hien thong bao yeu cau nhap chu de.
6. Neu hop le, ung dung hien trang thai dang tao.
7. Sau khi tao xong, ung dung thong bao thanh cong va quay lai danh sach banh xe.

## Luong quay banh xe

1. Tu danh sach banh xe, nguoi dung chon mot banh xe.
2. Ung dung mo man hinh quay voi ten banh xe, ket qua tam thoi `???`, canvas banh xe va cac nut thao tac.
3. Nguoi dung bam `NHAN DE QUAY` hoac bam truc tiep vao banh xe.
4. Banh xe quay theo thoi luong dang cau hinh.
5. Ung dung tinh nguoi thang dua theo cac muc va gia tri `Uu tien`.
6. Ket qua duoc dua vao lich su quay cua lan hien tai.
7. Ung dung dieu huong sang man hinh `Ket Qua`.

## Luong man hinh ket qua

1. Man hinh ket qua hien ten muc chien thang va anh/banh xe tai vi tri dung.
2. Nguoi dung co the bam `Chia se ket qua`.
3. Neu thiet bi ho tro native share, ung dung mo share sheet.
4. Neu khong ho tro native share, ung dung sao chep ket qua vao clipboard hoac hien alert.
5. Nguoi dung bam `Thu lai` de quay lai man hinh quay cua banh xe vua duoc chon.
6. Nguoi dung bam icon Home de ve trang chu.

## Luong tuy chinh banh xe

1. Tu man hinh quay, nguoi dung bam nut tuy chinh.
2. Ung dung mo man hinh `Tuy chinh`.
3. Nguoi dung co the thay doi `Thoi luong hoat hinh`.
4. Nguoi dung co the vao `Bang mau` de doi bo mau cua cac segment.
5. Nguoi dung co the bat/tat `Xoa nguoi thang`.
6. Khi bat `Xoa nguoi thang`, muc vua thang se bi loai khoi danh sach quay hien tai sau moi lan quay.
7. Nut quay lai dua nguoi dung ve man hinh quay banh xe.

## Luong chon bang mau

1. Tu man hinh tuy chinh, nguoi dung bam `Bang mau`.
2. Ung dung hien danh sach cac palette.
3. Nguoi dung chon mot palette tam thoi.
4. Nguoi dung bam `Luu`.
5. Ung dung ap dung bang mau vao banh xe va quay lai man hinh tuy chinh.

## Luong xao tron va khoi phuc

1. Tu man hinh quay, nguoi dung bam nut xao tron.
2. Ung dung dao thu tu cac muc trong banh xe hien tai.
3. Nguoi dung bam nut khoi phuc.
4. Ung dung dua danh sach muc ve thu tu ban dau cua banh xe.

## Luong lich su quay

1. Tu man hinh quay, nguoi dung bam icon lich su.
2. Neu chua co lan quay nao, ung dung thong bao chua co lich su.
3. Neu da co ket qua, ung dung hien danh sach ket qua theo thu tu moi nhat truoc.

## Trang thai va du lieu

- Danh sach banh xe duoc luu trong `customWheels` tren local storage.
- Moi banh xe gom:
  - `name`: ten banh xe.
  - `items`: danh sach muc.
  - Moi muc co `name` va `priority`.
- Trang thai quay hien tai gom danh sach muc, thu tu goc, rotation, lich su va trang thai dang quay.
- Cau hinh banh xe gom:
  - `duration`: thoi luong quay.
  - `paletteIndex`: bang mau dang dung.
  - `removeWinner`: co xoa nguoi thang hay khong.

## Dieu kien can luu y

- Can toi thieu 2 muc hop le de luu banh xe.
- `Uu tien` anh huong den xac suat chien thang cua tung muc.
- Hai muc dau tien trong form luon giu `Uu tien` toi thieu la 1.
- Voi cac muc them sau hai muc dau, thao tac giam `Uu tien` tu 1 ve 0 se xoa muc do thay vi luu priority 0.
- Neu bat `Xoa nguoi thang`, can dam bao banh xe van con du muc de quay tiep.
- Lich su quay chi gan voi phien quay hien tai, khong phai lich su toan app.
- Luong AI trong mockup dang la mo phong, chua the hien ket noi backend that.
