import { PlusOutlined } from '@ant-design/icons';
import type { ActionType, ProColumns } from '@ant-design/pro-components';
import { PageContainer, ProTable } from '@ant-design/pro-components';
import { Button, message, Popconfirm } from 'antd';
import React, { useRef, useState } from 'react';
import { Access, useAccess } from '@umijs/max';
import { listUsers, deleteUser } from '@/services/system/user';
import UserModal from './components/UserModal';

const UserList: React.FC = () => {
  const [createModalVisible, setCreateModalVisible] = useState<boolean>(false);
  const [updateModalVisible, setUpdateModalVisible] = useState<boolean>(false);
  const [currentRow, setCurrentRow] = useState<API.SysUser>();
  const actionRef = useRef<ActionType>();
  const access = useAccess();

  const columns: ProColumns<API.SysUser>[] = [
    {
      title: 'ID',
      dataIndex: 'id',
      width: 80,
      search: false,
    },
    {
      title: '用户名',
      dataIndex: 'username',
      width: 120,
    },
    {
      title: '昵称',
      dataIndex: 'nickname',
      width: 120,
    },
    {
      title: '手机号',
      dataIndex: 'phone',
      width: 120,
      search: false,
    },
    {
      title: '邮箱',
      dataIndex: 'email',
      width: 180,
      search: false,
    },
    {
      title: '状态',
      dataIndex: 'status',
      width: 100,
      valueEnum: {
        0: { text: '禁用', status: 'Error' },
        1: { text: '正常', status: 'Success' },
      },
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      width: 180,
      search: false,
    },
    {
      title: '操作',
      dataIndex: 'option',
      valueType: 'option',
      width: 200,
      render: (_, record) => [
        <Access key="edit" accessible={access.canUserEdit}>
          <a
            onClick={() => {
              setCurrentRow(record);
              setUpdateModalVisible(true);
            }}
          >
            编辑
          </a>
        </Access>,
        <Access key="delete" accessible={access.canUserDelete}>
          <Popconfirm
            title="确定要删除该用户吗？"
            onConfirm={async () => {
              const res = await deleteUser(record.id!);
              if (res.code === 200) {
                message.success('删除成功');
                actionRef.current?.reload();
              }
            }}
          >
            <a style={{ color: 'red' }}>删除</a>
          </Popconfirm>
        </Access>,
      ],
    },
  ];

  return (
    <PageContainer>
      <ProTable<API.SysUser>
        headerTitle="用户列表"
        actionRef={actionRef}
        rowKey="id"
        search={{
          labelWidth: 120,
        }}
        toolBarRender={() => [
          <Access key="add" accessible={access.canUserAdd}>
            <Button
              type="primary"
              onClick={() => {
                setCreateModalVisible(true);
              }}
            >
              <PlusOutlined /> 新建
            </Button>
          </Access>,
        ]}
        request={async (params) => {
          const res = await listUsers({
            pageNum: params.current,
            pageSize: params.pageSize,
          });
          return {
            data: res.data?.list || [],
            success: res.code === 200,
            total: res.data?.total || 0,
          };
        }}
        columns={columns}
      />
      
      <UserModal
        visible={createModalVisible}
        onCancel={() => setCreateModalVisible(false)}
        onSuccess={() => {
          setCreateModalVisible(false);
          actionRef.current?.reload();
        }}
      />
      
      <UserModal
        visible={updateModalVisible}
        current={currentRow}
        onCancel={() => {
          setUpdateModalVisible(false);
          setCurrentRow(undefined);
        }}
        onSuccess={() => {
          setUpdateModalVisible(false);
          setCurrentRow(undefined);
          actionRef.current?.reload();
        }}
      />
    </PageContainer>
  );
};

export default UserList;
