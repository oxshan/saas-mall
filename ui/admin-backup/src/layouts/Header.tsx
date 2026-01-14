import { Layout, Button, Dropdown, Avatar } from 'antd'
import {
  MenuFoldOutlined,
  MenuUnfoldOutlined,
  UserOutlined,
  LogoutOutlined,
} from '@ant-design/icons'
import { useNavigate } from 'react-router-dom'
import { useUserStore } from '@/stores/useUserStore'
import { useMenuStore } from '@/stores/useMenuStore'
import type { MenuProps } from 'antd'

const { Header } = Layout

interface Props {
  collapsed: boolean
  onCollapse: (collapsed: boolean) => void
}

const AppHeader: React.FC<Props> = ({ collapsed, onCollapse }) => {
  const navigate = useNavigate()
  const { userInfo, logout } = useUserStore()
  const resetMenu = useMenuStore((state) => state.reset)

  const handleLogout = () => {
    logout()
    resetMenu()
    navigate('/login')
  }

  const items: MenuProps['items'] = [
    {
      key: 'logout',
      icon: <LogoutOutlined />,
      label: '退出登录',
      onClick: handleLogout,
    },
  ]

  return (
    <Header className="bg-white px-6 flex items-center justify-between shadow-sm h-16 leading-[64px]">
      <div className="flex items-center">
        <Button
          type="text"
          icon={collapsed ? <MenuUnfoldOutlined /> : <MenuFoldOutlined />}
          onClick={() => onCollapse(!collapsed)}
          className="text-gray-600 hover:text-blue-500 text-lg"
        />
      </div>
      <Dropdown menu={{ items }} placement="bottomRight">
        <div className="flex items-center cursor-pointer hover:bg-gray-50 px-3 py-1 rounded-lg transition-colors">
          <Avatar
            icon={<UserOutlined />}
            className="bg-blue-500"
          />
          <span className="ml-2 text-gray-700">{userInfo?.nickname || '用户'}</span>
        </div>
      </Dropdown>
    </Header>
  )
}

export default AppHeader
