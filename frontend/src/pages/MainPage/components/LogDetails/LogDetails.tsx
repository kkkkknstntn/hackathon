import { Card, Tabs, Alert, Divider, Typography } from 'antd';
import './LogDetails.scss';
import type { LogDocument } from '../../../../shared/types/logs';
import { useEffect, useState } from 'react';

const { TabPane } = Tabs;
const { Text } = Typography;

interface LogDetailsProps {
  selectedLog: LogDocument | null;
}

export const LogDetails = ({ selectedLog }: LogDetailsProps) => {
  const [currentLog, setCurrentLog] = useState<string>('');
  
  useEffect(() => {
    if (selectedLog) {
      setCurrentLog(selectedLog.log);
    }
  }, [selectedLog]);

  if (!selectedLog) return null;

  return (
    <Card className='log-details-card' key={selectedLog.timestamp}>
      <Tabs defaultActiveKey="1">
        <TabPane tab="Сырой лог" key="1">
          <pre className="raw-log">{currentLog}</pre>
        </TabPane>
        <TabPane tab="Анализ" key="2">
          <Alert
            message={`Ошибка: ${selectedLog.errors[0]?.short_name || 'Неизвестная ошибка'}`}
            description={selectedLog.errors[0]?.full_error || 'Описание отсутствует'}
            type="error"
            showIcon
          />
          <Divider />
          <Text strong>Рекомендуемые действия:</Text>
          <ul>
            <li>Проверить зависимости: {selectedLog.package_dependencies.join(', ')}</li>
            <li>Обновить версию пакета</li>
          </ul>
        </TabPane>
      </Tabs>
    </Card>
  );
};
