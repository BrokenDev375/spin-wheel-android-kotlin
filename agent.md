# Agent Rules - Spin Wheel App

File nay la prompt/quy tac bat buoc cho moi agent khi tham gia implement app Spin Wheel.

## Nguyen tac tong quat

1. Lam theo tai lieu da co, khong tu suy dien qua muc.
2. Neu thieu tai lieu, thieu screenshot, thieu quyet dinh san pham, hoac co 2 cach lam deu hop ly, phai hoi user truoc khi tao/sua code lon.
3. Khong sua code thu vien `base-application` hoac AAR neu user chua phe duyet ro.
4. Khong tu thay doi luong startup da chot:

```text
Splash -> Interstitial -> Language -> IAP/Paywall -> Intro/Onboarding -> Home
```

5. Paywall hien truoc Intro la hanh vi dung theo tai lieu thu vien va kien truc app.
6. Uu tien flow dung va UI gan screenshot truoc; pixel polish de phase polish sau.
7. Moi thay doi phai nho, dung pham vi phase/nhanh dang lam.
8. Khong revert thay doi cua nguoi khac neu khong duoc yeu cau.
9. Khi lam xong, phai bao ro da build/test gi va con thieu gi.

## Thu tu phase va song song

1. Khong chay song song truoc khi Phase 3 Storage/Data layer duoc lam xong va chot.
2. Phase 3 phai chot cac data contract dung chung:
   - `Wheel`
   - `WheelItem`
   - Room database/schema
   - `WheelRepository`
   - `SettingsRepository`
   - history storage neu feature can
3. Sau Phase 3 moi duoc chia song song cac feature.
4. Cac feature doc lap co the lam song song sau Phase 3:
   - Coin
   - Dice
   - Card
   - Bottle
   - Finger
5. Cac feature phu thuoc shared `Wheel` phai bam sat schema Phase 3:
   - Wheel
   - Team
   - Drawing
6. Neu mot agent can sua data contract da chot, phai dung lai va hoi user truoc.

## Quy tac branch

1. Moi phase/feature song song lam tren mot branch rieng.
2. Dat ten branch ro nghia, vi du:
   - `phase-3-storage-data-layer`
   - `phase-5-wheel`
   - `feature-coin`
   - `feature-team`
3. Khong lam nhieu phase khac nhau tren cung mot branch neu user chua dong y.
4. Truoc khi bat dau, phai kiem tra:

```powershell
git status --short --branch
git log --oneline -5 --decorate
```

5. Neu working tree khong sach, phai xac dinh thay doi co lien quan khong. Neu khong chac, hoi user.
6. Sau khi commit local, khong tu push. Chi bao user lenh push:

```powershell
git push -u origin <branch-name>
```

## Quy tac tai lieu

1. File mo ta luong chuc nang phai nam trong folder `feature/`.
2. File review phase phai nam trong folder `review/`.
3. Moi phase thanh cong phai co review rieng trong `review/`.
4. Khi phase hoan thanh va da build/test dat, tick vao dau phase trong `implementation-plan.md`:

```md
## [x] Phase ...
```

5. Khong tick phase neu:
   - build chua pass,
   - con loi runtime nghiem trong,
   - user chua duyet khi phase yeu cau review,
   - con thieu tai lieu/quyet dinh bat buoc.
6. Neu co mock/test config, phai ghi chu ro trong review.

## Quy tac ky thuat

1. Architecture hien tai:
   - Android Kotlin
   - Jetpack Compose
   - Hilt
   - Room cho storage
   - SharedPreferences/DataStore cho setting nhe neu phu hop
   - MVVM nhe: Composable -> ViewModel -> Repository -> Data source
2. Dung Hilt cho Repository, Room DB, DAO, ViewModel tu Phase 3 tro di.
3. Khong tao singleton thu cong moi neu Hilt da giai quyet duoc.
4. Khong hand-roll storage bang string JSON neu Room/schema da phu hop.
5. Dung repository layer, khong de UI goi truc tiep DAO/storage.
6. Build debug truoc khi ket thuc phase:

```powershell
.\gradlew.bat :app:assembleDebug --stacktrace
```

7. Neu co device/emulator online, run app va xac nhan khong crash.
8. Neu khong co device, phai ghi ro "chua run duoc tren device vi khong co device online".

## Quy tac tranh conflict khi chay song song

1. Han che nhieu agent cung sua cac file trung tam:
   - `implementation-plan.md`
   - `AppNavHost.kt`
   - `Screen.kt`
   - `HomeScreen.kt`
   - Gradle files
   - Room schema/database file
2. Neu bat buoc sua file trung tam, phai noi ro trong review.
3. Feature branch chi nen sua:
   - folder screen cua feature do,
   - ViewModel/repository lien quan,
   - route/nav toi thieu,
   - test/review cua feature do.
4. Neu can them route moi, uu tien mot agent dieu phoi navigation merge sau, tranh 5 agent cung sua `AppNavHost.kt`.

## Check-in voi user

Agent phai hoi user truoc khi:

1. Thieu screenshot/tai lieu cho feature.
2. Co thay doi schema/data contract dung chung.
3. Can sua luong startup, Intro, Language, IAP, Ads.
4. Can sua code `base-application` hoac thay AAR.
5. Can doi version AGP/Kotlin/Hilt/Room lon.
6. Can xoa file, reset git, hoac revert thay doi khong phai cua agent.
7. Feature co hanh vi khac voi markdown/screenshot.

Mau cau hoi ngan:

```text
Minh dang thieu <tai lieu/quyet dinh>. Co 2 huong: <A> hoac <B>. Ban chot giup minh truoc khi implement nhe?
```

## Mau workflow cho moi agent

1. Doc:
   - `Readme.md`
   - `architecture.md`
   - `implementation-plan.md`
   - feature markdown lien quan trong `feature/`
   - review phase truoc trong `review/`
2. Kiem tra branch/status.
3. Neu thieu thong tin, hoi user truoc.
4. Implement dung pham vi.
5. Build/test.
6. Tao file review trong `review/`.
7. Tick phase/feature neu du dieu kien.
8. Commit local.
9. Bao user:
   - branch
   - commit
   - build/test da chay
   - viec chua verify duoc
   - lenh push

## Ket luan quan trong

Lam xong Phase 3 truoc, chot schema chung, sau do moi chia song song. Song song la de tang toc, khong duoc pha data contract, khong duoc tu sua luong thu vien, va khong duoc bo qua buoc hoi user khi thieu quyet dinh.
