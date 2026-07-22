# Review Chang 4 - Ads UI/flow

## Trang thai

Da duoc nguoi dung phe duyet. Cho tao commit rieng.

## Da thay doi

- Dat `NativeInterHost()` tai root `MainActivity`.
- Hien `native_home` o dau grid Home; premium/config tat Ads khong de lai item hoac
  khoang trong.
- Boc click tile Home qua `inter_home`, co chain `native_inter_home` va callback
  dieu huong exactly-once.
- Khoa click lap trong luc chuoi Ads dang xu ly.
- Doc `positionIntrol` mot lan cho moi phien Intro:
  - `1..9`: native inline tai slide tuong ung.
  - `11/22/33/44`: native modal sau slide tuong ung.
- Mo nut Next/Continue khi native inline load thanh cong, fail, bi tat/khong co unit,
  hoac het timeout 5 giay.
- Native modal timeout sau 5 giay neu SDK khong callback, sau do tiep tuc luong.
- Them test cho mapping inline/modal cua `IntroAdPositions`.

## Cau hinh dang dung

- `native_home_enable=true`.
- `inter_home`: ratio 1, max 20/ngay.
- `native_inter_home`: ratio 1, max 20/ngay.
- `native_intro1..4_enable=true`.
- `native_inter_intro`: ratio 1, max 20/ngay.
- `positionIntrol=[[1],[2],[3],[4]]`: moi phien chon ngau nhien mot slide co native inline.
- Debug dung Google test ad unit; release tiep tuc doc `ads_config`.

## Da kiem tra

- `:app:testDebugUnitTest`: 37/37 pass.
- `:app:lintDebug`: pass, 0 error; con 30 warning va 3 hint.
- `:app:assembleDebug`: pass.
- `:app:assembleRelease`: pass; artifact van la `app-release-unsigned.apk`.
- `git diff --check`: pass.
- Khong co API GMA load truc tiep hoac `android.util.Log` trong code app.

## Chua kiem tra duoc

- Khong co `adb` trong moi truong, nen chua test runtime cac nhanh ad fill, no-fill,
  premium va chain inter/native-inter tren thiet bi that.

## Phe duyet

- Nguoi dung da chap nhan ket qua Chang 4.
- Duoc phep tao commit rieng cho Chang 4.
