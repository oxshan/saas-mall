import { ModalForm, ProFormText, ProFormRadio, ProFormSelect, ProFormDigit, ProFormTreeSelect } from '@ant-design/pro-components';
import { message } from 'antd';
import React, { useEffect, useState } from 'react';
import { createMenu, updateMenu, getMenuTree } from '@/services/system/menu';

export type MenuModalProps = {
  visible: boolean;
  current?: API.SysMenu;
  onCancel: () => void;
  onSuccess: () => void;
};

const MenuModal: React.FC<MenuModalProps> = (props) => {
  const { visible, current, onCancel, onSuccess } = props;
  const isEdit = !!current;
  const [menuTree, setMenuTree] = useState<API.SysMenu[]>([]);

  useEffect(() => {
    if (visible) {
      loadMenuTree();
    }
  }, [visible]);

  const loadMenuTree = async () => {
    const res = await getMenuTree();
    if (res.code === 200) {
      setMenuTree(res.data || []);
    }
  };

  // 转换菜单树为 TreeSelect 需要的格式
  const convertToTreeSelectData = (menus: API.SysMenu[]): any[] => {
    return menus.map(menu => ({
      title: menu.name,
      value: menu.id,
      children: menu.children ? convertToTreeSelectData(menu.children) : undefined,
    }));
  };

  return (
    <ModalForm<API.SysMenu>
      title={isEdit ? '编辑菜单' : '新建菜单'}
      open={visible}
      onOpenChange={(open) => {
        if (!open) {
          onCancel();
        }
      }}
      onFinish={async (values) => {
        try {
          if (isEdit) {
            const res = await updateMenu({ ...values, id: current.id });
            if (res.code === 200) {
              message.success('更新成功');
              onSuccess();
              return true;
            }
          } else {
            const res = await createMenu(values);
            if (res.code === 200) {
              message.success('创建成功');
              onSuccess();
              return true;
            }
          }
        } catch (error) {
          return false;
        }
        return false;
      }}
      initialValues={current || { parentId: 0, sort: 0, visible: 1, status: 1 }}
      modalProps={{
        destroyOnClose: true,
      }}
    >
      <ProFormTreeSelect
        name="parentId"
        label="父级菜单"
        placeholder="请选择父级菜单"
        allowClear
        fieldProps={{
          treeData: [
            { title: '根菜单', value: 0 },
            ...convertToTreeSelectData(menuTree),
          ],
        }}
      />
      
      <ProFormText
        name="name"
        label="菜单名称"
        rules={[{ required: true, message: '请输入菜单名称' }]}
      />
      
      <ProFormSelect
        name="type"
        label="菜单类型"
        options={[
          { label: '目录', value: 1 },
          { label: '菜单', value: 2 },
          { label: '按钮', value: 3 },
        ]}
        rules={[{ required: true, message: '请选择菜单类型' }]}
      />
      
      <ProFormText
        name="path"
        label="路由路径"
      />
      
      <ProFormText
        name="component"
        label="组件路径"
      />
      
      <ProFormText
        name="perms"
        label="权限标识"
        placeholder="例如：system:user:list"
      />
      
      <ProFormText
        name="icon"
        label="图标"
      />
      
      <ProFormDigit
        name="sort"
        label="排序"
        min={0}
        rules={[{ required: true, message: '请输入排序' }]}
      />
      
      <ProFormRadio.Group
        name="visible"
        label="是否可见"
        options={[
          { label: '显示', value: 1 },
          { label: '隐藏', value: 0 },
        ]}
        rules={[{ required: true, message: '请选择是否可见' }]}
      />
      
      <ProFormRadio.Group
        name="status"
        label="状态"
        options={[
          { label: '正常', value: 1 },
          { label: '禁用', value: 0 },
        ]}
        rules={[{ required: true, message: '请选择状态' }]}
      />
    </ModalForm>
  );
};

export default MenuModal;
