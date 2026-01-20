import React, { useEffect, useRef, useState } from 'react';
import { Modal, Button, Input, message } from 'antd';
import { EnvironmentOutlined } from '@ant-design/icons';

interface MapPickerProps {
  value?: { longitude?: number; latitude?: number; address?: string };
  onChange?: (value: { longitude: number; latitude: number; address: string }) => void;
}

const MapPicker: React.FC<MapPickerProps> = ({ value, onChange }) => {
  const [visible, setVisible] = useState(false);
  const [searchKey, setSearchKey] = useState('');
  const [tempLocation, setTempLocation] = useState<{ longitude: number; latitude: number; address: string } | null>(null);
  const mapRef = useRef<any>(null);
  const markerRef = useRef<any>(null);

  const displayText = value?.address || '点击选择位置';

  const handleOpenMap = () => {
    setVisible(true);
  };

  useEffect(() => {
    if (visible) {
      initMap();
    }
  }, [visible]);

  const initMap = () => {
    // 使用高德地图 API
    const AMap = (window as any).AMap;
    if (!AMap) {
      message.error('地图加载失败，请刷新页面重试');
      return;
    }

    // 加载 Geocoder 插件
    AMap.plugin('AMap.Geocoder', () => {
      const map = new AMap.Map('map-container', {
        zoom: 13,
        center: [value?.longitude || 116.397428, value?.latitude || 39.90923],
      });

      mapRef.current = map;

      // 添加点击事件
      map.on('click', (e: any) => {
        const { lng, lat } = e.lnglat;
        updateMarker(lng, lat);
        getAddress(lng, lat);
      });

      // 如果有初始值，添加标记
      if (value?.longitude && value?.latitude) {
        updateMarker(value.longitude, value.latitude);
      }
    });
  };

  const updateMarker = (lng: number, lat: number) => {
    const AMap = (window as any).AMap;
    if (markerRef.current) {
      markerRef.current.setPosition([lng, lat]);
    } else {
      const marker = new AMap.Marker({
        position: [lng, lat],
        map: mapRef.current,
      });
      markerRef.current = marker;
    }
  };

  const getAddress = (lng: number, lat: number) => {
    const AMap = (window as any).AMap;
    const geocoder = new AMap.Geocoder();
    geocoder.getAddress([lng, lat], (status: string, result: any) => {
      if (status === 'complete' && result.info === 'OK') {
        const address = result.regeocode.formattedAddress;
        setTempLocation({ longitude: lng, latitude: lat, address });
        message.success('位置已选择，请点击确认按钮');
      } else {
        // 即使地理编码失败，也保存经纬度，使用默认地址
        console.error('Geocoder error:', status, result);
        setTempLocation({ 
          longitude: lng, 
          latitude: lat, 
          address: `位置: ${lng.toFixed(6)}, ${lat.toFixed(6)}` 
        });
        message.warning('地址解析失败，已保存坐标位置');
      }
    });
  };

  const handleConfirm = () => {
    if (tempLocation) {
      onChange?.(tempLocation);
      message.success('位置选择成功');
      setVisible(false);
      setTempLocation(null);
    } else {
      message.warning('请先在地图上选择位置');
    }
  };

  const handleCancel = () => {
    setVisible(false);
    setTempLocation(null);
  };

  return (
    <>
      <Input
        readOnly
        value={displayText}
        placeholder="点击选择位置"
        onClick={handleOpenMap}
        suffix={<EnvironmentOutlined />}
        style={{ cursor: 'pointer' }}
      />
      <Modal
        title="选择位置"
        open={visible}
        onCancel={handleCancel}
        onOk={handleConfirm}
        width={800}
        okText="确认"
        cancelText="取消"
      >
        <div style={{ marginBottom: 16 }}>
          {tempLocation && (
            <div style={{ padding: '8px', background: '#f0f0f0', borderRadius: '4px' }}>
              <div><strong>已选择位置：</strong></div>
              <div>{tempLocation.address}</div>
              <div style={{ fontSize: '12px', color: '#666', marginTop: '4px' }}>
                经度: {tempLocation.longitude.toFixed(6)}, 纬度: {tempLocation.latitude.toFixed(6)}
              </div>
            </div>
          )}
          {!tempLocation && (
            <div style={{ padding: '8px', background: '#fff7e6', borderRadius: '4px', border: '1px solid #ffd591' }}>
              请在地图上点击选择位置
            </div>
          )}
        </div>
        <div id="map-container" style={{ width: '100%', height: '500px' }} />
      </Modal>
    </>
  );
};

export default MapPicker;
