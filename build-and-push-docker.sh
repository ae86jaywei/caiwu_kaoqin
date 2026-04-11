#!/bin/bash

# 构建并推送 Android 构建 Docker 镜像到 GitHub Container Registry

# 设置变量
IMAGE_NAME="android-build-docker"
GITHUB_USERNAME="ae86jaywei"
TAG="latest"

# 进入 docker-android-build-box 目录
cd docker-android-build-box

# 登录到 GitHub Container Registry
echo "登录到 GitHub Container Registry..."
echo $CR_PAT | docker login ghcr.io -u $GITHUB_USERNAME --password-stdin

# 构建 Docker 镜像
echo "构建 Docker 镜像..."
docker buildx build \
  --platform linux/amd64,linux/arm64 \
  -t ghcr.io/$GITHUB_USERNAME/$IMAGE_NAME:$TAG \
  -t ghcr.io/$GITHUB_USERNAME/$IMAGE_NAME:$(date +%Y%m%d) \
  --push .

# 构建完成
echo "Docker 镜像构建并推送完成！"
echo "镜像地址: ghcr.io/$GITHUB_USERNAME/$IMAGE_NAME:$TAG"

# 返回到原始目录
cd ..