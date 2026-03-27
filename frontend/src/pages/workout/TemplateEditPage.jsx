import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Trash2, Minus, Plus } from 'lucide-react';
import PageHeader from '../../components/layout/PageHeader';
import ExercisePickerSheet from '../../components/workout/ExercisePickerSheet';
import ConfirmDialog from '../../components/ui/ConfirmDialog';
import {
    getTemplateById, updateTemplateName, deleteTemplate,
    addTemplatExercises, removeTemplateExercise,
    incrementTemplateSets, decrementTemplateSets,
} from '../../api/template';
import { useToast } from '../../context/ToastContext';
import { parseErrorMessage } from '../../utils/formatters';

export default function TemplateEditPage() {
    const { id } = useParams();
    const navigate = useNavigate();
    const { addToast } = useToast();
    const [template, setTemplate] = useState(null);
    const [name, setName] = useState('');
    const [showPicker, setShowPicker] = useState(false);
    const [showDelete, setShowDelete] = useState(false);

    const load = () =>
        getTemplateById(id).then(r => { setTemplate(r.data); setName(r.data.name); }).catch(() => { });

    useEffect(() => { load(); }, [id]);

    const handleNameBlur = async () => {
        if (!name.trim() || name === template?.name) return;
        try { await updateTemplateName(id, name); } catch (err) { addToast(parseErrorMessage(err)); }
    };

    const handleAddExercises = async (ids) => {
        try { const r = await addTemplatExercises(id, ids); setTemplate(r.data); }
        catch (err) { addToast(parseErrorMessage(err)); }
        setShowPicker(false);
    };

    const handleRemove = async (templateExerciseId) => {
        try { const r = await removeTemplateExercise(id, templateExerciseId); setTemplate(r.data); }
        catch (err) { addToast(parseErrorMessage(err)); }
    };

    const handleIncrement = async (templateExerciseId) => {
        try { const r = await incrementTemplateSets(id, templateExerciseId); setTemplate(r.data); }
        catch (err) { addToast(parseErrorMessage(err)); }
    };

    const handleDecrement = async (templateExerciseId) => {
        try { const r = await decrementTemplateSets(id, templateExerciseId); setTemplate(r.data); }
        catch (err) { addToast(parseErrorMessage(err)); }
    };

    const handleDelete = async () => {
        try { await deleteTemplate(id); navigate('/workout'); }
        catch (err) { addToast(parseErrorMessage(err)); }
        setShowDelete(false);
    };

    const exercises = [...(template?.exercises || [])].sort((a, b) => a.orderIndex - b.orderIndex);

    return (
        <div className="min-h-screen bg-background">
            <PageHeader title="Edit Routine" />

            <div className="px-4 py-4 flex flex-col gap-4">
                {/* Name field */}
                <input
                    className="input-field text-lg font-bold"
                    value={name}
                    onChange={e => setName(e.target.value)}
                    onBlur={handleNameBlur}
                    placeholder="Routine name"
                />

                {/* Add exercise button */}
                <button onClick={() => setShowPicker(true)} className="btn-outline flex items-center justify-center gap-2">
                    <Plus size={18} /> Add Exercise
                </button>

                {/* Exercise list */}
                {exercises.map(ex => (
                    <div key={ex.templateExerciseId} className="bg-white rounded-2xl border border-border p-4 flex items-center justify-between gap-3">
                        <div className="flex-1 min-w-0">
                            <p className="text-sm font-semibold text-text-primary">{ex.exerciseName}</p>
                            <div className="flex items-center gap-3 mt-2">
                                <span className="text-xs text-text-secondary">Sets:</span>
                                <button onClick={() => handleDecrement(ex.templateExerciseId)} className="w-7 h-7 rounded-full border border-border flex items-center justify-center text-text-secondary hover:bg-gray-50">
                                    <Minus size={14} />
                                </button>
                                <span className="text-sm font-bold text-text-primary">{ex.defaultSetCount}</span>
                                <button onClick={() => handleIncrement(ex.templateExerciseId)} className="w-7 h-7 rounded-full border border-border flex items-center justify-center text-text-secondary hover:bg-gray-50">
                                    <Plus size={14} />
                                </button>
                            </div>
                        </div>
                        <button onClick={() => handleRemove(ex.templateExerciseId)} className="text-danger p-1">
                            <Trash2 size={18} />
                        </button>
                    </div>
                ))}

                {/* Delete template */}
                <button onClick={() => setShowDelete(true)} className="w-full py-3 text-sm font-medium text-danger border border-danger/30 rounded-xl bg-white hover:bg-red-50 transition-colors mt-2">
                    Delete Template
                </button>
            </div>

            {showPicker && <ExercisePickerSheet onClose={() => setShowPicker(false)} onAdd={handleAddExercises} />}
            {showDelete && (
                <ConfirmDialog
                    title="Delete Template?"
                    message="This cannot be undone."
                    confirmText="Delete"
                    onConfirm={handleDelete}
                    onCancel={() => setShowDelete(false)}
                />
            )}
        </div>
    );
}
