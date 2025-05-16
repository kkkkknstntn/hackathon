import { Card, Divider, List, Space, Tag, Typography } from 'antd';
import { WarningOutlined } from '@ant-design/icons';
import './LogsList.scss';

const { Text, Paragraph } = Typography;

interface LogsListProps {
  logs: any[];
  selectedLog: any;
  handleLogSelect: (log: any) => void;
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
                  {log.errors.map((error: string) => (
                    <Tag color="red" key={error}>{error}</Tag>
                  ))}
                  <Text type="secondary">{new Date(log.timestamp).toLocaleString()}</Text>
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
            {selectedLog.dependencies.map((dep: any) => (
              <Tag 
                key={dep.name} 
                color={dep.status === 'error' ? 'red' : 'default'}
                className="dependency-tag"
              >
                {dep.name}
                {dep.status === 'error' && <WarningOutlined style={{ marginLeft: 5 }} />}
              </Tag>
            ))}
          </div>

          <Divider orientation="left">Информация о пакете</Divider>
          <div className="package-info">
            <Paragraph>
              <Text strong>Имя:</Text> {selectedLog.package_field.split('-')[0]}
            </Paragraph>
            <Paragraph>
              <Text strong>Версия:</Text> {selectedLog.packageInfo.version}
            </Paragraph>
            <Paragraph>
              <Text strong>Последнее обновление:</Text> {selectedLog.packageInfo.lastUpdated}
            </Paragraph>
            <Paragraph>
              <Text strong>Автор:</Text> {selectedLog.packageInfo.author}
            </Paragraph>
            <Paragraph>
              <Text strong>Лицензия:</Text> {selectedLog.packageInfo.license}
            </Paragraph>
          </div>
        </>
      )}
    </Card>
  );
};
