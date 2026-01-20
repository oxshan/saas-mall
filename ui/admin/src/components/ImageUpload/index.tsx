import React, { useState, useEffect } from 'react';
import { Upload, message } from 'antd';
import { PlusOutlined, LoadingOutlined } from '@ant-design/icons';
import type { RcFile } from 'antd/es/upload/interface';
import * as qiniu from 'qiniu-js';
import { getUploadToken } from '@/services/upload';

interface ImageUploadProps {
  value?: string;
  onChange?: (url: string) => void;
  maxSize?: number; // MB
}

const ImageUpload: React.FC<ImageUploadProps> = ({ value, onChange, maxSize = 2 }) => {
  const [loading, setLoading] = useState(false);
  const [imageUrl, setImageUrl] = useState<string>(value || '');

  // 监听 value 变化，用于回显
  useEffect(() => {
    console.log('ImageUpload value changed:', value);
    setImageUrl(value || '');
  }, [value]);

  // 上传前校验
  const beforeUpload = (file: RcFile) => {
    const isImage = file.type.startsWith('image/');
    if (!isImage) {
      message.error('只能上传图片文件！');
      return false;
    }
    const isLtMaxSize = file.size / 1024 / 1024 < maxSize;
    if (!isLtMaxSize) {
      message.error(`图片大小不能超过 ${maxSize}MB！`);
      return false;
    }
    return true;
  };

  // 自定义上传
  const customUpload = async (options: any) => {
    const { file } = options;
    
    try {
      setLoading(true);
      
      // 获取上传 token
      const tokenRes = await getUploadToken();
      if (tokenRes.code !== 200 || !tokenRes.data) {
        message.error('获取上传凭证失败');
        setLoading(false);
        return;
      }

      const { token, domain } = tokenRes.data;
      
      // 生成唯一文件名
      const timestamp = Date.now();
      const randomStr = Math.random().toString(36).substring(2, 8);
      const ext = file.name.substring(file.name.lastIndexOf('.'));
      const key = `shop/logo/${timestamp}_${randomStr}${ext}`;

      // 使用七牛云 SDK 上传
      const observable = qiniu.upload(file, key, token);
      
      observable.subscribe({
        next: (result) => {
          // 上传进度
          console.log('上传进度:', result.total.percent);
        },
        error: (err) => {
          console.error('上传失败:', err);
          message.error('上传失败，请重试');
          setLoading(false);
        },
        complete: (result) => {
          // 上传完成
          let url = domain;
          // 确保 domain 有协议前缀
          if (!url.startsWith('http://') && !url.startsWith('https://')) {
            url = 'https://' + url;
          }
          // 确保 domain 以 / 结尾
          if (!url.endsWith('/')) {
            url += '/';
          }
          url += result.key;
          
          console.log('上传完成，生成的 URL:', url);
          setImageUrl(url);
          onChange?.(url);
          message.success('上传成功');
          setLoading(false);
        },
      });
    } catch (error) {
      console.error('上传错误:', error);
      message.error('上传失败，请重试');
      setLoading(false);
    }
  };

  const uploadButton = (
    <div>
      {loading ? <LoadingOutlined /> : <PlusOutlined />}
      <div style={{ marginTop: 8 }}>上传图片</div>
    </div>
  );

  return (
    <Upload
      name="file"
      listType="picture-card"
      className="avatar-uploader"
      showUploadList={false}
      beforeUpload={beforeUpload}
      customRequest={customUpload}
    >
      {imageUrl ? (
        <img src={imageUrl} alt="logo" style={{ width: '100%', height: '100%', objectFit: 'cover' }} />
      ) : (
        uploadButton
      )}
    </Upload>
  );
};

export default ImageUpload;
