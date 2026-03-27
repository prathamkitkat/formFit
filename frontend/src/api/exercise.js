import api from './axios';

export const getExercises = () => api.get('/exercises');
export const createExercise = (data) => api.post('/exercises/custom', data);
