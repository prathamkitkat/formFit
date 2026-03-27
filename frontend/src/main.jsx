import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.jsx'
import { AuthProvider } from './context/AuthContext.jsx'
import { WorkoutProvider } from './context/WorkoutContext.jsx'
import { ToastProvider } from './context/ToastContext.jsx'

createRoot(document.getElementById('root')).render(
    <StrictMode>
        <AuthProvider>
            <WorkoutProvider>
                <ToastProvider>
                    <App />
                </ToastProvider>
            </WorkoutProvider>
        </AuthProvider>
    </StrictMode>,
)
