import { ModalForm, ProFormText, ProFormRadio, ProFormSelect } from '@ant-design/pro-components';
import { message } from 'antd';
import React from 'react';
import { createRole, updateRole } from '@/services/system/role';

export type RoleModalProps = {
  visible: boolean;
  current?: API.SysRole;
  onCancel: () => void;
  onSuccess: () => void;
};

const RoleModal: React.FC<RoleModalProps> = (props) => {
  const { visible, current, onCancel, onSuccess } = props;
  const isEdit = !!current;

  return (
    <ModalForm<API.SysRole>
      title={isEdit ? '编辑角色' : '新建角色'}
      open={visible}
      onOpenChange={(open) => {
        if (!open) {
          onCancel();
        }
      }}
      onFinish={async (values) => {
        try {
          if (isEdit) {
            const res = await updateRole({ ...values, id: current.id });
            if (res.code === 200) {
              message.success('更新成功');
              onSuccess();
              return true;
            }
          } else {
            const res = await createRole(values);
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
      initialValues={current}
      modalProps={{
        destroyOnClose: true,
      }}
    >
      <ProFormText
        name="code"
        label="角色编码"
        rules={[{ required: true, message: '请输入角色编码' }]}
        disabled={isEdit}
      />
      
      <ProFormText
        name="name"
        label="角色名称"
        rules={[{ required: true, message: '请输入角色名称' }]}
      />
      
      <ProFormSelect
        name="type"
        label="角色类型"
        options={[
          { label: '管理员', value: 1 },
          { label: '普通用户', value: 2 },
        ]}
        rules={[{ required: true, message: '请选择角色类型' }]}
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

export default RoleModal;
