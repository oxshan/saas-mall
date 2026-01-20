import { PlusOutlined } from '@ant-design/icons';
import type { ActionType, ProColumns } from '@ant-design/pro-components';
import { PageContainer, ProTable } from '@ant-design/pro-components';
import { Button, message, Popconfirm, Tag } from 'antd';
import React, { useRef, useState } from 'react';
import { Access, useAccess } from '@umijs/max';
import { listShops, deleteShop } from '@/services/shop/shop';
import ShopModal from './components/ShopModal';

const ShopList: React.FC = () => {
  const [createModalVisible, setCreateModalVisible] = useState<boolean>(false);
  const [updateModalVisible, setUpdateModalVisible] = useState<boolean>(false);
  const [currentRow, setCurrentRow] = useState<API.Shop>();
  const actionRef = useRef<ActionType>();
  const access = useAccess();

  const shopTypeMap = {
    1: { text: '单店', status: 'Default' },
    2: { text: '连锁总部', status: 'Processing' },
    3: { text: '连锁分店', status: 'Success' },
  };

  const statusMap = {
    0: { text: '停业', status: 'Error' },
    1: { text: '营业', status: 'Success' },
  };

  const columns: ProColumns<API.Shop>[] = [
    {
      title: 'ID',
      dataIndex: 'id',
      width: 80,
      search: false,
    },
    {
      title: '店铺编码',
      dataIndex: 'shopCode',
      width: 150,
      search: false,
    },
    {
      title: '店铺名称',
      dataIndex: 'shopName',
      width: 150,
    },
    {
      title: '店铺类型',
      dataIndex: 'shopType',
      width: 120,
      valueEnum: {
        1: { text: '单店', status: 'Default' },
        2: { text: '连锁总部', status: 'Processing' },
        3: { text: '连锁分店', status: 'Success' },
      },
    },
    {
      title: '地址',
      dataIndex: 'address',
      width: 200,
      search: false,
    },
    {
      title: '联系人',
      dataIndex: 'contactName',
      width: 100,
      search: false,
    },
    {
      title: '联系电话',
      dataIndex: 'contactPhone',
      width: 120,
      search: false,
    },
    {
      title: '状态',
      dataIndex: 'status',
      width: 100,
      valueEnum: {
        0: { text: '停业', status: 'Error' },
        1: { text: '营业', status: 'Success' },
      },
    },
    {
      title: '操作',
      dataIndex: 'option',
      valueType: 'option',
      width: 150,
      render: (_, record) => [
        <Access accessible={access.canAccess('shop:shop:edit')} key="edit">
          <a
            onClick={() => {
              setCurrentRow(record);
              setUpdateModalVisible(true);
            }}
          >
            编辑
          </a>
        </Access>,
        <Access accessible={access.canAccess('shop:shop:delete')} key="delete">
          <Popconfirm
            title="确定要删除吗？"
            onConfirm={async () => {
              const response = await deleteShop(record.id!);
              if (response.code === 200) {
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
      <ProTable<API.Shop>
        headerTitle="店铺列表"
        actionRef={actionRef}
        rowKey="id"
        search={{
          labelWidth: 120,
        }}
        toolBarRender={() => [
          <Access accessible={access.canAccess('shop:shop:add')} key="create">
            <Button
              type="primary"
              icon={<PlusOutlined />}
              onClick={() => setCreateModalVisible(true)}
            >
              新建店铺
            </Button>
          </Access>,
        ]}
        request={async (params) => {
          const response = await listShops({
            ...params,
            pageNum: params.current,
            pageSize: params.pageSize,
          });
          return {
            data: response.data?.list || [],
            total: response.data?.total || 0,
            success: response.code === 200,
          };
        }}
        columns={columns}
      />
      <ShopModal
        visible={createModalVisible}
        onCancel={() => setCreateModalVisible(false)}
        onSuccess={() => {
          setCreateModalVisible(false);
          actionRef.current?.reload();
        }}
      />
      <ShopModal
        visible={updateModalVisible}
        current={currentRow}
        onCancel={() => setUpdateModalVisible(false)}
        onSuccess={() => {
          setUpdateModalVisible(false);
          actionRef.current?.reload();
        }}
      />
    </PageContainer>
  );
};

export default ShopList;
