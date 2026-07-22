# Ke hoach dua Spin Wheel ve dung kien truc moi

> Tai lieu nay chia cong viec thanh tung chang doc lap de review va phe duyet.
> Chi bat dau mot chang khi nguoi dung da phe duyet chang do. Ket thuc moi chang phai
> dung lai, bao cao diff, ket qua test va cac rui ro con lai truoc khi sang chang tiep theo.

## 1. Muc tieu

- Dua project hien tai ve dung bo tai lieu `00_README.md` den `10_MIGRATE_ADS_TO_LIB.md`.
- Hoan thien cac phan dang thieu cua Phase 14 va Phase 15 ma khong lam hong cac feature da co.
- Tach thay doi thanh cac chang nho, co the build, test va review doc lap.
- Khong commit secret, keystore, password hoac khoa production vao Git.

## 2. Nguyen tac thuc hien

1. Moi lan chi co mot chang o trang thai `[~] Dang lam`.
2. Khong tu dong sang chang tiep theo khi chua co phe duyet.
3. Truoc moi chang: kiem tra branch, `git status` va baseline test.
4. Sau moi chang: build debug, chay test lien quan va kiem tra `git diff`.
5. Khong sua UI/logic ngoai pham vi cua chang dang duoc phe duyet.
6. Cac gia tri production nhu ad unit, IAP key va signing chi duoc them khi nguoi dung cung cap.
7. Moi chang nen co mot commit rieng sau khi nguoi dung review va yeu cau commit.

Quy uoc trang thai:

- `[ ]` Chua lam.
- `[~]` Dang lam.
- `[x]` Da hoan thanh va duoc phe duyet.
- `[!]` Bi chan, can thong tin hoac tai nguyen ben ngoai.

## 3. Baseline hien tai

- Branch lam viec: `phase-14-polish`.
- `testDebugUnitTest`: 22 test pass.
- `assembleDebug`: pass.
- `assembleRelease`: pass nhung APK dang la `app-release-unsigned.apk`.
- `lintDebug`: fail 1 loi Compose va co 25 warning.
- Worktree sach tai thoi diem lap ke hoach.

## 4. Thu tu cac chang

## [x] Chang 1 - On dinh Navigation va lint blocker

### Pham vi

- Go cac route/graph dang ky trung trong `AppNavHost`.
- Dam bao Bottle va Card mo man that, khong bi placeholder ghi de.
- Go cac placeholder da co graph that.
- Chi giu mot lan dang ky `teamNavGraph()`.
- Sua loi lint `StateFlow.value` trong composition tai Drawing.
- Don dependency Hilt Navigation bi khai bao trung.

### Khong lam trong chang nay

- Chua them Ads, IAP, Remote Config hoac thay luong Intro.
- Chua thay doi giao dien feature.

### Nghiem thu

- Moi tile Home mo dung graph/man hinh cua feature.
- Khong con route trung trong `AppNavHost`.
- `testDebugUnitTest`, `assembleDebug` va `lintDebug` pass.
- `assembleRelease` van pass.

### Diem dung phe duyet

- Bao cao danh sach route truoc/sau va ket qua 4 lenh kiem tra.
- Dung lai cho nguoi dung review truoc khi commit hoac sang Chang 2.

### Ket qua thuc hien - Da phe duyet

- Da go placeholder trung cua Coin, Number, Drawing, Dice, Bottle va Card.
- Da go lan dang ky thu hai cua `teamNavGraph()`.
- Tam feature graph top-level deu chi con mot lan dang ky.
- Da sua lint blocker tai `DrawingSpinScreen`.
- Da go dependency `hilt-navigation-compose` bi khai bao hai lan.
- `testDebugUnitTest`: 22/22 test pass.
- `lintDebug`: pass, 0 error (con 24 warning va 3 hint khong thuoc blocker Chang 1).
- `assembleDebug`: pass.
- `assembleRelease`: pass, van tao APK unsigned nhu baseline.
- Chua test bam tung tile tren may that vi khong co ADB device dang ket noi.
- Da duoc nguoi dung phe duyet ket qua va yeu cau commit.

## [x] Chang 2 - Nen Remote Config, AppStorage va gate Intro

### Pham vi

- Tao facade `firebase/Remote.kt`, uy quyen dung singleton cua lib.
- Tao `res/xml/config.xml` voi default cho placement, ratio/max, `positionIntrol`,
  `count_app_open` va `organic_number_not_guide`.
- Chuyen dang ky app defaults sang sau `super.onCreate()`.
- Tao `AppStorage` cho counter va trang thai nguon cai dat.
- Tao `InstallReferrerHelper` voi cache in-memory, SharedPreferences va Install Referrer.
- Thay `INTRO_DONE` don gian bang bang chan tri ads/organic trong `resolveStartRoute()`.
- Them unit test cho bang chan tri Intro va phan loai Install Referrer.

### Khong lam trong chang nay

- Chua hien native/inter ad tren cac man app.
- Chua thay Payment mock hoac Language UI.

### Nghiem thu

- Khong goi `FirebaseRemoteConfig.setDefaultsAsync()` truc tiep.
- App defaults duoc dang ky dung thu tu sau `super.onCreate()`.
- Counter tang dung mot lan cho moi lan mo chinh.
- Fallback khi Install Referrer chua resolve la ads campaign.
- Test du cac nhanh ads/organic theo `04_INTRO_CHECK.md`.
- Debug/release build pass.

### Diem dung phe duyet

- Trinh bang chan tri test va diff cua `MyApplication`, `MainActivity`, storage, Remote.
- Dung lai de nguoi dung phe duyet luong Intro.

### Ket qua thuc hien - Da phe duyet

- Da them facade `firebase/Remote.kt`, uy quyen sang `FirebaseRemoteConfigUtil.getInstance()`.
- Da them `res/xml/config.xml` cho placement enable, ratio/max, `positionIntrol`,
  `count_app_open` va `organic_number_not_guide`.
- Da chuyen dang ky app Remote Config defaults sang sau `super.onCreate()`.
- Da them `AppStorage` voi `goToHomeNumber`, `is_ads_campaign` va
  `ads_campaign_resolved`.
- Da them `InstallReferrerHelper` query async, cache in-memory + SharedPreferences,
  fallback ads campaign khi chua resolve hoac loi.
- Da thay `INTRO_DONE` trong route start bang bang chan tri ads/organic tai
  `MainActivity.resolveStartRoute()`.
- Da them unit test cho bang chan tri Intro va phan loai Install Referrer.
- `testDebugUnitTest`: 31/31 test pass.
- `lintDebug`: pass, 0 error (con 26 warning va 3 hint).
- `assembleDebug`: pass.
- `assembleRelease`: pass, van tao APK unsigned nhu baseline.
- Da duoc nguoi dung phe duyet ket qua va yeu cau commit truoc khi sang Chang 3.
- Commit Chang 2 trong commit rieng.

## [ ] Chang 3 - Xay loi Ads tai su dung

### Pham vi

- Tao package `advertisement/` gom `AdsViewModel`, `NativeAdSlot`, `AdManager`,
  `AdScenario`, `NativeInter`, `NativeAdsFull`, `AdPositions`.
- Tao `res/layout/ad_native_full.xml`.
- Dung `Admob.getInstance()` cua lib cho moi thao tac load/show.
- Them cache native theo Activity, bind mot lan, don key cu hon 7 ngay.
- Dam bao slot an khi load fail va khong ket shimmer.
- Dam bao callback dieu huong chi chay dung mot lan.
- Debug dung Google test ad unit; release doc ad unit tu `ads_config`.

### Khong lam trong chang nay

- Chua chen Ads vao toan bo man hinh.
- Chua nhap ad unit production neu chua duoc cung cap.

### Nghiem thu

- Khong co `AdLoader`, `AdRequest`, `InterstitialAd.load`, `AppOpenAd` hay
  `MobileAds.initialize` trong code app.
- Log Ads dung `println`, khong dung `android.util.Log`.
- `NativeAdSlot` xu ly thanh cong, fail va premium dung thiet ke.
- Unit test cho `AdScenario` va callback exactly-once pass.
- Debug/release build va lint pass.

### Diem dung phe duyet

- Review rieng loi Ads truoc khi chen vao UI.
- Dung lai de nguoi dung phe duyet placement va tan suat.

## [ ] Chang 4 - Tich hop Ads vao Home, Intro va dieu huong

### Pham vi

- Dat `NativeInterHost()` tai root `MainActivity` qua import.
- Them native placement vao Home va cac man da duoc phe duyet.
- Boc dieu huong tile Home qua `AdManager.showInter()`.
- Them chain inter -> native-inter -> navigate va fallback khi inter khong hien.
- Tich hop `positionIntrol` cho native inline/modal trong Intro.
- Dam bao nut Next/Continue cua Intro mo khi ad fill hoac fail, co timeout 5 giay.
- Premium bo toan bo Ads va khoang trong cua slot.

### Can nguoi dung phe duyet truoc

- Danh sach man hinh co native ad.
- Placement name, ratio va max/ngay.
- Co dung native-inter sau inter hay khong.

### Nghiem thu

- Home va Intro khong ket khi ad fail/no-fill.
- Dieu huong tiep tuc dung mot lan trong moi nhanh.
- Native khong reload vo han khi recompose/quay lai man.
- Debug test ad hien dung; premium khong con Ads.
- Test tren may that neu co ADB device.

### Diem dung phe duyet

- Bao cao log cho cac nhanh fill, fail, premium va fallback.
- Dung lai de nguoi dung test UI/UX Ads.

## [ ] Chang 5 - Thay Payment mock bang IAP that

### Pham vi

- Tao `platform/IapLauncher.kt` boc API mo IAP cua lib.
- Home va Settings deu mo duoc paywall cua lib.
- Go route, screen va ViewModel Payment mock khi khong con tham chieu.
- Thay `defaultIap*Key()` bang product/subscription id cua app khi duoc cung cap.
- Cau hinh premium gate va xac nhan premium tat Ads.
- Khong hien gia/subscription gia lap trong UI app.

### Du lieu can nguoi dung cung cap

- Product id mua mot lan neu co.
- Weekly/monthly/yearly subscription id.
- Google Play public license key.

### Nghiem thu

- Paywall mo duoc tu Home va Settings.
- Nut dong/restore/mua do lib xu ly, khong con Toast mock.
- Sau khi premium, resume/native/inter deu tat.
- Debug/release build va lint pass.

### Diem dung phe duyet

- Dung lai de nguoi dung test tai khoan Play test va xac nhan product mapping.

## [ ] Chang 6 - Dong bo Language va i18n

### Pham vi

- Dung `LanguageRouter.confirmLanguageSelection()` cho man ngon ngu rieng.
- Dong bo mot nguon luu language duy nhat giua lib va app.
- Ap locale va refresh UI ma khong khoi dong lai tu Splash.
- Tach text UI khoi Kotlin sang `strings_i18n.xml`.
- Them bo string mac dinh va cac locale duoc nguoi dung phe duyet.
- Xoa cac chuoi mock con sot lai nhu rate/share/restore.

### Can nguoi dung phe duyet truoc

- Danh sach ngon ngu se phat hanh o version dau.
- Ban dich nao la ban chinh thuc, ban dich nao chi la tam thoi.

### Nghiem thu

- Doi ngon ngu tu Settings cap nhat UI ngay.
- Mo lai app van giu ngon ngu da chon.
- Khong quay ve Splash khi doi language/night mode.
- Khong con text nguoi dung nhin thay bi hardcode trong cac man chinh.

### Diem dung phe duyet

- Nguoi dung review it nhat tieng Viet va tieng Anh tren cac man chinh.

## [ ] Chang 7 - Release config, asset va signing

### Pham vi

- Tach test ad unit cho debug va ad unit that cho release.
- Dien AppsFlyer, AdMob, Facebook va IAP config that khi duoc cung cap.
- Thay icon notification bang PNG alpha-only du 5 density.
- Thay anh preview IAP dung kich thuoc 365 x 174.
- Them monochrome launcher icon neu can.
- Cau hinh release signing bang file/local property khong commit secret.
- Bo `getResumeAdId()` test khoi release.
- Kiem tra ProGuard/R8 va Crashlytics mapping.

### Du lieu can nguoi dung cung cap

- AdMob app id va ad unit id production.
- Facebook app id/client token neu dung mediation.
- AppsFlyer key.
- Keystore, alias va password qua kenh local an toan.
- Asset branding chinh thuc.

### Nghiem thu

- Khong con `mock_`, test app id hay test ad unit trong release path.
- Tao duoc APK/AAB release da ky.
- `assembleRelease` va lint pass.
- Khong commit keystore/password/secret vao Git.

### Diem dung phe duyet

- Bao cao checksum/ten artifact va ket qua kiem tra signing, khong hien secret.
- Dung lai truoc khi cai/phat hanh artifact.

## [ ] Chang 8 - QA feature, UI polish va release candidate

### Pham vi

- Test end-to-end Intro -> Home -> tung feature -> Settings/IAP/Language.
- So sanh UI voi screenshot cua Wheel, Finger, Coin, Team, Number, Drawing, Bottle,
  Dice va Card.
- Sua overflow, overlap, back stack, state restoration va persistence.
- Chuyen business logic con nam trong Composable ve ViewModel khi can.
- Bo sung test cho Coin, Number, Navigation, Intro, Remote, Ads va premium.
- Test debug va release tren may that; doc crash/log Ads.

### Nghiem thu

- Tat ca feature chinh mo, thao tac, luu va khoi phuc du lieu dung.
- Rotate/background/foreground khong lam mat state quan trong.
- `testDebugUnitTest`, `lintDebug`, `assembleDebug`, `assembleRelease` pass.
- APK/AAB release da ky cai va chay duoc tren may that.
- Khong co crash main flow, shimmer ket hoac dieu huong lap.

### Diem dung phe duyet

- Nguoi dung review ban release candidate va quyet dinh phat hanh.

## 5. Bang theo doi phe duyet

| Chang | Trang thai | Phe duyet bat dau | Phe duyet ket qua | Commit |
|---|---|---|---|---|
| 1. Navigation + lint | Da hoan thanh | Da phe duyet | Da phe duyet | Da |
| 2. Remote + Intro gate | Da hoan thanh | Da phe duyet | Da phe duyet | Da |
| 3. Ads core | Chua lam | Da phe duyet | Chua | Chua |
| 4. Ads UI/flow | Chua lam | Chua | Chua | Chua |
| 5. IAP | Chua lam | Chua | Chua | Chua |
| 6. Language + i18n | Chua lam | Chua | Chua | Chua |
| 7. Release config | Chua lam | Chua | Chua | Chua |
| 8. QA + release candidate | Chua lam | Chua | Chua | Chua |

## 6. Mau bao cao sau moi chang

```text
Chang: <so va ten>
Trang thai: Hoan thanh / Bi chan

Da thay doi:
- ...

Da kiem tra:
- Lenh: ...
- Ket qua: ...

Con lai / rui ro:
- ...

Can phe duyet:
- Chap nhan ket qua chang nay?
- Cho phep commit?
- Cho phep bat dau chang tiep theo?
```

## 7. Buoc tiep theo

Sau khi commit **Chang 2 - Nen Remote Config, AppStorage va gate Intro**, bat dau
**Chang 3 - Xay loi Ads tai su dung** theo phe duyet cua nguoi dung.
