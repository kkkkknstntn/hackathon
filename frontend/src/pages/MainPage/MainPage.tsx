import { useState, useEffect } from 'react';
import { Row, Col } from 'antd';
import { Header } from './components/Header/Header';
import { Filters } from './components/FilterButtons/FilterButtons';
import { LogsList } from './components/LogsList/LogsList';
import { LogDetails } from './components/LogDetails/LogDetails';
import './MainPage.scss';

export const MainPage = () => {
  const [selectedLog, setSelectedLog] = useState<any>(null);
  const [logs, setLogs] = useState<any[]>([]);
  const [filters, setFilters] = useState({
    programmingLanguage: null,
    packageName: null,
    errorType: null
  });
  const [visibleFilters, setVisibleFilters] = useState({
    languages: false,
    packages: false,
    errors: false
  });

  const programmingLanguages = ['C++', 'PHP', 'C#'];
  const packagesByLanguage = {
    'C++': ['perl-YAML-PP-LibYAML', 'perl-YAML-PP-Ref', 'pgagent', 'plots'],
    'PHP': ['laravel/framework', 'symfony/http-foundation', 'guzzlehttp/guzzle'],
    'C#': ['Newtonsoft.Json', 'NUnit', 'Dapper']
  };
  const errorTypes = ['composer_error', 'rpm_error', 'command_nonzero', 'syntax_error', 'dependency_error'];

  useEffect(() => {
    const mockLogs = [
      {
        log: "Пример лога ошибки...",
        programming_language: "C++",
        errors: ["composer_error", "rpm_error"],
        package_field: "perl-YAML-PP-LibYAML-0.005-alt2",
        timestamp: "2025-05-16T17:04:45.303723",
        dependencies: [
          { name: "perl-YAML-PP", status: "ok" },
          { name: "perl-LibYAML", status: "error" },
          { name: "perl-Base", status: "ok" }
        ],
        packageInfo: {
          version: "0.005-alt2",
          lastUpdated: "2025-05-16",
          author: "John Doe",
          license: "MIT"
        }
      }
    ];
    setLogs(mockLogs);
  }, []);

  const handleLogSelect = (log: any) => {
    setSelectedLog(log);
  };

  const handleDateTimeChange = (dateTime: string | null) => {
    console.log('Selected datetime:', dateTime);
    // Здесь можно добавить логику фильтрации по дате
    // Например:
    // setFilters(prev => ({...prev, dateTime}));
  };

  const handleFilterSelect = (type: string, value: string) => {
    setFilters(prev => ({
      ...prev,
      [type]: prev[type as keyof typeof prev] === value ? null : value
    }));
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
      
      <Filters
        filters={filters}
        visibleFilters={visibleFilters}
        programmingLanguages={programmingLanguages}
        packagesByLanguage={packagesByLanguage}
        errorTypes={errorTypes}
        toggleFilter={toggleFilter}
        handleFilterSelect={handleFilterSelect}
		handleDateTimeChange={handleDateTimeChange}
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
    </div>
  );
};