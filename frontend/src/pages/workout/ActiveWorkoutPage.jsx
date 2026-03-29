import { useState, useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import { ChevronDown, Clock, MoreHorizontal, Plus, Dumbbell } from 'lucide-react';
import ExercisePickerSheet from '../../components/workout/ExercisePickerSheet';
import ConfirmDialog from '../../components/ui/ConfirmDialog';
import { useWorkout } from '../../context/WorkoutContext';
import { useToast } from '../../context/ToastContext';
import {
    getActiveWorkout, addSet, updateSet, removeSet, addExercises,
    removeExercise, discardWorkout, completeWorkout,
    updateExerciseNotes, replaceExercise,
} from '../../api/workout';
import { parseErrorMessage, formatDuration, formatActiveDuration } from '../../utils/formatters';
import { useDebouncedCallback } from '../../hooks/useDebounce';

// -- Elapsed time hook --
function useElapsed(startedAt) {
    const [elapsed, setElapsed] = useState(0);
    useEffect(() => {
        if (!startedAt) return;
        const update = () => setElapsed(Math.floor((Date.now() - new Date(startedAt).getTime()) / 1000));
        update();
        const t = setInterval(update, 1000);
        return () => clearInterval(t);
    }, [startedAt]);
    return elapsed;
}

function SetRow({ set, workoutId, workoutExerciseId, prevSet, onRemove }) {
    const [weight, setWeight] = useState(set.weight ?? '');
    const [reps, setReps] = useState(set.reps ?? '');
    const [checked, setChecked] = useState(false);
    const { addToast } = useToast();

    const save = useDebouncedCallback(async (w, r) => {
        if (w === '' || r === '') return;
        try {
            await updateSet(workoutId, workoutExerciseId, set.id, parseFloat(w), parseInt(r));
        } catch (err) {
            addToast(parseErrorMessage(err));
        }
    }, 600);

    const handleWeight = (v) => { setWeight(v); save(v, reps); };
    const handleReps = (v) => { setReps(v); save(weight, v); };

    return (
        <div className={`flex items-center gap-2 py-2 px-1 rounded-lg transition-colors ${checked ? 'bg-gray-100' : ''}`}>
            <button
                onClick={() => {
                    if (window.confirm("Remove this set?")) {
                        onRemove(set.id);
                    }
                }}
                className="w-7 h-7 flex items-center justify-center rounded transition-colors text-xs font-bold text-text-secondary hover:bg-red-50 hover:text-red-500"
                title="Remove set"
            >
                {set.setNumber}
            </button>
            <span className="w-20 text-xs text-text-muted text-center">
                {prevSet ? `${prevSet.weight}kg × ${prevSet.reps}` : '—'}
            </span>
            <input
                type="number"
                value={weight}
                onChange={e => handleWeight(e.target.value)}
                placeholder="0"
                className="w-14 border-b border-border text-center text-sm text-text-primary bg-transparent outline-none py-1"
            />
            <input
                type="number"
                value={reps}
                onChange={e => handleReps(e.target.value)}
                placeholder="0"
                className="w-14 border-b border-border text-center text-sm text-text-primary bg-transparent outline-none py-1"
            />
            <button
                onClick={() => setChecked(c => !c)}
                className={`w-7 h-7 rounded-full border-2 flex items-center justify-center flex-shrink-0 transition-colors ${checked ? 'bg-success border-success text-white' : 'border-border'}`}
            >
                {checked && <span className="text-xs">✓</span>}
            </button>
        </div>
    );
}

// -- Exercise section --
function ExerciseSection({ exercise, workoutId, onRemove, onReplace, refresh }) {
    const { addToast } = useToast();
    const [showMenu, setShowMenu] = useState(false);
    const [editingNotes, setEditingNotes] = useState(false);
    const [notes, setNotes] = useState(exercise.notes || '');
    const [sets, setSets] = useState(exercise.sets || []);
    const [addingSet, setAddingSet] = useState(false);

    const prevSets = exercise.previousPerformance?.sets || [];
    const sorted = [...sets].sort((a, b) => a.setNumber - b.setNumber);

    const handleAddSet = async () => {
        setAddingSet(true);
        try {
            const res = await addSet(workoutId, exercise.id);
            const newSetId = res.data;
            setSets(prev => [...prev, { id: newSetId, setNumber: prev.length + 1, weight: null, reps: null }]);
        } catch (err) {
            addToast(parseErrorMessage(err));
        } finally {
            setAddingSet(false);
        }
    };

    const handleRemoveSet = async (setId) => {
        try {
            await removeSet(workoutId, exercise.id, setId);
            setSets(prev => prev.filter(s => s.id !== setId));
        } catch (err) {
            addToast(parseErrorMessage(err));
        }
    };

    const saveNotes = async () => {
        setEditingNotes(false);
        try {
            await updateExerciseNotes(workoutId, exercise.id, notes);
        } catch (err) {
            addToast(parseErrorMessage(err));
        }
    };

    return (
        <div className="bg-white mb-2 px-4 py-4 border-b border-border">
            {/* Exercise header */}
            <div className="flex items-center justify-between mb-2">
                <div className="flex items-center gap-2 flex-1 min-w-0">
                    <div className="w-10 h-10 rounded-full bg-gray-200 flex items-center justify-center flex-shrink-0">
                        {exercise.imageUrl
                            ? <img src={exercise.imageUrl} className="w-10 h-10 rounded-full object-cover" />
                            : <span className="text-sm font-bold text-gray-500">{exercise.exerciseName?.[0]?.toUpperCase()}</span>
                        }
                    </div>
                    <p className="text-base font-semibold text-primary truncate">{exercise.exerciseName}</p>
                </div>
                <div className="relative">
                    <button onClick={() => setShowMenu(m => !m)} className="text-text-muted p-1"><MoreHorizontal size={18} /></button>
                    {showMenu && (
                        <div className="absolute right-0 top-8 bg-white border border-border rounded-xl shadow-lg z-20 min-w-[150px] overflow-hidden">
                            {[
                                { label: 'Add Note', action: () => { setEditingNotes(true); setShowMenu(false); } },
                                { label: 'Replace Exercise', action: () => { onReplace(exercise.id); setShowMenu(false); } },
                                { label: 'Remove Exercise', action: () => { onRemove(exercise.id); setShowMenu(false); }, danger: true },
                            ].map(item => (
                                <button
                                    key={item.label}
                                    onClick={item.action}
                                    className={`w-full text-left px-4 py-3 text-sm border-b border-border last:border-0 hover:bg-gray-50 ${item.danger ? 'text-danger' : 'text-text-primary'}`}
                                >
                                    {item.label}
                                </button>
                            ))}
                        </div>
                    )}
                </div>
            </div>

            {/* Notes */}
            {editingNotes ? (
                <textarea
                    className="w-full text-sm border border-border rounded-lg px-3 py-2 mb-2 outline-none focus:border-primary resize-none"
                    value={notes}
                    onChange={e => setNotes(e.target.value)}
                    onBlur={saveNotes}
                    placeholder="Add a note..."
                    autoFocus
                    rows={2}
                />
            ) : notes ? (
                <p className="text-sm text-text-secondary italic mb-2 cursor-pointer" onClick={() => setEditingNotes(true)}>{notes}</p>
            ) : null}

            {/* Column headers */}
            <div className="flex items-center gap-2 py-1 mb-1">
                <span className="w-7 text-xs font-bold text-text-muted text-center">SET</span>
                <span className="w-20 text-xs font-bold text-text-muted text-center">PREVIOUS</span>
                <span className="w-14 text-xs font-bold text-text-muted text-center">KG</span>
                <span className="w-14 text-xs font-bold text-text-muted text-center">REPS</span>
                <span className="w-7 text-xs font-bold text-text-muted text-center">✓</span>
            </div>

            {sorted.map((s, i) => (
                <SetRow
                    key={s.id}
                    set={s}
                    workoutId={workoutId}
                    workoutExerciseId={exercise.id}
                    prevSet={prevSets[i] || null}
                    onRemove={handleRemoveSet}
                />
            ))}

            <button
                onClick={handleAddSet}
                disabled={addingSet}
                className="mt-3 w-full border border-dashed border-border rounded-xl py-2.5 text-sm font-medium text-text-secondary flex items-center justify-center gap-1 hover:border-primary hover:text-primary transition-colors"
            >
                <Plus size={16} /> Add Set
            </button>
        </div>
    );
}

// -- Main page --
export default function ActiveWorkoutPage() {
    const navigate = useNavigate();
    const { addToast } = useToast();
    const { activeWorkout, setActiveWorkout } = useWorkout();
    const [workout, setWorkout] = useState(null);
    const [loading, setLoading] = useState(true);
    const [showPicker, setShowPicker] = useState(false);
    const [showDiscard, setShowDiscard] = useState(false);
    const [showFinish, setShowFinish] = useState(false);
    const [replaceTarget, setReplaceTarget] = useState(null);

    const elapsed = useElapsed(workout?.startedAt);

    useEffect(() => {
        if (activeWorkout) { setWorkout(activeWorkout); setLoading(false); return; }
        getActiveWorkout()
            .then(r => { if (r.data) { setWorkout(r.data); setActiveWorkout(r.data); } else navigate('/workout'); })
            .catch(() => navigate('/workout'))
            .finally(() => setLoading(false));
    }, []);

    const refresh = () => getActiveWorkout().then(r => { setWorkout(r.data); setActiveWorkout(r.data); }).catch(() => { });

    const exercises = [...(workout?.exercises || [])].sort((a, b) => a.orderIndex - b.orderIndex);
    const totalSets = exercises.reduce((s, e) => s + (e.sets?.length || 0), 0);
    const totalVolume = exercises.flatMap(e => e.sets || []).reduce((s, set) => {
        return s + (set.weight && set.reps ? set.weight * set.reps : 0);
    }, 0);

    const handleAddExercises = async (ids) => {
        try {
            const res = await addExercises(workout.id, ids);
            setWorkout(res.data); setActiveWorkout(res.data);
        } catch (err) { addToast(parseErrorMessage(err)); }
        setShowPicker(false);
        setReplaceTarget(null);
    };

    const handleRemove = async (workoutExerciseId) => {
        try {
            const res = await removeExercise(workout.id, workoutExerciseId);
            setWorkout(res.data); setActiveWorkout(res.data);
        } catch (err) { addToast(parseErrorMessage(err)); }
    };

    const handleReplace = (workoutExerciseId) => {
        setReplaceTarget(workoutExerciseId);
        setShowPicker(true);
    };

    const handlePickerAdd = async (ids) => {
        if (replaceTarget) {
            try {
                const res = await replaceExercise(workout.id, replaceTarget, ids[0]);
                setWorkout(res.data); setActiveWorkout(res.data);
            } catch (err) { addToast(parseErrorMessage(err)); }
            setShowPicker(false); setReplaceTarget(null);
        } else {
            await handleAddExercises(ids);
        }
    };

    const handleDiscard = async () => {
        try { await discardWorkout(workout.id); setActiveWorkout(null); navigate('/workout'); }
        catch (err) { addToast(parseErrorMessage(err)); }
        setShowDiscard(false);
    };

    const handleFinish = async () => {
        setShowFinish(false);
        try {
            await completeWorkout(workout.id);
            setActiveWorkout(null);
            navigate('/home');
        } catch (err) { addToast(parseErrorMessage(err)); }
    };

    if (loading) return (
        <div className="min-h-screen bg-white flex items-center justify-center">
            <div className="w-8 h-8 border-2 border-primary border-t-transparent rounded-full animate-spin" />
        </div>
    );

    return (
        <div className="min-h-screen bg-background">
            {/* Header */}
            <header className="sticky top-0 bg-white z-30 px-4 h-14 flex items-center justify-between border-b border-border">
                <button onClick={() => navigate('/workout')} className="text-text-secondary"><ChevronDown size={24} /></button>
                <h1 className="font-semibold text-text-primary">{workout?.name || 'Log Workout'}</h1>
                <div className="flex items-center gap-2">
                    <span className="text-xs text-text-secondary flex items-center gap-1">
                        <Clock size={14} /> {formatActiveDuration(elapsed)}
                    </span>
                    <button
                        onClick={() => setShowFinish(true)}
                        className="bg-primary text-white text-sm font-semibold px-4 py-1.5 rounded-lg"
                    >
                        Finish
                    </button>
                </div>
            </header>

            {/* Stats bar */}
            <div className="bg-white border-b border-border px-4 py-2 flex divide-x divide-border">
                {[
                    { label: 'Duration', value: formatActiveDuration(elapsed) },
                    { label: 'Volume', value: `${totalVolume.toLocaleString('en-US', { maximumFractionDigits: 1 })} kg` },
                    { label: 'Sets', value: String(totalSets) },
                ].map(({ label, value }) => (
                    <div key={label} className="flex-1 text-center px-2">
                        <p className="text-xs text-text-secondary">{label}</p>
                        <p className="text-sm font-semibold text-text-primary">{value}</p>
                    </div>
                ))}
            </div>

            {/* Empty state */}
            {exercises.length === 0 && (
                <div className="flex flex-col items-center py-20 px-8 text-center">
                    <Dumbbell size={48} className="text-text-muted mb-4" />
                    <p className="text-xl font-bold text-text-primary">Get started</p>
                    <p className="text-sm text-text-secondary mt-2 mb-8">Add an exercise to start your workout</p>
                    <button onClick={() => setShowPicker(true)} className="btn-primary max-w-xs">
                        + Add Exercise
                    </button>
                </div>
            )}

            {/* Exercises */}
            <div className="pb-40">
                {exercises.map(ex => (
                    <ExerciseSection
                        key={ex.id}
                        exercise={ex}
                        workoutId={workout.id}
                        onRemove={handleRemove}
                        onReplace={handleReplace}
                        refresh={refresh}
                    />
                ))}

                {exercises.length > 0 && (
                    <div className="px-4 mt-2 flex flex-col gap-3">
                        <button onClick={() => setShowPicker(true)} className="btn-outline">
                            + Add Exercise
                        </button>
                        <div className="flex gap-3">
                            <button className="flex-1 py-3 text-sm text-text-secondary border border-border rounded-xl bg-white hover:bg-gray-50" disabled>
                                Settings
                            </button>
                            <button onClick={() => setShowDiscard(true)} className="flex-1 py-3 text-sm font-medium text-danger border border-danger/30 rounded-xl bg-white hover:bg-red-50">
                                Discard Workout
                            </button>
                        </div>
                    </div>
                )}
            </div>

            {/* Modals */}
            {showPicker && <ExercisePickerSheet onClose={() => { setShowPicker(false); setReplaceTarget(null); }} onAdd={handlePickerAdd} />}
            {showDiscard && (
                <ConfirmDialog
                    title="Discard Workout?"
                    message="This cannot be undone."
                    confirmText="Discard"
                    onConfirm={handleDiscard}
                    onCancel={() => setShowDiscard(false)}
                />
            )}
            {showFinish && (
                <ConfirmDialog
                    title="Finish Workout?"
                    confirmText="Finish"
                    confirmClass="bg-primary text-white"
                    onConfirm={handleFinish}
                    onCancel={() => setShowFinish(false)}
                />
            )}
        </div>
    );
}
