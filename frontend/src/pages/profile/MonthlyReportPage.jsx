import { useState, useEffect } from 'react';
import { ChevronLeft, ChevronRight } from 'lucide-react';
import { format, addMonths, subMonths, getYear, getMonth } from 'date-fns';
import PageHeader from '../../components/layout/PageHeader';
import { getMonthlyReport } from '../../api/profile';
import { formatDuration } from '../../utils/formatters';

function Diff({ value, unit }) {
    if (value === undefined || value === null) return null;
    const pos = value >= 0;
    return (
        <span className={`text-xs font-semibold ${pos ? 'text-success' : 'text-danger'}`}>
            {pos ? '+' : ''}{value}{unit}
        </span>
    );
}

export default function MonthlyReportPage() {
    const [date, setDate] = useState(new Date());
    const [report, setReport] = useState(null);

    useEffect(() => {
        getMonthlyReport(getYear(date), getMonth(date) + 1)
            .then(r => setReport(r.data))
            .catch(() => setReport(null));
    }, [date]);

    const monthLabel = format(date, 'MMMM yyyy');

    const stats = report ? [
        { label: 'Workouts', value: report.workouts, diff: report.comparison?.workoutsDiff, unit: '' },
        { label: 'Duration', value: formatDuration(report.totalDurationMinutes), diff: report.comparison?.durationDiff, unit: ' min' },
        { label: 'Volume', value: `${(report.totalVolume || 0).toLocaleString('en-US', { maximumFractionDigits: 0 })} kg`, diff: report.comparison?.volumeDiff, unit: ' kg' },
        { label: 'Sets', value: report.totalSets, diff: report.comparison?.setsDiff, unit: '' },
    ] : [];

    return (
        <div className="min-h-screen bg-background">
            <PageHeader title="Monthly Report" />

            {/* Month picker */}
            <div className="bg-white px-4 py-3 flex items-center justify-between border-b border-border">
                <button onClick={() => setDate(d => subMonths(d, 1))} className="p-2 text-text-secondary"><ChevronLeft size={20} /></button>
                <p className="font-semibold text-text-primary">{monthLabel}</p>
                <button onClick={() => setDate(d => addMonths(d, 1))} className="p-2 text-text-secondary"><ChevronRight size={20} /></button>
            </div>

            {report ? (
                <div className="px-4 py-4 flex flex-col gap-3">
                    <div className="grid grid-cols-2 gap-3">
                        {stats.map(s => (
                            <div key={s.label} className="bg-white rounded-2xl border border-border p-4">
                                <p className="text-xs text-text-secondary mb-1">{s.label}</p>
                                <p className="text-xl font-bold text-text-primary">{s.value}</p>
                                {s.diff !== undefined && (
                                    <div className="flex items-center gap-1 mt-1">
                                        <span className="text-xs text-text-muted">vs last month</span>
                                        <Diff value={s.diff} unit={s.unit} />
                                    </div>
                                )}
                            </div>
                        ))}
                    </div>
                </div>
            ) : (
                <div className="flex flex-col items-center py-20 text-center px-8">
                    <span className="text-4xl mb-4">📊</span>
                    <p className="text-lg font-bold text-text-primary">No data</p>
                    <p className="text-sm text-text-secondary mt-1">No workouts logged in {monthLabel}.</p>
                </div>
            )}
        </div>
    );
}
