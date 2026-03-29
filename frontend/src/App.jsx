import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { useAuth } from './context/AuthContext';
import MiniActiveWorkout from './components/workout/MiniActiveWorkout';

// Auth
import LoginPage from './pages/auth/LoginPage';
import RegisterPage from './pages/auth/RegisterPage';

// Home
import HomePage from './pages/home/HomePage';

// Workout
import WorkoutTabPage from './pages/workout/WorkoutTabPage';
import ActiveWorkoutPage from './pages/workout/ActiveWorkoutPage';
import WorkoutDetailPage from './pages/workout/WorkoutDetailPage';
import TemplateEditPage from './pages/workout/TemplateEditPage';
import TemplateDetailPage from './pages/workout/TemplateDetailPage';

// Exercise
import CreateExercisePage from './pages/exercise/CreateExercisePage';

// Profile
import ProfilePage from './pages/profile/ProfilePage';
import StatisticsPage from './pages/profile/StatisticsPage';
import MonthlyReportPage from './pages/profile/MonthlyReportPage';
import FrequentExercisesPage from './pages/profile/FrequentExercisesPage';
import CalendarPage from './pages/profile/CalendarPage';
import ExercisesPage from './pages/profile/ExercisesPage';
import ExerciseStatsPage from './pages/profile/ExerciseStatsPage';

// Settings
import SettingsPage from './pages/settings/SettingsPage';

function ProtectedRoute({ children }) {
    const { token, loading } = useAuth();
    if (loading) return (
        <div className="min-h-screen flex items-center justify-center">
            <div className="w-10 h-10 border-2 border-primary border-t-transparent rounded-full animate-spin" />
        </div>
    );
    if (!token) return <Navigate to="/login" replace />;
    return children;
}

export default function App() {
    return (
        <BrowserRouter>
            <MiniActiveWorkout />
            <Routes>
                {/* Public */}
                <Route path="/login" element={<LoginPage />} />
                <Route path="/register" element={<RegisterPage />} />

                {/* Protected */}
                <Route path="/home" element={<ProtectedRoute><HomePage /></ProtectedRoute>} />

                <Route path="/workout" element={<ProtectedRoute><WorkoutTabPage /></ProtectedRoute>} />
                <Route path="/workout/active" element={<ProtectedRoute><ActiveWorkoutPage /></ProtectedRoute>} />
                <Route path="/workout/:id" element={<ProtectedRoute><WorkoutDetailPage /></ProtectedRoute>} />
                <Route path="/templates/:id" element={<ProtectedRoute><TemplateDetailPage /></ProtectedRoute>} />
                <Route path="/templates/:id/edit" element={<ProtectedRoute><TemplateEditPage /></ProtectedRoute>} />

                <Route path="/exercises/create" element={<ProtectedRoute><CreateExercisePage /></ProtectedRoute>} />

                <Route path="/profile" element={<ProtectedRoute><ProfilePage /></ProtectedRoute>} />
                <Route path="/profile/statistics" element={<ProtectedRoute><StatisticsPage /></ProtectedRoute>} />
                <Route path="/profile/statistics/monthly" element={<ProtectedRoute><MonthlyReportPage /></ProtectedRoute>} />
                <Route path="/profile/statistics/frequent" element={<ProtectedRoute><FrequentExercisesPage /></ProtectedRoute>} />
                <Route path="/profile/calendar" element={<ProtectedRoute><CalendarPage /></ProtectedRoute>} />
                <Route path="/profile/exercises" element={<ProtectedRoute><ExercisesPage /></ProtectedRoute>} />
                <Route path="/profile/exercises/:id" element={<ProtectedRoute><ExerciseStatsPage /></ProtectedRoute>} />

                <Route path="/settings" element={<ProtectedRoute><SettingsPage /></ProtectedRoute>} />

                {/* Fallback */}
                <Route path="/" element={<Navigate to="/home" replace />} />
                <Route path="*" element={<Navigate to="/home" replace />} />
            </Routes>
        </BrowserRouter>
    );
}
