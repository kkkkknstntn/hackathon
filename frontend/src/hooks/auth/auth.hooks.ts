import { authService } from '@/services/auth.service'
import type { LoginFormData, RegisterFormData } from '@/shared/types/auth'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'

export const useActivateAccount = (token: string, email: string) => {
	return useQuery({
		queryKey: ['activate', token, email],
		queryFn: () => authService.activateAccount(token, email),
		retry: false,
		enabled: !!token && !!email,
	})
}

export const useLogin = () => {
	const queryClient = useQueryClient()

	return useMutation({
		mutationFn: (data: LoginFormData) => authService.login(data),
		onSuccess: ({ accessToken, refreshToken, user }) => {
			localStorage.setItem('accessToken', accessToken)
			if (refreshToken) {
				localStorage.setItem('refreshToken', refreshToken)
			}
			queryClient.setQueryData(['currentUser'], user)
		},
	})
}

export const useRegister = () => {
	return useMutation({
		mutationFn: (data: RegisterFormData) => authService.register(data),
	})
}

export const useLogout = () => {
	const queryClient = useQueryClient()

	return useMutation({
		mutationFn: () => authService.logout(),
		onSuccess: () => {
			localStorage.removeItem('accessToken')
			localStorage.removeItem('refreshToken')
			queryClient.removeQueries()
		},
	})
}
