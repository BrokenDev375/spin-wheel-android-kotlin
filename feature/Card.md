# Card Flow

Folder screenshot tuong ung: `../screenshot/Card`

Trang thai tai lieu: Draft - cho duyet.

## Muc dich chuc nang

`Card` dai dien cho chuc nang `Lat The`. Chuc nang nay cho phep nguoi dung xao tron mot tap the, lat tung the de tim the thang/thua, tuy chinh so luong the, so nguoi chien thang, thoi luong hoat hinh va theme bo the.

## Man hinh lien quan

- `../screenshot/Card/Home.jpg`: man hinh lat the.
- `../screenshot/Card/Setting.jpg`: man hinh tuy chinh lat the.
- `../screenshot/Card/Label.jpg`: man hinh chon theme/bo the.
- `../screenshot/Card/result.jpg`: man hinh ket qua sau khi lat het the.

## Luong vao chuc nang

1. Tu `Home`, nguoi dung bam card `Lat The`.
2. Ung dung mo man hinh `Lat The`.
3. Man hinh hien khu vuc `The Thang` va grid cac the dang up.
4. Thanh thao tac duoi gom nut tuy chinh, nut xao tron va nut reset.
5. Nguoi dung co the bam nut quay lai de ve `Home`.

## Luong khoi tao van choi

1. Khi vao man hinh `Lat The`, ung dung doc cau hinh hien tai.
2. Ung dung tao danh sach the theo `totalCards`.
3. Ung dung random vi tri cac the thang theo so luong `winners`.
4. Cac the con lai la the thua.
5. Tat ca the trong grid duoc hien o mat sau/up.

## Luong xao tron

1. Tu man hinh `Lat The`, nguoi dung bam `NHAN DE XAO TRON`.
2. Ung dung tao lai vi tri the thang va the thua.
3. Grid the duoc reset ve trang thai chua lat.
4. Nguoi dung co the bat dau lat tung the.

## Luong lat the

1. Nguoi dung bam vao mot the trong grid.
2. Neu the chua lat, ung dung lat the do sang mat ket qua.
3. Neu the la the thang, hien icon/text thang theo theme dang chon.
4. Neu the la the thua, hien icon/text thua theo theme dang chon.
5. Moi lan lat thanh cong, ung dung tang bo dem so the da lat.
6. Khi tat ca the da duoc lat, ung dung doi mot khoang ngan.
7. Ung dung chuyen sang man hinh `Ket Qua`.

## Luong ket qua

1. Man hinh `Ket Qua` hien tat ca the o trang thai da lat.
2. The thang va the thua giu dung mau/icon/text theo theme dang chon.
3. Nguoi dung co the bam `Chia se ket qua`.
4. Nguoi dung bam `Thu lai` de tao van lat the moi.
5. Nguoi dung bam icon Home de ve trang chu.

## Luong tuy chinh

1. Tu man hinh `Lat The`, nguoi dung bam nut tuy chinh.
2. Ung dung mo man hinh `Tuy chinh`.
3. Nguoi dung co the tang/giam `Thoi luong hoat hinh`.
4. Nguoi dung co the tang/giam `Total Cards`.
5. Nguoi dung co the tang/giam `So nguoi chien thang`.
6. Nguoi dung co the bam `Card Deck Theme` de vao man hinh chon theme.
7. Cac thay doi cau hinh duoc luu lai vao storage.
8. Nut quay lai dua nguoi dung ve man hinh `Lat The`.

## Luong chon theme bo the

1. Tu man hinh `Tuy chinh`, nguoi dung bam `Card Deck Theme`.
2. Ung dung hien danh sach theme/bo the.
3. Moi theme hien preview cap the thang va the thua.
4. Nguoi dung chon mot theme tam thoi.
5. Theme dang chon duoc danh dau selected.
6. Nguoi dung bam `Luu`.
7. Ung dung ap dung theme moi va quay lai man hinh `Tuy chinh`.

## Luong reset

1. Tu man hinh `Lat The`, nguoi dung bam nut reset.
2. Ung dung khoi tao lai van choi theo cau hinh hien tai.
3. Tat ca the quay ve trang thai chua lat.
4. Vi tri the thang duoc random lai.

## Trang thai va du lieu

- `flipSettings`: cau hinh lat the, gom:
  - `duration`: thoi luong hoat hinh.
  - `totalCards`: tong so the.
  - `winners`: so the/nguoi chien thang.
  - `themeIndex`: theme dang ap dung.
- `flipTempSettings`: cau hinh tam thoi tren man hinh setting/label.
- `flipCardsState`: danh sach boolean xac dinh the nao la the thang.
- `flipRunId`: ma phien chay.
- `isFlipPlaying`: trang thai van choi dang cho phep lat the.
- `flippedCount`: so the da duoc lat.

## Dieu kien can luu y

- `totalCards` can nam trong khoang 2 den 12.
- `winners` can lon hon hoac bang 1 va nho hon `totalCards`.
- Khi tang/giam `totalCards`, can dam bao `winners` khong vuot qua `totalCards - 1`.
- Chi nen chuyen sang man hinh ket qua sau khi tat ca the da duoc lat.
