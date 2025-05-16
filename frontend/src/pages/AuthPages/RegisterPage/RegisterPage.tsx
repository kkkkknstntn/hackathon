import { useRegister } from '@/hooks/auth/auth.hooks'
import type { RegisterFormData } from '@/shared/types/auth'
import { Button, Card, Form, Input, Modal } from 'antd'
import { useState } from 'react'
import { Link } from 'react-router-dom'
import { EmailServiceModal } from '../components/EmailServiceModal'
import './AuthPages.scss'

export const RegisterPage = () => {
	const [form] = Form.useForm()
	const { mutate, isPending } = useRegister()
	const [successModalOpen, setSuccessModalOpen] = useState(false)
	const [registeredEmail, setRegisteredEmail] = useState('')

	const handleSubmit = (values: RegisterFormData) => {
		mutate(values, {
			onSuccess: () => {
				setRegisteredEmail(values.username)
				setSuccessModalOpen(true)
				form.resetFields()
			},
			onError: error => {
				Modal.error({
					title: 'Ошибка регистрации',
					content: error.message || 'Произошла ошибка при регистрации',
				})
			},
		})
	}

	return (
		<div className='auth-page'>
			<Card title='Регистрация' className='auth-card'>
				<Form form={form} onFinish={handleSubmit}>
					<Form.Item
						name='username'
						rules={[{ required: true, message: 'Введите имя пользователя' }]}
					>
						<Input placeholder='Имя пользователя' />
					</Form.Item>

					<Form.Item
						name='password'
						rules={[
							{
								required: true,
								min: 8,
								message: 'Пароль должен быть не менее 8 символов',
							},
						]}
					>
						<Input.Password placeholder='Пароль' />
					</Form.Item>

					<Button type='primary' htmlType='submit' loading={isPending} block>
						Зарегистрироваться
					</Button>

					<div className='auth-link'>
						Уже есть аккаунт? <Link to='/login'>Войти</Link>
					</div>
				</Form>
			</Card>

			<EmailServiceModal
				email={registeredEmail}
				open={successModalOpen}
				onClose={() => setSuccessModalOpen(false)}
			/>
		</div>
	)
}
