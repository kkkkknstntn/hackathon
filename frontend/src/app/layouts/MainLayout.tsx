import { customTheme, darkTheme, lightTheme } from '@/styles/theme'
import {
  SearchOutlined,
  BulbOutlined,
  BulbFilled
} from '@ant-design/icons'
import { Button, ConfigProvider, Layout, Input, Dropdown, type MenuProps, Space } from 'antd'
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
              <div className="logo-full">Error Logger Analyzer</div>
              <div className="logo-compact">
                <div>Error</div>
                <div>Logger</div>
                <div>Analyzer</div>
              </div>
            </div>

            <Space className="controls-group" size="middle">
              <Input
                className="search-input"
                placeholder="Поиск ошибок"
                prefix={<SearchOutlined />}
                size="middle"
                allowClear
              />
              
              <Dropdown menu={themeMenu} trigger={['click']}>
                <Button icon={currentTheme === 'dark' ? <BulbFilled /> : <BulbOutlined />} className="theme-btn">
                  <span className="btn-text">Тема</span>
                </Button>
              </Dropdown>
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