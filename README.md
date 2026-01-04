# TimelessPay Java SDK

Java SDK for integrating with the TimelessPay Payment Gateway Service.

## Installation

### Using JitPack

Add JitPack repository to your `build.gradle`:

```gradle
repositories {
    maven { url 'https://jitpack.io' }
}
```

Then add the dependency:

```gradle
dependencies {
    implementation 'com.github.timeless-sdk:java-sdk:0.1.1'
}
```

Or for Gradle Kotlin DSL (`build.gradle.kts`):

```kotlin
repositories {
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("com.github.timeless-sdk:java-sdk:0.1.1")
}
```

### Using GitHub Packages

Add GitHub Packages repository:

```gradle
repositories {
    maven {
        url = uri("https://maven.pkg.github.com/timeless-sdk/java-sdk")
    }
}
```

Then add the dependency:

```gradle
dependencies {
    implementation 'net.timelesspay:sdk:0.1.1'
}
```

## Usage

See the [Integration Guide](TimelessPay_INTEGRATION_GUIDE.md) for detailed usage instructions.

## License

Apache License 2.0

