# Chang 8 - Trial config, asset va debug build

Trang thai: Da thuc hien, cho phe duyet.

## Da thay doi

- Dung folder `res/` cap du an lam nguon va chi map cac asset co vai tro xac dinh.
- Map logo 512 x 512 vao `drawable-nodpi/spin_wheel_logo.png`; `icon_app.xml` dung
  logo nay cho splash va launcher.
- Map Lottie `splash_loading.json` tu folder nguon.
- Thay 5 icon IAP placeholder bang custom wheel, unlock all, support, no ads va
  VIP crown.
- Map 14 icon noi bo: Back, Home, Settings, Crown, Share, Language, Rate, Music,
  Sound, Vibration, History, Reset, Sliders va Shuffle. Cac glyph nay duoc dung qua
  `SpinIcon` tren toan app.
- Sua man Settings de tung dong dung icon dung chuc nang, thay cho cac glyph tam
  Home/Crown/Settings/Check/Minus/Chevron.
- Map 9 anh nen Home tu bo asset goc: Wheel, Finger, Coin, Team, Number, Drawing,
  Bottle, Dice va Card. The game dung drawable that voi `ContentScale.Crop`, khong
  con hien artwork Canvas tam.
- Giu gear cho Settings tren header; doi nut tuy chinh phia duoi Drawing va Dice
  sang icon sliders/cong tac.
- Them debug manifest de tat Firebase Analytics va Crashlytics collection.
- Ignore folder nguon `/res/` de khong commit nham config production-looking.

Khong map status notification PNG 5 density, notification illustration va preview:
folder nguon khong dinh danh ro bo asset tuong ung, trong khi cac asset hien tai da
hop le cho trial. Khong map font, audio, drawable gameplay, strings/config hoac ID/key
tu folder nguon.

## Cau hinh trial

- Debug dung Google test ad unit.
- Ads Resume tat trong debug.
- Force full ads test tat.
- `default_ads_config.json` co 16 placement, tat ca la Google test ad unit.
- `google-services.json` la mock va khop `com.vga.spinwheel`.
- Khong them signing config, keystore, password hay production secret.

## Da kiem tra

Lenh:

```powershell
.\gradlew.bat :app:testDebugUnitTest :app:lintDebug :app:assembleDebug
```

Ket qua:

- Build thanh cong.
- 28/28 unit test pass.
- Lint: 0 Fatal, 0 Error, 48 Warning.
- Merged debug manifest co
  `firebase_analytics_collection_enabled=false` va
  `firebase_crashlytics_collection_enabled=false`.
- APK: `app/build/outputs/apk/debug/app-debug.apk`.
- Kich thuoc: 79,607,464 byte.
- SHA-256:
  `6770496501B2C6EEA31079CA8DDEDB382712FEAC71A388B0999AA748B52353A3`.
- `git diff --check` khong co whitespace error.

## Con lai

- Chua test runtime/ADB vi moi truong khong co thiet bi.
- 48 lint warning khong chan build; phan lon la dependency moi, resource chua dung va
  khuyen nghi API/style, de xu ly trong Chang 9 neu nam trong pham vi QA.
- Chua build release, R8 hay mapping; chua cau hinh signing/production.

## Diem dung

Cho nguoi dung phe duyet ket qua Chang 8. Khong bat dau Chang 9 va khong thuc hien
release/signing production truoc khi co phe duyet.
