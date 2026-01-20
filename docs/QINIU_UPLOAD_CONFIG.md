# 七牛云上传配置指南

## 一、获取七牛云账号和密钥

### 1. 注册七牛云账号

访问 [七牛云官网](https://www.qiniu.com/) 并注册账号。

### 2. 创建存储空间

1. 登录七牛云控制台
2. 进入"对象存储" → "空间管理"
3. 点击"新建空间"
4. 填写空间名称（例如：saas-mall）
5. 选择存储区域（建议选择离用户最近的区域）
6. 访问控制选择"公开"（用于图片访问）
7. 点击"确定创建"

### 3. 获取 AccessKey 和 SecretKey

1. 点击右上角头像 → "密钥管理"
2. 查看或创建 AccessKey 和 SecretKey
3. 妥善保管这两个密钥，不要泄露

### 4. 配置 CDN 加速域名

1. 进入"对象存储" → "空间管理"
2. 点击刚创建的空间
3. 进入"域名管理"
4. 绑定自定义域名或使用测试域名
5. 记录 CDN 域名（例如：cdn.example.com）

## 二、配置后端服务

### 1. 配置环境变量

在 `mall-admin/src/main/resources/application.yml` 中已经配置了七牛云参数，你需要通过环境变量设置：

```bash
export QINIU_ACCESS_KEY=your-access-key
export QINIU_SECRET_KEY=your-secret-key
export QINIU_BUCKET=your-bucket-name
export QINIU_DOMAIN=your-cdn-domain.com
```

或者直接修改 `application.yml` 文件中的默认值。

### 2. 配置示例

```yaml
qiniu:
  access-key: your-access-key-here
  secret-key: your-secret-key-here
  bucket: saas-mall
  domain: cdn.example.com
  region: z0  # z0=华东, z1=华北, z2=华南, na0=北美, as0=东南亚
```

## 三、使用说明

### 1. 上传流程

1. 前端调用 `/api/upload/token` 接口获取上传凭证
2. 使用七牛云 SDK 直接上传文件到七牛云
3. 上传成功后获得文件 key
4. 通过 CDN 域名访问文件：`https://your-domain.com/file-key`

### 2. 店铺 Logo 上传

在店铺管理页面：
1. 点击"新建店铺"或"编辑店铺"
2. 在"店铺 Logo"字段点击上传按钮
3. 选择图片文件（支持 jpg、png、gif 等格式）
4. 图片大小限制：2MB
5. 上传成功后会自动显示预览

### 3. 注意事项

- 确保七牛云存储空间的访问控制设置为"公开"
- CDN 域名需要完成备案（使用测试域名可跳过）
- 上传的文件会存储在 `shop/logo/` 目录下
- 文件名格式：`时间戳_随机字符串.扩展名`

## 四、故障排查

### 1. 获取 Token 失败

检查：
- 后端服务是否正常运行
- 七牛云配置是否正确
- AccessKey 和 SecretKey 是否有效

### 2. 上传失败

检查：
- 网络连接是否正常
- 存储空间是否存在
- 存储空间是否有足够的容量
- 浏览器控制台是否有错误信息

### 3. 图片无法访问

检查：
- CDN 域名配置是否正确
- 存储空间访问控制是否为"公开"
- 文件是否上传成功

## 五、相关文件

### 后端文件

- `mall-common/mall-common-upload/` - 上传模块
- `mall-common/mall-common-upload/src/main/java/com/mall/common/upload/config/QiniuProperties.java` - 配置类
- `mall-common/mall-common-upload/src/main/java/com/mall/common/upload/service/QiniuUploadService.java` - 上传服务
- `mall-common/mall-common-upload/src/main/java/com/mall/common/upload/controller/UploadController.java` - 上传接口
- `mall-admin/src/main/resources/application.yml` - 配置文件

### 前端文件

- `ui/admin/src/services/upload.ts` - 上传服务接口
- `ui/admin/src/components/ImageUpload/index.tsx` - 图片上传组件
- `ui/admin/src/pages/shop/list/components/ShopModal.tsx` - 店铺表单（集成了 logo 上传）
