#!/bin/bash

# 应用签名配置脚本
# 此脚本检查是否提供了所有必需的签名secrets，如果提供了则设置相应的环境变量

set -e

echo "========== 开始检查签名配置 =========="

# 检查所有必需的签名secrets是否存在
if [ -n "$RELEASE_STORE_FILE_BASE64" ] && \
   [ -n "$RELEASE_STORE_PASSWORD" ] && \
   [ -n "$RELEASE_KEY_ALIAS" ] && \
   [ -n "$RELEASE_KEY_PASSWORD" ]; then
    
    echo "检测到所有签名secrets，开始配置正式签名..."
    
    # 解码base64编码的密钥库文件
    echo "$RELEASE_STORE_FILE_BASE64" | base64 -d > release-key.keystore
    
    # 验证解码后的文件
    if [ -f "release-key.keystore" ] && [ -s "release-key.keystore" ]; then
        echo "密钥库文件解码成功"
    else
        echo "错误：密钥库文件解码失败"
        exit 1
    fi
    
    # 设置环境变量供Gradle使用
    echo "RELEASE_STORE_FILE=$(pwd)/release-key.keystore" >> $GITHUB_ENV
    echo "RELEASE_STORE_PASSWORD=$RELEASE_STORE_PASSWORD" >> $GITHUB_ENV
    echo "RELEASE_KEY_ALIAS=$RELEASE_KEY_ALIAS" >> $GITHUB_ENV
    echo "RELEASE_KEY_PASSWORD=$RELEASE_KEY_PASSWORD" >> $GITHUB_ENV
    
    echo "签名环境变量已设置"
    echo "密钥库文件路径: $(pwd)/release-key.keystore"
else
    echo "未提供完整的签名secrets，将使用调试签名"
    echo "注意：如需使用正式签名，请在GitHub仓库设置中添加以下secrets："
    echo "  - RELEASE_STORE_FILE_BASE64: base64编码的密钥库文件"
    echo "  - RELEASE_STORE_PASSWORD: 密钥库密码"
    echo "  - RELEASE_KEY_ALIAS: 密钥别名"
    echo "  - RELEASE_KEY_PASSWORD: 密钥密码"
    
    # 确保调试密钥库存在
    DEBUG_KEYSTORE_PATH="$HOME/.android/debug.keystore"
    echo "检查调试密钥库: $DEBUG_KEYSTORE_PATH"
    
    # 检查keytool是否可用
    if ! command -v keytool &> /dev/null; then
        echo "错误：keytool命令不可用，请确保JDK已正确安装"
        exit 1
    fi
    
    echo "keytool路径: $(which keytool)"
    echo "当前用户: $(whoami)"
    echo "当前目录: $(pwd)"
    
    if [ ! -f "$DEBUG_KEYSTORE_PATH" ]; then
        echo "调试密钥库不存在，正在创建..."
        mkdir -p "$(dirname "$DEBUG_KEYSTORE_PATH")"
        echo "创建目录: $(dirname "$DEBUG_KEYSTORE_PATH")"
        ls -la "$(dirname "$DEBUG_KEYSTORE_PATH")"
        
        # 使用keytool生成调试密钥库
        echo "开始生成调试密钥库..."
        keytool -genkey -v \
            -keystore "$DEBUG_KEYSTORE_PATH" \
            -alias androiddebugkey \
            -keyalg RSA \
            -keysize 2048 \
            -validity 10000 \
            -storepass android \
            -keypass android \
            -dname "CN=Android Debug,O=Android,C=US" 2>&1
        
        KEYTOOL_EXIT_CODE=$?
        echo "keytool退出码: $KEYTOOL_EXIT_CODE"
        
        if [ $KEYTOOL_EXIT_CODE -eq 0 ] && [ -f "$DEBUG_KEYSTORE_PATH" ]; then
            echo "调试密钥库创建成功"
            ls -la "$DEBUG_KEYSTORE_PATH"
        else
            echo "错误：无法创建调试密钥库"
            exit 1
        fi
    else
        echo "调试密钥库已存在"
        ls -la "$DEBUG_KEYSTORE_PATH"
    fi
fi

echo "========== 签名配置检查完成 =========="