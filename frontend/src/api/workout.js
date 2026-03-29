import api from './axios';

export const getWorkouts = () => api.get('/workouts');
export const searchWorkouts = (query) => api.get(`/workouts/search?query=${encodeURIComponent(query)}`);
export const getActiveWorkout = () => api.get('/workouts/active');
export const getWorkoutById = (id) => api.get(`/workouts/${id}`);
export const startWorkout = (name = 'Log Workout') => api.post('/workouts', { name });
export const completeWorkout = (id) => api.post(`/workouts/${id}/complete`);
export const discardWorkout = (id) => api.post(`/workouts/${id}/discard`);
export const deleteWorkout = (id) => api.delete(`/workouts/${id}`);

// A simple queue to prevent optimistic locking race conditions on the workout entity
let workoutMutationQueue = Promise.resolve();
function queueRequest(requestFn) {
    return new Promise((resolve, reject) => {
        workoutMutationQueue = workoutMutationQueue.then(() => 
            requestFn().then(resolve).catch(reject)
        ).catch(() => {}); // absorb errors so the queue chain continues
    });
}

// Exercises within a workout
export const addExercises = (workoutId, exerciseIds) =>
    queueRequest(() => api.post(`/workouts/${workoutId}/exercises`, { exerciseIds }));
export const replaceExercise = (workoutId, workoutExerciseId, newExerciseId) =>
    queueRequest(() => api.put(`/workouts/${workoutId}/exercises/${workoutExerciseId}/replace`, { newExerciseId }));
export const removeExercise = (workoutId, workoutExerciseId) =>
    queueRequest(() => api.delete(`/workouts/${workoutId}/exercises/${workoutExerciseId}`));
export const updateExerciseNotes = (workoutId, workoutExerciseId, notes) =>
    queueRequest(() => api.put(`/workouts/${workoutId}/exercises/${workoutExerciseId}/notes`, { notes }));

// Sets
export const addSet = (workoutId, workoutExerciseId) =>
    queueRequest(() => api.post(`/workouts/${workoutId}/exercises/${workoutExerciseId}/sets`));
export const updateSet = (workoutId, workoutExerciseId, setId, weight, reps) =>
    queueRequest(() => api.put(`/workouts/${workoutId}/exercises/${workoutExerciseId}/sets/${setId}`, { weight, reps }));
export const removeSet = (workoutId, workoutExerciseId, setId) =>
    queueRequest(() => api.delete(`/workouts/${workoutId}/exercises/${workoutExerciseId}/sets/${setId}`));
