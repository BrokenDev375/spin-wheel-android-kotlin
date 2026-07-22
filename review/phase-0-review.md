# Phase 0 Review - Xac nhan yeu cau va tai lieu

Trang thai: Hoan thanh - da duoc user phe duyet.

## Muc tieu Phase 0

Phase 0 dung de xac nhan tai lieu hien co da du de bat dau implement app Android hay chua, dong thoi chot cac quyet dinh nen thong nhat truoc khi code.

## Tai lieu da review

- `Readme.md`
- `implementation-plan.md`
- `architecture.md`
- `checklist.md`
- `feature/Home.md`
- `feature/WheelChair.md`
- `feature/Finger.md`
- `feature/Coin.md`
- `feature/Team.md`
- `feature/Number.md`
- `feature/Drawing.md`
- `feature/bottle.md`
- `feature/Dice.md`
- `feature/Card.md`
- `test.html`
- `screenshot/`

## Ket qua review

### 1. Tai lieu tong quan

- `Readme.md` da mo ta dung tong quan app.
- `Readme.md` da ghi ro mockup giao dien nam trong `test.html`.
- `Readme.md` da co mapping folder screenshot voi file flow markdown tuong ung.
- `Readme.md` da nhac den `architecture.md` va `checklist.md`.

Ket luan: Dat.

### 2. So luong chuc nang

App co 9 chuc nang chinh tren Home:

1. `Banh Xe`
2. `Chon Ngon Tay`
3. `Dong Xu`
4. `Doi`
5. `So Ngau Nhien`
6. `Ve`
7. `Quay Chai`
8. `Xuc Xac`
9. `Lat The`

Nhom man hinh chung gom:

- Intro/onboarding.
- Home.
- Setting.
- Language.
- Payment/Pro.

Ket luan: Dat.

### 3. Mapping screenshot va file markdown

Tat ca folder trong `screenshot/` da co file `.md` tuong ung trong folder `feature/`:

| Folder screenshot | File flow |
| --- | --- |
| `screenshot/Home` | `feature/Home.md` |
| `screenshot/WheelChair` | `feature/WheelChair.md` |
| `screenshot/Finger` | `feature/Finger.md` |
| `screenshot/Coin` | `feature/Coin.md` |
| `screenshot/Team` | `feature/Team.md` |
| `screenshot/Number` | `feature/Number.md` |
| `screenshot/Drawing` | `feature/Drawing.md` |
| `screenshot/bottle` | `feature/bottle.md` |
| `screenshot/Dice` | `feature/Dice.md` |
| `screenshot/Card` | `feature/Card.md` |

Ket luan: Dat.

### 4. Mockup HTML

`test.html` co cac ham man hinh chinh:

- `showHomeScreen`
- `showFingerRandomHome`
- `showNumberHome`
- `showBottleHome`
- `showDiceHome`
- `showCoinHome`
- `showPaymentScreen`
- `showSettingScreen`
- `showLanguageScreen`
- `showDrawingHome`
- `showTeamHome`
- `showWheelScreen`
- `showFlipHome`

Da xac nhan cac sua doi gan day:

- Nut `Thu lai` cua ket qua `Banh Xe` quay ve dung banh xe vua chon.
- Luong `Doi` sau khi het animation doi nut thanh `Next`.
- Bam `Next` cua `Doi` chuyen sang man preview ket qua.

Ket luan: Dat.

### 5. Architecture va checklist

- `architecture.md` la blueprint Android Kotlin + Compose + `base-application`.
- `checklist.md` la checklist trien khai Android moi theo blueprint.
- Hai file nay dang o dang tai lieu dung chung, khong phai tai lieu flow rieng cua Spin Wheel.

Ket luan: Dat, co the dung lam nen tang ky thuat cho implement.

## Dieu da sua trong Phase 0

- Sua typo trong `feature/Card.md`: `nguo i` thanh `nguoi`.

## Quyet dinh user da chot

1. Implement app bang Android Kotlin + Jetpack Compose theo `architecture.md`: Dong y.
2. `base-application`, Ads va IAP: tich hop ngay tu dau.
3. Luu danh sach `Wheel`: dung Room.
4. App name chinh thuc: `Spin Wheel`.
5. Language/i18n giai doan dau: chi mockup UI, chua can i18n that.
6. Muc tieu UI: uu tien flow dung va UI gan screenshot truoc, pixel polish sau trong Phase 15.
7. Neu thieu tai lieu/quyet dinh trong qua trinh implement: hoi user truoc khi tu quyet.

## Diem da chot bo sung

1. Android package/applicationId: `com.vga.spinwheel`.
2. Payment/IAP ban dau: tich hop khung `base-application` tu dau, dung mock/test config truoc; IAP that cau hinh sau khi co product id/license key.

## De xuat mac dinh da duoc phe duyet

1. Android package/applicationId: `com.vga.spinwheel`.
2. Payment/IAP: tich hop khung `base-application` tu dau, dung mock/test config truoc; IAP that de cau hinh sau khi co key.

## Ket luan Phase 0

Tai lieu hien tai da du de bat dau Phase 1.

Da tick `[x]` Phase 0 trong `implementation-plan.md`.
