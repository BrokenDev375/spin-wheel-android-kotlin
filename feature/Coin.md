# Coin Flow

Folder screenshot tuong ung: `../screenshot/Coin`

Trang thai tai lieu: Draft - cho duyet.

## Muc dich chuc nang

`Coin` dai dien cho chuc nang `Dong Xu`. Chuc nang nay cho phep nguoi dung tung/xoay dong xu de nhan ket qua ngau nhien giua hai mat, tuy chinh thoi luong hoat hinh va doi mau/mau hien thi cua dong xu.

## Man hinh lien quan

- `../screenshot/Coin/CoinHome.jpg`: man hinh tung dong xu.
- `../screenshot/Coin/CoinSetting.jpg`: man hinh tuy chinh dong xu.
- `../screenshot/Coin/CoinLabel.jpg`: man hinh chon mau/mau tien xu.
- `../screenshot/Coin/CoinResult.jpg`: man hinh ket qua sau khi tung xu.

## Luong vao chuc nang

1. Tu `Home`, nguoi dung bam card `Dong Xu`.
2. Ung dung mo man hinh `Dong Xu`.
3. Man hinh hien khu vuc diem `HEADS - TAILS`, dong xu chinh va thanh thao tac duoi.
4. Nguoi dung co the bam nut quay lai de ve `Home`.

## Luong tung dong xu

1. Tu man hinh `Dong Xu`, nguoi dung bam `BAT DAU`.
2. Dong xu bat dau chay hoat hinh xoay/lac.
3. Hoat hinh dien ra theo thoi luong dang cau hinh.
4. Khi het thoi gian, ung dung chuyen sang man hinh `Ket Qua`.
5. Man hinh ket qua hien dong xu voi mat ket qua cuoi cung.

## Luong ket qua

1. Sau khi tung xu xong, ung dung hien man hinh `Ket Qua`.
2. Nguoi dung co the bam `Chia se ket qua`.
3. Nguoi dung co the bam `Thu lai` de quay lai man hinh tung dong xu.
4. Nguoi dung co the bam icon Home de ve trang chu.

## Luong tuy chinh

1. Tu man hinh `Dong Xu`, nguoi dung bam icon tuy chinh.
2. Ung dung mo man hinh `Tuy chinh`.
3. Nguoi dung co the tang/giam `Thoi luong hoat hinh`.
4. Nguoi dung co the bam muc `Mau tien xu` de vao man hinh chon mau.
5. Nut quay lai dua nguoi dung ve man hinh `Dong Xu`.

## Luong chon mau tien xu

1. Tu man hinh `Tuy chinh`, nguoi dung bam `Mau tien xu`.
2. Ung dung hien danh sach cac mau/skin dong xu.
3. Nguoi dung chon mot mau tam thoi.
4. Mau dang chon duoc danh dau bang trang thai selected/check.
5. Nguoi dung bam `Luu`.
6. Ung dung luu mau moi va quay lai man hinh `Tuy chinh`.

## Luong reset

1. Tu man hinh `Dong Xu`, nguoi dung bam nut reset.
2. Ung dung dua man hinh ve trang thai ban dau.
3. Diem/ket qua hien tai duoc lam moi theo trang thai mac dinh cua man hinh.

## Trang thai va du lieu

- `coinDuration`: thoi luong hoat hinh khi tung xu.
- `coinLabelIndex`: mau/skin dong xu dang duoc ap dung.
- `tempCoinLabelIndex`: mau/skin dang chon tam thoi tren man hinh label.
- Ket qua tung xu duoc hien tren man hinh ket qua; trong mockup hien tai chua thay luu lich su lau dai.

## Dieu kien can luu y

- Khi dong xu dang quay, can khoa cac thao tac co the lam sai trang thai ket qua.
- Neu ung dung can thong ke diem `HEADS` va `TAILS`, can cap nhat bo dem sau moi lan tung thanh cong.
- Neu native share khong kha dung, nen fallback sang copy ket qua hoac hien thong bao.
