import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Search, Bell } from 'lucide-react';
import WorkoutCard from '../../components/workout/WorkoutCard';
import { getWorkouts } from '../../api/workout';
import { useWorkout } from '../../context/WorkoutContext';
import BottomNav from '../../components/layout/BottomNav';

export default function HomePage() {
    const navigate = useNavigate();
    const [workouts, setWorkouts] = useState([]);
    const [loading, setLoading] = useState(true);
    const { activeWorkout } = useWorkout();

    useEffect(() => {
        getWorkouts()
            .then(r => setWorkouts(r.data))
            .catch(() => { })
            .finally(() => setLoading(false));
    }, []);

    return (
        <div className="min-h-screen bg-white pb-20">
            {/* Header */}
            <header className="sticky top-0 bg-white z-30 px-4 py-4 flex items-center justify-between border-b border-border">
                <div className="flex items-center gap-1">
                    <h1 className="text-2xl font-bold text-text-primary">Home</h1>
                    <span className="text-text-secondary text-lg">▾</span>
                </div>
                <div className="flex items-center gap-3 text-text-secondary">
                    <Search size={22} />
                    <Bell size={22} />
                </div>
            </header>



            {/* Feed */}
            <div className="mt-2">
                {loading && (
                    <div className="flex justify-center py-16">
                        <div className="w-8 h-8 border-2 border-primary border-t-transparent rounded-full animate-spin" />
                    </div>
                )}
                {!loading && workouts.length === 0 && (
                    <div className="flex flex-col items-center py-20 px-8 text-center">
                        <span className="text-5xl mb-4">🏋️</span>
                        <p className="text-lg font-bold text-text-primary">No workouts yet</p>
                        <p className="text-sm text-text-secondary mt-1">Complete your first workout to see it here.</p>
                    </div>
                )}
                {workouts.map(w => (
                    <WorkoutCard
                        key={w.id}
                        workout={w}
                        onDelete={(deletedId) => setWorkouts(prev => prev.filter(work => work.id !== deletedId))}
                    />
                ))}
            </div>

            <BottomNav />
        </div>
    );
}
