# Spin Wheel Mockup

## Tong quan

Day la bo mockup giao dien cho ung dung `Spin Wheel & Random Tools`. Ung dung tap trung vao cac cong cu random/ra quyet dinh nhanh nhu quay banh xe, tung dong xu, chia doi, tao so ngau nhien, xuc xac, quay chai, lat the va cac tro choi lua chon ngau nhien khac.

Giao dien mockup cua app nam trong file:

- `test.html`

Tat ca anh chup man hinh mo ta giao dien/luong chuc nang nam trong folder:

- `screenshot/`

Moi folder con trong `screenshot/` dai dien cho mot nhom man hinh/chuc nang cua app. Moi nhom chuc nang co mot file `.md` tuong ung trong folder `feature/` de mo ta luong hoat dong.

## So luong chuc nang

App co 9 chuc nang chinh tren man hinh Home:

1. `Banh Xe`
2. `Chon Ngon Tay`
3. `Dong Xu`
4. `Doi`
5. `So Ngau Nhien`
6. `Ve`
7. `Quay Chai`
8. `Xuc Xac`
9. `Lat The`

Ngoai 9 chuc nang chinh, app con co nhom man hinh chung:

- Intro/onboarding.
- Home/dieu huong chuc nang.
- Cai dat chung.
- Doi ngon ngu.
- Thanh toan Pro.

Nhom man hinh chung nay duoc mo ta trong `feature/Home.md`.

## Cau truc mockup

- `test.html`: file mockup giao dien chinh, chua HTML/CSS/JavaScript cho cac man hinh va luong tuong tac cua app.
- `screenshot/`: folder chua anh chup cac man hinh cua app.
- `feature/`: folder chua cac file `.md` mo ta luong hoat dong theo tung folder/chuc nang.
- `review/`: folder chua cac file review theo tung phase implement.
- Cac file `.md` o thu muc goc: tai lieu tong quan, kien truc, checklist va implementation plan.

## Tai lieu ky thuat

- `architecture.md`: blueprint kien truc Android Kotlin + Compose, mo ta cau truc app, dieu huong, tang du lieu, ads/IAP, build/release va cac pattern nen dung khi hien thuc app tu mockup.
- `checklist.md`: checklist trien khai app Android moi dua tren `architecture.md`, dung de tick tung hang muc khi setup project, manifest, tai nguyen, ads, IAP, theme, build va release.

## Mapping chuc nang, screenshot va tai lieu

| Chuc nang / Nhom man hinh | Folder anh chup | File mo ta luong |
| --- | --- | --- |
| Home, Intro, Setting, Language, Payment | `screenshot/Home` | `feature/Home.md` |
| Banh Xe | `screenshot/WheelChair` | `feature/WheelChair.md` |
| Chon Ngon Tay | `screenshot/Finger` | `feature/Finger.md` |
| Dong Xu | `screenshot/Coin` | `feature/Coin.md` |
| Doi | `screenshot/Team` | `feature/Team.md` |
| So Ngau Nhien | `screenshot/Number` | `feature/Number.md` |
| Ve | `screenshot/Drawing` | `feature/Drawing.md` |
| Quay Chai | `screenshot/bottle` | `feature/bottle.md` |
| Xuc Xac | `screenshot/Dice` | `feature/Dice.md` |
| Lat The | `screenshot/Card` | `feature/Card.md` |

## Chi tiet folder anh chup

### `screenshot/Home`

Chua anh cho nhom man hinh chung cua app:

- Intro/onboarding.
- Home danh sach chuc nang.
- Setting.
- Language.
- Payment/Pro.

Tai lieu tuong ung: `feature/Home.md`.

### `screenshot/WheelChair`

Chua anh cho chuc nang `Banh Xe`:

- Danh sach banh xe.
- Tao/sua banh xe.
- Them nhieu option.
- Validate loi.
- AI generate.
- Man hinh quay banh xe.
- Cai dat banh xe.
- Chon bang mau.
- Ket qua quay.

Tai lieu tuong ung: `feature/WheelChair.md`.

### `screenshot/Finger`

Chua anh cho chuc nang `Chon Ngon Tay`:

- Man hinh choi.
- Chon so ngon tay.
- Dem nguoc.
- Ket qua nhanh.
- Ket qua cuoi cung.

Tai lieu tuong ung: `feature/Finger.md`.

### `screenshot/Coin`

Chua anh cho chuc nang `Dong Xu`:

- Man hinh tung xu.
- Cai dat.
- Chon mau tien xu.
- Ket qua.

Tai lieu tuong ung: `feature/Coin.md`.

### `screenshot/Team`

Chua anh cho chuc nang `Doi`:

- Danh sach danh sach/banh xe dung de chia doi.
- Chi tiet danh sach.
- Cai dat chia doi.
- Man hinh ghep doi.
- Preview ket qua.

Tai lieu tuong ung: `feature/Team.md`.

### `screenshot/Number`

Chua anh cho chuc nang `So Ngau Nhien`:

- Man hinh tao so.
- Trang thai sau khi random.
- Cai dat min/max/count/duplicates/duration.
- Lich su.
- Ket qua.

Tai lieu tuong ung: `feature/Number.md`.

### `screenshot/Drawing`

Chua anh cho chuc nang `Ve`:

- Danh sach bo lua chon.
- Chi tiet bo lua chon.
- Cai dat.
- Chon theme/mau the.
- Ket qua.

Tai lieu tuong ung: `feature/Drawing.md`.

### `screenshot/bottle`

Chua anh cho chuc nang `Quay Chai`:

- Man hinh quay chai.
- Cai dat.
- Chon mau chai.
- Ket qua.

Tai lieu tuong ung: `feature/bottle.md`.

### `screenshot/Dice`

Chua anh cho chuc nang `Xuc Xac`:

- Man hinh xuc xac voi 1 den 6 vien.
- Cai dat.
- Chon mau xuc xac.
- Preview/ket qua.

Tai lieu tuong ung: `feature/Dice.md`.

### `screenshot/Card`

Chua anh cho chuc nang `Lat The`:

- Man hinh lat the.
- Cai dat.
- Chon theme bo the.
- Ket qua.

Tai lieu tuong ung: `feature/Card.md`.

## Ghi chu

- Cac file `.md` hien dang o trang thai draft de co the tiep tuc sua theo review.
- Ten folder/file anh duoc giu theo hien trang trong project.
- `WheelChair` trong folder screenshot dang dai dien cho chuc nang `Banh Xe`.
- `Card` trong folder screenshot dang dai dien cho chuc nang `Lat The`.
