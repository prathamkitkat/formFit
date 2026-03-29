import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { FolderPlus, RefreshCw } from 'lucide-react';
import TemplateCard from '../../components/template/TemplateCard';
import BottomNav from '../../components/layout/BottomNav';
import { getTemplates, createTemplate } from '../../api/template';
import { startWorkout } from '../../api/workout';
import { useWorkout } from '../../context/WorkoutContext';
import { useToast } from '../../context/ToastContext';
import { parseErrorMessage } from '../../utils/formatters';

export default function WorkoutTabPage() {
    const navigate = useNavigate();
    const { addToast } = useToast();
    const { setActiveWorkout } = useWorkout();
    const [templates, setTemplates] = useState([]);
    const [loadingTemplates, setLoadingTemplates] = useState(true);

    const fetchTemplates = () => {
        setLoadingTemplates(true);
        getTemplates().then(r => setTemplates(r.data)).catch(() => { }).finally(() => setLoadingTemplates(false));
    };

    useEffect(() => { fetchTemplates(); }, []);

    const handleStartEmpty = async () => {
        try {
            const res = await startWorkout('Log Workout');
            setActiveWorkout(res.data);
            navigate('/workout/active');
        } catch (err) {
            addToast(parseErrorMessage(err));
        }
    };

    const handleNewRoutine = async () => {
        try {
            const res = await createTemplate();
            navigate(`/templates/${res.data.templateId}/edit`);
        } catch (err) {
            addToast(parseErrorMessage(err));
        }
    };

    return (
        <div className="min-h-screen bg-background pb-20">
            {/* Header */}
            <header className="sticky top-0 bg-white z-30 px-4 py-4 flex items-center justify-between border-b border-border">
                <div className="flex items-center gap-1">
                    <h1 className="text-2xl font-bold text-text-primary">Workout</h1>
                    <span className="text-text-secondary text-lg">▾</span>
                </div>
                <button onClick={fetchTemplates} className="text-text-secondary">
                    <RefreshCw size={20} />
                </button>
            </header>

            <div className="px-4 pt-4 flex flex-col gap-4">
                {/* Start empty workout */}
                <button
                    onClick={handleStartEmpty}
                    className="bg-white border border-border rounded-xl py-4 flex items-center justify-center gap-2 text-text-primary font-medium hover:bg-gray-50 transition-colors shadow-sm"
                >
                    <span className="text-primary text-xl font-bold">+</span>
                    Start Empty Workout
                </button>

                {/* Routines section */}
                <div className="flex items-center justify-between">
                    <h2 className="text-lg font-bold text-text-primary">Routines</h2>
                    <button className="text-text-secondary"><FolderPlus size={20} /></button>
                </div>

                <div className="flex gap-3">
                    <button
                        onClick={handleNewRoutine}
                        className="flex-1 border border-border rounded-xl py-3 text-sm font-medium text-text-primary bg-white hover:bg-gray-50 transition-colors"
                    >
                        + New Routine
                    </button>
                    <button className="flex-1 border border-border rounded-xl py-3 text-sm font-medium text-text-primary bg-white hover:bg-gray-50 transition-colors">
                        Explore
                    </button>
                </div>

                {/* Template list */}
                {loadingTemplates ? (
                    <div className="flex justify-center py-8">
                        <div className="w-6 h-6 border-2 border-primary border-t-transparent rounded-full animate-spin" />
                    </div>
                ) : templates.length === 0 ? (
                    <p className="text-center text-text-muted text-sm py-8">No routines yet. Create one to get started.</p>
                ) : (
                    <div className="flex flex-col gap-3">
                        {templates.map(t => <TemplateCard key={t.templateId} template={t} onUpdate={fetchTemplates} />)}
                    </div>
                )}
            </div>

            <BottomNav />
        </div>
    );
}
