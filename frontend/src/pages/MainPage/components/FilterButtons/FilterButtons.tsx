import { useState } from 'react';
import { 
  Button, 
  Dropdown, 
  type MenuProps, 
  DatePicker, 
  Space, 
  ConfigProvider, 
  Input, 
  Checkbox 
} from 'antd';
import { 
  DownOutlined, 
  CalendarOutlined, 
  SearchOutlined 
} from '@ant-design/icons';
import './FilterButtons.scss';
import dayjs from 'dayjs';
import customParseFormat from 'dayjs/plugin/customParseFormat';
import type { SearchLogsParams } from '../../../../shared/types/logs';
import type { CheckboxChangeEvent } from 'antd/es/checkbox';

dayjs.extend(customParseFormat);

interface FiltersProps {
  filters: SearchLogsParams;
  visibleFilters: {
    packages: boolean;
    errors: boolean;
  };
  packages: string[];
  errors: string[];
  toggleFilter: (filter: string) => void;
  handleFilterSelect: (type: keyof SearchLogsParams, value: string | boolean) => void;
  handleDateTimeChange: (date: string | null) => void;
  handleApplyFilters: () => void; 
}

export const Filters = ({
  filters,
  visibleFilters,
  packages,
  errors,
  toggleFilter,
  handleFilterSelect,
  handleDateTimeChange,
  handleApplyFilters
}: FiltersProps) => {
  const [searchQuery, setSearchQuery] = useState('');
  const [exactMatch, setExactMatch] = useState(false);

  const renderMenuItems = (items: string[], filterType: keyof SearchLogsParams): MenuProps['items'] => 
    items.map(item => ({
      key: item,
      label: item,
      onClick: () => handleFilterSelect(filterType, item)
    }));

  const handleDateChange = (date: dayjs.Dayjs | null) => {
    handleDateTimeChange(date ? date.format('YYYY-MM-DDTHH:mm:ss') : null);
  };

  const handleSearch = () => {
    handleFilterSelect('query', searchQuery);
    handleFilterSelect('exact', exactMatch);
  };

  const handleApply = () => {
    handleSearch();
    handleApplyFilters();
  };

  return (
    <div className='filters-container'>
      <ConfigProvider theme={{
        components: {
          DatePicker: {
            cellWidth: 28,
            cellHeight: 28
          }
        }
      }}>
        <Space size={12} wrap>

          <Dropdown
            menu={{ items: renderMenuItems(packages, 'packageField') }}
            open={visibleFilters.packages}
            onOpenChange={() => toggleFilter('packages')}
            trigger={['click']}
          >
            <Button className='filter-button'>
              Пакеты{filters.packageField ? `: ${filters.packageField}` : ''} <DownOutlined />
            </Button>
          </Dropdown>

          <Dropdown
            menu={{ items: renderMenuItems(errors, 'errors') }}
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
            placeholder="Дата и время"
            onChange={handleDateChange}
            suffixIcon={<CalendarOutlined />}
            className="date-picker"
            allowClear
          />

          <Input
            placeholder="Поиск ошибок..."
            prefix={<SearchOutlined />}
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            onPressEnter={handleSearch}
            allowClear
            style={{ width: 250 }}
          />
          
          <Checkbox
            checked={exactMatch}
            onChange={(e: CheckboxChangeEvent) => setExactMatch(e.target.checked)}
          >
            Точный поиск
          </Checkbox>
          <Button 
            type="primary" 
            onClick={handleApply}
            className='apply-button'
          >
            Применить
          </Button>

          
        </Space>
      </ConfigProvider>
    </div>
  );
};
