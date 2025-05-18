import { customTheme, darkTheme, lightTheme } from '@/styles/theme'
import {
  BulbOutlined,
  BulbFilled, HomeOutlined, BarChartOutlined
} from '@ant-design/icons'
import { NavLink } from 'react-router-dom';
import { Button, ConfigProvider, Layout, Dropdown, type MenuProps, Space } from 'antd'
import { useState, type ReactNode } from 'react'
import './MainLayout.scss'
const { Header, Content } = Layout

interface MainLayoutProps {
  children?: ReactNode
}

const THEMES = {
  light: lightTheme,
  dark: darkTheme,
  custom: customTheme,
}

export const MainLayout = ({ children }: MainLayoutProps) => {
  const [currentTheme, setCurrentTheme] = useState<'light' | 'dark' | 'custom'>('light')
  const [] = useState('')

  const handleThemeChange = (key: string) => {
    setCurrentTheme(key as 'light' | 'dark' | 'custom')
  }

  const themeMenu: MenuProps = {
    items: [
      { key: 'light', label: 'Светлая тема' },
      { key: 'dark', label: 'Тёмная тема'},
      { key: 'custom', label: 'Кастомная тема'},
    ],
    onClick: ({ key }) => handleThemeChange(key)
  }

  return (
    <ConfigProvider theme={THEMES[currentTheme]}>
      <Layout className="main-layout">
        <Header className="main-header">
          <div className="header-content">
            <div className="logo-wrapper">
              <div className="logo-full">Logcutter</div>
              <div className="logo-compact">
                <div>Logcutter</div>
              </div>
            </div>

            <Space className="controls-group" size="middle">
              <Space size={8}>
                <NavLink to="/" end>
                    <Button
                      type={'default'}
                      className="nav-btn"
                      icon={<HomeOutlined />}
                    >
                      <span className="btn-text">Главная</span>
                    </Button>
                  </NavLink>
                  <NavLink to="/stats">
                    <Button
                      type={'default'}
                      className="nav-btn"
                      icon={<BarChartOutlined />}
                    >
                      <span className="btn-text">Статистика</span>
                    </Button>
                  </NavLink>
                <Dropdown menu={themeMenu} trigger={['click']}>
                  <Button icon={currentTheme === 'dark' ? <BulbFilled /> : <BulbOutlined />} className="theme-btn">
                    <span className="btn-text">Тема</span>
                  </Button>
              </Dropdown>
              </Space>
            </Space>

          </div>
        </Header>

        <Content className="main-content">
          {children}
        </Content>
      </Layout>
    </ConfigProvider>
  );

};
