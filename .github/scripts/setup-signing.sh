#!/bin/bash

# 应用签名配置脚本
# 此脚本检查是否提供了所有必需的签名secrets，如果提供了则设置相应的环境变量

set -e

echo "检查签名配置..."

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
fi

echo "签名配置检查完成"