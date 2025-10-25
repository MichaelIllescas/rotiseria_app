import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react-swc'

export default defineConfig({
  plugins: [react()],
  server: {
    host: true, // permite acceso externo
    allowedHosts: true // permite cualquier host (no solo localhost)
  }
})
