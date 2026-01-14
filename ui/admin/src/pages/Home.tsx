import { Button } from 'antd'
import { useUserStore } from '@/stores/useUserStore'
import { useNavigate } from 'react-router-dom'

const Home: React.FC = () => {
  const logout = useUserStore((state) => state.logout)
  const navigate = useNavigate()

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold mb-4">欢迎使用商家后台</h1>
      <Button type="primary" onClick={handleLogout}>
        退出登录
      </Button>
    </div>
  )
}

export default Home
