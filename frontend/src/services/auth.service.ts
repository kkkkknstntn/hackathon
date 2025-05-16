import { authAxios } from '@/app/api'
import type {
	AuthResponse,
	LoginFormData,
	RegisterFormData,
	UserResponseDTO,
} from '@/shared/types/auth'

export const authService = {
	async login(data: LoginFormData) {
		const response = await authAxios.post<AuthResponse>('/api/login', data)
		return response.data
	},

	async register(data: RegisterFormData) {
		const formData = new FormData()
		formData.append('username', data.username)
		formData.append('password', data.password)

		const response = await authAxios.post<UserResponseDTO>(
			'/api/users',
			formData
		)

		return response.data
	},

	async logout() {
		await authAxios.post('/api/logout')
	},

	async refresh() {
		const response = await authAxios.post<AuthResponse>('/api/refresh')
		return response.data
	},

	async activateAccount(token: string, email: string) {
		await authAxios.get('/api/activation/activate', {
			params: { token, email },
		})
	},
}
