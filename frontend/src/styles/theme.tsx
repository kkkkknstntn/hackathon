// theme.tsx
export const lightTheme = {
	token: {
		// Основные цвета
		colorPrimary: '#1890ff',
		colorInfo: '#1890ff',
		colorSuccess: '#52c41a',
		colorWarning: '#faad14',
		colorError: '#ff4d4f',

		// Текст
		colorTextBase: '#000000',
		colorText: 'rgba(0, 0, 0, 0.88)',
		colorTextSecondary: 'rgba(0, 0, 0, 0.65)',
		colorTextTertiary: 'rgba(0, 0, 0, 0.45)',
		colorTextQuaternary: 'rgba(0, 0, 0, 0.25)',

		// Фоны
		colorBgBase: '#ffffff',
		colorBgContainer: '#f8f9fa',
		colorBgElevated: '#ffffff',
		colorBgLayout: '#f0f2f5',

		// Границы
		colorBorder: '#d9d9d9',
		colorBorderSecondary: '#f0f0f0',

		// Разное
		fontSize: 14,
		borderRadius: 4,
		fontFamily: `'JetBrains Mono', 'Courier New', monospace`,
		controlHeight: 32,
	},
}

export const darkTheme = {
	token: {
		// Основные цвета
		colorPrimary: '#722ed1',
		colorInfo: '#1668dc',
		colorSuccess: '#49aa19',
		colorWarning: '#d89614',
		colorError: '#dc4446',

		// Текст
		colorTextBase: '#e6e6e6',
		colorText: 'rgba(255, 255, 255, 0.85)',
		colorTextSecondary: 'rgba(255, 255, 255, 0.65)',
		colorTextTertiary: 'rgba(255, 255, 255, 0.45)',
		colorTextQuaternary: 'rgba(255, 255, 255, 0.25)',

		// Фоны
		colorBgBase: '#141414',
		colorBgContainer: '#1d1d1d',
		colorBgElevated: '#262626',
		colorBgLayout: '#000000',

		// Границы
		colorBorder: '#424242',
		colorBorderSecondary: '#303030',

		// Разное
		fontSize: 16,
		borderRadius: 6,
		fontFamily: `'Segoe UI', system-ui, sans-serif`,
		controlHeight: 40,
	},
}

export const customTheme = {
	token: {
		// Основные цвета
		colorPrimary: '#32cd32',
		colorInfo: '#228b22',
		colorSuccess: '#3cb371',
		colorWarning: '#ffd700',
		colorError: '#ff4500',

		// Текст
		colorTextBase: '#32cd32',
		colorText: 'rgba(50, 205, 50, 0.85)',
		colorTextSecondary: 'rgba(50, 205, 50, 0.65)',
		colorTextTertiary: 'rgba(50, 205, 50, 0.45)',
		colorTextQuaternary: 'rgba(50, 205, 50, 0.25)',

		// Фоны
		colorBgBase: '#1a1a1a',
		colorBgContainer: '#000000',
		colorBgElevated: '#0d0d0d',
		colorBgLayout: '#121212',

		// Границы
		colorBorder: '#2d2d2d',
		colorBorderSecondary: '#404040',

		// Разное
		fontSize: 15,
		borderRadius: 0, // Убрать закругления для строгого вида
		fontFamily: `'Courier New', 'Lucida Console', monospace`,
		controlHeight: 36,
		controlInteractiveSize: 16,
	},
	components: {
		Input: {
			activeBorderColor: '#32cd32',
			hoverBorderColor: '#228b22',
		},
		Button: {
			defaultGhostBorderColor: '#32cd32',
			defaultGhostColor: '#32cd32',
		},
	},
}
