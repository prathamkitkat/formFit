import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { startTemplate, deleteTemplate } from '../../api/template';
import { useToast } from '../../context/ToastContext';
import { useWorkout } from '../../context/WorkoutContext';
import { parseErrorMessage } from '../../utils/formatters';
import ConfirmDialog from '../ui/ConfirmDialog';

export default function TemplateCard({ template, onUpdate }) {
    const navigate = useNavigate();
    const { addToast } = useToast();
    const { setActiveWorkout } = useWorkout();
    const { templateId, name, exerciseNames = [] } = template;
    const [showMenu, setShowMenu] = useState(false);
    const [showDelete, setShowDelete] = useState(false);

    const handleStart = async (e) => {
        e.stopPropagation();
        try {
            const res = await startTemplate(templateId);
            setActiveWorkout(res.data);
            navigate('/workout/active');
        } catch (err) {
            addToast(parseErrorMessage(err));
        }
    };

    const handleDelete = async () => {
        try {
            await deleteTemplate(templateId);
            if (onUpdate) onUpdate();
        } catch (err) {
            addToast(parseErrorMessage(err));
        }
        setShowDelete(false);
    };

    return (
        <>
            <div
                className="card cursor-pointer hover:shadow-sm transition-shadow relative"
                onClick={() => navigate(`/templates/${templateId}`)}
            >
                <div className="flex items-start justify-between mb-1">
                    <h3 className="text-base font-bold text-text-primary pr-8">{name}</h3>
                    <div className="absolute top-3 right-3">
                        <button
                            onClick={e => { e.stopPropagation(); setShowMenu(!showMenu); }}
                            className="text-text-muted text-xl leading-none px-2 py-1 -mr-2 hover:bg-gray-50 rounded-full"
                        >
                            ···
                        </button>
                        {showMenu && (
                            <>
                                <div className="fixed inset-0 z-20" onClick={(e) => { e.stopPropagation(); setShowMenu(false); }} />
                                <div className="absolute right-0 top-full mt-1 w-32 bg-white rounded-xl shadow-lg border border-border overflow-hidden z-30">
                                    <button
                                        onClick={(e) => { e.stopPropagation(); setShowMenu(false); navigate(`/templates/${templateId}/edit`); }}
                                        className="w-full text-left px-4 py-2.5 text-sm hover:bg-gray-50 border-b border-border text-text-primary"
                                    >
                                        Edit
                                    </button>
                                    <button
                                        onClick={(e) => { e.stopPropagation(); setShowMenu(false); setShowDelete(true); }}
                                        className="w-full text-left px-4 py-2.5 text-sm hover:bg-red-50 text-danger"
                                    >
                                        Delete
                                    </button>
                                </div>
                            </>
                        )}
                    </div>
                </div>
                <p className="text-sm text-text-secondary mb-3 truncate">
                    {exerciseNames.length > 0 ? exerciseNames.join(', ') : 'No exercises'}
                </p>
                <button
                    onClick={handleStart}
                    className="w-full bg-primary text-white rounded-xl py-2.5 text-sm font-semibold hover:bg-primary-dark transition-colors"
                >
                    Start Routine
                </button>
            </div>
            {showDelete && (
                <ConfirmDialog
                    title="Delete Routine?"
                    message="Are you sure you want to delete this routine?"
                    confirmText="Delete"
                    onConfirm={handleDelete}
                    onCancel={() => setShowDelete(false)}
                />
            )}
        </>
    );
}

