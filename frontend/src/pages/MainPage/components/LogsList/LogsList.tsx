import { Card, Divider, List, Space, Tag, Typography } from 'antd';
import './LogsList.scss';
import type { LogDocument } from '../../../../shared/types/logs';
import dayjs from 'dayjs';

const { Text, Paragraph } = Typography;

interface LogsListProps {
  logs: LogDocument[];
  selectedLog: LogDocument | null;
  handleLogSelect: (log: LogDocument) => void;
}

export const LogsList = ({ logs, selectedLog, handleLogSelect }: LogsListProps) => {
  return (
    <Card className='logs-card'>
      <List
        itemLayout="horizontal"
        dataSource={logs}
        renderItem={(log) => (
          <List.Item 
            onClick={() => handleLogSelect(log)}
            className={selectedLog?.timestamp === log.timestamp ? 'selected-log' : ''}
          >
            <List.Item.Meta
              title={<Text className='log-title'>{log.package_field}</Text>}
              description={
                <Space size="small">
                  {log.errors.map((error) => (
                    <Tag color="red" key={error.short_name}>{error.short_name}</Tag>
                  ))}
                  <Text type="secondary">
                    {dayjs(log.timestamp).format('YYYY-MM-DD HH:mm:ss')}
                  </Text>
                </Space>
              }
            />
          </List.Item>
        )}
      />
      
      {selectedLog && (
        <>
          <Divider orientation="left">Зависимости</Divider>
          <div className="dependencies">
            {selectedLog.package_dependencies.map((dep) => (
              <Tag 
                key={dep} 
                color="default"
                className="dependency-tag"
              >
                {dep}
              </Tag>
            ))}
          </div>

          <Divider orientation="left">Информация о пакете</Divider>
          <div className="package-info">
            <Paragraph>
              <Text strong>Язык:</Text> {selectedLog.programming_language}
            </Paragraph>
            <Paragraph>
              <Text strong>Группа:</Text> {selectedLog.package_group}
            </Paragraph>
            <Paragraph>
              <Text strong>Описание:</Text> {selectedLog.package_description}
            </Paragraph>
          </div>
        </>
      )}
    </Card>
  );
};
