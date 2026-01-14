import { PageContainer, StatisticCard } from '@ant-design/pro-components';
import { Col, Row } from 'antd';
import {
  UserOutlined,
  ShoppingOutlined,
  ShoppingCartOutlined,
  DollarOutlined,
} from '@ant-design/icons';

const Dashboard: React.FC = () => {
  return (
    <PageContainer>
      <Row gutter={[24, 24]}>
        <Col xs={24} sm={12} lg={6}>
          <StatisticCard
            statistic={{
              title: '用户总数',
              value: 1128,
              icon: <UserOutlined style={{ color: '#1890ff' }} />,
            }}
          />
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <StatisticCard
            statistic={{
              title: '商品总数',
              value: 256,
              icon: <ShoppingOutlined style={{ color: '#52c41a' }} />,
            }}
          />
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <StatisticCard
            statistic={{
              title: '今日订单',
              value: 93,
              icon: <ShoppingCartOutlined style={{ color: '#fa8c16' }} />,
            }}
          />
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <StatisticCard
            statistic={{
              title: '今日销售额',
              value: 9280,
              suffix: '元',
              icon: <DollarOutlined style={{ color: '#f5222d' }} />,
            }}
          />
        </Col>
      </Row>
    </PageContainer>
  );
};

export default Dashboard;
