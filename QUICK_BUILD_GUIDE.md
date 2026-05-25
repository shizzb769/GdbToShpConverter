# GDB转SHP转换器 - 快速构建指南

## 项目已准备完成！

您需要做的就是在本地计算机上使用Android Studio打开并构建项目。

## 快速开始（5分钟）

### 1. 下载项目
下载 `GdbToShpConverter.tar.gz` 文件到您的电脑

### 2. 解压项目
```bash
tar -xzvf GdbToShpConverter.tar.gz
```
或者使用Windows的7-Zip/WinRAR解压

### 3. 使用Android Studio打开
1. 启动 Android Studio
2. 选择 "Open an Existing Project"
3. 导航到解压后的 `GdbToShpConverter` 文件夹
4. 点击 "OK"

### 4. 等待依赖下载
- 首次打开会下载Gradle和所有依赖
- 可能需要10-30分钟（取决于网络速度）
- 请耐心等待，直到看到 "Gradle sync completed"

### 5. 构建APK
1. 在右侧菜单找到 "Gradle" 面板
2. 展开 `app` → `Tasks` → `build`
3. 双击 `assembleDebug`
4. 等待构建完成

### 6. 获取APK
构建成功后，APK位于：
```
app/build/outputs/apk/debug/app-debug.apk
```

## 或者使用命令行快速构建

```bash
# 进入项目目录
cd GdbToShpConverter

# 授权gradlew
chmod +x gradlew

# 构建debug版本
./gradlew assembleDebug

# APK将在 app/build/outputs/apk/debug/app-debug.apk
```

## 系统要求
- Android Studio Arctic Fox 或更高
- JDK 8 或更高
- 至少 4GB RAM
- 稳定的网络连接（用于下载依赖）

## 项目包含内容
✅ 完整的Android应用源代码
✅ GeoTools库集成（GDB/SHP转换引擎）
✅ Material Design UI界面
✅ 存储权限处理
✅ 异步转换处理
✅ 进度显示功能

## 功能特性
- 📁 支持选择GDB文件目录
- 📂 支持选择输出目录
- 🔄 自动转换所有图层
- 📊 实时显示转换进度
- 🗺️ 保持原始坐标系不变

## 如果遇到问题

### 问题1：Gradle下载失败
解决：配置国内镜像
在 `gradle.properties` 中添加：
```properties
distributionUrl=https\://mirrors.aliyun.com/gradle/gradle-8.0-bin.zip
```

### 问题2：SDK相关错误
解决：在Android Studio中通过 SDK Manager 安装所需平台

### 问题3：编译错误
解决：尝试 File → Invalidate Caches / Restart

## 技术支持
项目使用：
- Android Gradle Plugin 8.2.2
- GeoTools 29.1
- Android SDK 33
- minSdk 24 (Android 7.0)

## 下一步
1. 成功构建后，将 `app-debug.apk` 传输到手机
2. 在手机上安装APK（可能需要允许"安装未知来源应用"）
3. 打开应用，开始转换您的GDB文件！

祝您使用愉快！🎉
