# Review Chang 6 - Language va i18n

## Trang thai

Cho nguoi dung phe duyet. Chua commit.

## Da thay doi

- Sua correction cho Chang 5: `MainActivity` chuyen sang `AppCompatActivity` de phu hop
  nen `FragmentActivity/AppCompatActivity` ma lib Language/IAP yeu cau.
- Can lai `IapLauncher.open()` theo pattern lib: unwrap `Activity` tu `Context` va goi
  `NativeCodecSnowFlakeCortexAI.nativeAiStartIapActivity(activity)`.
- Them `LocaleHelper.updateLocale()` de `MainActivity` ap language mirror khi tao UI.
- `notifyLanguageSaved(languageCode)` chi dong bo language ve `AppStorage`; lib tu ap locale
  trong luong chon ngon ngu.
- Settings mo man Language co san cua lib bang
  `LanguageActivity.start(activity, MainActivity::class.java)`, khong route vao
  `LanguageSettingsRoute` hay man ngon ngu rieng cua app.
- `LanguageViewModel` chi giu language code va resource id; khong giu ban dich trong Kotlin
  nhung khong con duoc route tu Settings.
- Text chinh cua Home, Settings, Language, Intro va route title duoc dua sang
  `strings_i18n.xml`.
- Them locale mac dinh English va `values-vi`; them `values-vi/strings.xml` cho cac string
  lib/user-visible con nam trong `strings.xml`.
- Thay rate mock bang mo Play Store/web fallback.

## Da kiem tra

- `:app:testDebugUnitTest`: pass.
- `:app:lintDebug`: pass.
- `:app:assembleDebug`: pass.
- `:app:assembleRelease`: pass; artifact release van unsigned nhu baseline.

## Chua kiem tra duoc

- Runtime doi ngon ngu tren thiet bi that.
- Runtime mo paywall sau correction `AppCompatActivity`.
- UI review ban dich tieng Viet/tieng Anh tren may that.

## Can phe duyet

- Chap nhan correction Chang 5 nam trong commit tiep theo.
- Chap nhan ket qua development Chang 6.
- Cho phep commit rieng cho correction + Chang 6.
