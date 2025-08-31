# Dime Android - Personal Finance Tracker

A modern Android personal finance tracker built with Jetpack Compose, Room database, and Material 3 design.

## Features

- **Transaction Management**: Add, edit, and categorize income/expense transactions
- **Budget Tracking**: Set and monitor budgets with progress visualization
- **Spending Insights**: Charts and analytics for financial patterns
- **Biometric Security**: Secure app access with fingerprint/face unlock
- **Recurring Transactions**: Automatic processing of recurring payments
- **Data Export/Import**: Backup and restore data in JSON/CSV formats
- **Home Screen Widget**: Quick balance overview
- **Dark/Light Theme**: Automatic theme switching

## Installation

1. Download the APK from GitHub Releases
2. Enable "Install unknown apps" in Android settings
3. Install the APK file
4. Grant necessary permissions when prompted

## Requirements

- Android 5.0 (API 21) or higher
- 50MB storage space
- Biometric hardware (optional, for app lock feature)

## Building from Source

This project uses GitHub Actions for automatic APK building:

1. Fork this repository
2. Push changes to trigger automatic build
3. Download APK from Actions artifacts

## Architecture

- **UI**: Jetpack Compose with Material 3
- **Database**: Room with SQLite
- **Dependency Injection**: Hilt
- **Background Work**: WorkManager
- **Authentication**: AndroidX Biometric

## License

Open source personal finance tracker.
