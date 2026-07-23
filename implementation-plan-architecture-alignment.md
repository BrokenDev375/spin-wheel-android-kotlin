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

## [x] Chang 3 - Xay loi Ads tai su dung

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

### Ket qua thuc hien - Da phe duyet

- Da them package `advertisement/` gom `AdsViewModel`, `NativeAdSlot`, `AdManager`,
  `AdScenario`, `NativeInter`, `NativeAdsFull`, `AdPositions` va `OnceAction`.
- Da them `res/layout/ad_native_full.xml` cho native full-screen/modal.
- Native load/inter show deu di qua `Admob.getInstance()` cua lib.
- Native slot co cache theo `AdsViewModel`, bind mot lan trong `AndroidView.factory`,
  an slot khi load fail va khong retry/shimmer vo han.
- `AdScenario` dem ratio/max theo ngay va don key cu hon 7 ngay.
- `AdManager.showInter()` co fallback native-inter va callback final exactly-once.
- Debug placement tra Google test unit qua `Remote.adUnit()`; release doc tu `ads_config`.
- Da bo sung placement app vao `default_ads_config.json` va `res/xml/config.xml`.
- Khong tim thay `AdLoader`, `AdRequest`, `InterstitialAd.load`, `AppOpenAd`,
  `MobileAds.initialize` hoac `android.util.Log` trong code app.
- `testDebugUnitTest`: 36/36 test pass.
- `lintDebug`: pass, 0 error (con 30 warning va 3 hint).
- `assembleDebug`: pass.
- `assembleRelease`: pass, van tao APK unsigned nhu baseline.
- Chua chen Ads vao man hinh that; phan do thuoc Chang 4.
- Da duoc nguoi dung phe duyet ket qua va yeu cau commit.
- Commit Chang 3 trong commit rieng.

## [x] Chang 4 - Tich hop Ads vao Home, Intro va dieu huong

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

### Ket qua thuc hien - Da phe duyet

- Da dat `NativeInterHost()` tai root `MainActivity` qua import.
- Da them `native_home` vao Home voi key on dinh; premium/config tat Ads thi khong tao
  item va khong de lai khoang trong.
- Da boc dieu huong tile Home qua `AdManager.showInter("inter_home")`; khoa double-click
  trong luc cho chuoi inter -> native-inter -> navigate.
- Da tich hop `positionIntrol`: 1..9 la native inline theo slide, 11/22/33/44 la
  native modal sau slide tuong ung.
- Nut Next/Continue cua Intro mo khi native load thanh cong, fail, bi tat/khong co unit,
  hoac timeout sau 5 giay.
- Native full/modal tu dong fallback sau timeout 5 giay va callback dieu huong van exactly-once.
- Da them unit test cho mapping inline/modal cua `IntroAdPositions`.
- `testDebugUnitTest`: 37/37 test pass.
- `lintDebug`: pass, 0 error (con 30 warning va 3 hint).
- `assembleDebug`: pass.
- `assembleRelease`: pass, van tao APK unsigned nhu baseline.
- Chua test runtime fill/fail/premium tren may that vi moi truong khong co lenh `adb`.
- Da duoc nguoi dung phe duyet ket qua va yeu cau commit rieng.

## [x] Chang 5 - Thay Payment mock bang IAP that

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

### Ket qua thuc hien - Da phe duyet (development)

- Da tao `platform/IapLauncher.kt` boc API mo paywall cua lib va truy van premium.
- Nut Crown tai Home va muc `Nang cap Premium` tai Settings deu mo paywall cua lib.
- Da go route `Payment`, `PaymentScreen`, `PaymentViewModel`, `PaymentPlan` va key luu
  payment plan mock; khong con gia/trial/restore gia lap trong UI app.
- Home kiem tra lai `IAPUtils.isPremium()` khi resume; sau khi premium, native Home bi
  go khoi composition va khong de lai khoang trong. Inter/native-inter van duoc chan boi
  premium gate cua `Remote`, resume Ads van duoc chan boi `MyApplication.enableAdsResume()`.
- Tam giu `defaultIapPremiumKey()`, `defaultIapPremiumWeeklyKey()`,
  `defaultIapPremiumMonthlyKey()`, `defaultIapPremiumYearlyKey()` va
  `MOCK_PUBLIC_LICENSE_KEY` theo phe duyet cua nguoi dung.
- **Chan production:** chua co product/subscription id va Google Play public license key
  that; khong the nghiem thu mua, restore, gia, entitlement hoac phat hanh Billing that.
  Cac gia tri nay bat buoc phai duoc thay va test bang tai khoan Play test truoc release.
- `testDebugUnitTest`: 37/37 test pass.
- `lintDebug`: pass, 0 error (con 31 warning va 3 hint).
- `assembleDebug`: pass.
- `assembleRelease`: pass, van tao APK unsigned nhu baseline.
- Chua test mo/paywall runtime tren may that vi moi truong khong co lenh `adb`.
- Da duoc nguoi dung phe duyet ket qua development va yeu cau commit rieng.
- Correction sau review trong Chang 6: `MainActivity` duoc chuyen sang `AppCompatActivity`
  de dung nen `FragmentActivity/AppCompatActivity` theo yeu cau cua lib Language/IAP; `IapLauncher.open()`
  duoc can theo pattern chi tim `Activity` va goi thang
  `NativeCodecSnowFlakeCortexAI.nativeAiStartIapActivity(activity)`.

## [x] Chang 6 - Dong bo Language va i18n

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

### Ket qua thuc hien - Da phe duyet

- Da sua correction lien quan Chang 5: `MainActivity : AppCompatActivity`, khong con
  `ComponentActivity`; `IapLauncher.open()` mo paywall lib theo pattern tham khao.
- Da them `LocaleHelper.updateLocale()` de `MainActivity` ap lai language mirror khi tao UI.
- `MyApplication.notifyLanguageSaved()` chi dong bo language ve `AppStorage`; lib tu ap locale
  khi nguoi dung chon language.
- `AppStorage` la mirror language duy nhat cua app; `LanguageViewModel` doc selected code
  tu `AppStorage`.
- Settings mo man Language co san cua lib bang
  `LanguageActivity.start(activity, MainActivity::class.java)`, khong route vao man Language
  rieng cua app.
- Da tach text chinh cua Home, Settings, Language, Intro va title route sang
  `values/strings_i18n.xml` va `values-vi/strings_i18n.xml`.
- Da them `values-vi/strings.xml` cho cac string lib/user-visible con nam trong
  `values/strings.xml` de lint khong loi `MissingTranslation`.
- Da thay rate mock bang mo Play Store/web fallback.
- Correction sau review: da chuyen Intro/Onboarding ve dung pattern base-application:
  `getHomeActivity()` re nhanh sang `IntroActivity` hoac `MainActivity` theo
  `AppStorage.isOnboardingDone()`, `MainActivity` khong con tu route vao Intro.
- `IntroActivity` tu luu `onboarding_done` va mo `MainActivity` voi
  `FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK` khi user hoan tat onboarding.
- Da go `IntroGate`, route `Screen.Intro`, counter `goToHomeNumber` va test cu vi khong con
  dung trong luong chuan.
- `testDebugUnitTest`: pass.
- `processDebugManifest` va `compileDebugKotlin`: pass sau correction Intro/Onboarding.
- `lintDebug`: pass.
- `assembleDebug`: pass.
- `assembleRelease`: pass, van tao APK unsigned nhu baseline.
- Chua test runtime doi ngon ngu/paywall tren may that vi moi truong khong co ADB device.
- Da duoc nguoi dung phe duyet ket qua va yeu cau commit rieng.
- Commit Chang 6/correction trong commit rieng.

## [ ] Chang 7 - Audit Ads runtime theo folder mau

### Pham vi

- Nguoi dung se cung cap folder `advertisement/` mau da chay on dinh de doi chieu.
- Doi chieu folder mau voi `app/src/main/java/com/vga/spinwheel/advertisement/` hien tai.
- Chinh Ads theo dung tai lieu `03_ADS.md`, `05_ADS_RUNTIME_LESSONS.md`,
  `06_ADS_REUSABLE_COMPONENTS.md` va `10_MIGRATE_ADS_TO_LIB.md`.
- Giu nguyen nguyen tac moi load/show Ads di qua `Admob.getInstance()` cua lib, khong goi
  GMA SDK truc tiep.
- Kiem tra va can lai `AdsViewModel`, `NativeAdSlot`, `AdManager`, `AdScenario`,
  `NativeInter`, `NativeAdsFull`, `AdPositions`, `Remote.kt` va `ad_native_full.xml`.
- Dam bao native fail thi an slot, khong ket shimmer; bind native dung mot lan; cache native
  theo Activity; key slot on dinh trong list.
- Dam bao inter/native-inter fallback va callback dieu huong chay dung mot lan.
- Kiem tra `positionIntrol` inline/modal trong Intro va logic timeout/fallback.
- Kiem tra log Ads dung `println`, khong dung `android.util.Log`.
- Kiem tra debug dung Google test unit, release doc tu `ads_config`.
- Kiem tra premium bo toan bo Ads va khong de lai khoang trong UI.
- Cap nhat unit test cho frequency, callback exactly-once, positionIntrol va premium gate neu can.

### Du lieu can nguoi dung cung cap

- Folder `advertisement/` mau gom cac file core Ads dang chay on dinh.
- Neu co, cung cap kem `firebase/Remote.kt`, `res/layout/ad_native_full.xml`,
  `res/xml/config.xml` va `assets/default_ads_config.json` cua app mau de doi chieu.

### Khong lam trong chang nay

- Khong dien ad unit production neu nguoi dung chua cung cap.
- Khong cau hinh signing/keystore.
- Khong sua UI feature ngoai cac container/slot Ads can thiet.

### Nghiem thu

- Khong con `AdLoader`, `AdRequest`, `InterstitialAd.load`, `AppOpenAd`,
  `MobileAds.initialize` hoac manager Ads tu viet trong code app.
- `NativeInterHost()` dat o root va goi qua import.
- Native fail/no-fill an slot, khong shimmer ket.
- `showInter` va native-inter goi `onNext()` dung mot lan trong moi nhanh.
- `default_ads_config.json` va `res/xml/config.xml` du placement app/lib can thiet.
- `testDebugUnitTest`, `compileDebugKotlin`, `lintDebug` va build debug/release pass.
- Neu co thiet bi, test runtime bang `adb logcat -s System.out` voi log `ADSLOT|ADS`.

### Diem dung phe duyet

- Bao cao diff giua folder mau va folder hien tai, ly do moi diem giu/sua.
- Dung lai de nguoi dung review runtime Ads truoc khi sang release config.

## [ ] Chang 8 - Release config, asset va signing

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

## [ ] Chang 9 - QA feature, UI polish va release candidate

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
| 3. Ads core | Da hoan thanh | Da phe duyet | Da phe duyet | Da |
| 4. Ads UI/flow | Da hoan thanh | Da phe duyet | Da phe duyet | Da |
| 5. IAP | Da hoan thanh development | Da phe duyet | Da phe duyet | Da |
| 6. Language + i18n | Da hoan thanh development | Da phe duyet | Da phe duyet | Da |
| 7. Ads runtime audit | Chua lam | Chua | Chua | Chua |
| 8. Release config | Chua lam | Chua | Chua | Chua |
| 9. QA + release candidate | Chua lam | Chua | Chua | Chua |

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

Co the bat dau **Chang 7 - Audit Ads runtime theo folder mau** sau khi nguoi dung cung cap
folder `advertisement/` mau va phe duyet bat dau.
Phan nghiem thu Billing production cua Chang 5 van bi chan den khi co product/subscription
id va Google Play public license key that.
