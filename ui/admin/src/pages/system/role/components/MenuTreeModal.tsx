import { Modal, Tree, message } from 'antd';
import React, { useEffect, useState } from 'react';
import { getMenuTree } from '@/services/system/menu';
import { getRoleMenuIds, assignMenus } from '@/services/system/role';

export type MenuTreeModalProps = {
  visible: boolean;
  roleId?: number;
  onCancel: () => void;
};

const MenuTreeModal: React.FC<MenuTreeModalProps> = (props) => {
  const { visible, roleId, onCancel } = props;
  const [menuTree, setMenuTree] = useState<API.SysMenu[]>([]);
  const [checkedKeys, setCheckedKeys] = useState<number[]>([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (visible && roleId) {
      loadData();
    }
  }, [visible, roleId]);

  const loadData = async () => {
    setLoading(true);
    try {
      // 加载菜单树
      const menuRes = await getMenuTree();
      if (menuRes.code === 200) {
        setMenuTree(menuRes.data || []);
      }
      
      // 加载角色已有的菜单权限
      const menuIdsRes = await getRoleMenuIds(roleId!);
      if (menuIdsRes.code === 200) {
        setCheckedKeys(menuIdsRes.data || []);
      }
    } finally {
      setLoading(false);
    }
  };

  const handleOk = async () => {
    setLoading(true);
    try {
      const res = await assignMenus({
        roleId: roleId!,
        menuIds: checkedKeys,
      });
      if (res.code === 200) {
        message.success('分配菜单成功');
        onCancel();
      }
    } finally {
      setLoading(false);
    }
  };

  // 转换菜单树为 Tree 组件需要的格式
  const convertToTreeData = (menus: API.SysMenu[]): any[] => {
    return menus.map(menu => ({
      title: menu.name,
      key: menu.id,
      children: menu.children ? convertToTreeData(menu.children) : undefined,
    }));
  };

  return (
    <Modal
      title="分配菜单权限"
      open={visible}
      onOk={handleOk}
      onCancel={onCancel}
      confirmLoading={loading}
      width={600}
    >
      <Tree
        checkable
        checkedKeys={checkedKeys}
        onCheck={(checked: any) => {
          setCheckedKeys(checked);
        }}
        treeData={convertToTreeData(menuTree)}
      />
    </Modal>
  );
};

export default MenuTreeModal;
