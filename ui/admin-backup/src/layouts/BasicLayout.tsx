import { useState, useEffect } from 'react'
import { Layout, Spin } from 'antd'
import { Outlet, useNavigate } from 'react-router-dom'
import { useUserStore } from '@/stores/useUserStore'
import { useMenuStore } from '@/stores/useMenuStore'
import AppSider from './Sider'
import AppHeader from './Header'

const { Content } = Layout

const BasicLayout: React.FC = () => {
  const [collapsed, setCollapsed] = useState(false)
  const token = useUserStore((state) => state.token)
  const { loading, fetched, fetchMenus } = useMenuStore()
  const navigate = useNavigate()

  useEffect(() => {
    if (!token) {
      navigate('/login')
      return
    }
    if (!fetched) {
      fetchMenus()
    }
  }, [token, fetched, fetchMenus, navigate])

  if (loading) {
    return (
      <div className="h-screen flex items-center justify-center">
        <Spin size="large" />
      </div>
    )
  }

  const siderWidth = collapsed ? 80 : 220

  return (
    <Layout className="min-h-screen">
      <AppSider collapsed={collapsed} />
      <Layout style={{ marginLeft: siderWidth, transition: 'margin-left 0.2s' }}>
        <AppHeader collapsed={collapsed} onCollapse={setCollapsed} />
        <Content
          className="m-6 p-6 bg-white rounded-lg shadow-sm"
          style={{ minHeight: 'calc(100vh - 112px)', background: '#f0f5ff' }}
        >
          <div className="bg-white rounded-lg p-6 min-h-full">
            <Outlet />
          </div>
        </Content>
      </Layout>
    </Layout>
  )
}

export default BasicLayout
