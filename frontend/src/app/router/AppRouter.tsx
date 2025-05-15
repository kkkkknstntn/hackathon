import { ActivatePage } from '@/pages/AuthPages/ActivatePage/ActivatePage'
import { LoginPage } from '@/pages/AuthPages/LoginPage/LoginPage'
import { RegisterPage } from '@/pages/AuthPages/RegisterPage/RegisterPage'
import { MainPage } from '@/pages/MainPage/MainPage'
import { Outlet, Route, Routes } from 'react-router-dom'
import { MainLayout } from '../layouts/MainLayout'

export const Router = () => {
	return (
		<Routes>
			<Route path='/login' element={<LoginPage />} />
			<Route path='/register' element={<RegisterPage />} />
			<Route path='/activate-account' element={<ActivatePage />} />

			<Route element={<MainLayoutWrapper />}>
				<Route path='/' element={<MainPage />} />
			</Route>
		</Routes>
	)
}

const MainLayoutWrapper = () => {
	return (
		<MainLayout>
			<Outlet />
		</MainLayout>
	)
}
