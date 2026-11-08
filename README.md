# Akıllı Ev (IoT) Kontrol Merkezi — week3_assignment

**Week 3 Android/Kotlin Ödevi** · Jetpack Compose · MVVM · ViewModel · Coroutines · StateFlow

Evin içindeki 10 cihazı odalarına göre filtreleyen, cihaz adını aratan, cihazları açıp kapatan ve **yalnızca açık + çevrimiçi cihazların anlık watt değerlerini** üstte reaktif olarak gösteren Android uygulaması.

> **Not:** Veri kaynağı dersin PDF'indeki 10 örnek cihazdan oluşan yerel bellektir. Gerçek IoT cihazına bağlanma, sunucu, internete erişim veya kalıcı depolama ödevin kapsamında değildir. Uygulama yeniden açıldığında örnek durumlar sıfırlanır.

## Hızlı başlangıç

1. ZIP dosyasını çıkarın. Android Studio'da **File > Open** ile içindeki `week3_assignment` klasörünü seçin. `app` alt klasörünü ayrıca açmayın.
2. Gradle Sync'in tamamlanmasını bekleyin. İlk açılışta internet bağlantısı gerekir.
3. Android Studio'nun **Gradle JDK** ayarını **Embedded JDK 17** olarak yapılandırın (gerekirse Settings > Build, Execution, Deployment > Build Tools > Gradle).
4. Android SDK Manager'dan **Android API 35 (SDK Platform 35)** yüklü olduğundan emin olun.
5. Emülatör veya Android 7.0+ telefon seçip **Run ▶** düğmesine basın.

Kullanılan sürümler: Android Gradle Plugin 8.9.2, Gradle 8.11.1, Kotlin 2.0.21, Compose BOM 2025.06.00, JDK 17, minSdk 24, compileSdk 35.

## Özellikler

- **Arama + oda filtresi birlikte:** arama isimde büyük/küçük harf gözetmez, oda filtreleriyle eşzamanlı çalışır.
- **Canlı enerji kartı:** `cihaz.isOnline && cihaz.isOn` olanların `powerWatts` değerleri toplanır.
- **Anlık durum güncelleme:** anahtara dokununca Repository `StateFlow` yayınlar; ViewModel yeniden hesaplar; Compose otomatik yenilenir.
- **Çevrimdışı güvenliği:** Balkon Aydınlatması çevrimdışıdır; anahtarı pasiftir, toplamda yer almaz.
- **Performans:** liste `LazyColumn` ile çizilir, anahtarlar sabit ID'lerle anahtarlanır; filtre/toplam hesabı `Dispatchers.Default` üzerinde çalışır; UI'de senkron veri işlemi yapılmaz.
- **Boş sonuç durumu:** arama ve filtre hiçbir cihaz bulmadığında açıklayıcı ekran gösterilir.

### Başlangıç doğrulaması

| Gösterge | Başlangıç |
| --- | ---: |
| Cihaz | 10 |
| Çevrimiçi cihaz | 9 |
| Açık + çevrimiçi cihaz | 6 |
| **Toplam aktif güç** | **106,0 W** |

Örnek test: **Tavan Avizesi** açılırsa toplam **166,0 W**, **Yatak Odası Kliması** açılırsa başlangıca göre **1306,0 W** olur. Arama / oda filtrelemesi enerji sayacını değiştirmez.

## Mimari

```text
MainActivity
    └── DashboardRoute (StateFlow -> lifecycle-aware collect)
          ├── DashboardScreen (STATELESS COMPOSE; sadece çizim + callback)
          └── DashboardViewModel
                ├── combine(devices, searchQuery, selectedRoom)
                ├── filter / count / sumOf / UI veri dönüşümü
                ├── flowOn(Dispatchers.Default)
                └── DeviceRepository (interface)
                      └── InMemoryDeviceRepository
                            └── MutableStateFlow<List<SmartDevice>>
```

`ui/dashboard/DashboardScreen.kt` içinde `remember`, `mutableStateOf`, `.filter`, `.sumOf` veya toplam hesabı **bulunmaz**. Arayüz `DashboardUiState` ve callback alır. Repository'nin değişiklikleri coroutine üzerinde, `Dispatchers.Default` ile işlenir.

## Kaynak kod dizinleri

```text
app/src/main/java/com/example/week3_assignment/
├── MainActivity.kt
├── data/
│   ├── model/SmartDevice.kt
│   └── repository/
│       ├── DeviceRepository.kt
│       └── InMemoryDeviceRepository.kt
└── ui/
    ├── dashboard/
    │   ├── DashboardRoute.kt
    │   ├── DashboardScreen.kt
    │   ├── DashboardUiState.kt
    │   └── DashboardViewModel.kt
    └── theme/
        ├── Color.kt
        └── Theme.kt
```

## Testler

Android Studio > **Run > Run 'All Tests'** ya da komut satırı: `./gradlew testDebugUnitTest` (Windows: `gradlew.bat testDebugUnitTest`).

8 birim testi: başlangıç enerji ve sayılar, arama, birleşik oda + arama, sonuç bulunamaması, çevrimdışı güvenliği, 1200 W klimanın açılması, çevrimiçi cihazın iki kez durum değiştirmesi.

## PDF isterleriyle uyum

| PDF'deki ister | Uygulama |
| --- | --- |
| Cihaz adına göre arama | `DashboardViewModel.buildUiState()` |
| Odaya göre filtre | `DashboardViewModel.buildUiState()` |
| Sadece aktif ve online cihazların toplamı | `active.sumOf { it.powerWatts }` |
| Aç/Kapat ve reaktif toplam | `toggleDevice`, `StateFlow`, `combine` |
| MVVM / Jetpack ViewModel | `DashboardViewModel`, `DeviceRepository` |
| Asenkron Coroutines / thread yönetimi | `viewModelScope.launch`, `withContext(Default)`, `flowOn(Default)` |
| Stateless Compose | `DashboardScreen(state, callbacks)` |
| Akıcı liste ve UI | `LazyColumn`, stabil key'ler, main thread dışı hesap |
| 10 cihaz ve verilmiş özellikler | `seedDevices()` |

