import { useEffect } from 'react';
import { getActiveWorkout } from '../api/workout';
import { useWorkout } from '../context/WorkoutContext';

export function useActiveWorkout() {
    const { activeWorkout, setActiveWorkout } = useWorkout();

    useEffect(() => {
        getActiveWorkout()
            .then(res => {
                if (res.status === 200 && res.data) setActiveWorkout(res.data);
            })
            .catch(() => { });
    }, []);

    return activeWorkout;
}
