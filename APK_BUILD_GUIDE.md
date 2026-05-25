# GDB转SHP转换器 - APK构建指南

## 📱 应用简介
Android应用，用于将GDB (GeoDatabase) 格式文件转换为SHP (Shapefile) 格式，保持原始坐标系不变。

## 🚀 快速构建APK（三种方式）

### 方式一：使用GitHub Actions自动构建（推荐）

1. **创建GitHub仓库**：
   - 在GitHub上创建一个新仓库
   - 将项目代码上传到仓库（包括 `.github` 文件夹）

2. **触发自动构建**：
   - 推送代码后，GitHub Actions会自动开始构建
   - 在仓库的 "Actions" 标签页查看构建进度

3. **下载APK**：
   - 构建完成后，在工作流摘要中找到 "Artifacts"
   - 下载 `gdb-to-shp-converter-debug-apk.zip`
   - 解压后获得APK文件

### 方式二：在本地使用Android Studio构建

1. **安装Android Studio**：
   - 从 https://developer.android.com/studio 下载并安装
   - 首次启动会自动下载SDK和构建工具

2. **打开项目**：
   - 解压 `GdbToShpConverter.tar.gz`
   - 启动Android Studio
   - 选择 "Open an Existing Project"
   - 选择解压后的项目文件夹

3. **构建APK**：
   - 等待Gradle同步完成（首次可能需要几分钟）
   - 点击菜单 `Build` → `Build Bundle(s) / APK(s)` → `Build APK(s)`
   - 构建完成后，点击通知中的 "locate" 查看APK文件

4. **APK位置**：
   ```
   app/build/outputs/apk/debug/app-debug.apk
   ```

### 方式三：使用命令行构建

```bash
# 1. 进入项目目录
cd GdbToShpConverter

# 2. 确保有Java环境
java -version

# 3. 构建Debug APK
./gradlew assembleDebug

# 4. APK将在以下位置生成
ls -lh app/build/outputs/apk/debug/app-debug.apk
```

## 📦 安装到手机

1. **启用"未知来源"安装**：
   - Android 8.0+：设置 → 安全与隐私 → 更多安全设置 → 安装未知应用
   - 允许您的文件管理器安装应用

2. **传输APK到手机**：
   - 使用USB线连接手机和电脑
   - 将APK复制到手机存储
   - 或使用微信/QQ等工具发送到手机

3. **安装应用**：
   - 在文件管理器中找到APK文件
   - 点击安装
   - 按照提示完成安装

## 🎯 使用应用

1. **授予权限**：首次启动需要授予存储访问权限

2. **选择GDB文件**：点击"选择GDB文件"按钮，选择您的GDB目录

3. **选择输出目录**：点击"选择输出目录"按钮，选择保存位置

4. **开始转换**：点击"转换"按钮开始转换

5. **查看结果**：转换完成后，SHP文件将保存在您选择的输出目录

## ⚙️ 技术规格

- **最小SDK版本**：Android 7.0 (API 24)
- **目标SDK版本**：Android 13 (API 33)
- **构建工具**：Gradle 8.0+
- **JDK版本**：11+
- **核心依赖**：GeoTools 29.1

## 📋 项目结构

```
GdbToShpConverter/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/gdbtoshp/
│   │   │   ├── MainActivity.java          # 主界面
│   │   │   └── GDBToSHPConverter.java     # 转换引擎
│   │   ├── res/
│   │   │   ├── layout/activity_main.xml   # UI布局
│   │   │   └── values/                    # 资源配置
│   │   └── AndroidManifest.xml             # 应用清单
│   └── build.gradle                        # 应用构建配置
├── build.gradle                            # 项目构建配置
├── settings.gradle                         # Gradle设置
└── .github/workflows/build.yml            # GitHub Actions配置
```

## 🔧 常见问题

### Q: 构建时提示 "SDK location not found"
A: 在Android Studio中打开 `File` → `Project Structure` → `SDK Location` 设置SDK路径

### Q: 依赖下载失败
A: 检查网络连接，或在 `gradle.properties` 中配置国内镜像源

### Q: 安装时提示 "解析错误"
A: 确保下载的APK完整，或尝试重新构建

### Q: 应用无法读取GDB文件
A: 确保已授予存储权限，Android 11+需要使用"所有文件访问"权限

## 📄 许可证

本项目仅供学习和研究使用。

---
**需要帮助？** 请查看项目根目录的 `README.md` 文件。
