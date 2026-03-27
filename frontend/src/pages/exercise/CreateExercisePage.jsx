import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import PageHeader from '../../components/layout/PageHeader';
import { createExercise } from '../../api/exercise';
import { useToast } from '../../context/ToastContext';
import { parseErrorMessage } from '../../utils/formatters';
import { MUSCLE_GROUPS } from '../../utils/constants';

export default function CreateExercisePage() {
    const navigate = useNavigate();
    const { addToast } = useToast();
    const [form, setForm] = useState({ name: '', equipment: '', imageUrl: '' });
    const [selectedMuscles, setSelectedMuscles] = useState([]);
    const [loading, setLoading] = useState(false);

    const set = (k) => (e) => setForm(f => ({ ...f, [k]: e.target.value }));
    const toggleMuscle = (id) =>
        setSelectedMuscles(prev => prev.includes(id) ? prev.filter(x => x !== id) : [...prev, id]);

    const handleSave = async () => {
        if (!form.name.trim()) { addToast('Name is required', 'error'); return; }
        setLoading(true);
        try {
            await createExercise({
                name: form.name.trim(),
                equipment: form.equipment || null,
                imageUrl: form.imageUrl || null,
                videoUrl: null,
                muscleGroupIds: selectedMuscles,
            });
            addToast('Exercise created!', 'success');
            navigate(-1);
        } catch (err) {
            addToast(parseErrorMessage(err));
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="min-h-screen bg-background">
            <PageHeader
                title="Create Exercise"
                rightElement={
                    <button onClick={handleSave} disabled={loading} className="text-primary text-sm font-semibold disabled:opacity-50">
                        Save
                    </button>
                }
            />

            <div className="px-4 py-4 flex flex-col gap-4">
                <input className="input-field" placeholder="Exercise name *" maxLength={100} value={form.name} onChange={set('name')} />
                <input className="input-field" placeholder="Equipment (optional)" value={form.equipment} onChange={set('equipment')} />
                <input className="input-field" placeholder="Image URL (optional)" value={form.imageUrl} onChange={set('imageUrl')} />

                <div>
                    <p className="text-sm font-semibold text-text-primary mb-2">Muscle Groups</p>
                    <div className="flex flex-wrap gap-2">
                        {MUSCLE_GROUPS.map(mg => {
                            const selected = selectedMuscles.includes(mg.id);
                            return (
                                <button
                                    key={mg.id}
                                    onClick={() => toggleMuscle(mg.id)}
                                    className={`px-3 py-1.5 rounded-full text-sm font-medium border transition-colors ${selected ? 'bg-primary text-white border-primary' : 'border-border text-text-primary bg-white hover:bg-gray-50'
                                        }`}
                                >
                                    {mg.name}
                                </button>
                            );
                        })}
                    </div>
                </div>
            </div>
        </div>
    );
}
