import { PageContainer } from '@ant-design/pro-components';
import { Card, Tree, Spin, message } from 'antd';
import type { DataNode } from 'antd/es/tree';
import React, { useEffect, useState } from 'react';
import { getShopTree } from '@/services/shop/shop';

const ShopTree: React.FC = () => {
  const [treeData, setTreeData] = useState<DataNode[]>([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    loadTreeData();
  }, []);

  const loadTreeData = async () => {
    setLoading(true);
    try {
      const response = await getShopTree();
      if (response.code === 200 && response.data) {
        const data = convertToTreeData(response.data);
        setTreeData(data);
      }
    } catch (error) {
      message.error('加载店铺树失败');
    } finally {
      setLoading(false);
    }
  };

  const convertToTreeData = (data: any[]): DataNode[] => {
    return data.map((item) => ({
      title: `${item.shopName} (${item.shopCode})`,
      key: item.id,
      children: item.children ? convertToTreeData(item.children) : [],
    }));
  };

  return (
    <PageContainer>
      <Card>
        <Spin spinning={loading}>
          <Tree
            showLine
            defaultExpandAll
            treeData={treeData}
          />
        </Spin>
      </Card>
    </PageContainer>
  );
};

export default ShopTree;
