import { PlusOutlined } from '@ant-design/icons';
import {
  PageContainer,
  ProTable,
  ModalForm,
  ProFormText,
  ProFormSelect,
} from '@ant-design/pro-components';
import { Button, message, Popconfirm, Tag } from 'antd';
import { useState, useRef } from 'react';
import type { ActionType, ProColumns } from '@ant-design/pro-components';
import { getUserList, createUser, updateUser, deleteUser, type UserItem } from '@/services/user';
import { getAllRoles } from '@/services/role';

const UserPage: React.FC = () => {
  const actionRef = useRef<ActionType>();
  const [modalOpen, setModalOpen] = useState(false);
  const [currentRow, setCurrentRow] = useState<UserItem>();

  const columns: ProColumns<UserItem>[] = [
    { title: '用户名', dataIndex: 'username', width: 120 },
    { title: '昵称', dataIndex: 'nickname', width: 120 },
    { title: '邮箱', dataIndex: 'email', width: 180, search: false },
    { title: '手机号', dataIndex: 'phone', width: 140, search: false },
    {
      title: '状态',
      dataIndex: 'status',
      width: 80,
      search: false,
      render: (_, record) => (
        <Tag color={record.status === 1 ? 'green' : 'red'}>
          {record.status === 1 ? '启用' : '禁用'}
        </Tag>
      ),
    },
    { title: '创建时间', dataIndex: 'createTime', width: 180, search: false },
    {
      title: '操作',
      width: 150,
      valueType: 'option',
      render: (_, record) => [
        <a key="edit" onClick={() => { setCurrentRow(record); setModalOpen(true); }}>
          编辑
        </a>,
        <Popconfirm
          key="delete"
          title="确定删除？"
          onConfirm={async () => {
            await deleteUser(record.id);
            message.success('删除成功');
            actionRef.current?.reload();
          }}
        >
          <a style={{ color: '#ff4d4f' }}>删除</a>
        </Popconfirm>,
      ],
    },
  ];

  return (
    <PageContainer>
      <ProTable<UserItem>
        actionRef={actionRef}
        columns={columns}
        rowKey="id"
        request={async (params) => {
          const res = await getUserList({
            current: params.current,
            pageSize: params.pageSize,
            username: params.username,
          });
          return { data: res.list, total: res.total, success: true };
        }}
        toolBarRender={() => [
          <Button
            key="add"
            type="primary"
            icon={<PlusOutlined />}
            onClick={() => { setCurrentRow(undefined); setModalOpen(true); }}
          >
            新增用户
          </Button>,
        ]}
      />
      <ModalForm
        title={currentRow ? '编辑用户' : '新增用户'}
        open={modalOpen}
        onOpenChange={setModalOpen}
        initialValues={currentRow}
        onFinish={async (values) => {
          if (currentRow) {
            await updateUser(currentRow.id, values);
          } else {
            await createUser(values);
          }
          message.success('保存成功');
          setModalOpen(false);
          actionRef.current?.reload();
          return true;
        }}
      >
        <ProFormText name="username" label="用户名" rules={[{ required: true }]} />
        <ProFormText name="nickname" label="昵称" rules={[{ required: true }]} />
        {!currentRow && (
          <ProFormText.Password name="password" label="密码" rules={[{ required: true }]} />
        )}
        <ProFormText name="email" label="邮箱" />
        <ProFormText name="phone" label="手机号" />
        <ProFormSelect
          name="roleIds"
          label="角色"
          mode="multiple"
          request={async () => {
            const roles = await getAllRoles();
            return roles.map((r) => ({ label: r.name, value: r.id }));
          }}
        />
      </ModalForm>
    </PageContainer>
  );
};

export default UserPage;
