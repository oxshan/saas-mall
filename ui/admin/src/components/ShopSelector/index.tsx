import { ShopOutlined } from '@ant-design/icons';
import { Dropdown, message } from 'antd';
import type { MenuProps } from 'antd';
import React, { useEffect, useState } from 'react';
import { getMyShops, switchShop } from '@/services/shop/userShop';
import styles from './index.less';

const ShopSelector: React.FC = () => {
  const [shops, setShops] = useState<API.Shop[]>([]);
  const [currentShop, setCurrentShop] = useState<API.Shop>();
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    loadShops();
  }, []);

  const loadShops = async () => {
    try {
      const response = await getMyShops();
      if (response.code === 200 && response.data) {
        setShops(response.data);
        if (response.data.length > 0) {
          setCurrentShop(response.data[0]);
        }
      }
    } catch (error) {
      console.error('加载店铺列表失败', error);
    }
  };

  const handleShopSwitch = async (shopId: number) => {
    if (loading) return;
    setLoading(true);
    try {
      const response = await switchShop({ shopId });
      if (response.code === 200) {
        message.success('切换店铺成功');
        setCurrentShop(response.data);
        window.location.reload();
      }
    } catch (error) {
      message.error('切换店铺失败');
    } finally {
      setLoading(false);
    }
  };

  const menuItems: MenuProps['items'] = shops.map((shop) => ({
    key: shop.id!,
    label: shop.shopName,
    onClick: () => handleShopSwitch(shop.id!),
  }));

  if (shops.length === 0) {
    return null;
  }

  if (shops.length === 1) {
    return (
      <div className={styles.shopSelector}>
        <ShopOutlined />
        <span className={styles.shopName}>{currentShop?.shopName}</span>
      </div>
    );
  }

  return (
    <Dropdown menu={{ items: menuItems }} placement="bottomRight">
      <div className={styles.shopSelector}>
        <ShopOutlined />
        <span className={styles.shopName}>{currentShop?.shopName}</span>
      </div>
    </Dropdown>
  );
};

export default ShopSelector;
