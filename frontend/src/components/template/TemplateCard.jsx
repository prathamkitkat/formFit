import { useNavigate } from 'react-router-dom';
import { startTemplate } from '../../api/template';
import { useToast } from '../../context/ToastContext';
import { useWorkout } from '../../context/WorkoutContext';
import { parseErrorMessage } from '../../utils/formatters';

export default function TemplateCard({ template }) {
    const navigate = useNavigate();
    const { addToast } = useToast();
    const { setActiveWorkout } = useWorkout();
    const { templateId, name, exerciseNames = [] } = template;

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

    return (
        <div
            className="card cursor-pointer hover:shadow-sm transition-shadow"
            onClick={() => navigate(`/templates/${templateId}`)}
        >
            <div className="flex items-start justify-between mb-1">
                <h3 className="text-base font-bold text-text-primary">{name}</h3>
                <button
                    onClick={e => { e.stopPropagation(); }}
                    className="text-text-muted text-lg leading-none px-1"
                >
                    ···
                </button>
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
    );
}
