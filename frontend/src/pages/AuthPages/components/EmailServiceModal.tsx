import { MailOutlined } from '@ant-design/icons'
import { Button, Modal } from 'antd'
import { useEffect, useState } from 'react'

type EmailDomain = 'gmail' | 'yandex' | 'mail' | 'other'

const getEmailServiceLink = (email: string): { url: string; name: string } => {
	const domain = email.split('@')[1]?.split('.')[0] || 'gmail'

	const services: Record<EmailDomain, { url: string; name: string }> = {
		gmail: { url: 'https://mail.google.com', name: 'Gmail' },
		yandex: { url: 'https://mail.yandex.ru', name: 'Яндекс.Почта' },
		mail: { url: 'https://e.mail.ru', name: 'Mail.ru' },
		other: { url: 'https://mail.google.com', name: 'Email-сервис' },
	}

	return services[domain as EmailDomain] || services.other
}

export const EmailServiceModal = ({
	email,
	open,
	onClose,
}: {
	email: string
	open: boolean
	onClose: () => void
}) => {
	const [service, setService] = useState<{ url: string; name: string }>()

	useEffect(() => {
		if (email) {
			setService(getEmailServiceLink(email))
		}
	}, [email])

	return (
		<Modal
			title='Активация аккаунта'
			open={open}
			onCancel={onClose}
			footer={[
				<Button key='close' onClick={onClose}>
					Закрыть
				</Button>,
			]}
		>
			<div style={{ textAlign: 'center', padding: '20px 0' }}>
				<p>Мы отправили письмо с подтверждением на вашу почту: {email}</p>
				{service && (
					<Button
						type='primary'
						icon={<MailOutlined />}
						href={service.url}
						target='_blank'
						style={{ marginTop: 16 }}
					>
						Открыть {service.name}
					</Button>
				)}
			</div>
		</Modal>
	)
}
