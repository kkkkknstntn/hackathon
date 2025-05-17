import { Button, Dropdown, type MenuProps, DatePicker, Space, ConfigProvider } from 'antd';
import { DownOutlined, CalendarOutlined } from '@ant-design/icons';
import './FilterButtons.scss';
import dayjs from 'dayjs';
import customParseFormat from 'dayjs/plugin/customParseFormat';
import type { SearchLogsParams } from '../../../../shared/types/logs';

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
  handleFilterSelect: (type: keyof SearchLogsParams, value: string) => void;
  handleDateTimeChange: (date: string | null) => void;
  handleApplyFilters: () => void; // Новый проп
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
  const renderMenuItems = (items: string[], filterType: keyof SearchLogsParams): MenuProps['items'] => 
    items.map(item => ({
      key: item,
      label: item,
      onClick: () => handleFilterSelect(filterType, item)
    }));

  const handleDateChange = (date: dayjs.Dayjs | null) => {
    handleDateTimeChange(date ? date.format('YYYY-MM-DDTHH:mm:ss') : null);
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
            showTime={{ format: 'HH:mm:ss' }}
            format="YYYY-MM-DD HH:mm:ss"
            placeholder="Дата и время"
            onChange={handleDateChange}
            suffixIcon={<CalendarOutlined />}
            className="date-picker"
            allowClear
          />

          <Button 
            type="primary" 
            onClick={handleApplyFilters}
            className='apply-button'
          >
            Применить
          </Button>
        </Space>
      </ConfigProvider>
    </div>
  );
};
