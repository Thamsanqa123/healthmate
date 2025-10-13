
# HealthMate - Mobile Health & Fitness Tracker



## 📱 App Overview

HealthMate is a comprehensive mobile health and fitness tracking application designed specifically for South African users. The app helps users track their meals, workouts, and overall health progress with a user-friendly interface and innovative features.

##  Features

###  Authentication & Security
- **Email/Password Registration & Login** with encrypted password storage
- **Google Single Sign-On (SSO)** integration
- Secure Firebase Authentication
- Password encryption using SHA-256

###  Workout Tracking
- Log various exercise types (Running, Cycling, Strength Training, Yoga, etc.)
- Track duration, calories burned, and distance
- GPS route tracking for outdoor activities
- Workout history with detailed statistics

###  Nutrition & Meal Logging
- Log meals by type (Breakfast, Lunch, Dinner, Snacks)
- Track calories, macros (carbs, protein, fat)
- Food database with search functionality
- Water intake tracking

### 🌐 Connectivity & Sync
- **RESTful API integration** with custom backend
- **Offline-first architecture** using Room Database
- Automatic data synchronization when online
- Cloud backup of user data

###  User Experience
- **Multilingual support** (English )
- Personalized dashboard with daily summaries
- Push notifications for reminders
- Gamified badges and achievements
- Customizable user settings and goals

##  Technical Architecture

### Frontend
- **Android Native** with Kotlin
- **MVVM Architecture** with Repository pattern
- **Jetpack Components**: 
  - Room Database for local storage
  - ViewModel & LiveData
  - Navigation Component
  - WorkManager for background tasks

### Backend & APIs
- **RESTful API** with Node.js/Express
- **Firebase** for authentication and real-time features
- **Firestore Database** for cloud storage
- **Firebase Cloud Functions** for serverless operations

### Local Storage
- **Room Database** for offline data persistence
- Encrypted shared preferences for sensitive data
- Efficient data synchronization strategies


## Installation & Setup

### Prerequisites
- Android Studio Arctic Fox or later
- JDK 17 or higher
- Android SDK API 34
- Firebase project setup

### Build Instructions

1. **Clone the repository**
   ```bash
   git clone https://github.com/your-username/healthmate-android.git
   cd healthmate-android
