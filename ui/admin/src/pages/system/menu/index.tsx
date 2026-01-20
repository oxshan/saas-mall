import { PlusOutlined } from '@ant-design/icons';
import type { ActionType, ProColumns } from '@ant-design/pro-components';
import { PageContainer, ProTable } from '@ant-design/pro-components';
import { Button, message, Popconfirm, Tag } from 'antd';
import React, { useRef, useState } from 'react';
import { Access, useAccess } from '@umijs/max';
import { getMenuTree, deleteMenu } from '@/services/system/menu';
import MenuModal from './components/MenuModal';

const MenuList: React.FC = () => {
  const [createModalVisible, setCreateModalVisible] = useState<boolean>(false);
  const [updateModalVisible, setUpdateModalVisible] = useState<boolean>(false);
  const [currentRow, setCurrentRow] = useState<API.SysMenu>();
  const actionRef = useRef<ActionType>();
  const access = useAccess();

  const columns: ProColumns<API.SysMenu>[] = [
    {
      title: 'ID',
      dataIndex: 'id',
      width: 80,
    },
    {
      title: '菜单名称',
      dataIndex: 'name',
      width: 200,
    },
    {
      title: '路径',
      dataIndex: 'path',
      width: 180,
    },
    {
      title: '权限标识',
      dataIndex: 'perms',
      width: 180,
    },
    {
      title: '类型',
      dataIndex: 'type',
      width: 100,
      render: (_, record) => {
        const typeMap: Record<number, { text: string; color: string }> = {
          1: { text: '目录', color: 'blue' },
          2: { text: '菜单', color: 'green' },
          3: { text: '按钮', color: 'orange' },
        };
        const type = typeMap[record.type];
        return type ? <Tag color={type.color}>{type.text}</Tag> : '-';
      },
    },
    {
      title: '图标',
      dataIndex: 'icon',
      width: 100,
    },
    {
      title: '排序',
      dataIndex: 'sort',
      width: 80,
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
      title: '操作',
      dataIndex: 'option',
      valueType: 'option',
      width: 200,
      render: (_, record) => [
        <Access key="edit" accessible={access.canMenuEdit}>
          <a
            onClick={() => {
              setCurrentRow(record);
              setUpdateModalVisible(true);
            }}
          >
            编辑
          </a>
        </Access>,
        <Access key="delete" accessible={access.canMenuDelete}>
          <Popconfirm
            title="确定要删除该菜单吗？"
            onConfirm={async () => {
              const res = await deleteMenu(record.id!);
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
      <ProTable<API.SysMenu>
        headerTitle="菜单列表"
        actionRef={actionRef}
        rowKey="id"
        search={false}
        toolBarRender={() => [
          <Access key="add" accessible={access.canMenuAdd}>
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
        request={async () => {
          const res = await getMenuTree();
          return {
            data: res.data || [],
            success: res.code === 200,
          };
        }}
        columns={columns}
        pagination={false}
      />
      
      <MenuModal
        visible={createModalVisible}
        onCancel={() => setCreateModalVisible(false)}
        onSuccess={() => {
          setCreateModalVisible(false);
          actionRef.current?.reload();
        }}
      />
      
      <MenuModal
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

export default MenuList;
