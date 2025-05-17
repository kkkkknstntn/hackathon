import { authAxios } from '@/app/api';

export interface ErrorStat {
  count: number;
  packages: string[];
}

export interface ErrorStats {
  [key: string]: ErrorStat;
}

export const fetchErrorStats = async (): Promise<ErrorStats> => {
  try {
    const response = await authAxios.get<ErrorStats>('/api/errorStats');
    
    // Проверка на пустой ответ
    if (!response.data || typeof response.data !== 'object') {
      throw new Error('Invalid data format');
    }
    
    return response.data;
  } catch (error) {
    // Логирование ошибки для отладки
    console.error('Error fetching stats:', error);
    throw new Error('Failed to fetch error statistics');
  }
};

