import { useState, useEffect } from 'react';
import { Search, X } from 'lucide-react';
import { getExercises } from '../../api/exercise';
import { useNavigate } from 'react-router-dom';
import { MUSCLE_GROUPS } from '../../utils/constants';

function ExerciseImage({ imageUrl, name }) {
    if (imageUrl) {
        return <img src={imageUrl} alt={name} className="w-12 h-12 rounded-full object-cover flex-shrink-0" />;
    }
    return (
        <div className="w-12 h-12 rounded-full bg-gray-200 flex items-center justify-center flex-shrink-0">
            <span className="text-base font-bold text-gray-500">{name?.[0]?.toUpperCase()}</span>
        </div>
    );
}

export default function ExercisePickerSheet({ onClose, onAdd }) {
    const navigate = useNavigate();
    const [exercises, setExercises] = useState([]);
    const [search, setSearch] = useState('');
    const [selectedIds, setSelectedIds] = useState([]);
    const [equipmentFilter, setEquipmentFilter] = useState('');
    const [muscleFilter, setMuscleFilter] = useState('');

    useEffect(() => {
        getExercises().then(r => {
            const mapped = r.data.map(ex => ({ ...ex, id: ex.id || ex.exerciseId }));
            setExercises(mapped);
        }).catch(() => { });
    }, []);

    const equipments = [...new Set(exercises.map(e => e.equipment).filter(Boolean))];

    const filtered = exercises.filter(ex => {
        const exName = ex.name || ex.exerciseName || '';
        const matchSearch = exName.toLowerCase().includes(search.toLowerCase());
        const matchEquip = !equipmentFilter || ex.equipment === equipmentFilter;
        const matchMuscle = !muscleFilter ||
            ex.muscleGroups?.some(mg => mg.name === muscleFilter);
        return matchSearch && matchEquip && matchMuscle;
    });

    const toggle = (id) =>
        setSelectedIds(prev => prev.includes(id) ? prev.filter(x => x !== id) : [...prev, id]);

    const handleAdd = () => {
        if (selectedIds.length > 0) onAdd(selectedIds);
    };

    return (
        <div className="fixed inset-0 bg-black/50 z-50 flex items-end">
            <div className="bg-white w-full max-w-[480px] mx-auto rounded-t-2xl flex flex-col h-[90vh]">
                {/* Header */}
                <div className="flex items-center justify-between px-4 py-3 border-b border-border">
                    <button onClick={onClose} className="text-text-secondary font-medium text-sm">Cancel</button>
                    <h2 className="font-semibold text-text-primary">Add Exercise</h2>
                    <button onClick={() => navigate('/exercises/create')} className="text-primary font-medium text-sm">Create</button>
                </div>

                {/* Search */}
                <div className="px-4 py-2 border-b border-border">
                    <div className="flex items-center gap-2 bg-background rounded-xl px-3 py-2">
                        <Search size={16} className="text-text-muted" />
                        <input
                            className="flex-1 bg-transparent text-sm text-text-primary placeholder:text-text-muted outline-none"
                            placeholder="Search exercise..."
                            value={search}
                            onChange={e => setSearch(e.target.value)}
                        />
                        {search && <button onClick={() => setSearch('')}><X size={14} className="text-text-muted" /></button>}
                    </div>
                </div>

                {/* Filters */}
                <div className="flex gap-2 px-4 py-2 border-b border-border overflow-x-auto">
                    <select
                        className="text-xs border border-border rounded-lg px-2 py-1 bg-white text-text-primary"
                        value={equipmentFilter}
                        onChange={e => setEquipmentFilter(e.target.value)}
                    >
                        <option value="">All Equipment</option>
                        {equipments.map(eq => <option key={eq} value={eq}>{eq}</option>)}
                    </select>
                    <select
                        className="text-xs border border-border rounded-lg px-2 py-1 bg-white text-text-primary"
                        value={muscleFilter}
                        onChange={e => setMuscleFilter(e.target.value)}
                    >
                        <option value="">All Muscles</option>
                        {MUSCLE_GROUPS.map(mg => <option key={mg.id} value={mg.name}>{mg.name}</option>)}
                    </select>
                </div>

                {/* List */}
                <div className="flex-1 overflow-y-auto">
                    {filtered.length === 0 && (
                        <p className="text-center text-text-muted text-sm py-8">No exercises found</p>
                    )}
                    {filtered.map(ex => {
                        const selected = selectedIds.includes(ex.id);
                        return (
                            <div
                                key={ex.id}
                                className={`flex items-center gap-3 px-4 py-3 border-b border-border cursor-pointer active:bg-gray-50 transition-colors ${selected ? 'border-l-4 border-l-primary' : ''}`}
                                onClick={() => toggle(ex.id)}
                            >
                                <ExerciseImage imageUrl={ex.imageUrl} name={ex.name || ex.exerciseName} />
                                <div className="flex-1 min-w-0">
                                    <p className="text-sm font-semibold text-text-primary">{ex.name || ex.exerciseName}</p>
                                    <p className="text-xs text-text-secondary truncate">
                                        {[...(ex.muscleGroups?.map(mg => mg.name) || []), ex.equipment].filter(Boolean).join(', ') || '—'}
                                    </p>
                                </div>
                            </div>
                        );
                    })}
                </div>

                {/* Bottom button */}
                <div className="px-4 py-4 border-t border-border">
                    <button
                        onClick={handleAdd}
                        disabled={selectedIds.length === 0}
                        className="btn-primary"
                    >
                        {selectedIds.length === 0
                            ? 'Select exercises'
                            : `Add ${selectedIds.length} exercise${selectedIds.length !== 1 ? 's' : ''}`}
                    </button>
                </div>
            </div>
        </div>
    );
}
