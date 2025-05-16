import { Typography } from 'antd';
import './Header.scss';

const { Title, Text } = Typography;

export const Header = () => {
  return (
    <div className='header-container'>
      <Title level={1} className='main-title'>
        Error Logger Analyzer Dashboard
      </Title>
      <Text className='subtitle'>Система мониторинга и анализа ошибок</Text>
    </div>
  );
};
