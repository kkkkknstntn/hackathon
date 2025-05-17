import { Card, Row, Col, Typography, Spin, Empty, Alert } from 'antd';
import { useQuery } from '@tanstack/react-query';
import { fetchErrorStats } from '../../services/stats.service';
import './StatsPage.scss';

const { Title, Text } = Typography;

export const StatsPage = () => {
  const { data: stats, isLoading, isError } = useQuery({
    queryKey: ['errorStats'],
    queryFn: fetchErrorStats,
    retry: 2 // Количество попыток повторного запроса при ошибке
  });

  // Проверка на пустые данные
  const isEmptyData = !stats || Object.keys(stats).length === 0;

  if (isLoading) {
    return (
      <div className="stats-page loading">
        <Spin size="large" tip="Загрузка статистики..." />
      </div>
    );
  }

  if (isError) {
    return (
      <div className="stats-page error">
        <Alert
          message="Ошибка загрузки данных"
          description="Не удалось загрузить статистику ошибок. Попробуйте обновить страницу."
          type="error"
          showIcon
        />
      </div>
    );
  }

  return (
    <div className="stats-page">
      <Title level={2}>Статистика ошибок</Title>
      
      {isEmptyData ? (
        <Empty
          image={Empty.PRESENTED_IMAGE_SIMPLE}
          description="Нет данных для отображения"
        />
      ) : (
        <Row gutter={[16, 16]}>
          {Object.entries(stats).map(([errorName, stat]) => (
            <Col xs={24} md={12} lg={8} key={errorName}>
              <Card title={errorName} className="stat-card">
                <Text strong>Количество: </Text>
                {stat?.count ?? 'Нет данных'}
                
                <div style={{ marginTop: 8 }}>
                  <Text strong>Пакеты: </Text>
                  {stat?.packages?.length > 0 
                    ? stat.packages.join(', ')
                    : 'Не найдено'}
                </div>
              </Card>
            </Col>
          ))}
        </Row>
      )}
    </div>
  );
};
