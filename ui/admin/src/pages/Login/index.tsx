import { LockOutlined, UserOutlined } from '@ant-design/icons';
import { LoginForm, ProFormText } from '@ant-design/pro-components';
import { history, useModel } from '@umijs/max';
import { message } from 'antd';
import { login, getCurrentUser } from '@/services/auth';

const Login: React.FC = () => {
  const { refresh } = useModel('@@initialState');

  const handleSubmit = async (values: { username: string; password: string }) => {
    try {
      const res = await login(values);
      if (!res?.token) {
        message.error('登录失败：服务器返回数据异常');
        return;
      }
      localStorage.setItem('token', res.token);
      message.success('登录成功');
      await refresh();
      history.push('/dashboard');
    } catch (error: any) {
      message.error(error.message || '登录失败，请检查后端服务是否启动');
    }
  };

  return (
    <div className="flex items-center justify-center min-h-screen bg-gradient-to-br from-blue-50 to-blue-100">
      <LoginForm
        title="商家后台"
        subTitle="SaaS Mall 管理系统"
        onFinish={handleSubmit}
      >
        <ProFormText
          name="username"
          fieldProps={{ size: 'large', prefix: <UserOutlined /> }}
          placeholder="用户名: admin"
          rules={[{ required: true, message: '请输入用户名' }]}
        />
        <ProFormText.Password
          name="password"
          fieldProps={{ size: 'large', prefix: <LockOutlined /> }}
          placeholder="密码: 123456"
          rules={[{ required: true, message: '请输入密码' }]}
        />
      </LoginForm>
    </div>
  );
};

export default Login;
