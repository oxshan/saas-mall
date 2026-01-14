import { ConfigProvider } from 'antd'
import zhCN from 'antd/locale/zh_CN'
import AppRouter from '@/router'

const theme = {
  token: {
    colorPrimary: '#1890ff',
    colorBgContainer: '#ffffff',
    colorBgLayout: '#f0f5ff',
    borderRadius: 6,
  },
  components: {
    Layout: {
      siderBg: '#001529',
      headerBg: '#ffffff',
    },
    Menu: {
      darkItemBg: '#001529',
      darkSubMenuItemBg: '#000c17',
      darkItemSelectedBg: '#1890ff',
    },
  },
}

const App: React.FC = () => {
  return (
    <ConfigProvider locale={zhCN} theme={theme}>
      <AppRouter />
    </ConfigProvider>
  )
}

export default App
