import react from '@vitejs/plugin-react-swc'
import path from 'path'
import { defineConfig, loadEnv } from 'vite'

export default defineConfig(({ mode }) => {
	const env = loadEnv(mode, process.cwd(), '')

	return {
		resolve: {
			alias: {
				'@': path.resolve(__dirname, './src'),
			},
		},
		plugins: [react()],
		// css: {
		// 	preprocessorOptions: {
		// 		less: {
		// 			javascriptEnabled: true,
		// 			modifyVars: {
		// 				'@primary-color': '#1DA57A',
		// 				'@font-family': "'Roboto', sans-serif",
		// 			},
		// 		},
		// 	},
		// },
		server: {
			host: env.VITE_HOST,
			port: Number(env.VITE_PORT),
			strictPort: true,
			allowedHosts: [env.VITE_ALLOWED_HOSTS],
			proxy: {
				'/api': {
					target: env.VITE_PROXY_TARGET,
					changeOrigin: true,
					secure: false,
				},
			},
		},
	}
})
