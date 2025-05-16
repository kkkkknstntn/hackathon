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

		colorErrorBg: '#fff2f0',
		colorErrorBorder: '#ffccc7',
		colorErrorText: '#ff4d4f',
		colorWarningBg: '#fffbe6',
		colorWarningBorder: '#ffe58f',
		colorWarningText: '#faad14',
		colorInfoBg: '#e6f4ff',
		colorInfoBorder: '#91caff',
		colorInfoText: '#1890ff',
		colorSuccessBg: '#f6ffed',
		colorSuccessBorder: '#b7eb8f',
		colorSuccessText: '#52c41a',
	},
	components: {
		Layout: {
			headerBg: '#ffffff',
			headerColor: '#000000',
			headerPadding: '0 24px',
		},
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

		colorErrorBg: '#2a1215',
		colorErrorBorder: '#58181c',
		colorErrorText: '#ff7875',
		colorWarningBg: '#2b1d11',
		colorWarningBorder: '#593815',
		colorWarningText: '#ffc53d',
		colorInfoBg: '#111d2c',
		colorInfoBorder: '#15345b',
		colorInfoText: '#69b1ff',
		colorSuccessBg: '#162312',
		colorSuccessBorder: '#274916',
		colorSuccessText: '#95de64',
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
		Alert: {
			fontSize: 14,
			borderRadius: 0,
			paddingBlock: 12,
			paddingInline: 16,
		},
		Card: {
			colorBgContainer: '#000000',
			colorBorder: '#32cd32',
			boxShadow: '0 0 10px rgba(50, 205, 50, 0.3)',
		},
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

		colorErrorBg: '#1a0000',
		colorErrorBorder: '#4d0000',
		colorErrorText: '#ff3333',
		colorWarningBg: '#1a1a00',
		colorWarningBorder: '#4d4d00',
		colorWarningText: '#ffff00',
		colorInfoBg: '#001a1a',
		colorInfoBorder: '#004d4d',
		colorInfoText: '#00ffff',
		colorSuccessBg: '#001a00',
		colorSuccessBorder: '#004d00',
		colorSuccessText: '#00ff00',
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
		Alert: {
			colorText: '#32cd32',
			fontSize: 14,
			borderRadius: 0,
			paddingBlock: 12,
			paddingInline: 16,
		},
		Card: {
			colorBgContainer: '#000000',
			colorBorder: '#32cd32',
			boxShadow: '0 0 10px rgba(50, 205, 50, 0.3)',
		},
	},
}
