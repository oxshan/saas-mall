import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import { useUserStore } from '@/stores/useUserStore'
import Login from '@/pages/Login'
import Home from '@/pages/Home'

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
              <Home />
            </PrivateRoute>
          }
        />
      </Routes>
    </BrowserRouter>
  )
}

export default AppRouter
