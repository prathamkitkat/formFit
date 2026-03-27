import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { ChevronLeft, ChevronRight } from 'lucide-react';
import { format, addMonths, subMonths, getYear, getMonth, getDaysInMonth, startOfMonth, getDay, isToday, parseISO } from 'date-fns';
import PageHeader from '../../components/layout/PageHeader';
import { getCalendar } from '../../api/profile';

const DAYS = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];

export default function CalendarPage() {
    const navigate = useNavigate();
    const [date, setDate] = useState(new Date());
    const [calData, setCalData] = useState([]);

    useEffect(() => {
        getCalendar(getYear(date), getMonth(date) + 1)
            .then(r => setCalData(r.data))
            .catch(() => setCalData([]));
    }, [date]);

    const byDate = Object.fromEntries(calData.map(d => [d.date, d]));
    const daysInMonth = getDaysInMonth(date);
    const firstDow = getDay(startOfMonth(date));
    const today = format(new Date(), 'yyyy-MM-dd');
    const restDays = calData.filter(d => d.workoutCount === 0).length;
    const streak = 0; // placeholder

    const days = [];
    for (let i = 0; i < firstDow; i++) days.push(null);
    for (let i = 1; i <= daysInMonth; i++) days.push(i);

    return (
        <div className="min-h-screen bg-background">
            <PageHeader title="Calendar" />

            {/* Month nav */}
            <div className="bg-white px-4 py-3 flex items-center justify-between border-b border-border">
                <button onClick={() => setDate(d => subMonths(d, 1))} className="p-2 text-text-secondary"><ChevronLeft size={20} /></button>
                <p className="font-semibold text-text-primary">{format(date, 'MMMM yyyy')}</p>
                <button onClick={() => setDate(d => addMonths(d, 1))} className="p-2 text-text-secondary"><ChevronRight size={20} /></button>
            </div>

            {/* Streak row */}
            <div className="bg-white px-4 py-3 flex gap-4 border-b border-border">
                <span className="text-sm text-text-secondary">🔥 {streak} week streak</span>
                <span className="text-sm text-text-secondary">🌙 {restDays} rest days</span>
            </div>

            <div className="bg-white px-4 py-4">
                {/* Day headers */}
                <div className="grid grid-cols-7 mb-2">
                    {DAYS.map(d => <p key={d} className="text-center text-xs font-semibold text-text-muted">{d}</p>)}
                </div>

                {/* Day cells */}
                <div className="grid grid-cols-7 gap-y-3">
                    {days.map((day, i) => {
                        if (!day) return <div key={`e-${i}`} />;
                        const dateStr = `${getYear(date)}-${String(getMonth(date) + 1).padStart(2, '0')}-${String(day).padStart(2, '0')}`;
                        const entry = byDate[dateStr];
                        const hasWorkout = entry?.workoutCount > 0;
                        const isTodayDate = dateStr === today;

                        return (
                            <div key={dateStr} className="flex flex-col items-center gap-0.5">
                                <button
                                    onClick={() => hasWorkout && navigate(`/workout/${entry.workoutIds[0]}`)}
                                    className={`w-8 h-8 rounded-full flex items-center justify-center text-sm font-medium transition-colors
                    ${hasWorkout ? 'bg-primary text-white' : isTodayDate ? 'text-primary font-bold' : 'text-text-primary'}
                    ${isTodayDate && hasWorkout ? 'ring-2 ring-primary ring-offset-1' : ''}
                    ${hasWorkout ? 'cursor-pointer hover:bg-primary-dark' : 'cursor-default'}
                  `}
                                >
                                    {day}
                                </button>
                            </div>
                        );
                    })}
                </div>
            </div>
        </div>
    );
}
