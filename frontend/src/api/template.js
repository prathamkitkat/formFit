import api from './axios';

export const getTemplates = () => api.get('/templates');
export const getTemplateById = (id) => api.get(`/templates/${id}`);
export const createTemplate = () => api.post('/templates');
export const updateTemplateName = (id, name) => api.put(`/templates/${id}/name`, { name });
export const deleteTemplate = (id) => api.delete(`/templates/${id}`);
export const startTemplate = (id) => api.post(`/templates/${id}/start`);
export const addTemplatExercises = (id, exerciseIds) =>
    api.post(`/templates/${id}/exercises`, { exerciseIds });
export const removeTemplateExercise = (id, templateExerciseId) =>
    api.delete(`/templates/${id}/exercises/${templateExerciseId}`);
export const incrementTemplateSets = (id, templateExerciseId) =>
    api.post(`/templates/${id}/exercises/${templateExerciseId}/sets/increment`);
export const decrementTemplateSets = (id, templateExerciseId) =>
    api.post(`/templates/${id}/exercises/${templateExerciseId}/sets/decrement`);
export const saveTemplate = (id) => api.post(`/templates/${id}/save`);
