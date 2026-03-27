import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Settings, ArrowUpRight, Edit3 } from 'lucide-react';
import { format, parseISO } from 'date-fns';
import { BarChart, Bar, XAxis, YAxis, ResponsiveContainer, Tooltip, CartesianGrid } from 'recharts';
import WorkoutCard from '../../components/workout/WorkoutCard';
import BottomNav from '../../components/layout/BottomNav';
import { useAuth } from '../../context/AuthContext';
import { getStats } from '../../api/profile';
import { getWorkouts } from '../../api/workout';
import { formatDuration, formatVolume } from '../../utils/formatters';
import { PERIODS } from '../../utils/constants';

const METRICS = [
    { key: 'totalDuration', label: 'Duration', transform: v => +(v / 60).toFixed(2), unit: 'hrs' },
    { key: 'totalVolume', label: 'Volume', transform: v => v, unit: 'kg' },
    { key: 'totalReps', label: 'Reps', transform: v => v, unit: 'reps' },
];

export default function ProfilePage() {
    const navigate = useNavigate();
    const { currentUser } = useAuth();
    const [stats, setStats] = useState(null);
    const [period, setPeriod] = useState('3M');
    const [metric, setMetric] = useState(METRICS[0]);
    const [workouts, setWorkouts] = useState([]);
    const [showPeriods, setShowPeriods] = useState(false);

    useEffect(() => {
        getStats(period).then(r => setStats(r.data)).catch(() => { });
    }, [period]);

    useEffect(() => {
        getWorkouts().then(r => setWorkouts(r.data.slice(0, 5))).catch(() => { });
    }, []);

    const durationArr = stats?.duration || [];
    const volumeArr = stats?.volume || [];
    const repsArr = stats?.reps || [];

    const rawGraphData = durationArr.map((d, i) => ({
        date: d.date,
        totalDuration: d.value,
        totalVolume: volumeArr[i]?.value || 0,
        totalReps: repsArr[i]?.value || 0
    }));

    const graphData = rawGraphData
        .map(d => ({
            ...d,
            value: metric.transform(d[metric.key] || 0),
        }))
        .filter(d => d.value > 0);

    const totalDuration = durationArr.reduce((sum, d) => sum + d.value, 0);
    const totalWorkouts = durationArr.filter(d => d.value > 0).length;

    const username = currentUser?.username || 'You';

    return (
        <div className="min-h-screen bg-white pb-20">
            {/* Header */}
            <header className="sticky top-0 bg-white z-30 px-4 py-4 flex items-center justify-between">
                <h1 className="text-2xl font-bold text-text-primary">{username}</h1>
                <div className="flex items-center gap-4 text-text-primary">
                    <Edit3 size={20} onClick={() => navigate('/settings')} className="cursor-pointer" />
                    <ArrowUpRight size={20} />
                    <Settings size={20} onClick={() => navigate('/settings')} className="cursor-pointer" />
                </div>
            </header>

            <div className="px-4 py-2 flex flex-col gap-6">
                {/* User info */}
                <div className="flex items-center gap-4 border-b border-[#f3f4f6] pb-6">
                    <div className="w-[84px] h-[84px] rounded-full bg-[#455A64] text-white flex items-center justify-center text-[40px] font-normal flex-shrink-0">
                        {username[0]?.toUpperCase()}
                    </div>
                    <div>
                        <p className="text-[22px] font-semibold text-text-primary mb-1">{username}</p>
                        <div className="flex gap-4 text-sm">
                            <div className="flex flex-col">
                                <span className="text-gray-500">Workouts</span>
                                <span className="text-lg text-text-primary">{stats ? totalWorkouts : '—'}</span>
                            </div>
                            <div className="flex flex-col">
                                <span className="text-gray-500">Followers</span>
                                <span className="text-lg text-text-primary">1</span>
                            </div>
                            <div className="flex flex-col">
                                <span className="text-gray-500">Following</span>
                                <span className="text-lg text-text-primary">1</span>
                            </div>
                        </div>
                    </div>
                </div>

                {/* Graph section */}
                <div>
                    <div className="flex items-end justify-between mb-4">
                        <div className="flex items-baseline gap-1">
                            <p className="text-[22px] font-semibold text-text-primary">
                                {stats ? `${metric.transform(totalDuration || 0).toFixed(1)} ${metric.unit === 'hrs' ? 'hours' : metric.unit}` : `0 ${metric.unit === 'hrs' ? 'hours' : metric.unit}`}
                            </p>
                            <span className="text-gray-500 text-lg">this week</span>
                        </div>
                        <div className="relative">
                            <button onClick={() => setShowPeriods(v => !v)} className="text-[15px] text-[#007AFF] flex items-center gap-1 font-normal">
                                {PERIODS.find(p => p.value === period)?.label || 'Last 3 months'} <span>▾</span>
                            </button>
                            {showPeriods && (
                                <div className="absolute right-0 mt-2 w-40 bg-white border border-border rounded-xl shadow-lg z-20 overflow-hidden">
                                    {PERIODS.map(p => (
                                        <button key={p.value} onClick={() => { setPeriod(p.value); setShowPeriods(false); }}
                                            className="w-full text-left px-4 py-2.5 text-sm hover:bg-gray-50 border-b border-border last:border-0 text-text-primary">
                                            {p.label}
                                        </button>
                                    ))}
                                </div>
                            )}
                        </div>
                    </div>

                    <div className="h-52 mt-6 -ml-5">
                        {graphData.length > 0 ? (
                            <ResponsiveContainer width="100%" height="100%">
                                <BarChart data={graphData} maxBarSize={48}>
                                    <CartesianGrid vertical={false} stroke="#f0f0f0" />
                                    <XAxis
                                        dataKey="date"
                                        tickFormatter={(val) => {
                                            try { return format(parseISO(val), 'MMM d'); } catch (e) { return val; }
                                        }}
                                        tick={{ fontSize: 11, fill: '#6B7280' }}
                                        tickLine={false}
                                        axisLine={{ stroke: '#e5e5e5' }}
                                        tickMargin={10}
                                    />
                                    <YAxis
                                        tick={{ fontSize: 11, fill: '#888' }}
                                        tickLine={false}
                                        axisLine={false}
                                        tickFormatter={v => `${v} ${metric.unit === 'hrs' ? 'hrs' : ''}`}
                                        width={45}
                                    />
                                    <Tooltip formatter={v => [`${v} ${metric.unit}`, metric.label]} cursor={{ fill: 'transparent' }} />
                                    <Bar dataKey="value" fill="#007AFF" radius={[2, 2, 0, 0]} />
                                </BarChart>
                            </ResponsiveContainer>
                        ) : (
                            <div className="flex items-center justify-center h-full text-text-muted text-sm border-b border-[#f3f4f6]">No data yet</div>
                        )}
                    </div>

                    <div className="flex gap-2 mt-5">
                        {METRICS.map(m => (
                            <button
                                key={m.key}
                                onClick={() => setMetric(m)}
                                className={`text-[15px] px-5 py-1.5 rounded-full border transition-colors ${metric.key === m.key ? 'bg-[#007AFF] border-[#007AFF] text-white font-medium' : 'bg-[#F2F2F7] border-[#F2F2F7] text-text-primary'}`}
                            >
                                {m.label}
                            </button>
                        ))}
                    </div>
                </div>

                {/* Dashboard */}
                <div className="mt-2">
                    <h2 className="text-xl text-gray-400 mb-3 font-normal">Dashboard</h2>
                    <div className="grid grid-cols-2 gap-3">
                        {[
                            { label: 'Statistics', emoji: '📈', path: '/profile/statistics' },
                            { label: 'Exercises', emoji: '🏋️', path: '/profile/exercises' },
                            { label: 'Measures', emoji: '🧑‍⚕️', path: '/profile/measures' },
                            { label: 'Calendar', emoji: '📅', path: '/profile/calendar' },
                        ].map(item => (
                            <div key={item.label}
                                onClick={() => navigate(item.path)}
                                className="bg-[#F2F2F7] rounded-xl p-3.5 flex items-center gap-3 cursor-pointer hover:bg-gray-200 transition-colors">
                                <span className="text-xl">{item.emoji}</span>
                                <span className="text-[15px] font-medium text-text-primary">{item.label}</span>
                            </div>
                        ))}
                    </div>
                </div>

                {/* Recent workouts */}
                {workouts.length > 0 && (
                    <div>
                        <h2 className="text-lg font-bold text-text-primary mb-2">Recent Workouts</h2>
                        <div className="bg-white rounded-2xl border border-border overflow-hidden">
                            {workouts.map(w => (
                                <WorkoutCard
                                    key={w.id}
                                    workout={w}
                                    onDelete={(deletedId) => setWorkouts(prev => prev.filter(work => work.id !== deletedId))}
                                />
                            ))}
                        </div>
                    </div>
                )}
            </div>
            <BottomNav />
        </div>
    );
}
