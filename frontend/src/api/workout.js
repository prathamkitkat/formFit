import api from './axios';

export const getWorkouts = () => api.get('/workouts');
export const getActiveWorkout = () => api.get('/workouts/active');
export const getWorkoutById = (id) => api.get(`/workouts/${id}`);
export const startWorkout = (name = 'Log Workout') => api.post('/workouts', { name });
export const completeWorkout = (id) => api.post(`/workouts/${id}/complete`);
export const discardWorkout = (id) => api.post(`/workouts/${id}/discard`);
export const deleteWorkout = (id) => api.delete(`/workouts/${id}`);

// Exercises within a workout
export const addExercises = (workoutId, exerciseIds) =>
    api.post(`/workouts/${workoutId}/exercises`, { exerciseIds });
export const replaceExercise = (workoutId, workoutExerciseId, newExerciseId) =>
    api.put(`/workouts/${workoutId}/exercises/${workoutExerciseId}/replace`, { newExerciseId });
export const removeExercise = (workoutId, workoutExerciseId) =>
    api.delete(`/workouts/${workoutId}/exercises/${workoutExerciseId}`);
export const updateExerciseNotes = (workoutId, workoutExerciseId, notes) =>
    api.put(`/workouts/${workoutId}/exercises/${workoutExerciseId}/notes`, { notes });

// Sets
export const addSet = (workoutId, workoutExerciseId) =>
    api.post(`/workouts/${workoutId}/exercises/${workoutExerciseId}/sets`);
export const updateSet = (workoutId, workoutExerciseId, setId, weight, reps) =>
    api.put(`/workouts/${workoutId}/exercises/${workoutExerciseId}/sets/${setId}`, { weight, reps });
export const removeSet = (workoutId, workoutExerciseId, setId) =>
    api.delete(`/workouts/${workoutId}/exercises/${workoutExerciseId}/sets/${setId}`);
