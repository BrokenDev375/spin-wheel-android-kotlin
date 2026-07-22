# Phase 3 Review - Storage va Data Layer

Branch: `phase-3-storage-data-layer`

## Trang thai

- Da fast-forward branch nay len nen Hilt tu `phase-3-hilt-integration`.
- Da implement Room storage, data model, repository va settings/history layer.
- Da tick `[x]` Phase 3 trong `implementation-plan.md`.
- Chua thay doi UI/navigation/startup flow.

## Scope da implement

1. Room dependency:
   - `androidx.room:room-runtime:2.8.4`
   - `androidx.room:room-ktx:2.8.4`
   - `androidx.room:room-compiler:2.8.4` qua KSP
   - Export schema ra `app/schemas`

2. Domain model:
   - `RandomFeature`
   - `Wheel`
   - `WheelItem`
   - `RandomResult`

3. Room database:
   - `SpinWheelDatabase`
   - `WheelEntity`
   - `WheelItemEntity`
   - `RandomHistoryEntity`
   - `WheelDao`
   - `RandomHistoryDao`

4. Data contract version 1:
   - Bang `wheels`:
     - `id`
     - `name`
     - `createdAt`
     - `updatedAt`
   - Bang `wheel_items`:
     - `id`
     - `wheelId`
     - `name`
     - `priority`
     - `sortOrder`
   - Bang `random_history`:
     - `id`
     - `feature`
     - `sourceId`
     - `title`
     - `value`
     - `payload`
     - `createdAt`

5. Repository:
   - `WheelRepository`
     - observe danh sach wheel
     - get danh sach
     - get theo id
     - create
     - upsert/update
     - duplicate
     - delete
   - `SettingsRepository`
     - get/put/observe `Boolean`, `Int`, `Long`, `String`
     - get all settings theo feature
     - remove key
     - clear theo feature
   - `RandomHistoryRepository`
     - observe history theo feature
     - get history theo feature
     - add/upsert result
     - delete result
     - clear theo feature/all

6. Hilt DI:
   - `DataModule` provide:
     - `SpinWheelDatabase`
     - `WheelDao`
     - `RandomHistoryDao`
     - `SharedPreferences` cho settings

7. Test:
   - Them `ModelMappersTest` de kiem tra:
     - item order theo `sortOrder`
     - map `Wheel` sang entity
     - map history feature key

## Kiem thu da chay

Lenh:

```powershell
.\gradlew.bat :app:testDebugUnitTest :app:assembleDebug --stacktrace
```

Ket qua:

- `BUILD SUCCESSFUL`
- Unit test pass.
- APK debug tao tai `app/build/outputs/apk/debug/app-debug.apk`.
- Room schema sinh tai `app/schemas/com.vga.spinwheel.data.db.SpinWheelDatabase/1.json`.

## Chua kiem duoc

- Chua run app tren device/emulator vi `adb devices` khong co device online.
- Chua test persistence thuc te bang thao tac UI vi Phase 3 moi la data layer, cac man CRUD UI se nam o Phase 5+.

## Ghi chu version

- Dung Room `2.8.4` de phu hop hon voi Kotlin/KSP moi. Tham chieu release note AndroidX Room: `https://developer.android.com/jetpack/androidx/releases/room`.

## Ket luan

Phase 3 da chot data contract nen cac agent co the bat dau chia song song sau khi branch nay duoc merge/push. Neu feature sau can thay doi schema `Wheel`, `WheelItem` hoac `random_history`, phai hoi user truoc theo `agent.md`.
