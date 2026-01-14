import { PlusOutlined } from '@ant-design/icons';
import {
  PageContainer,
  ProTable,
  ModalForm,
  ProFormText,
  ProFormDigit,
  ProFormTreeSelect,
  ProFormRadio,
} from '@ant-design/pro-components';
import { Button, message, Popconfirm } from 'antd';
import { useState, useRef } from 'react';
import type { ActionType, ProColumns } from '@ant-design/pro-components';
import { getMenuList, createMenu, updateMenu, deleteMenu } from '@/services/menu';
import type { MenuItem } from '@/types/menu';

// 菜单表单弹窗
interface MenuModalProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  currentRow?: MenuItem;
  menuList: MenuItem[];
  onSuccess: () => void;
}

const MenuModal: React.FC<MenuModalProps> = ({
  open, onOpenChange, currentRow, menuList, onSuccess,
}) => {
  const convertToTreeData = (menus: MenuItem[]): any[] => {
    return menus
      .filter((m) => m.type !== 2)
      .map((m) => ({
        value: m.id,
        title: m.name,
        children: m.children ? convertToTreeData(m.children) : undefined,
      }));
  };

  return (
    <ModalForm
      title={currentRow ? '编辑菜单' : '新增菜单'}
      open={open}
      onOpenChange={onOpenChange}
      initialValues={currentRow || { type: 1, visible: 1, sort: 0 }}
      onFinish={async (values) => {
        if (currentRow) {
          await updateMenu(currentRow.id, values);
        } else {
          await createMenu(values);
        }
        message.success('保存成功');
        onOpenChange(false);
        onSuccess();
        return true;
      }}
    >
      <ProFormTreeSelect
        name="parentId"
        label="上级菜单"
        allowClear
        fieldProps={{
          treeData: [{ value: 0, title: '根目录' }, ...convertToTreeData(menuList)],
        }}
      />
      <ProFormRadio.Group
        name="type"
        label="菜单类型"
        options={[
          { label: '目录', value: 0 },
          { label: '菜单', value: 1 },
          { label: '按钮', value: 2 },
        ]}
      />
      <ProFormText name="name" label="菜单名称" rules={[{ required: true }]} />
      <ProFormText name="path" label="路由路径" />
      <ProFormText name="icon" label="图标" />
      <ProFormText name="permission" label="权限标识" />
      <ProFormDigit name="sort" label="排序" min={0} />
      <ProFormRadio.Group
        name="visible"
        label="是否显示"
        options={[
          { label: '显示', value: 1 },
          { label: '隐藏', value: 0 },
        ]}
      />
    </ModalForm>
  );
};

const MenuPage: React.FC = () => {
  const actionRef = useRef<ActionType>();
  const [modalOpen, setModalOpen] = useState(false);
  const [currentRow, setCurrentRow] = useState<MenuItem>();
  const [menuList, setMenuList] = useState<MenuItem[]>([]);

  const columns: ProColumns<MenuItem>[] = [
    { title: '菜单名称', dataIndex: 'name', width: 200 },
    { title: '路径', dataIndex: 'path', width: 200 },
    { title: '图标', dataIndex: 'icon', width: 100 },
    {
      title: '类型',
      dataIndex: 'type',
      width: 100,
      valueEnum: { 0: '目录', 1: '菜单', 2: '按钮' },
    },
    { title: '排序', dataIndex: 'sort', width: 80 },
    {
      title: '状态',
      dataIndex: 'visible',
      width: 80,
      valueEnum: { 1: { text: '显示', status: 'Success' }, 0: { text: '隐藏', status: 'Error' } },
    },
  ];

  return (
    <PageContainer>
      <ProTable<MenuItem>
        actionRef={actionRef}
        columns={[
          ...columns,
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
                  await deleteMenu(record.id);
                  message.success('删除成功');
                  actionRef.current?.reload();
                }}
              >
                <a style={{ color: '#ff4d4f' }}>删除</a>
              </Popconfirm>,
            ],
          },
        ]}
        rowKey="id"
        search={false}
        pagination={false}
        request={async () => {
          const data = await getMenuList();
          setMenuList(data);
          return { data, success: true };
        }}
        toolBarRender={() => [
          <Button
            key="add"
            type="primary"
            icon={<PlusOutlined />}
            onClick={() => { setCurrentRow(undefined); setModalOpen(true); }}
          >
            新增菜单
          </Button>,
        ]}
      />
      <MenuModal
        open={modalOpen}
        onOpenChange={setModalOpen}
        currentRow={currentRow}
        menuList={menuList}
        onSuccess={() => actionRef.current?.reload()}
      />
    </PageContainer>
  );
};

export default MenuPage;
