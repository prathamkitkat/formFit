import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import PageHeader from '../../components/layout/PageHeader';
import { getFrequentExercises } from '../../api/profile';

export default function FrequentExercisesPage() {
    const navigate = useNavigate();
    const [exercises, setExercises] = useState([]);
    const [days, setDays] = useState(30);

    useEffect(() => {
        getFrequentExercises(days).then(r => setExercises(r.data)).catch(() => { });
    }, [days]);

    return (
        <div className="min-h-screen bg-background">
            <PageHeader title="Main Exercises" />

            {/* Days picker */}
            <div className="bg-white px-4 py-3 flex gap-2 overflow-x-auto border-b border-border">
                {[14, 30, 90, 180, 365].map(d => (
                    <button
                        key={d}
                        onClick={() => setDays(d)}
                        className={`text-sm px-4 py-1.5 rounded-full whitespace-nowrap transition-colors ${d === days ? 'bg-primary text-white font-semibold' : 'bg-gray-100 text-text-primary'}`}
                    >
                        Last {d} days
                    </button>
                ))}
            </div>

            <div className="bg-white">
                {exercises.length === 0 && (
                    <p className="text-center text-text-muted text-sm py-12">
                        No frequent exercises found.
                    </p>
                )}
                {exercises.map((ex, index) => (
                    <button
                        key={ex.exerciseId}
                        onClick={() => navigate(`/profile/exercises/${ex.exerciseId}`)}
                        className="w-full flex items-center gap-4 px-4 py-3 border-b border-border hover:bg-gray-50 text-left"
                    >
                        <span className="text-lg font-bold text-text-muted w-4">{index + 1}</span>
                        <div className="w-12 h-12 rounded-full bg-gray-200 flex items-center justify-center flex-shrink-0">
                            {ex.imageUrl
                                ? <img src={ex.imageUrl} className="w-12 h-12 rounded-full object-cover" />
                                : <span className="text-lg font-bold text-gray-500">{ex.exerciseName?.[0]?.toUpperCase()}</span>
                            }
                        </div>
                        <div className="flex-1 min-w-0">
                            <p className="text-sm font-semibold text-text-primary">{ex.exerciseName}</p>
                            <p className="text-xs text-text-secondary mt-0.5">{ex.timesPerformed} times</p>
                        </div>
                        <span className="text-text-muted">›</span>
                    </button>
                ))}
            </div>
        </div>
    );
}
