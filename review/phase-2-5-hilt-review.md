# Phase 2.5 Review - Hilt va Dependency Injection

Branch: `phase-3-hilt-integration`

## Trang thai

- Da them phase Hilt vao `implementation-plan.md` va tick `[x]`.
- Da tich hop Hilt vao app host, khong thay doi luong Splash/Language/IAP/Home cua `base-application`.
- Da build debug thanh cong.

## Thay doi da thuc hien

1. Gradle root:
   - Them plugin `com.google.devtools.ksp`.
   - Them plugin `com.google.dagger.hilt.android`.

2. Gradle app:
   - Apply `com.google.devtools.ksp`.
   - Apply `com.google.dagger.hilt.android`.
   - Them `com.google.dagger:hilt-android`.
   - Them `com.google.dagger:hilt-compiler` qua KSP.
   - Them `kotlinx-coroutines-android` de dung cho dispatcher/repository sau nay.

3. Application va Activity:
   - `MyApplication` duoc annotate `@HiltAndroidApp`.
   - `MainActivity` duoc annotate `@AndroidEntryPoint`.

4. DI foundation:
   - Them `core/di/AppDispatchers.kt`.
   - Them `core/di/AppModule.kt`.
   - Cung cap `AppDispatchers` singleton gom `io`, `default`, `main`.
   - Application `Context` se dung built-in `@ApplicationContext` cua Hilt khi can inject o Phase 3.

## Quyet dinh version Hilt

- Thu `2.60.1`: khong dung duoc vi Hilt Gradle plugin yeu cau AGP `9.0.0+`, project hien tai dung AGP `8.12.0`.
- Thu `2.48`: khong phu hop Kotlin `2.1.x`, loi metadata khi Hilt processor doc Kotlin metadata.
- Chot `2.57.2`: build thanh cong voi AGP `8.12.0` va Kotlin `2.1.20`.

## Kiem thu

Lenh da chay:

```powershell
.\gradlew.bat :app:assembleDebug --stacktrace
```

Ket qua:

- `BUILD SUCCESSFUL`
- APK debug tao tai `app/build/outputs/apk/debug/app-debug.apk`.

Canh bao con lai:

- Warning SDK XML version do command-line tools/Android Studio lech version.
- Warning namespace AppsFlyer trong dependency cua lib.
- Warning `tools:replace` trong manifest khong co declaration tuong ung de replace.
- Cac warning nay da ton tai o nen/lib, khong chan build Hilt.

## Chua kiem duoc

- Chua run tren device/emulator vi `adb devices` khong co device online.
- Khi co device, can install APK va mo app de xac nhan runtime vao duoc Home sau luong Splash/Language/IAP cua lib.

## Ket luan

Phase 2.5 Hilt foundation da hoan thanh o muc compile/build. Phase 3 Storage co the tiep tuc tren nen Hilt nay de inject Room database, DAO, Repository va ViewModel.
