# Drawing Flow

Folder screenshot tuong ung: `../screenshot/Drawing`

Trang thai tai lieu: Draft - cho duyet.

## Muc dich chuc nang

`Drawing` dai dien cho chuc nang `Ve`. Chuc nang nay cho phep nguoi dung chon ngau nhien mot muc tu danh sach da tao, hien thi ket qua duoi dang cac the xep chong. Du lieu danh sach dung chung voi cac banh xe da tao trong ung dung.

## Man hinh lien quan

- `../screenshot/Drawing/Home.jpg`: man hinh danh sach cac bo lua chon cho chuc nang Ve.
- `../screenshot/Drawing/specificHome.jpg`: man hinh chi tiet mot bo lua chon truoc khi random.
- `../screenshot/Drawing/Setting.jpg`: man hinh tuy chinh chuc nang Ve.
- `../screenshot/Drawing/drawinglabel.jpg`: man hinh chon mau/theme the.
- `../screenshot/Drawing/result.jpg`: man hinh ket qua sau khi random.

## Luong vao chuc nang

1. Tu `Home`, nguoi dung bam card `Ve`.
2. Ung dung mo man hinh `Ve`.
3. Neu da co danh sach trong storage, ung dung hien cac card danh sach.
4. Neu chua co danh sach, ung dung co the hien danh sach demo/mac dinh de nguoi dung trai nghiem.
5. Nguoi dung co the bam nut quay lai de ve `Home`.

## Luong tao danh sach moi

1. Tu man hinh `Ve`, nguoi dung bam `Tao Banh Xe moi`.
2. Ung dung mo form them/sua banh xe dung chung.
3. Nguoi dung nhap ten danh sach.
4. Nguoi dung nhap toi thieu 2 muc.
5. Nguoi dung co the them tung muc hoac them nhieu muc cung luc.
6. Nguoi dung bam `Luu`.
7. Ung dung validate du lieu va luu danh sach vao storage.
8. Ung dung quay lai man hinh `Ve`.

## Luong quan ly danh sach

1. Tu man hinh `Ve`, nguoi dung bam menu ba cham tren mot card danh sach.
2. Ung dung hien cac thao tac:
   - `Sua`: mo form sua danh sach.
   - `Nhan ban`: tao ban sao cua danh sach.
   - `Xoa`: xoa danh sach sau khi xac nhan.
3. Neu danh sach la ban demo/mac dinh, ung dung khong cho xoa truc tiep va goi y tao ban moi de sua/xoa.
4. Sau khi thao tac, ung dung cap nhat lai danh sach.

## Luong tao danh sach bang AI

1. Tu man hinh `Ve`, nguoi dung bam `Trinh tao AI`.
2. Ung dung mo man hinh nhap chu de cho chuc nang Ve.
3. Nguoi dung nhap chu de, vi du y tuong ve hoac hoat dong cuoi tuan.
4. Nguoi dung bam `Tao tuy chon`.
5. Neu input rong, ung dung hien thong bao yeu cau nhap chu de.
6. Neu hop le, ung dung tao danh sach goi y tu chu de.
7. Danh sach moi duoc luu vao storage va nguoi dung quay lai man hinh `Ve`.

## Luong vao chi tiet danh sach

1. Tu man hinh `Ve`, nguoi dung bam vao mot card danh sach.
2. Ung dung mo man hinh chi tiet.
3. Man hinh hien ten danh sach va stack the dai dien cho cac muc.
4. Thanh thao tac duoi gom:
   - Nut tuy chinh.
   - Nut `Nhan de ghep noi`.
   - Nut reset/quay lai tu dau.
5. Nguoi dung bam nut quay lai de tro ve man hinh danh sach `Ve`.

## Luong random

1. Tu man hinh chi tiet danh sach, nguoi dung bam `Nhan de ghep noi`.
2. Ung dung chuyen stack the sang trang thai rung/lac.
3. Hoat hinh chay theo thoi luong dang cau hinh.
4. Khi het thoi gian, ung dung chon ngau nhien mot muc trong danh sach.
5. Man hinh chi tiet hien so thu tu/ket qua tam thoi cua muc duoc chon.
6. Sau do ung dung tu dong chuyen sang man hinh `Ket Qua`.

## Luong ket qua

1. Man hinh `Ket Qua` hien preview stack the.
2. The/muc chien thang duoc dua len noi bat o phia truoc.
3. Ket qua hien so thu tu cua muc duoc chon.
4. Nguoi dung co the bam `Chia se ket qua`.
5. Neu thiet bi ho tro native share, ung dung mo share sheet.
6. Neu khong ho tro native share, ung dung copy ket qua vao clipboard hoac hien thong bao.
7. Nguoi dung bam `Thu lai` de quay lai man hinh danh sach `Ve`.
8. Nguoi dung bam icon Home de ve trang chu.

## Luong tuy chinh

1. Tu man hinh chi tiet danh sach, nguoi dung bam nut tuy chinh.
2. Ung dung mo man hinh `Tuy chinh`.
3. Nguoi dung co the tang/giam `Thoi luong hoat hinh`.
4. Nguoi dung co the bam muc `Ve` de vao man hinh chon theme/mau the.
5. Nut quay lai dua nguoi dung ve man hinh chi tiet danh sach.

## Luong chon theme the

1. Tu man hinh `Tuy chinh`, nguoi dung bam muc `Ve`.
2. Ung dung hien danh sach theme/mau the.
3. Nguoi dung chon mot theme tam thoi.
4. Theme dang chon duoc danh dau bang trang thai selected.
5. Nguoi dung bam `Luu`.
6. Ung dung ap dung theme moi va quay lai man hinh `Tuy chinh`.

## Luong reset

1. Tu man hinh chi tiet danh sach, nguoi dung bam nut reset.
2. Ung dung dua stack the ve trang thai ban dau.
3. Ket qua tam thoi bi xoa khoi man hinh.

## Trang thai va du lieu

- Danh sach lua chon dung chung `customWheels` voi chuc nang `Banh Xe` va `Doi`.
- Neu khong co danh sach nguoi dung tao, ung dung dung `DRAWING_FALLBACK_WHEEL` lam du lieu demo.
- `drawingAnimationDuration`: thoi luong hoat hinh random.
- `drawingThemeIndex`: theme dang ap dung.
- `drawingTempThemeIndex`: theme dang chon tam thoi tren man hinh label.
- `drawingLastResult`: ket qua gan nhat, gom danh sach, muc thang va so thu tu.
- `drawingRunId`: ma phien chay, dung de huy timer cu khi nguoi dung doi man hinh.

## Dieu kien can luu y

- Danh sach can co it nhat mot muc hop le de random.
- Khi dang chay animation, can tranh chuyen/sua danh sach lam sai ket qua.
- Neu sua schema `customWheels`, can kiem tra lai ca `Banh Xe`, `Doi` va `Ve`.
- Luong AI trong mockup dang la mo phong, chua the hien ket noi backend that.
