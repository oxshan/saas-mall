import { Card, Row, Col, Statistic } from 'antd'
import {
  UserOutlined,
  ShoppingOutlined,
  ShoppingCartOutlined,
  DollarOutlined,
} from '@ant-design/icons'

const Dashboard: React.FC = () => {
  return (
    <div>
      <h2 className="text-xl font-semibold text-gray-800 mb-6">工作台</h2>
      <Row gutter={[24, 24]}>
        <Col xs={24} sm={12} lg={6}>
          <Card hoverable className="border-0 shadow-sm">
            <Statistic
              title={<span className="text-gray-500">用户总数</span>}
              value={1128}
              prefix={<UserOutlined className="text-blue-500" />}
              valueStyle={{ color: '#1890ff' }}
            />
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <Card hoverable className="border-0 shadow-sm">
            <Statistic
              title={<span className="text-gray-500">商品总数</span>}
              value={256}
              prefix={<ShoppingOutlined className="text-green-500" />}
              valueStyle={{ color: '#52c41a' }}
            />
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <Card hoverable className="border-0 shadow-sm">
            <Statistic
              title={<span className="text-gray-500">今日订单</span>}
              value={93}
              prefix={<ShoppingCartOutlined className="text-orange-500" />}
              valueStyle={{ color: '#fa8c16' }}
            />
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={6}>
          <Card hoverable className="border-0 shadow-sm">
            <Statistic
              title={<span className="text-gray-500">今日销售额</span>}
              value={9280}
              precision={2}
              prefix={<DollarOutlined className="text-red-500" />}
              suffix="元"
              valueStyle={{ color: '#f5222d' }}
            />
          </Card>
        </Col>
      </Row>
    </div>
  )
}

export default Dashboard
