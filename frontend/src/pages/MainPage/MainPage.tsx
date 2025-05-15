import { CodeOutlined, WarningOutlined } from '@ant-design/icons'
import { Alert, Card, Col, Divider, Row, Space, Typography } from 'antd'
import './MainPage.scss'

const { Title, Text, Paragraph } = Typography

export const MainPage = () => {
	return (
		<div className='main-page'>
			<Row gutter={[16, 16]}>
				<Col span={24}>
					<Card className='header-card'>
						<Title level={1} className='main-title'>
							Error Logger Analyser Dashboard
						</Title>
						<Text type='secondary'>Система мониторинга и анализа ошибок</Text>
					</Card>
				</Col>

				<Col span={24}>
					<Divider orientation='left'>Основные показатели</Divider>
				</Col>

				<Col xs={24} md={12} lg={8}>
					<Card className='metric-card'>
						<Title level={4}>Всего ошибок</Title>
						<Text strong className='metric-value'>
							1,234
						</Text>
						<Paragraph type='secondary'>За последние 24 часа</Paragraph>
					</Card>
				</Col>

				<Col xs={24} md={12} lg={8}>
					<Card className='metric-card warning'>
						<Title level={4}>Критические ошибки</Title>
						<Text strong className='metric-value warning-text'>
							56
						</Text>
						<Paragraph type='secondary'>
							Требуют немедленного внимания
						</Paragraph>
					</Card>
				</Col>

				<Col xs={24} lg={8}>
					<Card className='metric-card success'>
						<Title level={4}>Решено проблем</Title>
						<Text strong className='metric-value success-text'>
							890
						</Text>
						<Paragraph type='secondary'>За текущий месяц</Paragraph>
					</Card>
				</Col>

				<Col span={24}>
					<Divider orientation='left'>Последние события</Divider>
				</Col>

				<Col span={24}>
					<Card>
						<Space direction='vertical' size={16} style={{ width: '100%' }}>
							<Alert
								message='Ошибка авторизации'
								description='Неверные учетные данные в модуле пользователей'
								type='error'
								showIcon
								icon={<WarningOutlined />}
							/>
							<Alert
								message='Предупреждение системы'
								description='Высокая нагрузка на сервер БД'
								type='warning'
								showIcon
							/>
							<Alert
								message='Информационное сообщение'
								description='Обновление v1.2.3 успешно установлено'
								type='info'
								showIcon
							/>
						</Space>
					</Card>
				</Col>

				<Col span={24}>
					<Divider orientation='left'>Пример кода</Divider>
				</Col>

				<Col span={24}>
					<Card className='code-example'>
						<Paragraph>
							<Text strong>Пример обработки ошибки:</Text>
							<pre className='code-block'>
								<CodeOutlined />{' '}
								{`
                try {
                  fetchData();
                } catch (error) {
                  logger.error('API Error:', error);
                  showNotification('Ошибка загрузки данных');
                }
                `}
							</pre>
						</Paragraph>
					</Card>
				</Col>
			</Row>
		</div>
	)
}
