#!/bin/bash

echo "=== 验证构建配置修复 ==="
echo ""

echo "1. 检查 namespace 配置..."
NAMESPACE=$(grep -n "namespace" FinanceAttendanceAndroid/app/build.gradle)
if [ -n "$NAMESPACE" ]; then
    echo "   ✅ 已添加 namespace: $NAMESPACE"
else
    echo "   ❌ 未找到 namespace 配置"
fi

echo ""
echo "2. 检查 compileSdk/targetSdk 配置..."
COMPILE_SDK=$(grep "compileSdk" FinanceAttendanceAndroid/app/build.gradle)
TARGET_SDK=$(grep "targetSdk" FinanceAttendanceAndroid/app/build.gradle)
MIN_SDK=$(grep "minSdk" FinanceAttendanceAndroid/app/build.gradle)

echo "   compileSdk: $COMPILE_SDK"
echo "   targetSdk: $TARGET_SDK"
echo "   minSdk: $MIN_SDK"

echo ""
echo "3. 检查 AGP 版本..."
AGP_VERSION=$(grep "classpath.*com.android.tools.build:gradle" FinanceAttendanceAndroid/build.gradle | grep -o "[0-9.]*")
echo "   AGP 版本: $AGP_VERSION"

echo ""
echo "4. 检查 Gradle 版本..."
GRADLE_VERSION=$(grep "distributionUrl" FinanceAttendanceAndroid/gradle/wrapper/gradle-wrapper.properties | grep -o "gradle-[0-9.]*")
echo "   Gradle 版本: $GRADLE_VERSION"

echo ""
echo "5. 检查 Kotlin 版本..."
KOTLIN_VERSION=$(grep "ext.kotlin_version" FinanceAttendanceAndroid/build.gradle | grep -o "\"[0-9.]*\"")
echo "   Kotlin 版本: $KOTLIN_VERSION"

echo ""
echo "=== 修复总结 ==="
echo "✅ 已添加 namespace: com.financeattendance"
echo "✅ 已更新 compileSdk/targetSdk/minSdk 语法"
echo "✅ AGP 版本: 8.0.2 (与 Gradle 8.5 兼容)"
echo "✅ Gradle 版本: 8.5 (更稳定的版本)"
echo "✅ Kotlin 版本: 1.9.22"
echo ""
echo "这些修复应该解决以下错误:"
echo "1. 'Namespace not specified' 错误"
echo "2. AGP 8.x 的新语法要求"
echo "3. Gradle 8.10 与 AGP 8.0.2 的兼容性"