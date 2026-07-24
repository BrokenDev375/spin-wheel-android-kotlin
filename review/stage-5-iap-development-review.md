# Review Chang 5 - IAP development integration

## Trang thai

Da duoc nguoi dung phe duyet de commit rieng.

## Da thay doi

- Them `platform/IapLauncher.kt` de app chi mo paywall qua API cua thu vien.
- Nut Crown tai Home va muc Premium tai Settings mo paywall cua lib.
- Xoa route/man hinh/ViewModel Payment mock va key luu goi mock.
- Home cap nhat premium khi resume de go native ad ngay sau khi mua thanh cong.
- Inter/native-inter va resume Ads tiep tuc dung premium gate da co.
- Khong con gia, trial, restore hay subscription gia lap trong UI app.

## Cau hinh development dang giu

- `defaultIapPremiumKey()`.
- `defaultIapPremiumWeeklyKey()`.
- `defaultIapPremiumMonthlyKey()`.
- `defaultIapPremiumYearlyKey()`.
- `public_license_key=MOCK_PUBLIC_LICENSE_KEY`.

## Chan production

Chua co product/subscription id va Google Play public license key that. Vi vay khong the:

- Xac minh gia/goi tra ve tu Google Play.
- Test mua, restore va entitlement bang tai khoan Play test.
- Xac nhan premium ben vung sau khi cai lai app.
- Phat hanh build co Billing production.

Truoc release phai thay toan bo key tren, upload build vao Play Console test track va
nghiem thu bang licensed tester.

## Da kiem tra

- `:app:testDebugUnitTest`: 37/37 pass.
- `:app:lintDebug`: pass, 0 error; con 31 warning va 3 hint.
- `:app:assembleDebug`: pass.
- `:app:assembleRelease`: pass; artifact van la `app-release-unsigned.apk`.
- `git diff --check`: pass.
- Khong con Payment mock/gia/trial/restore gia lap trong code app.
- Chua test runtime tren thiet bi vi moi truong khong co lenh `adb`.

## Can phe duyet

- Chap nhan phan tich hop development cua Chang 5.
- Da duoc phe duyet commit rieng sau khi build/test dat.
