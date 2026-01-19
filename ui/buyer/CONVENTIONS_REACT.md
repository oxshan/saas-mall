# React 前端开发规范

本项目使用 **Ant Design Pro** + **UmiJS** 技术栈。

## 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| UmiJS | 4.x | 企业级前端框架 |
| Ant Design | 5.x | UI 组件库 |
| Ant Design Pro Components | 2.x | 高级业务组件 |
| TypeScript | 5.x | 类型系统 |

## 目录结构

```
src/
├── pages/           # 页面组件
│   ├── Login/       # 登录页
│   ├── Dashboard/   # 工作台
│   └── System/      # 系统管理
├── services/        # API 服务
├── types/           # 类型定义
├── utils/           # 工具函数
├── access.ts        # 权限配置
└── app.tsx          # 运行时配置
```

## 命名规范

### 文件命名
- 页面组件: `PascalCase` 目录 + `index.tsx`
- 服务文件: `camelCase.ts`
- 类型文件: `camelCase.ts`

### 变量命名
- 组件: `PascalCase`
- 函数/变量: `camelCase`
- 常量: `UPPER_SNAKE_CASE`
- 类型/接口: `PascalCase`

## 组件规范

### 页面组件

使用 `PageContainer` 包裹页面内容：

```tsx
import { PageContainer } from '@ant-design/pro-components';

const MyPage: React.FC = () => {
  return (
    <PageContainer>
      {/* 页面内容 */}
    </PageContainer>
  );
};

export default MyPage;
```

### 表格页面

使用 `ProTable` 实现 CRUD 表格：

```tsx
import { ProTable } from '@ant-design/pro-components';
import type { ActionType, ProColumns } from '@ant-design/pro-components';

const columns: ProColumns<DataType>[] = [
  { title: '名称', dataIndex: 'name' },
  { title: '操作', valueType: 'option', render: () => [...] },
];

<ProTable
  actionRef={actionRef}
  columns={columns}
  rowKey="id"
  request={async (params) => {
    const res = await fetchData(params);
    return { data: res.list, total: res.total, success: true };
  }}
/>
```

## API 服务规范

### 服务文件结构

```tsx
// services/user.ts
import { request } from '@umijs/max';

export async function getUserList(params: PageParams) {
  return request<PageResult<UserItem>>('/admin/user/page', {
    method: 'GET',
    params,
  });
}
```

### 响应数据处理

统一在 `utils/request.ts` 中处理响应拦截：
- 成功: 返回 `res.data`
- 失败: 显示错误消息并 reject
- 401: 清除 token 并跳转登录页

## 表单规范

使用 `ModalForm` 或 `DrawerForm` 实现表单弹窗：

```tsx
import { ModalForm, ProFormText } from '@ant-design/pro-components';

<ModalForm
  title="新增用户"
  open={open}
  onOpenChange={setOpen}
  onFinish={async (values) => {
    await createUser(values);
    message.success('保存成功');
    return true;
  }}
>
  <ProFormText name="username" label="用户名" rules={[{ required: true }]} />
</ModalForm>
```

## 类型定义规范

所有类型定义放在 `src/types/` 目录下：

```tsx
// types/common.ts
export interface Result<T> {
  code: number;
  msg: string;
  data: T;
}

export interface PageResult<T> {
  list: T[];
  total: number;
}
```

