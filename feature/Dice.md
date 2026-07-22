# Dice Flow

Folder screenshot tuong ung: `../screenshot/Dice`

Trang thai tai lieu: Draft - cho duyet.

## Muc dich chuc nang

`Dice` dai dien cho chuc nang `Xuc Xac`. Chuc nang nay cho phep nguoi dung chon so luong xuc xac, lac xuc xac de tao ket qua ngau nhien, xem tong diem, tuy chinh thoi luong hoat hinh va doi mau/mau xuc xac.

## Man hinh lien quan

- `../screenshot/Dice/homeDice1.jpg`: man hinh xuc xac voi 1 vien.
- `../screenshot/Dice/HomeDice2.jpg`: man hinh xuc xac voi 2 vien.
- `../screenshot/Dice/HomeDice3.jpg`: man hinh xuc xac voi 3 vien.
- `../screenshot/Dice/HomeDice4.jpg`: man hinh xuc xac voi 4 vien.
- `../screenshot/Dice/HomeDice5.jpg`: man hinh xuc xac voi 5 vien.
- `../screenshot/Dice/HomeDice6.jpg`: man hinh xuc xac voi 6 vien.
- `../screenshot/Dice/setting.jpg`: man hinh tuy chinh xuc xac.
- `../screenshot/Dice/label.jpg`: man hinh chon mau xuc xac.
- `../screenshot/Dice/preview.jpg`: man hinh ket qua/preview sau khi lac.

## Luong vao chuc nang

1. Tu `Home`, nguoi dung bam card `Xuc Xac`.
2. Ung dung mo man hinh `Xuc Xac`.
3. Man hinh hien header, bo chon `DiceCount`, khu vuc xuc xac, tong diem va thanh thao tac duoi.
4. Nguoi dung co the bam nut quay lai de ve `Home`.

## Luong chon so luong xuc xac

1. Tu man hinh `Xuc Xac`, nguoi dung chon so luong tu 1 den 6 trong hang `DiceCount`.
2. Ung dung cap nhat `diceCount`.
3. Ket qua cu bi xoa de man hinh quay ve trang thai ban dau.
4. So luong vien xuc xac tren man hinh thay doi theo lua chon.

## Luong lac xuc xac

1. Nguoi dung bam nut `CHOI`.
2. Ung dung chuyen sang trang thai rolling.
3. Trong khi rolling, cac vien xuc xac thay doi mat lien tuc theo chu ky ngan.
4. Cac nut thao tac bi khoa de tranh doi setting/reset giua phien.
5. Sau thoi luong hoat hinh dang cau hinh, ung dung tao ket qua cuoi.
6. Ung dung hien cac mat xuc xac cuoi cung tren man hinh chinh.
7. Ung dung tinh tong diem cua tat ca vien xuc xac.
8. Sau mot khoang ngan, ung dung chuyen sang man hinh preview/ket qua.

## Luong ket qua/preview

1. Man hinh preview hien tieu de `Ket Qua`.
2. Khu vuc preview hien tong diem va cac vien xuc xac vua lac.
3. Nguoi dung co the bam `Chia se ket qua`.
4. Neu thiet bi ho tro native share, ung dung mo share sheet.
5. Neu khong ho tro native share, ung dung copy ket qua vao clipboard hoac hien thong bao.
6. Nguoi dung bam `Thu lai` de quay lai man hinh xuc xac.
7. Nguoi dung bam icon Home de ve trang chu.

## Luong tuy chinh

1. Tu man hinh `Xuc Xac`, nguoi dung bam nut tuy chinh.
2. Ung dung mo man hinh `Tuy chinh`.
3. Nguoi dung co the tang/giam `Thoi luong hoat hinh`.
4. Nguoi dung co the bam muc `Xuc Xac` de vao man hinh chon mau.
5. Nut quay lai dua nguoi dung ve man hinh `Xuc Xac`.

## Luong chon mau xuc xac

1. Tu man hinh `Tuy chinh`, nguoi dung bam muc `Xuc Xac`.
2. Ung dung hien danh sach cac mau/skin xuc xac.
3. Nguoi dung chon mot mau tam thoi.
4. Mau dang chon duoc danh dau bang trang thai selected/check.
5. Nguoi dung bam `Luu`.
6. Ung dung ap dung mau moi va quay lai man hinh `Tuy chinh`.

## Luong reset

1. Tu man hinh `Xuc Xac`, nguoi dung bam nut reset.
2. Ung dung xoa ket qua gan nhat.
3. Cac vien xuc xac quay ve trang thai mac dinh theo so luong da chon.
4. Tong diem quay ve trang thai chua co ket qua.

## Trang thai va du lieu

- `diceDuration`: thoi luong hoat hinh khi lac.
- `diceCount`: so luong vien xuc xac, tu 1 den 6.
- `diceStyleIndex`: mau/skin xuc xac dang ap dung.
- `diceTempStyleIndex`: mau/skin dang chon tam thoi tren man hinh label.
- `diceLastResults`: danh sach ket qua tung vien xuc xac gan nhat.
- `diceRunId`: ma phien chay, dung de huy timer/interval cu khi nguoi dung chuyen man hinh.

## Dieu kien can luu y

- `diceCount` can nam trong khoang 1 den 6.
- Moi ket qua vien xuc xac can nam trong khoang 1 den 6.
- Tong diem bang tong tat ca gia tri trong `diceLastResults`.
- Khi dang rolling, nen khoa nut setting, play, reset va chon so luong.
