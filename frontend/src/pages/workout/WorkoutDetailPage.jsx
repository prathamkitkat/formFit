import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import PageHeader from '../../components/layout/PageHeader';
import PrBadge from '../../components/ui/PrBadge';
import { getWorkoutById } from '../../api/workout';
import { formatDuration, formatVolume, formatWorkoutDate } from '../../utils/formatters';

function ExerciseImage({ imageUrl, name }) {
    if (imageUrl) return <img src={imageUrl} alt={name} className="w-12 h-12 rounded-full object-cover flex-shrink-0" />;
    return (
        <div className="w-12 h-12 rounded-full bg-gray-200 flex items-center justify-center flex-shrink-0">
            <span className="text-base font-bold text-gray-500">{name?.[0]?.toUpperCase()}</span>
        </div>
    );
}

function SetRow({ set, index }) {
    return (
        <div className="flex items-center gap-3 py-2">
            <span className="w-8 text-sm font-bold text-text-secondary text-center">{set.setNumber}</span>
            <span className="flex-1 text-sm text-text-primary">
                {set.weight}kg × {set.reps}
            </span>
            <div className="flex flex-wrap gap-1 justify-end">
                {set.prs?.map(pr => <PrBadge key={pr} label={pr} />)}
            </div>
        </div>
    );
}

function ExerciseSection({ exercise }) {
    const { exerciseName, imageUrl, notes, sets = [] } = exercise;
    const sorted = [...sets].sort((a, b) => a.setNumber - b.setNumber);

    return (
        <div className="px-4 py-4 border-b border-border">
            <div className="flex items-center gap-3 mb-2">
                <ExerciseImage imageUrl={imageUrl} name={exerciseName} />
                <div>
                    <p className="text-base font-semibold text-primary">{exerciseName}</p>
                    {notes && <p className="text-sm text-text-secondary italic">{notes}</p>}
                </div>
            </div>

            <div className="mt-2">
                <div className="flex items-center gap-3 py-1 border-b border-border mb-1">
                    <span className="w-8 text-xs font-bold text-text-muted text-center">SET</span>
                    <span className="flex-1 text-xs font-bold text-text-muted">WEIGHT & REPS</span>
                    <span className="text-xs font-bold text-text-muted">PR</span>
                </div>
                {sorted.map((s, i) => <SetRow key={s.id} set={s} index={i} />)}
            </div>
        </div>
    );
}

export default function WorkoutDetailPage() {
    const { id } = useParams();
    const [workout, setWorkout] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        getWorkoutById(id)
            .then(r => setWorkout(r.data))
            .catch(() => { })
            .finally(() => setLoading(false));
    }, [id]);

    if (loading) {
        return (
            <div className="min-h-screen bg-white flex items-center justify-center">
                <div className="w-8 h-8 border-2 border-primary border-t-transparent rounded-full animate-spin" />
            </div>
        );
    }

    if (!workout) {
        return (
            <div className="min-h-screen bg-white">
                <PageHeader title="Workout Detail" />
                <p className="text-center text-text-secondary mt-16">Workout not found</p>
            </div>
        );
    }

    const exercises = [...(workout.exercises || [])].sort((a, b) => a.orderIndex - b.orderIndex);
    const totalSets = exercises.reduce((sum, e) => sum + (e.sets?.length || 0), 0);
    const totalVolume = exercises.flatMap(e => e.sets || []).reduce((sum, s) => {
        return sum + (s.weight && s.reps ? s.weight * s.reps : 0);
    }, 0);

    return (
        <div className="min-h-screen bg-white">
            <PageHeader title="Workout Detail" />

            {/* Workout header */}
            <div className="px-4 py-4 border-b border-border">
                <h1 className="text-xl font-bold text-text-primary mb-1">{workout.name}</h1>
                <p className="text-xs text-text-secondary mb-4">
                    {formatWorkoutDate(workout.startedAt, workout.endedAt)}
                </p>

                {/* Stats badges */}
                <div className="flex gap-3">
                    {[
                        { label: 'Time', value: formatDuration(workout.duration) },
                        { label: 'Volume', value: formatVolume(totalVolume) },
                        { label: 'Sets', value: String(totalSets) },
                    ].map(({ label, value }) => (
                        <div key={label} className="flex-1 bg-background rounded-xl px-3 py-2 text-center">
                            <p className="text-xs text-text-secondary">{label}</p>
                            <p className="text-sm font-semibold text-text-primary">{value}</p>
                        </div>
                    ))}
                </div>
            </div>

            {/* Exercises */}
            {exercises.map(ex => <ExerciseSection key={ex.id} exercise={ex} />)}
        </div>
    );
}
