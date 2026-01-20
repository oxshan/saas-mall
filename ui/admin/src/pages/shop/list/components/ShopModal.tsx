import { ModalForm, ProFormText, ProFormSelect, ProFormTextArea } from '@ant-design/pro-components';
import { message, Form } from 'antd';
import React, { useState } from 'react';
import { createShop, updateShop } from '@/services/shop/shop';
import MapPicker from '@/components/MapPicker';
import ImageUpload from '@/components/ImageUpload';

interface ShopModalProps {
  visible: boolean;
  current?: API.Shop;
  onCancel: () => void;
  onSuccess: () => void;
}

const ShopModal: React.FC<ShopModalProps> = ({ visible, current, onCancel, onSuccess }) => {
  const [form] = Form.useForm();

  return (
    <ModalForm
      form={form}
      title={current ? '编辑店铺' : '新建店铺'}
      open={visible}
      onOpenChange={(open) => {
        if (!open) {
          onCancel();
        }
      }}
      onFinish={async (values) => {
        const data = { 
          ...values, 
          id: current?.id,
          longitude: values.location?.longitude,
          latitude: values.location?.latitude,
        };
        const response = current ? await updateShop(data) : await createShop(data);
        if (response.code === 200) {
          message.success(current ? '更新成功' : '创建成功');
          onSuccess();
          return true;
        }
        return false;
      }}
      initialValues={current}
    >
      <ProFormText
        name="shopName"
        label="店铺名称"
        rules={[{ required: true, message: '请输入店铺名称' }]}
        placeholder="请输入店铺名称"
      />
      <Form.Item
        label="店铺 Logo"
        name="logo"
      >
        <ImageUpload />
      </Form.Item>
      <ProFormSelect
        name="shopType"
        label="店铺类型"
        rules={[{ required: true, message: '请选择店铺类型' }]}
        options={[
          { label: '单店', value: 1 },
          { label: '连锁总部', value: 2 },
          { label: '连锁分店', value: 3 },
        ]}
        disabled={!!current}
      />
      <ProFormText
        name="province"
        label="省份"
        placeholder="请输入省份"
      />
      <ProFormText
        name="city"
        label="城市"
        placeholder="请输入城市"
      />
      <ProFormText
        name="district"
        label="区县"
        placeholder="请输入区县"
      />
      <ProFormText
        name="address"
        label="详细地址"
        placeholder="请输入详细地址"
      />
      <Form.Item
        label="地图选点"
        name="location"
      >
        <MapPicker />
      </Form.Item>
      <ProFormText
        name="contactName"
        label="联系人"
        placeholder="请输入联系人"
      />
      <ProFormText
        name="contactPhone"
        label="联系电话"
        placeholder="请输入联系电话"
      />
      <ProFormText
        name="businessHours"
        label="营业时间"
        placeholder="例如：09:00-21:00"
      />
      <ProFormTextArea
        name="description"
        label="店铺描述"
        placeholder="请输入店铺描述"
      />
      <ProFormSelect
        name="status"
        label="状态"
        rules={[{ required: true, message: '请选择状态' }]}
        options={[
          { label: '停业', value: 0 },
          { label: '营业', value: 1 },
        ]}
      />
    </ModalForm>
  );
};

export default ShopModal;
