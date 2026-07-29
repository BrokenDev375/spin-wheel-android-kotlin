# Review — Phase 2: Bánh xe (Wheel Label Fix)

**Branch**: `phase-2-banh-xe`
**Ngày**: 2026-07-29
**Build**: BUILD SUCCESSFUL (assembleDebug, 29s)
**Runtime test**: Chua test tren device (khong co device online)

## Thay doi thuc hien

### File sua: WheelCanvas.kt

#### 1. Them helper formatWheelLabel(name, itemsCount)
- itemCount <= 4: toi da 14 ky tu
- itemCount <= 8: toi da 10 ky tu
- itemCount > 8 : toi da 6 ky tu
- Neu label dai hon, cat va them ... o cuoi

#### 2. Giam textSize theo so item
- Nen tang: radius * 0.085f, coerceIn [16f, 32f]
- items.size > 8 → nhan factor 0.8f de text nho hon

#### 3. Tang textRadius tu 0.27f len 0.5f
- Text cu gan tam (0.27f) kho doc
- Tang len 0.5f → text nam giua than o, ro hon

## Khong thay doi
- WheelViewModel (logic winner/result giu nguyen)
- WheelSpinScreen
- WheelResultScreen
- Data model WheelItem/Wheel

## Commit de xuat
fix(wheel): improve wheel label truncation and text sizing
