import { Card, Divider, List, Space, Tag, Typography, Empty, Collapse, Tooltip} from 'antd';
import './LogsList.scss';
import type { LogDocument } from '../../../../shared/types/logs';
import dayjs from 'dayjs';
import { CaretRightOutlined } from '@ant-design/icons';

const { Text, Paragraph } = Typography;
const { Panel } = Collapse;

interface LogsListProps {
  logs: LogDocument[];
  selectedLog: LogDocument | null;
  handleLogSelect: (log: LogDocument) => void;
  errorNames: string[]; // Добавляем пропс с названиями ошибок
}

export const LogsList = ({ 
  logs, 
  selectedLog, 
  handleLogSelect,
  errorNames 
}: LogsListProps) => {
  const hasDependencyError = (dependency: string) => {
    if (!selectedLog) return false;
    return selectedLog.errors.some(error => 
      error.full_error.toLowerCase().includes(dependency.toLowerCase())
    );
  };

  // Функция для получения уникальных коротких имен ошибок
  const getUniqueErrorTags = (log: LogDocument) => {
    return Array.from(new Set(log.errors.map(e => e.short_name)))
      .filter(name => errorNames.includes(name)); // Фильтруем только существующие имена
  };

  const truncateErrorName = (name: string, maxLength = 15) => {
  return name.length > maxLength 
    ? `${name.substring(0, maxLength)}...` 
    : name;
  };

  const getUniqueLogs = (logs: LogDocument[]) => {
    const uniqueMap = new Map();
    
    logs.forEach(log => {
      const key = `${log.package_field}-${log.timestamp}-${log.errors
        .map(e => e.short_name)
        .sort()
        .join(',')}`;
        
      if (!uniqueMap.has(key)) {
        uniqueMap.set(key, log);
      }
    });
  
    return Array.from(uniqueMap.values());
  };

  const uniqueLogs = getUniqueLogs(logs);

  return (
    <Card className='logs-card'>
      {logs.length === 0 ? (
        <Empty
          image={Empty.PRESENTED_IMAGE_SIMPLE}
          description="Логи не найдены"
          className="empty-list"
        />
      ) : (
        <>
          <List
            itemLayout="horizontal"
            dataSource={uniqueLogs} // Используем отфильтрованный список
            renderItem={(log) => {
              const uniqueErrors = getUniqueErrorTags(log);
              
              return (
                <List.Item 
                  onClick={() => handleLogSelect(log)}
                  className={selectedLog?.timestamp === log.timestamp ? 'selected-log' : ''}
                >
                  <List.Item.Meta
                    title={<Text className='log-title'>{log.package_field}</Text>}
                    description={
                      <Space size="small" direction="vertical" style={{ width: '100%' }}>
                        <div className="error-tags">
                          {uniqueErrors.map((errorName, i) => (
                            <Tooltip 
                              title={errorName} 
                              key={errorName}
                              mouseEnterDelay={0.5}
                            >
                              <Tag 
                                color={i === 0 ? 'red' : 'orange'} 
                                className="error-tag"
                              >
                                {truncateErrorName(errorName)}
                              </Tag>
                            </Tooltip>
                          ))}
                        </div>
                        <Text type="secondary" className="log-time">
                          {dayjs(log.timestamp).format('YYYY-MM-DD HH:mm:ss')}
                        </Text>
                      </Space>
                    }
                  />
                </List.Item>
              );
            }}
          />
          
          {selectedLog && (
            <>
              <Divider orientation="left">Детали ошибок</Divider>
              <Collapse
                bordered={false}
                expandIcon={({ isActive }) => <CaretRightOutlined rotate={isActive ? 90 : 0} />}
                className="error-collapse"
              >
                {selectedLog.errors.map((error, index) => (
                  <Panel 
                    header={`Ошибка #${index + 1} - ${truncateErrorName(error.short_name)}`} 
                    key={index}
                    className="error-panel"
                  >
                    <div className="error-details">
                      <Tooltip title={error.short_name}>
                        <Text strong style={{ marginBottom: 8, display: 'block' }}>
                          Полное название: {error.short_name}
                        </Text>
                      </Tooltip>
                      <Paragraph className="full-error-message">
                        {error.full_error}
                      </Paragraph>
                    </div>
                  </Panel>
                ))}
              </Collapse>

              <Divider orientation="left">Зависимости</Divider>
                <div className="dependencies">
                  {selectedLog.package_dependencies.map((dep) => (
                    <Tooltip title={dep} key={dep}>
                      <Tag 
                        color={hasDependencyError(dep) ? 'red' : 'default'}
                        className="dependency-tag"
                      >
                        {truncateErrorName(dep, 20)}
                      </Tag>
                    </Tooltip>
                  ))}
                </div>
              <Divider orientation="left">Информация о пакете</Divider>
              <div className="package-info">
                <Paragraph>
                  <Text strong>Группа:</Text> {selectedLog.package_group}
                </Paragraph>
                <Paragraph>
                  <Text strong>Описание:</Text> {selectedLog.package_description}
                </Paragraph>
              </div>
            </>
          )}
        </>
      )}
    </Card>
  );
};
