import { useActivateAccount } from '@/hooks/auth/auth.hooks'
import { Button, Modal, Result, Spin } from 'antd'
import { useEffect } from 'react'
import { useSearchParams } from 'react-router-dom'
import './AuthPages.scss'

export const ActivatePage = () => {
	const [searchParams] = useSearchParams()
	const email = searchParams.get('email')
	const token = searchParams.get('token')

	const { isPending, isError, error } = useActivateAccount(
		token || '',
		email || ''
	)

	useEffect(() => {
		if (isError && error) {
			Modal.error({
				title: 'Ошибка активации',
				content: error.message,
			})
		}
	}, [isError, error])

	if (isPending) {
		return (
			<div className='auth-page'>
				<Spin size='large' tip='Активация аккаунта...' />
			</div>
		)
	}

	return (
		<div className='auth-page'>
			<Result
				status={!isError ? 'error' : 'success'}
				title={!isError ? 'Ошибка активации' : 'Аккаунт активирован'}
				subTitle={
					!isError
						? 'Неверная или устаревшая ссылка активации'
						: 'Теперь вы можете войти в систему'
				}
				extra={[
					<Button type='primary' key='login' href='/login'>
						Перейти к авторизации
					</Button>,
				]}
			/>
		</div>
	)
}
