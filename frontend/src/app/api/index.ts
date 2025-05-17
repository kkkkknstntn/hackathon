import { authService } from '@/services/auth.service'
import axios from 'axios'

export const authAxios = axios.create({
	withCredentials: true,
	baseURL: 'http://localhost:8080',
})

authAxios.interceptors.request.use(config => {
	if (config.url?.includes('/api/refresh')) {
		const refreshToken = localStorage.getItem('refreshToken')
		if (refreshToken) {
			config.headers.Authorization = `Bearer ${refreshToken}`
		}
	} else {
		const accessToken = localStorage.getItem('accessToken')
		if (accessToken) {
			config.headers.Authorization = `Bearer ${accessToken}`
		}
	}
	return config
})

authAxios.interceptors.response.use(
	response => response,
	async error => {
		const originalRequest = error.config
		if (
			[777].includes(error.response?.status) &&
			!originalRequest._retry
		) {
			originalRequest._retry = true
			try {
				const { accessToken } = await authService.refresh()
				localStorage.setItem('accessToken', accessToken)
				return authAxios(originalRequest)
			} catch (refreshError) {
				localStorage.removeItem('accessToken')
				//window.location.href = '/login'
				return Promise.reject(refreshError)
			}
		}
		return Promise.reject(error)
	}
)
