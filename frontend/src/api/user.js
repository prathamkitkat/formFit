import api from './axios';

export const getMe = () => api.get('/user/me');
export const updateUsername = (username) => api.patch('/user/username', { username });
export const updateEmail = (email) => api.patch('/user/email', { email });
export const updatePassword = (currentPassword, newPassword) =>
    api.patch('/user/password', { currentPassword, newPassword });
export const updateWeightUnit = (weightUnit) => api.patch('/user/weight-unit', { weightUnit });
