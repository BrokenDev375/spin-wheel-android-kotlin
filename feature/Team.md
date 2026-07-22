# Team Flow

Folder screenshot tuong ung: `../screenshot/Team`

Trang thai tai lieu: Draft - cho duyet.

## Muc dich chuc nang

`Team` dai dien cho chuc nang `Doi`. Chuc nang nay dung danh sach nguoi/option da tao trong banh xe de chia thanh cac doi ngau nhien. Nguoi dung co the tao danh sach moi, chon danh sach da co, cau hinh so thanh vien moi doi va bat dau ghep doi.

## Man hinh lien quan

- `../screenshot/Team/TeamHome.jpg`: man hinh danh sach cac banh xe/danh sach co the dung de tao doi.
- `../screenshot/Team/SpecifcTeamHome.jpg`: man hinh chi tiet mot danh sach truoc khi ghep doi.
- `../screenshot/Team/SpecificteamSetting.jpg`: man hinh tuy chinh cau hinh ghep doi.
- `../screenshot/Team/TeamPlay.jpg`: man hinh dang ghep doi.
- `../screenshot/Team/Preview.jpg`: man preview ket qua cac doi sau khi bam `Next`.

## Luong vao chuc nang

1. Tu `Home`, nguoi dung bam card `Doi`.
2. Ung dung mo man hinh `Doi`.
3. Neu chua co danh sach nao, ung dung hien trang thai rong va goi y them moi.
4. Neu da co danh sach trong storage, ung dung hien cac card danh sach.
5. Moi card hien ten danh sach va so luong option/thanh vien.
6. Nguoi dung co the bam nut quay lai de ve `Home`.

## Luong tao danh sach moi

1. Tu man hinh `Doi`, nguoi dung bam `Tao Banh Xe moi`.
2. Ung dung mo form them/sua banh xe dung chung voi chuc nang `Banh Xe`.
3. Nguoi dung nhap ten danh sach.
4. Nguoi dung nhap cac thanh vien vao danh sach muc.
5. Nguoi dung co the them tung muc hoac them nhieu muc cung luc.
6. Nguoi dung bam `Luu`.
7. Ung dung validate ten danh sach va toi thieu 2 muc hop le.
8. Neu hop le, danh sach duoc luu vao storage.
9. Ung dung quay lai man hinh `Doi`.

## Luong quan ly danh sach

1. Tu man hinh `Doi`, nguoi dung bam menu ba cham tren mot card danh sach.
2. Ung dung hien menu thao tac.
3. Nguoi dung co the chon `Sua` de mo form cap nhat danh sach.
4. Nguoi dung co the chon `Nhan ban` de tao ban sao cua danh sach.
5. Nguoi dung co the chon `Xoa` de xoa danh sach sau khi xac nhan.
6. Sau moi thao tac, man hinh danh sach duoc cap nhat lai.

## Luong tao danh sach bang AI

1. Tu man hinh `Doi`, nguoi dung bam `Trinh tao AI`.
2. Ung dung mo man hinh nhap chu de.
3. Nguoi dung nhap chu de/danh sach mong muon.
4. Nguoi dung bam nut tao.
5. Neu input rong, ung dung hien thong bao yeu cau nhap noi dung.
6. Neu hop le, ung dung tao danh sach goi y va quay lai man hinh `Doi`.

## Luong vao chi tiet danh sach

1. Tu man hinh `Doi`, nguoi dung bam vao mot card danh sach.
2. Ung dung mo man hinh chi tiet danh sach.
3. Man hinh hien ten danh sach va cac thanh vien theo thu tu.
4. Thanh thao tac duoi gom:
   - Nut tuy chinh.
   - Nut `NHAN DE GHEP NOI`.
   - Nut reset/quay lai tu dau.
5. Nguoi dung bam nut quay lai de tro ve man hinh danh sach `Doi`.

## Luong tuy chinh ghep doi

1. Tu man hinh chi tiet danh sach, nguoi dung bam nut tuy chinh.
2. Ung dung mo man hinh `Tuy chinh`.
3. Nguoi dung co the tang/giam `Cac muc cua nhom`.
4. Gia tri nay quy dinh so thanh vien trong moi doi.
5. Nguoi dung co the tang/giam `Thoi luong hoat hinh`.
6. Nguoi dung co the bat/tat `Gieo hat cua nhom`.
7. Nut quay lai dua nguoi dung ve man hinh chi tiet danh sach.

## Luong ghep doi

1. Tu man hinh chi tiet danh sach, nguoi dung bam `NHAN DE GHEP NOI`.
2. Ung dung lay danh sach thanh vien hien tai.
3. Ung dung tinh so doi dua theo so thanh vien moi doi.
4. Trong thoi gian hoat hinh, danh sach thanh vien duoc xao tron lien tuc.
5. Cac bang `Team 1`, `Team 2`, ... duoc cap nhat lien tuc trong qua trinh xao tron.
6. Khi het thoi luong hoat hinh, ung dung dung xao tron va giu lai ket qua cuoi.
7. Nut chinh doi tu `NHAN DE GHEP NOI` thanh `Next`.
8. Nguoi dung bam `Next`.
9. Ung dung chuyen sang man preview ket qua.

## Luong preview ket qua

1. Man preview hien tieu de `Ket Qua`.
2. Man preview hien khung ket qua lon chua cac bang doi dang cuon ngang.
3. Moi bang co header mau cam dang `Team 1`, `Team 2`, ... va danh sach thanh vien ben duoi.
4. Nguoi dung co the bam `Chia se ket qua` de chia se/copy ket qua chia doi.
5. Nguoi dung co the bam `Thu lai` de chay lai luong ghep doi cho danh sach dang chon.
6. Nguoi dung co the bam icon Home de ve trang chu.

## Luong reset

1. Tu man hinh chi tiet danh sach hoac man hinh play, nguoi dung bam nut reset.
2. Ung dung quay lai trang thai chi tiet danh sach ban dau.
3. Ket qua ghep doi hien tai bi loai bo khoi man hinh.

## Trang thai va du lieu

- Danh sach doi dung chung du lieu `customWheels` voi chuc nang `Banh Xe`.
- Moi danh sach gom:
  - `name`: ten danh sach.
  - `items`: danh sach thanh vien.
- `teamGroupItems`: so thanh vien toi da trong moi doi.
- `teamAnimationDuration`: thoi luong xao tron/ghep doi.
- `teamSeedEnabled`: trang thai bat/tat gieo hat cua nhom.

## Dieu kien can luu y

- Can co danh sach thanh vien hop le truoc khi ghep doi.
- Neu so thanh vien khong chia het cho so thanh vien moi doi, doi cuoi co the it thanh vien hon.
- Khi dang chay hoat hinh ghep doi, can tranh thao tac lam thay doi danh sach goc.
- Chuc nang nay phu thuoc vao danh sach duoc tao tu form banh xe dung chung, nen thay doi schema `customWheels` se anh huong ca `Banh Xe`, `Doi` va `Ve`.
