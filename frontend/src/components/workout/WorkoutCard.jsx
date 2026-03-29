import { useState, useRef, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { ThumbsUp, MessageCircle, Upload } from 'lucide-react';
import Avatar from '../ui/Avatar';
import { formatDuration, formatVolume, timeAgo } from '../../utils/formatters';
import { useAuth } from '../../context/AuthContext';
import { deleteWorkout } from '../../api/workout';

function ExerciseImage({ imageUrl, name }) {
    if (imageUrl) {
        return <img src={imageUrl} alt={name} className="w-10 h-10 rounded-full object-cover flex-shrink-0" />;
    }
    return (
        <div className="w-10 h-10 rounded-full bg-gray-200 flex items-center justify-center flex-shrink-0">
            <span className="text-sm font-bold text-gray-500">{name?.[0]?.toUpperCase()}</span>
        </div>
    );
}

export default function WorkoutCard({ workout, onDelete }) {
    const navigate = useNavigate();
    const { currentUser } = useAuth();
    const [menuOpen, setMenuOpen] = useState(false);
    const menuRef = useRef(null);

    useEffect(() => {
        function handleClickOutside(event) {
            if (menuRef.current && !menuRef.current.contains(event.target)) {
                setMenuOpen(false);
            }
        }
        document.addEventListener("mousedown", handleClickOutside);
        return () => document.removeEventListener("mousedown", handleClickOutside);
    }, [menuRef]);

    const {
        id,
        name,
        endedAt,
        duration,
        totalVolume,
        recordsCount,
        exerciseCount,
        exerciseSummaries = [],
    } = workout;

    const handleDelete = async (e) => {
        e.stopPropagation();
        try {
            await deleteWorkout(id);
            if (onDelete) {
                onDelete(id);
            }
        } catch (error) {
            console.error("Failed to delete workout:", error);
            alert("Failed to delete workout.");
        }
        setMenuOpen(false);
    };

    const username = currentUser?.username || 'You';
    const extraCount = exerciseCount > 3 ? exerciseCount - 3 : 0;

    return (
        <article
            className="bg-white px-4 py-4 border-b border-border cursor-pointer active:bg-gray-50"
            onClick={() => navigate(`/workout/${id}`)}
        >
            {/* Header row */}
            <div className="flex items-center justify-between mb-3">
                <div className="flex items-center gap-2">
                    <Avatar name={username} size={10} />
                    <div>
                        <p className="text-sm font-semibold text-text-primary leading-none">{username}</p>
                        <p className="text-xs text-text-secondary">{timeAgo(endedAt)}</p>
                    </div>
                </div>
                <div className="relative" ref={menuRef}>
                    <button
                        className="text-text-secondary p-1 rounded-full hover:bg-gray-100 transition-colors"
                        onClick={e => { e.stopPropagation(); setMenuOpen(!menuOpen); }}
                    >
                        ···
                    </button>
                    {menuOpen && (
                        <div className="absolute right-0 mt-2 w-32 bg-white rounded-md shadow-lg border border-gray-200 z-50 overflow-hidden">
                            <button
                                className="w-full text-left px-4 py-2 text-sm text-red-600 hover:bg-red-50 transition-colors font-medium"
                                onClick={handleDelete}
                            >
                                Delete Workout
                            </button>
                        </div>
                    )}
                </div>
            </div>

            {/* Workout name */}
            <h2 className="text-lg font-bold text-text-primary mb-3">{name}</h2>

            {/* Stats row */}
            <div className="flex gap-6 mb-4">
                <div>
                    <p className="text-xs text-text-secondary">Time</p>
                    <p className="text-sm font-semibold text-text-primary">{formatDuration(duration)}</p>
                </div>
                <div>
                    <p className="text-xs text-text-secondary">Volume</p>
                    <p className="text-sm font-semibold text-text-primary">{formatVolume(totalVolume)}</p>
                </div>
                {recordsCount > 0 && (
                    <div>
                        <p className="text-xs text-text-secondary">Records</p>
                        <p className="text-sm font-semibold text-text-primary">🥇 {recordsCount}</p>
                    </div>
                )}
            </div>

            {/* Exercise summaries */}
            <div className="flex flex-col gap-2 mb-3">
                {exerciseSummaries.map((ex, i) => (
                    <div key={i} className="flex items-center gap-3">
                        <ExerciseImage name={ex.exerciseName} imageUrl={ex.imageUrl} />
                        <p className="text-sm text-text-primary">
                            <span className="font-medium">{ex.setCount} set{ex.setCount !== 1 ? 's' : ''}</span>{' '}
                            {ex.exerciseName}
                        </p>
                    </div>
                ))}
                {extraCount > 0 && (
                    <p className="text-sm text-text-muted text-center mt-1">
                        See {extraCount} more exercise{extraCount !== 1 ? 's' : ''}
                    </p>
                )}
            </div>

            {/* Action buttons */}
            <div className="flex items-center gap-5 pt-3 border-t border-border text-text-primary mt-1">
                <button onClick={e => e.stopPropagation()} className="hover:text-primary transition-colors p-1 -ml-1"><ThumbsUp strokeWidth={1.5} size={22} /></button>
                <button onClick={e => e.stopPropagation()} className="hover:text-primary transition-colors p-1"><MessageCircle strokeWidth={1.5} size={22} /></button>
                <button onClick={e => e.stopPropagation()} className="hover:text-primary transition-colors p-1"><Upload strokeWidth={1.5} size={22} /></button>
            </div>
        </article>
    );
}
