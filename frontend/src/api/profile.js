import api from './axios';

export const getStats = (period = '3M') => api.get(`/profile/graphs?period=${period}`);
export const getMonthlyReport = (year, month) =>
    api.get(`/profile/stats/monthly-report?year=${year}&month=${month}`);
export const getFrequentExercises = (days = 30) => api.get(`/profile/stats/frequent-exercises?days=${days}`);
export const getCalendar = (year, month) =>
    api.get(`/profile/calendar?year=${year}&month=${month}`);
export const getUserExercises = () => api.get('/profile/exercises');
export const getExerciseStats = (exerciseId) =>
    api.get(`/profile/exercises/${exerciseId}/stats`);
