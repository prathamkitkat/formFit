import { createContext, useContext, useState, useEffect } from 'react';
import { getActiveWorkout } from '../api/workout';
import { useAuth } from './AuthContext';

const WorkoutContext = createContext(null);

export function WorkoutProvider({ children }) {
    const { currentUser } = useAuth();
    const [activeWorkout, setActiveWorkout] = useState(null);

    useEffect(() => {
        if (currentUser) {
            getActiveWorkout()
                .then(r => setActiveWorkout(r.data))
                .catch(() => setActiveWorkout(null));
        } else {
            setActiveWorkout(null);
        }
    }, [currentUser]);

    return (
        <WorkoutContext.Provider value={{ activeWorkout, setActiveWorkout }}>
            {children}
        </WorkoutContext.Provider>
    );
}

export function useWorkout() {
    return useContext(WorkoutContext);
}
