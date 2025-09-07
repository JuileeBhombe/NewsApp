# News Feed App

A Kotlin Android app that fetches and displays news articles from NewsAPI.org.

[![Test Coverage](https://img.shields.io/badge/coverage-28%25-blue.svg)](app/build/reports/jacoco/index.html)

## 🎯 Assignment Requirements

### Core Features
- ✅ **List/Grid Screen**: Display news articles with title, description (2 lines), and thumbnail image
- ✅ **Details Screen**: Navigate to full article view with complete title, description, and header image
- ✅ **Navigation**: Smooth transition between list and detail screens

### Architecture
- ✅ **MVVM Pattern**: Clean separation of concerns
- ✅ **Repository Pattern**: Centralized data access layer
- ✅ **Dependency Injection**: Dagger Hilt for DI
- ✅ **State Management**: ViewModel with LiveData/StateFlow

## 🛠️ Tech Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose
- **Networking**: Retrofit + Kotlin Serialization
- **Image Loading**: Coil
- **Architecture**: MVVM
- **DI**: Dagger Hilt
- **API**: NewsAPI.org

## 🚀 Quick Setup

1. **Get API Key** from [NewsAPI.org](https://newsapi.org/)
2. **Add to build.gradle.kts**:
   ```kotlin
   buildConfigField("String", "NEWS_API_KEY", "\"YOUR_API_KEY\"")
   ```
3. **Build and run** in Android Studio

## 📱 Features

### List Screen
- Article cards with title, truncated description, and thumbnail
- List layout with proper spacing
- Click to navigate to details

### Detail Screen
- Full article title and complete description
- Full-width header image (when available)
- Back navigation

## 🏗️ Project Structure

```
app/src/main/java/com/juileebhombe/newsapp/
├── data/           # API services & DTOs
├── domain/         # Use cases & entities
├── presentation/   # ViewModels & UI screens
├── repository/     # Data repository layer
└── util/          # Constants & utilities
```

## 🧪 Testing

### Running Tests

**Unit Tests:**
```bash
./gradlew testDebugUnitTest
```

**Instrumentation Tests:**
```bash
./gradlew connectedDebugAndroidTest
```

**All Tests:**
```bash
./gradlew test
```

### Code Coverage

**Generate & View:**
```bash
./gradlew jacocoTestReport
open app/build/reports/jacoco/index.html
```

Reports include line/branch coverage with interactive HTML visualization.

## 📋 Requirements Met

- ✅ Clean code structure with separation of concerns
- ✅ MVVM architecture implementation
- ✅ Repository pattern for data access
- ✅ Unit tests with good coverage
- ✅ Error handling and state management
- ✅ Scalable and readable codebase
- ✅ Best practices (DI, error handling, state management)
