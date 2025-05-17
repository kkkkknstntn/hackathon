import { Button, Dropdown, Menu, DatePicker, Space, ConfigProvider } from 'antd';
import { DownOutlined, CalendarOutlined } from '@ant-design/icons';
import './FilterButtons.scss';
import dayjs from 'dayjs';
import customParseFormat from 'dayjs/plugin/customParseFormat';
import type { SearchLogsParams } from '../../../../shared/types/logs';

dayjs.extend(customParseFormat);

interface FiltersProps {
  filters: SearchLogsParams;
  visibleFilters: {
    languages: boolean;
    packages: boolean;
    errors: boolean;
  };
  programmingLanguages: string[];
  packagesByLanguage: Record<string, string[]>;
  errorTypes: string[];
  toggleFilter: (filter: string) => void;
  handleFilterSelect: (type: keyof SearchLogsParams, value: string) => void;
  handleDateTimeChange: (date: string | null) => void;
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
  const renderFilterMenu = (items: string[], filterType: keyof SearchLogsParams) => (
    <Menu 
      style={{ maxHeight: '300px', overflowY: 'auto' }}
      selectedKeys={filters[filterType] ? [filters[filterType] as string] : []}
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

  const handleDateChange = (date: dayjs.Dayjs | null) => {
    handleDateTimeChange(date ? date.format('YYYY-MM-DD') : null);
  };

  return (
    <div className='filters-container'>
      <ConfigProvider theme={{ components: { DatePicker: { cellWidth: 28, cellHeight: 28 } } }}>
        <Space size={12} wrap>
          <Dropdown
            overlay={renderFilterMenu(programmingLanguages, 'programmingLanguage')}
            open={visibleFilters.languages}
            onOpenChange={() => toggleFilter('languages')}
            trigger={['click']}
          >
            <Button className='filter-button'>
              Языки{filters.programmingLanguage ? `: ${filters.programmingLanguage}` : ''} <DownOutlined />
            </Button>
          </Dropdown>

          <Dropdown
            overlay={renderFilterMenu(
              filters.programmingLanguage ? 
                packagesByLanguage[filters.programmingLanguage] || [] : [],
              'packageField'
            )}
            open={visibleFilters.packages}
            onOpenChange={() => toggleFilter('packages')}
            trigger={['click']}
            disabled={!filters.programmingLanguage}
          >
            <Button className='filter-button'>
              Пакеты{filters.packageField ? `: ${filters.packageField}` : ''} <DownOutlined />
            </Button>
          </Dropdown>

          <Dropdown
            overlay={renderFilterMenu(errorTypes, 'errors')}
            open={visibleFilters.errors}
            onOpenChange={() => toggleFilter('errors')}
            trigger={['click']}
          >
            <Button className='filter-button'>
              Ошибки{filters.errors ? `: ${filters.errors}` : ''} <DownOutlined />
            </Button>
          </Dropdown>

          <DatePicker
            format="YYYY-MM-DD"
            placeholder="Дата"
            onChange={handleDateChange}
            suffixIcon={<CalendarOutlined />}
            className="date-picker"
            allowClear
          />
        </Space>
      </ConfigProvider>
    </div>
  );
};
