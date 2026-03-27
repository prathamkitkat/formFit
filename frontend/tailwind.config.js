/** @type {import('tailwindcss').Config} */
export default {
    content: ['./index.html', './src/**/*.{js,jsx}'],
    theme: {
        extend: {
            colors: {
                primary: '#2196F3',
                'primary-dark': '#1976D2',
                surface: '#FFFFFF',
                background: '#F5F5F5',
                card: '#FFFFFF',
                border: '#E0E0E0',
                'text-primary': '#1A1A1A',
                'text-secondary': '#757575',
                'text-muted': '#BDBDBD',
                pr: '#FFB300',
                danger: '#F44336',
                success: '#4CAF50',
            },
            fontFamily: {
                sans: ['Inter', 'system-ui', 'sans-serif'],
            },
        },
    },
    plugins: [],
}
