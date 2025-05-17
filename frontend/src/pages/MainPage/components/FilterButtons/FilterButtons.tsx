import { Button, Dropdown, Menu, DatePicker, Space, ConfigProvider } from 'antd';
import { DownOutlined, CalendarOutlined } from '@ant-design/icons';
import './FilterButtons.scss';
import dayjs, { Dayjs } from 'dayjs';
import customParseFormat from 'dayjs/plugin/customParseFormat';
import advancedFormat from 'dayjs/plugin/advancedFormat';

dayjs.extend(customParseFormat);
dayjs.extend(advancedFormat);

interface FiltersProps {
  filters: any;
  visibleFilters: any;
  programmingLanguages: string[];
  packagesByLanguage: Record<string, string[]>;
  errorTypes: string[];
  toggleFilter: (filter: string) => void;
  handleFilterSelect: (type: string, value: string) => void;
  handleDateTimeChange: (dateTime: string | null) => void;
}

export const Filters = ({
  filters,
  visibleFilters,
  programmingLanguages,
  packagesByLanguage,
  errorTypes,
  toggleFilter,
  handleFilterSelect,
  handleDateTimeChange
}: FiltersProps) => {
  const renderFilterMenu = (items: string[], filterType: string) => (
    <Menu 
      style={{ maxHeight: '300px', overflowY: 'auto' }}
      selectedKeys={filters[filterType] ? [filters[filterType]] : []}
    >
      {items.map(item => (
        <Menu.Item 
          key={item}
          onClick={() => handleFilterSelect(filterType, item)}
        >
          {item}
        </Menu.Item>
      ))}
    </Menu>
  );

  // Обработчик даты без неиспользуемого параметра dateString
  const handleChange = (date: Dayjs | null) => {
    handleDateTimeChange(date ? date.format('DD.MM.YYYY HH:mm:ss') : null);
  };

  // Обработчики видимости Dropdown без неиспользуемого параметра visible
  const handleLanguagesVisibleChange = () => toggleFilter('languages');
  const handlePackagesVisibleChange = () => toggleFilter('packages');
  const handleErrorsVisibleChange = () => toggleFilter('errors');

  return (
    <div className='filters-container'>
      <ConfigProvider
        theme={{
          components: {
            DatePicker: {
              cellWidth: 28,
              cellHeight: 28,
            }
          }
        }}
      >
        <Space size={12} wrap>
          <Dropdown
            overlay={renderFilterMenu(programmingLanguages, 'programmingLanguage')}
            visible={visibleFilters.languages}
            onVisibleChange={handleLanguagesVisibleChange}
            trigger={['click']}
          >
            <Button className='filter-button'>
              Языки программирования{filters.programmingLanguage ? `: ${filters.programmingLanguage}` : ''} <DownOutlined />
            </Button>
          </Dropdown>

          <Dropdown
            overlay={renderFilterMenu(
              filters.programmingLanguage ? packagesByLanguage[filters.programmingLanguage] || [] : [],
              'packageName'
            )}
            visible={visibleFilters.packages}
            onVisibleChange={handlePackagesVisibleChange}
            trigger={['click']}
            disabled={!filters.programmingLanguage}
          >
            <Button className='filter-button'>
              Имена пакетов{filters.packageName ? `: ${filters.packageName}` : ''} <DownOutlined />
            </Button>
          </Dropdown>

          <Dropdown
            overlay={renderFilterMenu(errorTypes, 'errorType')}
            visible={visibleFilters.errors}
            onVisibleChange={handleErrorsVisibleChange}
            trigger={['click']}
          >
            <Button className='filter-button'>
              Типы ошибок{filters.errorType ? `: ${filters.errorType}` : ''} <DownOutlined />
            </Button>
          </Dropdown>

          <DatePicker
            showTime={{ 
              format: 'HH:mm:ss',
              defaultValue: dayjs('00:00:00', 'HH:mm:ss')
            }}
            format="DD.MM.YYYY HH:mm:ss"
            placeholder="Дата и время"
            onChange={(date, _dateString) => handleChange(date)}
            suffixIcon={<CalendarOutlined />}
            className="date-time-picker"
            allowClear
            inputReadOnly
          />
        </Space>
      </ConfigProvider>
    </div>
  );
};
