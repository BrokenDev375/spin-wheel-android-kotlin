# Finger Flow

Folder screenshot tuong ung: `../screenshot/Finger`

Trang thai tai lieu: Draft - cho duyet.

## Muc dich chuc nang

`Finger` dai dien cho chuc nang `Chon Ngon Tay`. Chuc nang nay dung de chon ngau nhien mot nguoi chien thang trong nhom bang cach de nguoi dung cham ngon tay len man hinh, sau do ung dung dem nguoc va hien ket qua.

## Man hinh lien quan

- `../screenshot/Finger/FingerRandomHome.jpg`: man hinh vao chuc nang chon ngon tay.
- `../screenshot/Finger/NumberOfFinger.jpg`: man hinh/dropdown chon so ngon tay.
- `../screenshot/Finger/FingerRandomPlay.jpg`: man hinh dang cho/dem nguoc khi co diem cham.
- `../screenshot/Finger/FingerRandomResult1.jpg`: man hinh ket qua nhanh tren khu vuc cham.
- `../screenshot/Finger/FingerResult2.jpg`: man hinh ket qua cuoi cung.

## Luong vao chuc nang

1. Tu `Home`, nguoi dung bam card `Chon Ngon Tay`.
2. Ung dung mo man hinh `Chon`.
3. Header gom nut quay lai, tieu de va nut chon so ngon tay.
4. Nguoi dung co the quay lai Home bang nut mui ten.

## Luong chon so ngon tay

1. Tu man hinh `Chon`, nguoi dung bam nut dropdown o goc tren phai.
2. Ung dung hien danh sach so luong ngon tay co the chon.
3. Nguoi dung chon so ngon tay mong muon.
4. Ung dung luu so ngon tay dang chon va dong dropdown.
5. Lan choi tiep theo su dung so ngon tay vua chon.

## Luong choi chinh

1. Nguoi dung dat cac ngon tay len vung choi.
2. Ung dung ghi nhan vi tri cac diem cham tren man hinh.
3. Khi du dieu kien bat dau, ung dung hien dem nguoc.
4. Bo dem nguoc hien `2S`, sau do doi sang `1S`.
5. Khi het thoi gian dem nguoc, ung dung chon ngau nhien mot diem cham la nguoi thang.
6. Ung dung chuyen sang trang thai ket qua nhanh.

## Luong ket qua nhanh

1. Diem thang duoc gan nhan `THANG` va bieu tuong chien thang.
2. Diem thua duoc gan bieu tuong thua.
3. Trang thai ket qua nhanh hien trong thoi gian ngan de nguoi dung nhin thay vi tri thang/thua.
4. Sau do ung dung tu dong chuyen sang man hinh ket qua cuoi cung.

## Luong ket qua cuoi cung

1. Man hinh `Ket Qua` hien lai cac diem cham trong khung ket qua.
2. Diem chien thang tiep tuc duoc danh dau bang nhan `THANG`.
3. Nguoi dung co the bam `Chia se ket qua`.
4. Nguoi dung co the bam `Thu lai` de quay lai man hinh chon ngon tay va choi lai.
5. Nguoi dung co the bam icon Home de ve trang chu.

## Luong huy/quay lai

1. Neu nguoi dung bam nut quay lai khi dang choi hoac dang dem nguoc, ung dung huy phien chon hien tai.
2. Ung dung tang ma phien chay noi bo de tranh callback cu cua bo dem nguoc tiep tuc cap nhat UI.
3. Ung dung quay ve Home.

## Trang thai va du lieu

- `fingerCount`: so ngon tay nguoi dung chon.
- `fingerRunId`: ma phien hien tai, dung de huy cac timer cu khi nguoi dung thoat.
- `points`: danh sach/toa do cac diem cham tren man hinh.
- `winner`: diem cham duoc chon la nguoi thang.

## Dieu kien can luu y

- Can co it nhat mot diem cham hop le de co ket qua.
- Khi dang dem nguoc, viec quay lai can huy phien hien tai de tranh hien ket qua sai man hinh.
- Neu trien khai that tren mobile, can xu ly multi-touch de nhan dien dung tung ngon tay cua nguoi dung.
