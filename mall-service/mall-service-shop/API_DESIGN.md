# API 设计文档

## 一、概述

### 1.1 接口规范

- **协议**: HTTP/HTTPS
- **请求方式**: GET、POST、DELETE
- **数据格式**: JSON
- **字符编码**: UTF-8
- **认证方式**: JWT Token（Header: Authorization: Bearer {token}）

### 1.2 统一响应格式

**成功响应：**
```json
{
  "code": 200,
  "msg": "success",
  "data": {}
}
```

**失败响应：**
```json
{
  "code": 500,
  "msg": "错误信息",
  "data": null
}
```

**分页响应：**
```json
{
  "code": 200,
  "msg": "success",
  "data": {
    "list": [],
    "total": 100,
    "pageNum": 1,
    "pageSize": 10,
    "totalPages": 10
  }
}
```

---

## 二、店铺管理接口

### 2.1 店铺列表

**接口地址：** `GET /shop/list`

**接口说明：** 查询店铺列表（分页）

**请求参数：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pageNum | Integer | 否 | 页码，默认1 |
| pageSize | Integer | 否 | 每页大小，默认10 |
| shopName | String | 否 | 店铺名称（模糊查询） |
| shopType | Integer | 否 | 店铺类型 |
| status | Integer | 否 | 状态 |

**响应示例：**
```json
{
  "code": 200,
  "msg": "success",
  "data": {
    "list": [
      {
        "id": 1,
        "parentId": 0,
        "shopCode": "SHOP20250120001",
        "shopName": "星巴克总部",
        "shopType": 2,
        "province": "北京市",
        "city": "北京市",
        "district": "朝阳区",
        "address": "建国路88号",
        "contactName": "张三",
        "contactPhone": "13800138000",
        "businessHours": "08:00-22:00",
        "status": 1,
        "createdAt": "2025-01-20 10:00:00",
        "branchCount": 5
      }
    ],
    "total": 1,
    "pageNum": 1,
    "pageSize": 10
  }
}
```

---

### 2.2 店铺详情

**接口地址：** `GET /shop/{id}`

**接口说明：** 查询店铺详细信息

**路径参数：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | Long | 是 | 店铺ID |

**响应示例：**
```json
{
  "code": 200,
  "msg": "success",
  "data": {
    "id": 1,
    "parentId": 0,
    "shopCode": "SHOP20250120001",
    "shopName": "星巴克总部",
    "shopType": 2,
    "province": "北京市",
    "city": "北京市",
    "district": "朝阳区",
    "address": "建国路88号",
    "longitude": 116.447398,
    "latitude": 39.906217,
    "contactName": "张三",
    "contactPhone": "13800138000",
    "businessHours": "08:00-22:00",
    "logo": "https://example.com/logo.png",
    "images": ["https://example.com/img1.jpg"],
    "description": "星巴克总部店铺",
    "status": 1,
    "createdAt": "2025-01-20 10:00:00"
  }
}
```

---

### 2.3 创建店铺

**接口地址：** `POST /shop/add`

**接口说明：** 创建店铺（单店/分店）

**请求参数：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| parentId | Long | 否 | 父店铺ID（创建分店时必填） |
| shopName | String | 是 | 店铺名称 |
| shopType | Integer | 是 | 店铺类型：1-单店，3-连锁分店 |
| province | String | 否 | 省份 |
| city | String | 否 | 城市 |
| district | String | 否 | 区县 |
| address | String | 否 | 详细地址 |
| longitude | Decimal | 否 | 经度 |
| latitude | Decimal | 否 | 纬度 |
| contactName | String | 否 | 联系人 |
| contactPhone | String | 否 | 联系电话 |
| businessHours | String | 否 | 营业时间 |
| logo | String | 否 | 店铺Logo |
| description | String | 否 | 店铺描述 |

**请求示例（创建分店）：**
```json
{
  "parentId": 1,
  "shopName": "星巴克朝阳门店",
  "shopType": 3,
  "province": "北京市",
  "city": "北京市",
  "district": "朝阳区",
  "address": "朝阳门外大街1号",
  "contactName": "王五",
  "contactPhone": "13700137000",
  "businessHours": "08:00-22:00"
}
```

**响应示例：**
```json
{
  "code": 200,
  "msg": "创建成功",
  "data": {
    "shopId": 2,
    "shopCode": "SHOP20250120002"
  }
}
```

---

### 2.4 更新店铺

**接口地址：** `POST /shop/update`

**接口说明：** 更新店铺信息

**请求参数：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | Long | 是 | 店铺ID |
| shopName | String | 否 | 店铺名称 |
| contactName | String | 否 | 联系人 |
| contactPhone | String | 否 | 联系电话 |
| businessHours | String | 否 | 营业时间 |
| description | String | 否 | 店铺描述 |

**请求示例：**
```json
{
  "id": 1,
  "shopName": "星巴克总部（更新）",
  "businessHours": "07:00-23:00"
}
```

**响应示例：**
```json
{
  "code": 200,
  "msg": "更新成功",
  "data": null
}
```

---

### 2.5 删除店铺

**接口地址：** `DELETE /shop/{id}`

**接口说明：** 删除店铺（逻辑删除）

**路径参数：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | Long | 是 | 店铺ID |

**响应示例：**
```json
{
  "code": 200,
  "msg": "删除成功",
  "data": null
}
```

---

### 2.6 店铺树形结构

**接口地址：** `GET /shop/tree`

**接口说明：** 查询店铺树形结构（连锁品牌）

**请求参数：**

无

**响应示例：**
```json
{
  "code": 200,
  "msg": "success",
  "data": [
    {
      "id": 1,
      "shopCode": "SHOP20250120001",
      "shopName": "星巴克总部",
      "shopType": 2,
      "status": 1,
      "children": [
        {
          "id": 2,
          "shopCode": "SHOP20250120002",
          "shopName": "星巴克朝阳门店",
          "shopType": 3,
          "status": 1,
          "children": []
        }
      ]
    }
  ]
}
```

---

## 三、用户店铺关联接口

### 3.1 获取我的店铺列表

**接口地址：** `GET /user-shop/my-shops`

**接口说明：** 获取当前用户可管理的店铺列表

**响应示例：**
```json
{
  "code": 200,
  "msg": "success",
  "data": [
    {
      "shopId": 1,
      "shopCode": "SHOP20250120001",
      "shopName": "星巴克总部",
      "shopType": 2,
      "isDefault": 1
    },
    {
      "shopId": 2,
      "shopCode": "SHOP20250120002",
      "shopName": "星巴克朝阳门店",
      "shopType": 3,
      "isDefault": 0
    }
  ]
}
```

---

### 3.2 切换店铺

**接口地址：** `POST /user-shop/switch`

**接口说明：** 切换当前操作的店铺

**请求参数：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| shopId | Long | 是 | 店铺ID |

**请求示例：**
```json
{
  "shopId": 2
}
```

**响应示例：**
```json
{
  "code": 200,
  "msg": "切换成功",
  "data": {
    "shopId": 2,
    "shopName": "星巴克朝阳门店",
    "shopType": 3
  }
}
```

---

### 3.3 设置默认店铺

**接口地址：** `POST /user-shop/set-default`

**接口说明：** 设置默认店铺

**请求参数：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| shopId | Long | 是 | 店铺ID |

**响应示例：**
```json
{
  "code": 200,
  "msg": "设置成功",
  "data": null
}
```

---

### 3.4 分配用户到店铺

**接口地址：** `POST /user-shop/assign`

**接口说明：** 分配用户到店铺（管理员操作）

**请求参数：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userId | Long | 是 | 用户ID |
| shopId | Long | 是 | 店铺ID |
| isDefault | Integer | 否 | 是否默认店铺，默认0 |

**请求示例：**
```json
{
  "userId": 10,
  "shopId": 2,
  "isDefault": 0
}
```

**响应示例：**
```json
{
  "code": 200,
  "msg": "分配成功",
  "data": null
}
```

---

### 3.5 移除用户店铺权限

**接口地址：** `DELETE /user-shop/remove`

**接口说明：** 移除用户的店铺权限

**请求参数：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userId | Long | 是 | 用户ID |
| shopId | Long | 是 | 店铺ID |

**请求示例：**
```json
{
  "userId": 10,
  "shopId": 2
}
```

**响应示例：**
```json
{
  "code": 200,
  "msg": "移除成功",
  "data": null
}
```
