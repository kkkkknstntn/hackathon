import { useQuery } from '@tanstack/react-query';
import type { 
  SearchLogsParams, 
  SearchLogsResponseDTO 
} from '../shared/types/logs';
import dayjs from 'dayjs';
import { authAxios } from '@/app/api';

export const fetchLogs = async (params: SearchLogsParams): Promise<SearchLogsResponseDTO> => {
  // Исправлено: явная проверка на массив и тип для errors
  const backendParams: Record<string, any> = {
    query: params.query,
    programmingLanguage: params.programmingLanguage,
    errors: params.errors,
    packageField: params.packageField,
    packageDependencies: params.packageDependencies,
    packageDescription: params.packageDescription,
    packageGroup: params.packageGroup,
    packageSummary: params.packageSummary,
    log: params.log,
    date: params.date ? dayjs(params.date).format('YYYY-MM-DD') : undefined
  };

  // Удаляем undefined параметры (оптимизированная версия)
  const filteredParams = Object.fromEntries(
    Object.entries(backendParams).filter(([_, v]) => v !== undefined)
  );

  console.log('Sending request with params:', filteredParams);

  try {
    const response = await authAxios.get<SearchLogsResponseDTO>('/searchLogs', {
      params: filteredParams,
      paramsSerializer: { indexes: null },
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      }
    });

    console.log('Response data:', response.data);
    
    if (!response.data?.logs) {
      throw new Error('Invalid response format: missing logs array');
    }
    
    return response.data;
  } catch (error) {
    console.error('Fetch logs error:', error);
    throw new Error('Failed to fetch logs. Please try again later.');
  }
};

// Остальные функции остаются без изменений
export const fetchPackages = async (): Promise<string[]> => {
  const response = await authAxios.get<string[]>('/api/filters', {
    params: { enumKey: 'PACKAGES' }
  });
  return response.data;
};

export const fetchErrors = async (): Promise<string[]> => {
  const response = await authAxios.get<string[]>('/api/filters', {
    params: { enumKey: 'ERRORS' }
  });
  return response.data;
};

export const useLogsQuery = (params: SearchLogsParams) => {
  return useQuery({
    queryKey: ['logs', params],
    queryFn: () => fetchLogs(params),
    staleTime: 5 * 60 * 1000,
    retry: 2
  });
};

export const usePackagesQuery = () => {
  return useQuery({
    queryKey: ['packages'],
    queryFn: fetchPackages,
    staleTime: 60 * 60 * 1000,
  });
};

export const useErrorsQuery = () => {
  return useQuery({
    queryKey: ['errors'],
    queryFn: fetchErrors,
    staleTime: 60 * 60 * 1000,
  });
};
