import { useEffect, useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { Trash2, ChevronUp } from 'lucide-react';
import { useWorkout } from '../../context/WorkoutContext';
import { discardWorkout } from '../../api/workout';
import { useToast } from '../../context/ToastContext';
import ConfirmDialog from '../ui/ConfirmDialog';
import { formatActiveDuration } from '../../utils/formatters';

export default function MiniActiveWorkout() {
    const navigate = useNavigate();
    const location = useLocation();
    const { activeWorkout, setActiveWorkout } = useWorkout();
    const { addToast } = useToast();
    const [elapsed, setElapsed] = useState(0);
    const [showDiscard, setShowDiscard] = useState(false);

    useEffect(() => {
        if (!activeWorkout?.startedAt) return;
        const update = () => setElapsed(Math.floor((Date.now() - new Date(activeWorkout.startedAt).getTime()) / 1000));
        update();
        const t = setInterval(update, 1000);
        return () => clearInterval(t);
    }, [activeWorkout]);

    const handleDiscard = async (e) => {
        if (e) e.stopPropagation();
        try {
            await discardWorkout(activeWorkout.id);
            setActiveWorkout(null);
        } catch (err) {
            addToast(err?.response?.data?.message || 'Failed to discard workout');
        }
        setShowDiscard(false);
    };

    if (!activeWorkout || location.pathname === '/workout/active') return null;

    return (
        <>
            <div
                onClick={() => navigate('/workout/active')}
                className="fixed bottom-[80px] left-4 right-4 bg-white border border-border shadow-[0_4px_20px_-4px_rgba(0,0,0,0.1)] px-4 py-3 flex items-center justify-between z-40 cursor-pointer active:scale-[0.98] transition-all rounded-full"
            >
                <div className="flex items-center gap-3">
                    <div className="w-8 h-8 rounded-full flex items-center justify-center">
                        <ChevronUp size={20} className="text-text-secondary" />
                    </div>
                    <div>
                        <div className="flex items-center gap-2">
                            <span className="w-2 h-2 rounded-full bg-success opacity-90 shadow-[0_0_5px_rgba(34,197,94,0.6)]" />
                            <p className="text-sm font-bold text-text-primary capitalize truncate max-w-[120px]">{activeWorkout.name || 'Workout'}</p>
                            <span className="text-sm font-bold text-text-primary">{formatActiveDuration(elapsed)}</span>
                        </div>
                        <p className="text-xs text-text-secondary mt-0.5">
                            {activeWorkout?.exercises?.length === 0 ? 'No exercise' : `${activeWorkout?.exercises?.length} exercise${activeWorkout?.exercises?.length !== 1 ? 's' : ''}`}
                        </p>
                    </div>
                </div>

                <button
                    onClick={(e) => { e.stopPropagation(); setShowDiscard(true); }}
                    className="w-10 h-10 flex items-center justify-center border border-danger/30 text-danger bg-danger/5 hover:bg-danger/10 rounded-full transition-colors flex-shrink-0"
                >
                    <Trash2 size={18} />
                </button>
            </div>

            {showDiscard && (
                <ConfirmDialog
                    title="Discard Workout?"
                    message="Current progress will be lost."
                    confirmText="Discard"
                    onConfirm={handleDiscard}
                    onCancel={(e) => { if (e) e.stopPropagation(); setShowDiscard(false); }}
                />
            )}
        </>
    );
}
