import { PlusOutlined } from '@ant-design/icons';
import {
  PageContainer,
  ProTable,
  ModalForm,
  ProFormText,
  ProFormTextArea,
} from '@ant-design/pro-components';
import { Button, message, Popconfirm, Tag, Tree } from 'antd';
import { useState, useRef, useEffect } from 'react';
import type { ActionType, ProColumns } from '@ant-design/pro-components';
import { getRoleList, createRole, updateRole, deleteRole, type RoleItem } from '@/services/role';
import { getMenuList } from '@/services/menu';
import type { MenuItem } from '@/types/menu';

const RolePage: React.FC = () => {
  const actionRef = useRef<ActionType>();
  const [modalOpen, setModalOpen] = useState(false);
  const [currentRow, setCurrentRow] = useState<RoleItem>();
  const [menuTree, setMenuTree] = useState<MenuItem[]>([]);
  const [checkedKeys, setCheckedKeys] = useState<number[]>([]);

  useEffect(() => {
    getMenuList().then(setMenuTree);
  }, []);

  const columns: ProColumns<RoleItem>[] = [
    { title: '角色名称', dataIndex: 'name', width: 150 },
    { title: '角色编码', dataIndex: 'code', width: 150 },
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
    { title: '备注', dataIndex: 'remark', search: false, ellipsis: true },
    { title: '创建时间', dataIndex: 'createTime', width: 180, search: false },
    {
      title: '操作',
      width: 150,
      valueType: 'option',
      render: (_, record) => [
        <a
          key="edit"
          onClick={() => {
            setCurrentRow(record);
            setCheckedKeys(record.menuIds || []);
            setModalOpen(true);
          }}
        >
          编辑
        </a>,
        <Popconfirm
          key="delete"
          title="确定删除？"
          onConfirm={async () => {
            await deleteRole(record.id);
            message.success('删除成功');
            actionRef.current?.reload();
          }}
        >
          <a style={{ color: '#ff4d4f' }}>删除</a>
        </Popconfirm>,
      ],
    },
  ];

  const convertToTreeData = (menus: MenuItem[]): any[] => {
    return menus.map((m) => ({
      key: m.id,
      title: m.name,
      children: m.children ? convertToTreeData(m.children) : undefined,
    }));
  };

  return (
    <PageContainer>
      <ProTable<RoleItem>
        actionRef={actionRef}
        columns={columns}
        rowKey="id"
        request={async (params) => {
          const res = await getRoleList({
            current: params.current,
            pageSize: params.pageSize,
            name: params.name,
          });
          return { data: res.list, total: res.total, success: true };
        }}
        toolBarRender={() => [
          <Button
            key="add"
            type="primary"
            icon={<PlusOutlined />}
            onClick={() => {
              setCurrentRow(undefined);
              setCheckedKeys([]);
              setModalOpen(true);
            }}
          >
            新增角色
          </Button>,
        ]}
      />
      <ModalForm
        title={currentRow ? '编辑角色' : '新增角色'}
        open={modalOpen}
        onOpenChange={setModalOpen}
        initialValues={currentRow}
        onFinish={async (values) => {
          const data = { ...values, menuIds: checkedKeys };
          if (currentRow) {
            await updateRole(currentRow.id, data);
          } else {
            await createRole(data);
          }
          message.success('保存成功');
          setModalOpen(false);
          actionRef.current?.reload();
          return true;
        }}
      >
        <ProFormText name="name" label="角色名称" rules={[{ required: true }]} />
        <ProFormText name="code" label="角色编码" rules={[{ required: true }]} />
        <ProFormTextArea name="remark" label="备注" />
        <div style={{ marginBottom: 24 }}>
          <div style={{ marginBottom: 8 }}>菜单权限</div>
          <Tree
            checkable
            treeData={convertToTreeData(menuTree)}
            checkedKeys={checkedKeys}
            onCheck={(keys) => setCheckedKeys(keys as number[])}
          />
        </div>
      </ModalForm>
    </PageContainer>
  );
};

export default RolePage;
