import axios from 'axios';
import { useQuery } from '@tanstack/react-query';
import type { 
  SearchLogsParams, 
  SearchLogsResponseDTO 
} from '../shared/types/logs';
import dayjs from 'dayjs';

const API_BASE_URL = import.meta.env.VITE_PROXY_TARGET;

const api = axios.create({
  baseURL: API_BASE_URL,
  withCredentials: true,
});

export const fetchLogs = async (params: SearchLogsParams): Promise<SearchLogsResponseDTO> => {
  const processedParams = {
    ...params,
    date: params.date ? dayjs(params.date).format('YYYY-MM-DDTHH:mm:ss') : undefined
  };
  
  const response = await api.get<SearchLogsResponseDTO>('/api/searchLogs', { 
    params: processedParams 
  });
  return response.data;
};

export const fetchUniquePackages = async (): Promise<string[]> => {
  const response = await api.get<string[]>('/api/uniquePackages');
  return response.data;
};

export const fetchProgrammingLanguages = async (): Promise<string[]> => {
  const response = await api.get<SearchLogsResponseDTO>('/api/searchLogs', {
    params: { limit: 1 }
  });
  return Array.from(new Set(
    response.data.logs.map(log => log.programming_language)
  ));
};

export const fetchErrorTypes = async (): Promise<string[]> => {
  const response = await api.get<SearchLogsResponseDTO>('/api/searchLogs', {
    params: { limit: 100 }
  });
  
  return Array.from(new Set(
    response.data.logs.flatMap(log => 
      log.errors.map(error => error.short_name)
    )
  ));
};

export const useLogsQuery = (params: SearchLogsParams) => {
  return useQuery({
    queryKey: ['logs', params],
    queryFn: () => fetchLogs(params),
    staleTime: 5 * 60 * 1000,
  });
};

export const useUniquePackagesQuery = () => {
  return useQuery({
    queryKey: ['uniquePackages'],
    queryFn: fetchUniquePackages,
    staleTime: 60 * 60 * 1000,
  });
};

export const useProgrammingLanguagesQuery = () => {
  return useQuery({
    queryKey: ['programmingLanguages'],
    queryFn: fetchProgrammingLanguages,
    staleTime: 60 * 60 * 1000,
  });
};

export const useErrorTypesQuery = () => {
  return useQuery({
    queryKey: ['errorTypes'],
    queryFn: fetchErrorTypes,
    staleTime: 60 * 60 * 1000,
  });
};

