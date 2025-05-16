import { Button, Dropdown, Menu, DatePicker, Space, ConfigProvider, type DatePickerProps } from 'antd';
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

  const handleChange: DatePickerProps['onChange'] = (date, dateString) => {
    if (Array.isArray(dateString)) {
      // Обработка случая с диапазоном дат (если нужно)
      handleDateTimeChange(dateString[0]); // или другая логика
    } else {
      // Одиночная дата
      handleDateTimeChange(date ? date.format('DD.MM.YYYY HH:mm:ss') : null);
    }
  };

  return (
    <div className='filters-container'>
      <ConfigProvider
        theme={{
          components: {
            DatePicker: {
              cellWidth: 20,
              cellHeight: 20,
            }
          }
        }}
      >
        <Space size={12} wrap>
          {/* ... остальные компоненты ... */}
          
          <DatePicker
            showTime={{ 
              format: 'HH:mm:ss',
              defaultValue: dayjs('00:00:00', 'HH:mm:ss')
            }}
            format="DD.MM.YYYY HH:mm:ss"
            placeholder="Дата и время"
            onChange={handleChange}
            suffixIcon={<CalendarOutlined />}
            className="date-time-picker"
            allowClear
            inputReadOnly
            popupClassName="custom-datepicker-dropdown"
          />
        </Space>
      </ConfigProvider>
    </div>
  );
};
