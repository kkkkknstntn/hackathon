export type UserResponseDTO = {
	isEnabled: boolean
	id: number
	username: string
	roles: string[]
	createdAt: string
}

export type LoginFormData = {
	username: string
	password: string
}

export type RegisterFormData = {
	username: string
	password: string
}

export type Tokens = {
	accessToken: string
	refreshToken: string
}

export type AuthResponse = {
	user: UserResponseDTO
} & Tokens
