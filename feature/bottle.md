# bottle Flow

Folder screenshot tuong ung: `../screenshot/bottle`

Trang thai tai lieu: Draft - cho duyet.

## Muc dich chuc nang

`bottle` dai dien cho chuc nang `Quay Chai`. Chuc nang nay cho phep nguoi dung quay mot chai theo huong ngau nhien, dung ket qua goc xoay cuoi cung de xac dinh huong/nguoi duoc chon, tuy chinh thoi luong hoat hinh va doi mau chai.

## Man hinh lien quan

- `../screenshot/bottle/home.jpg`: man hinh quay chai.
- `../screenshot/bottle/setting.jpg`: man hinh tuy chinh quay chai.
- `../screenshot/bottle/label.jpg`: man hinh chon mau chai.
- `../screenshot/bottle/result.jpg`: man hinh ket qua sau khi quay chai.

## Luong vao chuc nang

1. Tu `Home`, nguoi dung bam card `Quay Chai`.
2. Ung dung mo man hinh `Quay Chai`.
3. Man hinh hien chai o khu vuc trung tam va thanh thao tac duoi.
4. Nguoi dung co the bam nut quay lai de ve `Home`.

## Luong quay chai

1. Tu man hinh `Quay Chai`, nguoi dung bam `BAT DAU`.
2. Ung dung chuyen chai sang trang thai spinning.
3. Chai quay trong thoi luong dang cau hinh.
4. Khi het thoi gian, ung dung random mot goc tu 0 den 359 do.
5. Chai dung lai tai goc random do.
6. Sau mot khoang ngan de nguoi dung thay vi tri dung, ung dung chuyen sang man hinh `Ket Qua`.

## Luong ket qua

1. Man hinh `Ket Qua` hien chai o goc xoay cuoi cung.
2. Nguoi dung co the bam `Chia se ket qua`.
3. Neu thiet bi ho tro native share, ung dung mo share sheet.
4. Neu khong ho tro native share, ung dung copy ket qua quay chai vao clipboard hoac hien thong bao.
5. Nguoi dung bam `Thu lai` de quay lai man hinh quay chai.
6. Nguoi dung bam icon Home de ve trang chu.

## Luong tuy chinh

1. Tu man hinh `Quay Chai`, nguoi dung bam nut tuy chinh.
2. Ung dung mo man hinh `Tuy chinh`.
3. Nguoi dung co the tang/giam `Thoi luong hoat hinh`.
4. Nguoi dung co the bam muc `Quay Chai` de vao man hinh chon mau chai.
5. Nut quay lai dua nguoi dung ve man hinh `Quay Chai`.

## Luong chon mau chai

1. Tu man hinh `Tuy chinh`, nguoi dung bam muc `Quay Chai`.
2. Ung dung hien danh sach cac mau/skin chai.
3. Nguoi dung chon mot mau tam thoi.
4. Mau dang chon duoc danh dau bang trang thai selected/check.
5. Nguoi dung bam `Luu`.
6. Ung dung ap dung mau chai moi va quay lai man hinh `Tuy chinh`.

## Luong reset

1. Tu man hinh `Quay Chai`, nguoi dung bam nut reset.
2. Ung dung huy phien quay hien tai neu co.
3. Goc chai duoc dua ve 0 do.
4. Man hinh quay ve trang thai ban dau.

## Trang thai va du lieu

- `bottleDuration`: thoi luong hoat hinh quay chai.
- `bottleStyleIndex`: mau/skin chai dang ap dung.
- `bottleTempStyleIndex`: mau/skin dang chon tam thoi tren man hinh label.
- `bottleLastAngle`: goc xoay cuoi cung cua chai.
- `bottleRunId`: ma phien chay, dung de huy timer cu khi nguoi dung chuyen man hinh/reset.

## Dieu kien can luu y

- Goc ket qua nen nam trong khoang 0 den 359 do.
- Khi chai dang quay, nen khoa nut setting/start/reset de tranh sai trang thai.
- Ket qua chia se co the gom goc xoay cuoi cung cua chai.
- Neu can xac dinh nguoi duoc chon theo vi tri thuc te, can map goc xoay voi vi tri nguoi choi quanh thiet bi.
