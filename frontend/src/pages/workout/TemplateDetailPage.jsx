import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { ChevronLeft, Play, MoreVertical } from 'lucide-react';
import { getTemplateById, deleteTemplate, startTemplate } from '../../api/template';
import { useWorkout } from '../../context/WorkoutContext';
import { useToast } from '../../context/ToastContext';
import { parseErrorMessage } from '../../utils/formatters';
import ConfirmDialog from '../../components/ui/ConfirmDialog';

export default function TemplateDetailPage() {
    const { id } = useParams();
    const navigate = useNavigate();
    const { addToast } = useToast();
    const { setActiveWorkout } = useWorkout();
    const [template, setTemplate] = useState(null);
    const [showDelete, setShowDelete] = useState(false);
    const [showMenu, setShowMenu] = useState(false);

    useEffect(() => {
        getTemplateById(id)
            .then(r => setTemplate(r.data))
            .catch(err => addToast(parseErrorMessage(err)));
    }, [id]);

    const handleStart = async () => {
        try {
            const res = await startTemplate(id);
            setActiveWorkout(res.data);
            navigate('/workout/active');
        } catch (err) {
            addToast(parseErrorMessage(err));
        }
    };

    const handleDelete = async () => {
        try {
            await deleteTemplate(id);
            navigate('/workout');
        } catch (err) {
            addToast(parseErrorMessage(err));
            setShowDelete(false);
        }
    };

    if (!template) return <div className="min-h-screen bg-background flex items-center justify-center">Loading...</div>;

    const exercises = [...(template.exercises || [])].sort((a, b) => a.orderIndex - b.orderIndex);

    return (
        <div className="min-h-screen bg-background pb-20">
            {/* Header */}
            <div className="bg-white px-4 py-3 flex items-center justify-between border-b border-border sticky top-0 z-10">
                <button onClick={() => navigate(-1)} className="p-2 -ml-2 text-text-secondary hover:bg-gray-50 rounded-full">
                    <ChevronLeft size={24} />
                </button>
                <h1 className="text-base font-bold text-text-primary capitalize truncate px-4">{template.name || 'Routine Details'}</h1>
                <div className="relative">
                    <button 
                        onClick={() => setShowMenu(!showMenu)} 
                        className="p-2 -mr-2 text-text-secondary hover:bg-gray-50 rounded-full"
                    >
                        <MoreVertical size={20} />
                    </button>
                    {showMenu && (
                        <>
                            <div className="fixed inset-0 z-20" onClick={() => setShowMenu(false)} />
                            <div className="absolute right-0 top-full mt-1 w-32 bg-white rounded-xl shadow-lg border border-border overflow-hidden z-30">
                                <button
                                    onClick={() => { setShowMenu(false); navigate(`/templates/${id}/edit`); }}
                                    className="w-full text-left px-4 py-3 text-sm hover:bg-gray-50 text-text-primary font-medium border-b border-border"
                                >
                                    Edit
                                </button>
                                <button
                                    onClick={() => { setShowMenu(false); setShowDelete(true); }}
                                    className="w-full text-left px-4 py-3 text-sm hover:bg-red-50 text-danger font-medium"
                                >
                                    Delete
                                </button>
                            </div>
                        </>
                    )}
                </div>
            </div>

            <div className="px-4 py-6 flex flex-col gap-6">
                <div>
                    <h2 className="text-xs font-semibold text-text-secondary uppercase tracking-wider mb-3">Exercises</h2>
                    {exercises.length === 0 ? (
                        <p className="text-sm text-text-muted">No exercises added yet.</p>
                    ) : (
                        <div className="flex flex-col gap-3">
                            {exercises.map(ex => (
                                <div key={ex.templateExerciseId} className="bg-white rounded-2xl border border-border px-4 py-3.5 flex flex-col gap-1 shadow-sm">
                                    <p className="text-sm font-bold text-text-primary">{ex.exerciseName}</p>
                                    <p className="text-xs font-medium text-text-secondary">{ex.defaultSetCount} sets</p>
                                </div>
                            ))}
                        </div>
                    )}
                </div>

                <button onClick={handleStart} className="w-full bg-primary text-white rounded-xl py-3.5 text-sm font-bold flex items-center justify-center gap-2 hover:bg-primary-dark transition-colors mt-2 shadow-sm">
                    <Play size={18} fill="currentColor" />
                    Start Routine
                </button>
            </div>

            {showDelete && (
                <ConfirmDialog
                    title="Delete Routine?"
                    message="Are you sure you want to delete this routine? This cannot be undone."
                    confirmText="Delete"
                    onConfirm={handleDelete}
                    onCancel={() => setShowDelete(false)}
                />
            )}
        </div>
    );
}
