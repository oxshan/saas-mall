import { RunTimeLayoutConfig, RequestConfig } from '@umijs/max';
import { history } from '@umijs/max';
import { requestConfig } from '@/utils/request';
import { getCurrentUser, logout } from '@/services/auth';
import type { UserInfo } from '@/types/user';
import { LogoutOutlined, UserOutlined } from '@ant-design/icons';
import { Dropdown, Avatar, Space } from 'antd';

// 全局初始化数据
export async function getInitialState(): Promise<{
  currentUser?: UserInfo;
}> {
  const token = localStorage.getItem('token');
  if (!token) {
    history.push('/login');
    return {};
  }

  try {
    const currentUser = await getCurrentUser();
    console.log('获取用户信息成功:', currentUser);
    return { currentUser };
  } catch (error) {
    console.error('获取用户信息失败:', error);
    return {};
  }
}

// 布局配置
export const layout: RunTimeLayoutConfig = ({ initialState }) => {
  return {
    logo: '/logo.svg',
    menu: { locale: false },
    actionsRender: () => [
      <Dropdown
        key="user"
        menu={{
          items: [
            {
              key: 'logout',
              icon: <LogoutOutlined />,
              label: '退出登录',
              onClick: () => logout(),
            },
          ],
        }}
      >
        <Space style={{ cursor: 'pointer' }}>
          <Avatar size="small" icon={<UserOutlined />} />
          <span>{initialState?.currentUser?.nickname || '用户'}</span>
        </Space>
      </Dropdown>,
    ],
    onPageChange: () => {
      const { pathname } = history.location;
      const token = localStorage.getItem('token');
      if (!token && pathname !== '/login') {
        history.push('/login');
      }
    },
  };
};

// 请求配置
export const request: RequestConfig = requestConfig;
