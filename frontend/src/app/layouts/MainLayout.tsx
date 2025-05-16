import { customTheme, darkTheme, lightTheme } from '@/styles/theme'
import {
	DashboardOutlined,
	FileTextOutlined,
	MenuFoldOutlined,
	MenuUnfoldOutlined,
	WarningOutlined,
} from '@ant-design/icons'
import { Button, ConfigProvider, Layout, Menu, Space } from 'antd'
import { useState, type ReactNode } from 'react'
import { Link, Outlet, useLocation } from 'react-router-dom'
import './MainLayout.scss'

const { Header, Sider, Content } = Layout

interface MenuItem {
	key: string
	path: string
	label: string
	icon: ReactNode
}

interface MainLayoutProps {
	children?: ReactNode
}

// Конфигурация меню
const MENU_ITEMS: MenuItem[] = [
	{
		key: '1',
		path: '/',
		label: 'Главная',
		icon: <DashboardOutlined />,
	},
	{
		key: '2',
		path: '/link1',
		label: 'Ссылка 1',
		icon: <FileTextOutlined />,
	},
	{
		key: '3',
		path: '/link2',
		label: 'Ссылка 2',
		icon: <WarningOutlined />,
	},
]

const THEMES = {
	light: lightTheme,
	dark: darkTheme,
	custom: customTheme,
}

export const MainLayout = ({ children }: MainLayoutProps) => {
	const [collapsed, setCollapsed] = useState(false)
	const [currentTheme, setCurrentTheme] = useState<'light' | 'dark' | 'custom'>(
		'light'
	)
	const location = useLocation()

	const selectedKeys = MENU_ITEMS.filter(
		item => location.pathname === item.path
	).map(item => item.key)

	return (
		<ConfigProvider theme={THEMES[currentTheme]}>
			<Layout className='main-layout' hasSider style={{ minHeight: '100vh' }}>
				<Sider
					theme='light'
					width={250}
					breakpoint='lg'
					collapsedWidth='0'
					trigger={null}
					collapsible
					collapsed={collapsed}
				>
					<div className='logo'>Error Logger Analyser</div>
					<Menu
						mode='inline'
						selectedKeys={selectedKeys}
						items={MENU_ITEMS.map(item => ({
							key: item.key,
							icon: item.icon,
							label: <Link to={item.path}>{item.label}</Link>,
						}))}
					/>
				</Sider>
				<Layout>
					<Header
						className='header'
						style={{
							display: 'flex',
							alignItems: 'center',
							justifyContent: 'space-between',
						}}
					>
						<Button
							type='text'
							icon={collapsed ? <MenuUnfoldOutlined /> : <MenuFoldOutlined />}
							onClick={() => setCollapsed(!collapsed)}
							className='menu-collapse-button'
						/>

						{/* Кнопки переключения темы */}
						<Space size='middle'>
							<Button
								type={currentTheme === 'light' ? 'primary' : 'default'}
								onClick={() => setCurrentTheme('light')}
							>
								Светлая
							</Button>
							<Button
								type={currentTheme === 'dark' ? 'primary' : 'default'}
								onClick={() => setCurrentTheme('dark')}
							>
								Тёмная
							</Button>
							<Button
								type={currentTheme === 'custom' ? 'primary' : 'default'}
								onClick={() => setCurrentTheme('custom')}
							>
								Кастомная
							</Button>
						</Space>
					</Header>
					<Content className='content'>
						<div className='content-wrapper'>
							<Outlet />
							{children}
						</div>
					</Content>
				</Layout>
			</Layout>
		</ConfigProvider>
	)
}
