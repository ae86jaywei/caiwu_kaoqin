#!/bin/bash

# 构建 Android 构建 Docker 镜像

# 设置变量
IMAGE_NAME="android-build-docker"
TAG="latest"

# 进入 docker-android-build-box 目录
cd docker-android-build-box

# 构建 Docker 镜像（使用 tagged 版本以确保可重复性）
echo "构建 Docker 镜像（使用 tagged 版本）..."
docker buildx build \
  --target complete \
  --build-arg ANDROID_SDK_TOOLS_TAGGED=tagged \
  --build-arg ANDROID_SDKS=tagged \
  --build-arg NDK_TAGGED=tagged \
  --build-arg NODE_TAGGED=tagged \
  --build-arg BUNDLETOOL_TAGGED=tagged \
  --build-arg FLUTTER_TAGGED=tagged \
  --build-arg JENV_TAGGED=tagged \
  -t $IMAGE_NAME:$TAG .

# 构建完成
echo "Docker 镜像构建完成！"
echo "镜像标签: $IMAGE_NAME:$TAG"
echo ""
echo "使用方法:"
echo "1. 标记镜像: docker tag $IMAGE_NAME:$TAG ae86jaywei/$IMAGE_NAME:$TAG"
echo "2. 推送到 Docker Hub: docker push ae86jaywei/$IMAGE_NAME:$TAG"
echo "3. 或推送到 GitHub Container Registry: docker tag $IMAGE_NAME:$TAG ghcr.io/ae86jaywei/$IMAGE_NAME:$TAG && docker push ghcr.io/ae86jaywei/$IMAGE_NAME:$TAG"

# 返回到原始目录
cd ..