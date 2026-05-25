# GDB转SHP转换器

这是一个Android应用程序，用于将File Geodatabase (GDB) 格式文件转换为Shapefile (SHP) 格式。

## 功能特性

- 支持将GDB文件中的所有图层转换为SHP文件
- 保持原始坐标系不变
- 简单易用的用户界面
- 显示转换进度
- 支持Android 7.0 (API 24) 及更高版本

## 项目结构

```
.
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/example/gdbtoshp/
│   │       │   ├── MainActivity.java          # 主界面
│   │       │   └── GDBToSHPConverter.java     # 转换核心类
│   │       ├── res/
│   │       │   ├── layout/
│   │       │   │   └── activity_main.xml      # 布局文件
│   │       │   └── values/
│   │       │       ├── strings.xml
│   │       │       ├── colors.xml
│   │       │       └── themes.xml
│   │       └── AndroidManifest.xml
│   └── build.gradle
├── build.gradle
├── settings.gradle
└── gradle.properties
```

## 依赖库

- AndroidX AppCompat
- Material Components
- ConstraintLayout
- GeoTools 29.1 (用于GIS数据处理)

## 构建和安装

### 前置条件

- Android Studio Arctic Fox 或更高版本
- JDK 8 或更高版本
- Android SDK (API 33)

### 构建步骤

**方法一：使用 Android Studio（推荐）**

1. 克隆或下载此项目
2. 打开 Android Studio
3. 选择 "Open an Existing Project"
4. 导航到项目根目录并选择
5. 等待 Gradle 同步完成（首次同步可能需要一些时间下载依赖）
6. 连接 Android 设备或启动模拟器
7. 点击 "Run" 按钮（绿色三角形）构建并安装应用

**方法二：从现有 Android 项目迁移**

1. 在 Android Studio 中创建一个新的空 Android 项目
2. 将以下文件复制到新项目中：
   - `app/src/main/java/com/example/gdbtoshp/` 目录下的所有 Java 文件
   - `app/src/main/res/` 目录下的所有资源文件
   - `app/src/main/AndroidManifest.xml`
3. 更新 `app/build.gradle` 文件，添加 GeoTools 依赖
4. 同步 Gradle 并构建项目

### 关键依赖配置

确保 `app/build.gradle` 中包含以下依赖：

```gradle
dependencies {
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.9.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    
    // GeoTools for GIS data processing
    implementation 'org.geotools:gt-main:29.1'
    implementation 'org.geotools:gt-shapefile:29.1'
    implementation 'org.geotools:gt-geopkg:29.1'
    implementation 'org.geotools:gt-epsg-hsql:29.1'
}
```

同时在 `settings.gradle` 中添加 GeoTools 仓库：

```gradle
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url "https://repo.osgeo.org/repository/release/" }
    }
}
```

### 安装APK

生成的APK文件位于：
```
app/build/outputs/apk/debug/app-debug.apk
```

可以通过以下命令安装到设备：
```bash
./gradlew installDebug
```

## 使用说明

1. 首次启动应用时，请授予存储权限
2. 点击 "选择GDB文件" 按钮，选择要转换的GDB目录
3. 点击 "选择输出目录" 按钮，选择SHP文件保存位置
4. 点击 "转换" 按钮开始转换
5. 等待转换完成，转换后的SHP文件将保存在指定目录

## 注意事项

- GDB文件应为ESRI File Geodatabase格式
- 每个图层将被转换为单独的SHP文件
- 转换过程保持原始坐标系不变
- 对于大文件，转换可能需要一些时间，请耐心等待

## 许可证

本项目仅供学习和研究使用。
