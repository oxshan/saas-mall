import { useMemo } from 'react'
import { Layout, Menu } from 'antd'
import { useNavigate, useLocation } from 'react-router-dom'
import {
  DashboardOutlined,
  UserOutlined,
  TeamOutlined,
  MenuOutlined,
  SettingOutlined,
} from '@ant-design/icons'
import { useMenuStore } from '@/stores/useMenuStore'
import type { MenuItem } from '@/types/menu'
import type { MenuProps } from 'antd'

const { Sider } = Layout

// 图标映射
const iconMap: Record<string, React.ReactNode> = {
  dashboard: <DashboardOutlined />,
  user: <UserOutlined />,
  team: <TeamOutlined />,
  menu: <MenuOutlined />,
  setting: <SettingOutlined />,
}

interface Props {
  collapsed: boolean
}

const AppSider: React.FC<Props> = ({ collapsed }) => {
  const navigate = useNavigate()
  const location = useLocation()
  const menus = useMenuStore((state) => state.menus)

  // 转换菜单数据为 Ant Design 格式
  const menuItems: MenuProps['items'] = useMemo(() => {
    const convert = (items: MenuItem[]): MenuProps['items'] => {
      return items
        .filter((item) => item.type !== 2 && item.visible === 1)
        .map((item) => ({
          key: item.path,
          icon: item.icon ? iconMap[item.icon] : null,
          label: item.name,
          children: item.children?.length ? convert(item.children) : undefined,
        }))
    }
    return convert(menus)
  }, [menus])

  const handleClick: MenuProps['onClick'] = ({ key }) => {
    navigate(key)
  }

  return (
    <Sider
      trigger={null}
      collapsible
      collapsed={collapsed}
      theme="dark"
      width={220}
      style={{
        overflow: 'auto',
        height: '100vh',
        position: 'fixed',
        left: 0,
        top: 0,
        bottom: 0,
      }}
    >
      <div className="h-16 flex items-center justify-center border-b border-white/10">
        <div className="flex items-center gap-2">
          <div className="w-8 h-8 bg-blue-500 rounded-lg flex items-center justify-center">
            <span className="text-white font-bold text-lg">M</span>
          </div>
          {!collapsed && (
            <span className="text-white font-semibold text-base">商家后台</span>
          )}
        </div>
      </div>
      <Menu
        theme="dark"
        mode="inline"
        selectedKeys={[location.pathname]}
        items={menuItems}
        onClick={handleClick}
        style={{ borderRight: 0, marginTop: 8 }}
      />
    </Sider>
  )
}

export default AppSider
