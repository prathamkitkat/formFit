import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import PageHeader from '../../components/layout/PageHeader';
import { getExerciseStats } from '../../api/profile';
import { formatDate } from '../../utils/formatters';

const PR_ITEMS = [
    { label: 'Heaviest Weight', key: 'heaviestWeight', dateKey: 'heaviestWeightDate', unit: 'kg' },
    { label: 'Best 1RM', key: 'best1RM', dateKey: 'best1RMDate', unit: 'kg' },
    { label: 'Best Set Volume', key: 'bestSetVolume', dateKey: 'bestSetVolumeDate', unit: 'kg' },
    { label: 'Best Session Volume', key: 'bestSessionVolume', dateKey: 'bestSessionVolumeDate', unit: 'kg' },
];

export default function ExerciseStatsPage() {
    const { id } = useParams();
    const [stats, setStats] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        getExerciseStats(id).then(r => setStats(r.data)).catch(() => { }).finally(() => setLoading(false));
    }, [id]);

    return (
        <div className="min-h-screen bg-background">
            <PageHeader title={stats?.exerciseName || 'Exercise Stats'} />

            {loading ? (
                <div className="flex justify-center py-16">
                    <div className="w-8 h-8 border-2 border-primary border-t-transparent rounded-full animate-spin" />
                </div>
            ) : !stats ? (
                <p className="text-center text-text-secondary mt-16">No stats available</p>
            ) : (
                <div className="px-4 py-4 flex flex-col gap-3">
                    <h2 className="text-base font-bold text-text-primary">Personal Records</h2>
                    {PR_ITEMS.map(item => (
                        <div key={item.key} className="bg-white rounded-2xl border border-border p-4 flex items-center gap-3">
                            <span className="text-2xl">🏅</span>
                            <div className="flex-1">
                                <p className="text-xs text-text-secondary">{item.label}</p>
                                <p className="text-lg font-bold text-text-primary">
                                    {stats[item.key] != null
                                        ? `${stats[item.key].toLocaleString('en-US', { maximumFractionDigits: 1 })} ${item.unit}`
                                        : '—'}
                                </p>
                                {stats[item.dateKey] && (
                                    <p className="text-xs text-text-muted">{formatDate(stats[item.dateKey])}</p>
                                )}
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
}
