import { useState, useEffect } from 'react';
import { Row, Col, Spin, notification} from 'antd';
import { Header } from './components/Header/Header';
import { Filters } from './components/FilterButtons/FilterButtons';
import { LogsList } from './components/LogsList/LogsList';
import { LogDetails } from './components/LogDetails/LogDetails';
import { 
  fetchLogs, 
  fetchPackages,
  fetchErrors
} from '../../services/logs.service';
import type { SearchLogsParams, LogDocument } from '../../shared/types/logs';
import './MainPage.scss';

export const MainPage = () => {
  const [selectedLog, setSelectedLog] = useState<LogDocument | null>(null);
  const [logs, setLogs] = useState<LogDocument[]>([]);
  const [filters, setFilters] = useState<SearchLogsParams>({});
  const [visibleFilters, setVisibleFilters] = useState({
    packages: false,
    errors: false
  });
  const [packages, setPackages] = useState<string[]>([]);
  const [errors, setErrors] = useState<string[]>([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const initData = async () => {
      setLoading(true);
      try {
        const [pkgs, errs] = await Promise.all([
          fetchPackages(),
          fetchErrors()
        ]);
        
        setPackages(pkgs);
        setErrors(errs);
      } catch (error) {
        console.error('Initialization error:', error);
      } finally {
        setLoading(false);
      }
    };
    
    initData();
  }, []);

  const loadLogs = async () => {
    setLoading(true);
    try {
      const response = await fetchLogs(filters);
      console.log('Received logs:', response.logs);
      
      if (response.logs.length === 0) {
        notification.info({
          message: 'Информация',
          description: 'Логи по заданным фильтрам не найдены'
        });
      }
      
      setLogs(response.logs);
    } catch (error) {
      console.error('Error loading logs:', error);
      notification.error({
        message: 'Ошибка',
        description: error instanceof Error ? error.message : 'Неизвестная ошибка'
      });
      setLogs([]);
    } finally {
      setLoading(false);
    }
  };

  const handleLogSelect = (log: LogDocument) => {
    setSelectedLog(log);
  };

  const handleDateTimeChange = (dateTime: string | null) => {
    setFilters(prev => ({...prev, date: dateTime || undefined}));
  };

  const handleFilterSelect = (type: keyof SearchLogsParams, value: string) => {
    const newFilters = {
      ...filters,
      [type]: filters[type] === value ? undefined : value
    };
    setFilters(newFilters);
    setVisibleFilters(prev => ({ ...prev, [type]: false }));
  };

  const toggleFilter = (filter: string) => {
    setVisibleFilters(prev => ({
      ...prev,
      [filter]: !prev[filter as keyof typeof prev]
    }));
  };

  return (
    <div className='main-page'>
      <Header />
      
      <Spin spinning={loading} delay={300}>
        <Filters
          filters={filters}
          visibleFilters={visibleFilters}
          packages={packages}
          errors={errors}
          toggleFilter={toggleFilter}
          handleFilterSelect={handleFilterSelect}
          handleDateTimeChange={handleDateTimeChange}
          handleApplyFilters={loadLogs}
        />

        <Row gutter={[16, 16]}>
          <Col xs={24} md={12}>
            <LogsList 
              logs={logs} 
              selectedLog={selectedLog} 
              handleLogSelect={handleLogSelect} 
            />
          </Col>
          <Col xs={24} md={12}>
            <LogDetails selectedLog={selectedLog} />
          </Col>
        </Row>
      </Spin>
    </div>
  );
};
