import { useState } from 'react';
import { Card, Row, Col, Typography, Spin, Empty, Alert, List } from 'antd';
import { useQuery } from '@tanstack/react-query';
import { fetchErrorStats } from '../../services/stats.service';
import './StatsPage.scss';

const { Title, Text } = Typography;

export const StatsPage = () => {
  const [selectedError, setSelectedError] = useState<string | null>(null);
  const { data: stats, isLoading, isError } = useQuery({
    queryKey: ['errorStats'],
    queryFn: fetchErrorStats,
    retry: 2
  });

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
      <Title level={2} className="page-title">Статистика ошибок</Title>
      
      {isEmptyData ? (
        <Empty
          image={Empty.PRESENTED_IMAGE_SIMPLE}
          description="Нет данных для отображения"
        />
      ) : (
        <Row gutter={[24, 24]} className="stats-container">
          {/* Список ошибок слева */}
          <Col xs={24} md={8} className="errors-list">
            <Card title="Список ошибок" className="list-card">
              <List
                dataSource={Object.keys(stats)}
                renderItem={(errorName) => (
                  <List.Item
                    onClick={() => setSelectedError(errorName)}
                    className={`list-item ${selectedError === errorName ? 'selected' : ''}`}
                  >
                    {errorName}
                  </List.Item>
                )}
              />
            </Card>
          </Col>

          {/* Детали ошибки справа */}
          <Col xs={24} md={16} className="error-details">
            {selectedError ? (
              <Card 
                title={`Детали ошибки: ${selectedError}`} 
                className="details-card"
              >
                <div className="details-content">
                  <Text strong>Количество: </Text>
                  <Text>{stats[selectedError]?.count ?? 'Нет данных'}</Text>
                  
                  <div className="packages-section">
                    <Text strong>Пакеты: </Text>
                    {stats[selectedError]?.packages?.length > 0 ? (
                      <div className="packages-list">
                        {stats[selectedError].packages.join(', ')}
                      </div>
                    ) : (
                      <Text>Не найдено</Text>
                    )}
                  </div>
                </div>
              </Card>
            ) : (
              <Card className="details-card">
                <Empty
                  image={Empty.PRESENTED_IMAGE_SIMPLE}
                  description="Выберите ошибку для просмотра деталей"
                />
              </Card>
            )}
          </Col>
        </Row>
      )}
    </div>
  );
};
