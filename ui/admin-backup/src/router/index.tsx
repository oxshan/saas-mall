import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import { useUserStore } from '@/stores/useUserStore'
import Login from '@/pages/Login'
import BasicLayout from '@/layouts/BasicLayout'
import Dashboard from '@/pages/Dashboard'
import UserList from '@/pages/system/UserList'
import RoleList from '@/pages/system/RoleList'
import MenuList from '@/pages/system/MenuList'

// 路由守卫
const PrivateRoute: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const token = useUserStore((state) => state.token)
  return token ? <>{children}</> : <Navigate to="/login" replace />
}

const AppRouter: React.FC = () => {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route
          path="/"
          element={
            <PrivateRoute>
              <BasicLayout />
            </PrivateRoute>
          }
        >
          <Route index element={<Navigate to="/dashboard" replace />} />
          <Route path="dashboard" element={<Dashboard />} />
          <Route path="system/user" element={<UserList />} />
          <Route path="system/role" element={<RoleList />} />
          <Route path="system/menu" element={<MenuList />} />
        </Route>
      </Routes>
    </BrowserRouter>
  )
}

export default AppRouter
