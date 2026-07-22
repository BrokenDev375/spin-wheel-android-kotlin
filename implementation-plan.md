# Implementation Plan - Spin Wheel App

Trang thai tai lieu: Draft - cho duyet.

## 1. Muc tieu

Xay dung app Android `Spin Wheel & Random Tools` dua tren:

- Mockup giao dien: `test.html`
- Tong quan va mapping tai lieu: `Readme.md`
- Kien truc Android: `architecture.md`
- Checklist trien khai: `checklist.md`
- Luong chuc nang chi tiet:
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

Ket qua mong muon la mot app Android Kotlin + Jetpack Compose co day du cac man hinh, dieu huong, state, luu tru local va flow tuong tac nhu tai lieu da mo ta.

## 2. Pham vi implement

### Chuc nang chinh

App co 9 chuc nang chinh:

1. `Banh Xe`
2. `Chon Ngon Tay`
3. `Dong Xu`
4. `Doi`
5. `So Ngau Nhien`
6. `Ve`
7. `Quay Chai`
8. `Xuc Xac`
9. `Lat The`

### Nhom man hinh chung

- Intro/onboarding.
- Home.
- Setting.
- Language.
- Payment/Pro.

## 3. Nguyen tac implement

1. Bám giao dien va luong trong `test.html` truoc, sau do tach thanh code Android Compose.
2. Moi chuc nang co route rieng trong `NavHost`.
3. State UI nam trong ViewModel, data dai han luu trong repository/storage.
4. Shared data nhu danh sach banh xe duoc dung chung cho `Banh Xe`, `Doi`, `Ve`.
5. Moi phase phai co checkpoint test truoc khi sang phase tiep theo.
6. Tich hop `base-application`, Ads va IAP ngay tu dau o muc khung/test config; core app van uu tien flow dung va UI gan screenshot truoc, pixel polish sau.
7. Review cua moi phase phai duoc luu trong folder `review/`.
8. Tai lieu luong hoat dong chuc nang phai duoc luu trong folder `feature/`.

## 4. Kien truc de xuat

Theo `architecture.md`, app se dung:

- Kotlin.
- Jetpack Compose.
- Single Activity: `MainActivity`.
- Compose Navigation.
- MVVM nhe.
- Repository pattern.
- SharedPreferences/DataStore cho setting nhe.
- Room cho danh sach banh xe va du lieu can persist.
- `base-application` cho Splash, Ads, IAP, Language/Payment, tich hop tu Phase 1.

## 5. Cau truc package de xuat

```text
app/src/main/java/<package>/
├── core/
│   ├── MainActivity.kt
│   ├── AppStorage.kt
│   └── AppConstants.kt
├── data/
│   ├── model/
│   │   ├── Wheel.kt
│   │   ├── WheelItem.kt
│   │   └── RandomResult.kt
│   └── repo/
│       ├── WheelRepository.kt
│       └── SettingsRepository.kt
├── ui/
│   ├── nav/
│   │   ├── Screen.kt
│   │   └── AppNavHost.kt
│   ├── theme/
│   ├── components/
│   └── screen/
│       ├── home/
│       ├── wheel/
│       ├── finger/
│       ├── coin/
│       ├── team/
│       ├── number/
│       ├── drawing/
│       ├── bottle/
│       ├── dice/
│       └── card/
└── MainApplication.kt
```

## 6. Data model can co

### Wheel

Dung chung cho `Banh Xe`, `Doi`, `Ve`.

```kotlin
data class Wheel(
    val id: String,
    val name: String,
    val items: List<WheelItem>,
    val createdAt: Long,
    val updatedAt: Long
)
```

### WheelItem

```kotlin
data class WheelItem(
    val id: String,
    val name: String,
    val priority: Int = 1
)
```

### Settings

Can tach setting theo feature:

- `WheelSettings`
- `NumberSettings`
- `TeamSettings`
- `DrawingSettings`
- `BottleSettings`
- `DiceSettings`
- `CoinSettings`
- `CardSettings`
- `AppSettings`

## 7. Ke hoach theo phase

## [x] Phase 0 - Xac nhan yeu cau va tai lieu

Muc tieu: dam bao tai lieu du de implement.

Cong viec:

1. Review `Readme.md`.
2. Review tat ca file flow `.md`.
3. Review `architecture.md` va `checklist.md`.
4. Chot app se implement native Android Compose hay tiep tuc mockup HTML.
5. Chot ten package Android, app name, icon, theme mau chinh.

Output:

- Ban implement plan duoc duyet.
- Danh sach diem can chot truoc khi code.

Checkpoint duyet:

- User phe duyet plan nay.

## [x] Phase 1 - Khoi tao project Android

Muc tieu: tao khung project co the build/run.

Cong viec:

1. Tao project Android Kotlin + Compose.
2. Cau hinh Gradle, AGP, Kotlin, Compose.
3. Tich hop module `base-application` AAR theo `architecture.md`.
4. Tao `MyApplication : BaseApplication`.
5. Cau hinh manifest de launcher di qua Splash cua lib.
6. Cau hinh resources bat buoc toi thieu cho lib.
7. Dung test/mock config cho Ads/IAP neu chua co key that.
8. Tao `MainActivity`.
9. Tao `AppTheme`.
10. Tao `AppNavHost`.
11. Tao route enum/sealed class cho tat ca man hinh.
12. Tao UI shell co status/navigation safe area.

Output:

- App build thanh cong.
- Luong Splash lib vao duoc `MainActivity`.
- Home route chay duoc voi placeholder UI.

Test:

- Build debug.
- Run tren emulator/device.
- Khong crash khi vao Home.
- Splash/Language/IAP cua lib neu bat config test khong lam ket app.

## [x] Phase 2 - UI system va component dung chung

Muc tieu: tao bo component lap lai de cac feature dung chung.

Cong viec:

1. Dinh nghia color palette theo mockup.
2. Tao typography, spacing, radius.
3. Tao component:
   - Header co back/home/settings.
   - Bottom action bar.
   - Icon button.
   - Primary/secondary button.
   - Setting row.
   - Stepper.
   - Toggle.
   - Card grid item.
   - Result screen layout.
4. Tao helper animation co ban.

Output:

- Component library noi bo.
- Home screen co UI gan mockup.

Test:

- Preview Compose cho cac component chinh.
- Kiem tra mobile portrait.

## [x] Phase 2.5 - Hilt va dependency injection

Muc tieu: tich hop Hilt truoc khi xay data layer, Repository va ViewModel that.

Cong viec:

1. Them Gradle plugin va dependency Hilt.
2. Annotate `MyApplication` bang `@HiltAndroidApp`.
3. Annotate `MainActivity` bang `@AndroidEntryPoint`.
4. Tao DI module toi thieu de lam nen cho Phase 3:
   - Application `Context` dung built-in `@ApplicationContext` cua Hilt.
   - `AppDispatchers` singleton cho Repository/Room.
5. Build debug de xac nhan Hilt compile va splash/lib flow khong crash.

Output:

- App co Hilt DI foundation.
- Phase 3 Storage co the inject Room database, DAO va Repository.

Test:

- Build debug.
- Run tren device/emulator vao duoc Home sau luong Splash/Language/IAP cua lib.

## [x] Phase 3 - Storage va data layer

Muc tieu: co data layer de luu danh sach va setting.

Cong viec:

1. Implement model `Wheel`, `WheelItem`.
2. Implement `WheelRepository`.
3. Implement CRUD:
   - Tao.
   - Sua.
   - Nhan ban.
   - Xoa.
   - Lay danh sach.
4. Implement `SettingsRepository`.
5. Luu setting tung feature.
6. Luu history cho feature can history.

Output:

- Data luu lai sau khi dong/mo app.
- Cac feature co the dung chung danh sach wheel.

Test:

- Tao wheel -> restart app -> wheel con ton tai.
- Sua/xoa/nhan ban hoat dong dung.

## [x] Phase 4.0 - Skeleton dieu huong cho Phase 4 va Phase 5

Muc tieu: tao khung route truoc de agent Phase 5 co the implement `Banh Xe` song song ma it conflict voi Phase 4.

Cong viec:

1. Them route skeleton cho Intro, Settings, Language, Payment.
2. Tach route khung cua `Banh Xe` sang nav graph rieng.
3. Dinh nghia route contract cho cac man Phase 5:
   - Danh sach banh xe.
   - Them banh xe.
   - Sua banh xe.
   - Quay banh xe.
   - Cai dat banh xe.
   - Ket qua.
   - Lich su.
4. Build debug de xac nhan navigation skeleton compile.

Output:

- Agent Phase 5 co the implement trong `ui/screen/wheel/` va `ui/nav/WheelNavGraph.kt`.
- Phase 4 chinh van chua implement day du Intro/Settings/Language/Payment.

Test:

- Build debug.
- Home -> route Wheel skeleton.
- Home -> Settings/Payment skeleton.

## [x] Phase 4 - Home, Intro, Setting, Language, Payment

Tai lieu tham chieu: `feature/Home.md`.

Cong viec:

1. Implement Intro 4 slide.
2. Implement Home grid 9 chuc nang.
3. Implement dieu huong tu Home sang tung feature.
4. Implement Setting chung:
   - Share.
   - Language.
   - Rate.
   - Music toggle.
   - Game sound toggle.
   - Vibration toggle.
5. Implement Language screen:
   - List ngon ngu.
   - Search.
   - Select language.
   - Done.
6. Implement Payment/Pro mock:
   - Feature comparison.
   - Weekly/Monthly/Annual package.
   - Restore.
   - Subscribe button.

Output:

- Luong vao app va dieu huong chinh hoan thanh.

Test:

- Intro -> Home.
- Home -> tung route.
- Setting -> Language -> Done.
- Payment chon goi va back ve Home.

## [x] Phase 5 - Banh Xe

Tai lieu tham chieu: `feature/WheelChair.md`.

Cong viec:

1. Implement danh sach banh xe.
2. Implement empty state.
3. Implement add/edit wheel form.
4. Implement add one item.
5. Implement add multiple items modal.
6. Implement validate:
   - Ten rong.
   - It hon 2 muc.
   - Muc rong.
7. Implement menu:
   - Sua.
   - Nhan ban.
   - Xoa.
8. Implement spin wheel canvas/custom Compose drawing.
9. Implement priority-weighted random.
10. Implement spin animation.
11. Implement result screen.
12. Implement retry ve dung banh xe vua chon.
13. Implement share result.
14. Implement wheel settings:
   - Duration.
   - Palette.
   - Remove winner.
15. Implement spin history.
16. Implement AI generate mock.

Output:

- Feature `Banh Xe` chay end-to-end.

Test:

- Tao wheel -> quay -> ket qua.
- Priority anh huong xac suat.
- Retry quay lai dung wheel da chon.
- Remove winner hoat dong.
- Palette update UI.

## [x] Phase 6 - Chon Ngon Tay

Tai lieu tham chieu: `feature/Finger.md`.

Cong viec:

1. Implement finger home/play screen.
2. Implement dropdown chon so ngon tay.
3. Implement multi-touch input.
4. Implement countdown 2s -> 1s.
5. Implement random winner.
6. Implement quick result.
7. Implement final result.
8. Implement share/retry/home.

Output:

- Feature `Chon Ngon Tay` chay tren thiet bi that co multi-touch.

Test:

- 2-5 ngon tay.
- Thoat giua countdown khong bi callback cu.
- Ket qua hien dung winner/loser.

## [x] Phase 7 - Dong Xu

Tai lieu tham chieu: `feature/Coin.md`.

Cong viec:

1. Implement coin home.
2. Implement flip animation.
3. Random heads/tails.
4. Implement score heads/tails neu can.
5. Implement result screen.
6. Implement setting duration.
7. Implement coin label/theme.
8. Implement reset/share/retry.

Output:

- Feature `Dong Xu` hoan thanh.

Test:

- Flip -> result.
- Doi coin theme.
- Duration thay doi thoi gian animation.

## [x] Phase 8 - Doi

Tai lieu tham chieu: `feature/Team.md`.

Cong viec:

1. Dung chung danh sach `Wheel`.
2. Implement Team home.
3. Implement tao/sua/xoa/nhan ban danh sach.
4. Implement detail danh sach.
5. Implement setting:
   - So thanh vien moi doi.
   - Duration.
   - Seed toggle.
6. Implement ghep doi animation.
7. Khi het animation, nut doi thanh `Next`.
8. Bam `Next` chuyen sang preview ket qua giong `screenshot/Team/Preview.jpg`.
9. Implement share result.
10. Implement retry/reset/home.

Output:

- Feature `Doi` hoan thanh theo flow moi.

Test:

- Danh sach 5 nguoi, group size 2 -> tao 3 doi, doi cuoi co 1 nguoi.
- Het animation chua sang preview ngay.
- Bam Next moi sang preview.
- Preview co share va retry.

## [x] Phase 9 - So Ngau Nhien

Tai lieu tham chieu: `feature/Number.md`.

Cong viec:

1. Implement number home.
2. Implement random animation.
3. Implement random logic:
   - Min/max.
   - Count.
   - Allow duplicates.
4. Implement setting.
5. Implement history.
6. Implement reset history.
7. Implement result screen.
8. Implement share/retry/home.

Output:

- Feature `So Ngau Nhien` hoan thanh.

Test:

- Min > max duoc normalize.
- No duplicates khong trung.
- Count khong vuot range khi no duplicates.
- History luu toi da 30 ket qua.

## [x] Phase 10 - Ve

Tai lieu tham chieu: `feature/Drawing.md`.

Cong viec:

1. Dung chung danh sach `Wheel`.
2. Implement Drawing home.
3. Implement fallback demo wheel.
4. Implement tao/sua/xoa/nhan ban.
5. Implement AI generate mock.
6. Implement specific drawing home.
7. Implement stack card UI.
8. Implement random animation.
9. Implement result screen.
10. Implement setting duration.
11. Implement label/theme.
12. Implement share/retry/home.

Output:

- Feature `Ve` hoan thanh.

Test:

- Khong co wheel thi co demo.
- Random hien dung item.
- Theme doi mau stack card.

## [x] Phase 11 - Quay Chai

Tai lieu tham chieu: `feature/bottle.md`.

Cong viec:

1. Implement bottle home.
2. Implement bottle drawing/asset.
3. Implement spin animation.
4. Random final angle 0-359.
5. Implement result screen.
6. Implement setting duration.
7. Implement bottle label/theme.
8. Implement reset/share/retry/home.

Output:

- Feature `Quay Chai` hoan thanh.

Test:

- Spin -> angle -> result.
- Reset dua ve angle 0.
- Theme bottle update dung.

## [x] Phase 12 - Xuc Xac

Tai lieu tham chieu: `feature/Dice.md`.

Cong viec:

1. Implement dice home.
2. Implement dice count 1-6.
3. Implement dice face component.
4. Implement rolling animation.
5. Random result tung vien.
6. Tinh total.
7. Implement result/preview screen.
8. Implement setting duration.
9. Implement dice label/theme.
10. Implement reset/share/retry/home.

Output:

- Feature `Xuc Xac` hoan thanh.

Test:

- Dice count 1-6.
- Moi ket qua tu 1 den 6.
- Total dung.
- Theme dice update dung.

## [x] Phase 13 - Lat The

Tai lieu tham chieu: `feature/Card.md`.

Cong viec:

1. Implement card home.
2. Implement total cards.
3. Implement winners count.
4. Random winner positions.
5. Implement shuffle/reset.
6. Implement tap to flip.
7. Khi lat het tat ca the, chuyen sang result.
8. Implement setting.
9. Implement theme label.
10. Implement result/share/retry/home.

Output:

- Feature `Lat The` hoan thanh.

Test:

- Winners < totalCards.
- Flip tung card dung trang thai.
- Lat het card moi hien result.
- Theme card update dung.

## [ ] Phase 14 - Hoan thien Ads, IAP va release config

Tai lieu tham chieu: `architecture.md`, `checklist.md`.

Cong viec:

1. Review lai cau hinh `base-application` da tich hop tu Phase 1.
2. Bo sung resource, icon, string, notification, remote config con thieu.
3. Cau hinh ad unit test/that theo moi moi truong.
4. Tich hop ads placements:
   - Native Home.
   - Inter navigation.
   - Native inter neu can.
5. Cau hinh IAP product/subscription neu co key that.
6. Premium tat ads.
7. Payment mock chuyen sang IAP that neu du key.
8. Chay checklist release lien quan Ads/IAP.

Output:

- App san sang test hoan chinh voi ads/IAP.

Test:

- Splash -> Language -> IAP -> Home.
- Premium tat ads.
- Debug dung test ad unit.

## [ ] Phase 15 - QA, polish, release

Cong viec:

1. So sanh UI voi screenshot tung folder.
2. Test tung feature end-to-end.
3. Test rotate/background/foreground neu can.
4. Test data persistence.
5. Test performance animation.
6. Test release R8.
7. Fix text overflow.
8. Fix dark mode neu can.
9. Chuan bi asset release.
10. Hoan thanh checklist release.

Output:

- Ban build release candidate.

Test:

- Debug build pass.
- Release build pass.
- Cai may that pass.
- Khong crash main flow.

## 8. Thu tu uu tien implement

Nen implement theo thu tu sau de giam rui ro:

1. Home + navigation.
2. Shared data layer `Wheel`.
3. Banh Xe, vi day la feature trung tam va cung cap data cho `Doi`, `Ve`.
4. Doi, vi phu thuoc `Wheel`.
5. Ve, vi phu thuoc `Wheel`.
6. Number, Dice, Coin, Bottle, Finger, Card.
7. Settings chung.
8. Payment/IAP/Ads.
9. QA/release.

## 9. Tieu chi hoan thanh

App duoc xem la hoan thanh khi:

1. Du 9 chuc nang chinh tren Home.
2. Tat ca route co the vao/ra bang back/home.
3. Tat ca flow trong file `.md` duoc implement.
4. Du lieu can luu duoc persist.
5. Random logic dung theo yeu cau tung feature.
6. UI gan voi screenshot trong folder tuong ung.
7. Khong crash khi test cac luong chinh.
8. Release build duoc neu bat phase release.

## 10. Rui ro va cach xu ly

| Rui ro | Anh huong | Cach xu ly |
| --- | --- | --- |
| UI Compose khac pixel so voi mockup HTML | Can polish nhieu | Lam component dung chung va so sanh theo screenshot tung phase |
| Shared data `Wheel` anh huong nhieu feature | Sua 1 noi gay loi nhieu noi | Chot schema som, viet repository ro rang |
| Multi-touch cua Finger kho test tren emulator | Feature sai tren may that | Test tren thiet bi that |
| Animation canvas/wheel phuc tap | Cham tien do | Implement ban dung logic truoc, polish animation sau |
| Ads/IAP lam nhiem phuc tap som | Cham core feature | Tich hop khung tu dau, dung test/mock config; cau hinh that hoan thien o Phase 14 |
| Text tieng Viet bi tran UI | UX xau | Test nhieu kich thuoc man hinh, dung responsive constraints |

## 11. Diem can ban phe duyet truoc khi code

Da chot:

1. Implement bang Android Kotlin + Compose theo `architecture.md`.
2. Tich hop `base-application`, Ads va IAP ngay tu dau.
3. Dung Room cho danh sach `Wheel`.
4. App name: `Spin Wheel`.
5. Language/i18n phase dau chi mockup UI.
6. Uu tien flow dung va UI gan screenshot truoc, pixel polish sau trong Phase 15.
7. Neu thieu tai lieu/quyet dinh, hoi user truoc khi tu quyet.

Con can chot:

1. Khong con diem nao cho Phase 0.

Da chot bo sung:

1. Android package/applicationId: `com.vga.spinwheel`.
2. Payment/IAP ban dau dung mock/test config; IAP that cau hinh sau khi co key san pham that.

## 12. De xuat cach tien hanh sau khi duyet

Sau khi plan nay duoc phe duyet:

1. Bat dau Phase 1.
2. Moi phase tao task checklist rieng.
3. Hoan thanh phase nao thi test va tao file review trong `review/phase-N-review.md`.
4. Chi tick `[x]` phase trong file nay sau khi review/checkpoint pass.
5. Chi sang phase tiep theo khi checkpoint pass.
6. Neu phat sinh khac mockup/tai lieu, cap nhat file `.md` lien quan truoc khi code tiep.
