export const lightTheme = {
  token: {
    // Основные цвета
    colorPrimary: 'rgb(24, 144, 255)',
    colorInfo: 'rgb(24, 144, 255)',
    colorSuccess: 'rgb(82, 196, 26)',
    colorWarning: 'rgb(250, 173, 20)',
    colorError: 'rgb(255, 77, 79)',

    // Текст
    colorTextBase: 'rgb(0, 0, 0)',
    colorText: 'rgba(0, 0, 0, 0.88)',
    colorTextSecondary: 'rgba(0, 0, 0, 0.65)',
    colorTextTertiary: 'rgba(0, 0, 0, 0.45)',
    colorTextQuaternary: 'rgba(0, 0, 0, 0.25)',

    // Фоны
    colorBgBase: 'rgb(255, 255, 255)',
    colorBgContainer: 'rgb(248, 249, 250)',
    colorBgElevated: 'rgb(255, 255, 255)',
    colorBgLayout: 'rgb(200, 222, 255)',

    // Границы
    colorBorder: 'rgb(148, 148, 148)', // цвет обводки карточек
    colorBorderSecondary: 'rgb(172, 172, 172)', // цвет разделяшок

    // Разное
    fontSize: 14,
    borderRadius: 4,
    fontFamily: `'Courier New', 'Lucida Console', monospace`,
    controlHeight: 32,

    colorErrorBg: 'rgb(255, 242, 240)',
    colorErrorBorder: 'rgb(255, 204, 199)',
    colorErrorText: 'rgb(255, 77, 79)',
    colorWarningBg: 'rgb(255, 251, 230)',
    colorWarningBorder: 'rgb(255, 229, 143)',
    colorWarningText: 'rgb(250, 173, 20)',
    colorInfoBg: 'rgb(230, 244, 255)',
    colorInfoBorder: 'rgb(145, 202, 255)',
    colorInfoText: 'rgb(24, 144, 255)',
    colorSuccessBg: 'rgb(246, 255, 237)',
    colorSuccessBorder: 'rgb(183, 235, 143)',
    colorSuccessText: 'rgb(82, 196, 26)',
  },
  components: {
	Input: {
      activeBorderColor: 'rgb(24, 144, 255)',
      hoverBorderColor: 'rgb(24, 144, 255)',
    },
    Button: {
      defaultGhostBorderColor: 'rgb(50, 205, 50)',
      defaultGhostColor: 'rgb(50, 205, 50)',
    },
    Alert: {
      fontSize: 14,
      borderRadius: 0,
      paddingBlock: 12,
      paddingInline: 16,
    },
	Layout: {
		headerBg: 'rgb(178, 209, 255)', 
		headerColor: 'rgb(0, 0, 0)',
		headerPadding: '0 24px',
	},
  },
}

export const darkTheme = {
  token: {
    // Основные цвета
    colorPrimary: 'rgb(114, 46, 209)',
    colorInfo: 'rgb(22, 104, 220)',
    colorSuccess: 'rgb(73, 170, 25)',
    colorWarning: 'rgb(216, 150, 20)',
    colorError: 'rgb(220, 68, 70)',

    // Текст - делаем все тексты белыми с разной прозрачностью
    colorTextBase: 'rgb(255, 255, 255)',
    colorText: 'rgba(255, 255, 255, 0.95)',
    colorTextSecondary: 'rgba(255, 255, 255, 0.75)',
    colorTextTertiary: 'rgba(255, 255, 255, 0.55)',
    colorTextQuaternary: 'rgba(255, 255, 255, 0.55)',

    // Фоны - делаем темнее
    colorBgBase: 'rgb(10, 10, 10)', // Темнее чем было
    colorBgContainer: 'rgb(20, 20, 20)', // Темнее чем было
    colorBgElevated: 'rgb(30, 30, 30)', // Темнее чем было
    colorBgLayout: 'rgb(15, 15, 15)', // Темнее чем было

    // Границы - делаем светлее для контраста
    colorBorder: 'rgb(100, 100, 100)', // Светлее чем было
    colorBorderSecondary: 'rgb(70, 70, 70)', // Светлее чем было

    // Разное
    fontSize: 14,
    borderRadius: 4,
    fontFamily: `'Courier New', 'Lucida Console', monospace`,
    controlHeight: 32,

    colorErrorBg: 'rgb(42, 18, 21)',
    colorErrorBorder: 'rgb(88, 24, 28)',
    colorErrorText: 'rgb(255, 120, 117)',
    colorWarningBg: 'rgb(43, 29, 17)',
    colorWarningBorder: 'rgb(89, 56, 21)',
    colorWarningText: 'rgb(255, 197, 61)',
    colorInfoBg: 'rgb(17, 29, 44)',
    colorInfoBorder: 'rgb(21, 52, 91)',
    colorInfoText: 'rgb(105, 177, 255)',
    colorSuccessBg: 'rgb(22, 35, 18)',
    colorSuccessBorder: 'rgb(39, 73, 22)',
    colorSuccessText: 'rgb(149, 222, 100)',
  },
  components: {
    Input: {
      activeBorderColor: 'rgb(114, 46, 209)',
      hoverBorderColor: 'rgb(114, 46, 209)',
    },
    Button: {
      defaultGhostBorderColor: 'rgb(114, 46, 209)',
      defaultGhostColor: 'rgb(114, 46, 209)',
    },
    Alert: {
      fontSize: 14,
      borderRadius: 0,
      paddingBlock: 12,
      paddingInline: 16,
    },
    Card: {
      colorBgContainer: 'rgb(0, 0, 0)',
      colorBorder: 'rgb(50, 205, 50)',
      boxShadow: '0 0 10px rgba(50, 205, 50, 0.3)',
    },
    Layout: {
      headerBg: 'rgb(56, 0, 63)',
      headerColor: 'rgb(221, 171, 255)',
      headerPadding: '0 24px',
    },
  },
}

export const customTheme = {
  token: {
    // Основные цвета
    colorPrimary: 'rgb(50, 205, 50)',
    colorInfo: 'rgb(34, 139, 34)',
    colorSuccess: 'rgb(60, 179, 113)',
    colorWarning: 'rgb(255, 215, 0)',
    colorError: 'rgb(255, 69, 0)',

    // Текст
    colorTextBase: 'rgb(50, 205, 50)',
    colorText: 'rgba(50, 205, 50, 0.85)',
    colorTextSecondary: 'rgba(50, 205, 50, 0.65)',
    colorTextTertiary: 'rgba(50, 205, 50, 0.45)',
    colorTextQuaternary: 'rgba(50, 205, 50, 0.25)',

    // Фоны
    colorBgBase: 'rgb(26, 26, 26)',
    colorBgContainer: 'rgb(0, 0, 0)',
    colorBgElevated: 'rgb(13, 13, 13)',
    colorBgLayout: 'rgb(18, 18, 18)',

    // Границы
    colorBorder: 'rgb(45, 45, 45)',
    colorBorderSecondary: 'rgb(64, 64, 64)',

    // Разное
    fontSize: 14,
    borderRadius: 4,
    fontFamily: `'Courier New', 'Lucida Console', monospace`,
    controlHeight: 32,
    controlInteractiveSize: 16,

    colorErrorBg: 'rgb(26, 0, 0)',
    colorErrorBorder: 'rgb(77, 0, 0)',
    colorErrorText: 'rgb(255, 51, 51)',
    colorWarningBg: 'rgb(26, 26, 0)',
    colorWarningBorder: 'rgb(77, 77, 0)',
    colorWarningText: 'rgb(255, 255, 0)',
    colorInfoBg: 'rgb(0, 26, 26)',
    colorInfoBorder: 'rgb(0, 77, 77)',
    colorInfoText: 'rgb(0, 255, 255)',
    colorSuccessBg: 'rgb(0, 26, 0)',
    colorSuccessBorder: 'rgb(0, 77, 0)',
    colorSuccessText: 'rgb(0, 255, 0)',
  },
  components: {
    Input: {
      activeBorderColor: 'rgb(50, 205, 50)',
      hoverBorderColor: 'rgb(34, 139, 34)',
    },
    Button: {
      defaultGhostBorderColor: 'rgb(50, 205, 50)',
      defaultGhostColor: 'rgb(50, 205, 50)',
    },
    Alert: {
      colorText: 'rgb(50, 205, 50)',
      fontSize: 14,
      borderRadius: 0,
      paddingBlock: 12,
      paddingInline: 16,
    },
    Card: {
      colorBgContainer: 'rgb(0, 0, 0)',
      colorBorder: 'rgb(50, 205, 50)',
      boxShadow: '0 0 10px rgba(50, 205, 50, 0.3)',
    },
	Layout: {
      headerBg: 'rgba(29, 68, 29, 0.66)',
      headerColor: 'rgb(50, 205, 50)',
      headerPadding: '0 24px',
    },
  },
}
