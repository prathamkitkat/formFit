import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Search } from 'lucide-react';
import PageHeader from '../../components/layout/PageHeader';
import { getUserExercises } from '../../api/profile';
import { MUSCLE_GROUPS } from '../../utils/constants';

export default function ExercisesPage() {
    const navigate = useNavigate();
    const [exercises, setExercises] = useState([]);
    const [search, setSearch] = useState('');

    useEffect(() => {
        getUserExercises().then(r => setExercises(r.data)).catch(() => { });
    }, []);

    const filtered = exercises.filter(ex =>
        (ex.exerciseName || '').toLowerCase().includes(search.toLowerCase())
    );

    return (
        <div className="min-h-screen bg-background">
            <PageHeader
                title="Exercises"
                rightElement={
                    <button onClick={() => navigate('/exercises/create')} className="text-primary text-sm font-semibold">Create</button>
                }
            />

            <div className="px-4 py-3 bg-white border-b border-border">
                <div className="flex items-center gap-2 bg-background rounded-xl px-3 py-2">
                    <Search size={16} className="text-text-muted" />
                    <input
                        className="flex-1 bg-transparent text-sm outline-none placeholder:text-text-muted"
                        placeholder="Search exercises..."
                        value={search}
                        onChange={e => setSearch(e.target.value)}
                    />
                </div>
            </div>

            <div className="bg-white">
                {filtered.length === 0 && (
                    <p className="text-center text-text-muted text-sm py-12">
                        {exercises.length === 0 ? 'No exercises found. Start working out!' : 'No results.'}
                    </p>
                )}
                {filtered.map(ex => (
                    <button
                        key={ex.exerciseId}
                        onClick={() => navigate(`/profile/exercises/${ex.exerciseId}`)}
                        className="w-full flex items-center gap-3 px-4 py-3 border-b border-border hover:bg-gray-50 text-left"
                    >
                        <div className="w-12 h-12 rounded-full bg-gray-200 flex items-center justify-center flex-shrink-0">
                            {ex.imageUrl
                                ? <img src={ex.imageUrl} className="w-12 h-12 rounded-full object-cover" />
                                : <span className="text-lg font-bold text-gray-500">{ex.exerciseName?.[0]?.toUpperCase()}</span>
                            }
                        </div>
                        <div className="flex-1 min-w-0">
                            <p className="text-sm font-semibold text-text-primary">{ex.exerciseName}</p>
                        </div>
                        <span className="text-text-muted">›</span>
                    </button>
                ))}
            </div>
        </div>
    );
}
