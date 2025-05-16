import { useLogin } from '@/hooks/auth/auth.hooks'
import type { LoginFormData } from '@/shared/types/auth'
import { Button, Card, Form, Input, Modal } from 'antd'
import { Link, useNavigate } from 'react-router-dom'
import './AuthPages.scss'

export const LoginPage = () => {
	const [form] = Form.useForm()
	const { mutate, isPending } = useLogin()
	const navigate = useNavigate()

	const handleSubmit = (values: LoginFormData) => {
		mutate(values, {
			onSuccess: () => {
				navigate('/courses')
			},
			onError: () => {
				Modal.error({
					title: 'Ошибка входа',
					content: 'Неверные учетные данные или аккаунт не активирован',
				})
			},
		})
	}
	return (
		<div className='auth-page'>
			<Card title='Вход в систему' className='auth-card'>
				<Form form={form} onFinish={handleSubmit}>
					<Form.Item
						name='username'
						rules={[{ required: true, message: 'Введите имя пользователя' }]}
					>
						<Input placeholder='Имя пользователя' />
					</Form.Item>

					<Form.Item
						name='password'
						rules={[{ required: true, message: 'Введите пароль' }]}
					>
						<Input.Password placeholder='Пароль' />
					</Form.Item>

					<Button type='primary' htmlType='submit' loading={isPending} block>
						Войти
					</Button>

					<div className='auth-link'>
						Нет аккаунта? <Link to='/register'>Зарегистрироваться</Link>
					</div>
				</Form>
			</Card>
		</div>
	)
}
