import { PlusOutlined } from '@ant-design/icons';
import type { ActionType, ProColumns } from '@ant-design/pro-components';
import { PageContainer, ProTable } from '@ant-design/pro-components';
import { Button, message, Popconfirm } from 'antd';
import React, { useRef, useState } from 'react';
import { Access, useAccess } from '@umijs/max';
import { listRoles, deleteRole } from '@/services/system/role';
import RoleModal from './components/RoleModal';
import MenuTreeModal from './components/MenuTreeModal';

const RoleList: React.FC = () => {
  const [createModalVisible, setCreateModalVisible] = useState<boolean>(false);
  const [updateModalVisible, setUpdateModalVisible] = useState<boolean>(false);
  const [menuModalVisible, setMenuModalVisible] = useState<boolean>(false);
  const [currentRow, setCurrentRow] = useState<API.SysRole>();
  const actionRef = useRef<ActionType>();
  const access = useAccess();

  const columns: ProColumns<API.SysRole>[] = [
    {
      title: 'ID',
      dataIndex: 'id',
      width: 80,
      search: false,
    },
    {
      title: '角色编码',
      dataIndex: 'code',
      width: 150,
    },
    {
      title: '角色名称',
      dataIndex: 'name',
      width: 150,
    },
    {
      title: '角色类型',
      dataIndex: 'type',
      width: 120,
      valueEnum: {
        1: { text: '管理员', status: 'Success' },
        2: { text: '普通用户', status: 'Default' },
      },
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
      width: 250,
      render: (_, record) => [
        <a
          key="menu"
          onClick={() => {
            setCurrentRow(record);
            setMenuModalVisible(true);
          }}
        >
          分配菜单
        </a>,
        <Access key="edit" accessible={access.canRoleEdit}>
          <a
            onClick={() => {
              setCurrentRow(record);
              setUpdateModalVisible(true);
            }}
          >
            编辑
          </a>
        </Access>,
        <Access key="delete" accessible={access.canRoleDelete}>
          <Popconfirm
            title="确定要删除该角色吗？"
            onConfirm={async () => {
              const res = await deleteRole(record.id!);
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
      <ProTable<API.SysRole>
        headerTitle="角色列表"
        actionRef={actionRef}
        rowKey="id"
        search={{
          labelWidth: 120,
        }}
        toolBarRender={() => [
          <Access key="add" accessible={access.canRoleAdd}>
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
          const res = await listRoles({
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
      
      <RoleModal
        visible={createModalVisible}
        onCancel={() => setCreateModalVisible(false)}
        onSuccess={() => {
          setCreateModalVisible(false);
          actionRef.current?.reload();
        }}
      />
      
      <RoleModal
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
      
      <MenuTreeModal
        visible={menuModalVisible}
        roleId={currentRow?.id}
        onCancel={() => {
          setMenuModalVisible(false);
          setCurrentRow(undefined);
        }}
      />
    </PageContainer>
  );
};

export default RoleList;
