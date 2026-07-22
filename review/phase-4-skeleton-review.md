# Phase 4.0 Review - Navigation Skeleton

Branch: `phase-4-skeleton`

## Trang thai

- Da tao skeleton dieu huong cho Phase 4 va Phase 5.
- Da tick `[x]` Phase 4.0 trong `implementation-plan.md`.
- Chua tick Phase 4 chinh.
- Chua implement day du Intro, Settings, Language, Payment.
- Chua thay doi startup flow cua `base-application`.

## Thay doi da thuc hien

1. Route chung:
   - Them `Screen.Intro`.
   - Them `Screen.Language`.

2. Wheel route contract cho Phase 5:
   - `WheelRoutes.HOME`: `wheel`
   - `WheelRoutes.ADD`: `wheel/add`
   - `WheelRoutes.EDIT`: `wheel/{wheelId}/edit`
   - `WheelRoutes.SPIN`: `wheel/{wheelId}/spin`
   - `WheelRoutes.SETTINGS`: `wheel/{wheelId}/settings`
   - `WheelRoutes.RESULT`: `wheel/{wheelId}/result/{resultId}`
   - `WheelRoutes.HISTORY`: `wheel/{wheelId}/history`

3. Wheel nav graph:
   - Tao `ui/nav/WheelNavGraph.kt`.
   - Route `wheel` dang mo `WheelSkeletonScreen`.
   - Cac route con cua Wheel dang dung `PlaceholderScreen`.
   - Agent Phase 5 nen implement trong:
     - `ui/screen/wheel/`
     - `ui/nav/WheelNavGraph.kt`
   - Neu can doi `WheelRoutes`, phai hoi user truoc vi day la route contract da chot cho song song.

4. Phase 4 skeleton:
   - `Screen.Intro` dung placeholder.
   - `Screen.Settings` dung `SettingsSkeletonScreen`.
   - `Screen.Language` dung placeholder.
   - `Screen.Payment` dung placeholder.
   - Settings skeleton co row `Ngon ngu` de chuyen sang route Language.

## Kiem thu da chay

Lenh:

```powershell
.\gradlew.bat :app:assembleDebug --stacktrace
```

Ket qua:

- `BUILD SUCCESSFUL`
- APK debug tao tai `app/build/outputs/apk/debug/app-debug.apk`.

## Chua kiem duoc

- Chua run tren device/emulator vi `adb devices` khong co device online.
- Chua test click navigation runtime tren may that.

## Luu y cho agent Phase 5

1. Khong sua schema Phase 3 neu chua hoi user.
2. Dung `WheelRepository` cho data CRUD.
3. Uu tien thay placeholder trong `WheelNavGraph.kt` bang screen that.
4. Neu can them route con ngoai danh sach trong `WheelRoutes.kt`, hoi user truoc.
5. Han che sua `AppNavHost.kt`; route Wheel da duoc tach rieng de tranh conflict voi Phase 4.

## Ket luan

Skeleton da san sang de agent khac bat dau Phase 5. Can bao user truoc khi tiep tuc implement phan con lai cua Phase 4.
