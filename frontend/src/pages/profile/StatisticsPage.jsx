import { useNavigate } from 'react-router-dom';
import PageHeader from '../../components/layout/PageHeader';

const ITEMS = [
    {
        emoji: '📊',
        title: 'Monthly Report',
        subtitle: 'Recap of your monthly workouts and statistics',
        path: '/profile/statistics/monthly',
    },
    {
        emoji: '🏋️',
        title: 'Main Exercises',
        subtitle: 'List of exercises you do most often',
        path: '/profile/statistics/frequent',
    },
];

export default function StatisticsPage() {
    const navigate = useNavigate();
    return (
        <div className="min-h-screen bg-background">
            <PageHeader title="Statistics" />
            <div className="bg-white">
                {ITEMS.map(item => (
                    <button
                        key={item.title}
                        onClick={() => navigate(item.path)}
                        className="w-full flex items-start gap-4 px-4 py-4 border-b border-border hover:bg-gray-50 transition-colors text-left"
                    >
                        <span className="text-2xl mt-0.5">{item.emoji}</span>
                        <div className="flex-1 min-w-0">
                            <p className="text-sm font-semibold text-text-primary">{item.title}</p>
                            <p className="text-xs text-text-secondary mt-0.5">{item.subtitle}</p>
                        </div>
                        <span className="text-text-muted text-lg">›</span>
                    </button>
                ))}
            </div>
        </div>
    );
}
