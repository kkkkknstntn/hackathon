import { Card, Tabs, Alert, Divider, Typography } from 'antd';
import './LogDetails.scss';

const { TabPane } = Tabs;
const { Text } = Typography;

interface LogDetailsProps {
  selectedLog: any;
}

export const LogDetails = ({ selectedLog }: LogDetailsProps) => {
  if (!selectedLog) return null;

  return (
    <Card className='log-details-card'>
      <Tabs defaultActiveKey="1">
        <TabPane tab="Сырой лог" key="1">
          <pre className="raw-log">{selectedLog.log}</pre>
        </TabPane>
        <TabPane tab="Анализ" key="2">
          <Alert
            message="Ошибка сборки"
            description="Пакет не прошел тесты"
            type="error"
            showIcon
          />
          <Divider />
          <Text strong>Рекомендуемые действия:</Text>
          <ul>
            <li>Проверить зависимости</li>
            <li>Обновить версию пакета</li>
            <li>Проверить системные требования</li>
          </ul>
        </TabPane>
      </Tabs>
    </Card>
  );
};
