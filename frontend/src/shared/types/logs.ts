export interface Error {
  full_error: string;
  short_name: string;
}

export interface LogDocument {
  programming_language: string;
  errors: Error[];
  package_field: string;
  timestamp: string;
  package_dependencies: string[];
  package_description: string;
  package_group: string;
  package_summary: string;
  log: string;
  first_log_date: string;
  last_log_date: string;
}

export interface SearchLogsResponseDTO {
  uniquePackages: string[];
  logs: LogDocument[];
}

export interface SearchLogsParams {
  query?: string;
  programmingLanguage?: string;
  errors?: string;
  packageField?: string;
  packageDependencies?: string;
  packageDescription?: string;
  packageGroup?: string;
  packageSummary?: string;
  log?: string;
  date?: string; // Формат: YYYY-MM-DD
  limit?: number;
}
