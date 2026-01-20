import { ModalForm, ProFormText, ProFormRadio } from '@ant-design/pro-components';
import { message } from 'antd';
import React, { useEffect } from 'react';
import { createUser, updateUser } from '@/services/system/user';

export type UserModalProps = {
  visible: boolean;
  current?: API.SysUser;
  onCancel: () => void;
  onSuccess: () => void;
};

const UserModal: React.FC<UserModalProps> = (props) => {
  const { visible, current, onCancel, onSuccess } = props;
  const isEdit = !!current;

  return (
    <ModalForm<API.SysUser>
      title={isEdit ? '编辑用户' : '新建用户'}
      open={visible}
      onOpenChange={(open) => {
        if (!open) {
          onCancel();
        }
      }}
      onFinish={async (values) => {
        try {
          if (isEdit) {
            const res = await updateUser({ ...values, id: current.id });
            if (res.code === 200) {
              message.success('更新成功');
              onSuccess();
              return true;
            }
          } else {
            const res = await createUser(values);
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
        name="username"
        label="用户名"
        rules={[{ required: true, message: '请输入用户名' }]}
        disabled={isEdit}
      />
      
      {!isEdit && (
        <ProFormText.Password
          name="password"
          label="密码"
          rules={[{ required: true, message: '请输入密码' }]}
        />
      )}
      
      <ProFormText
        name="nickname"
        label="昵称"
        rules={[{ required: true, message: '请输入昵称' }]}
      />
      
      <ProFormText
        name="phone"
        label="手机号"
      />
      
      <ProFormText
        name="email"
        label="邮箱"
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

export default UserModal;
