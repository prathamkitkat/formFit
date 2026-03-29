import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Search, Bell, X } from 'lucide-react';
import WorkoutCard from '../../components/workout/WorkoutCard';
import { getWorkouts, searchWorkouts } from '../../api/workout';
import { useWorkout } from '../../context/WorkoutContext';
import BottomNav from '../../components/layout/BottomNav';

export default function HomePage() {
    const navigate = useNavigate();
    const [workouts, setWorkouts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [showSearch, setShowSearch] = useState(false);
    const [searchQuery, setSearchQuery] = useState('');
    const [isSearching, setIsSearching] = useState(false);
    const { activeWorkout } = useWorkout();

    const loadWorkouts = () => {
        setLoading(true);
        getWorkouts()
            .then(r => setWorkouts(r.data))
            .catch(() => { })
            .finally(() => setLoading(false));
    };

    useEffect(() => {
        loadWorkouts();
    }, []);

    useEffect(() => {
        if (!showSearch) return;
        
        if (searchQuery.trim().length > 0) {
            const delayDebounceFn = setTimeout(() => {
                setLoading(true);
                setIsSearching(true);
                searchWorkouts(searchQuery.trim())
                    .then(r => setWorkouts(r.data))
                    .catch(() => {})
                    .finally(() => setLoading(false));
            }, 300);
            return () => clearTimeout(delayDebounceFn);
        } else if (isSearching || workouts.length === 0) {
            loadWorkouts();
            setIsSearching(false);
        }
    }, [searchQuery, showSearch]);

    const handleCloseSearch = () => {
        setShowSearch(false);
        setSearchQuery('');
        if (isSearching) {
            loadWorkouts();
            setIsSearching(false);
        }
    };

    return (
        <div className="min-h-screen bg-white pb-20">
            {/* Header */}
            {showSearch ? (
                <header className="sticky top-0 bg-white z-30 px-4 py-3 flex items-center gap-3 border-b border-border shadow-sm">
                    <div className="flex-1 relative">
                        <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-text-muted" size={18} />
                        <input 
                            className="w-full bg-gray-100 rounded-xl py-2 pl-10 pr-9 text-sm focus:outline-none focus:ring-2 focus:ring-primary/20 transition-all font-medium text-text-primary"
                            placeholder="Search workouts by name..."
                            autoFocus
                            value={searchQuery}
                            onChange={e => setSearchQuery(e.target.value)}
                        />
                        {searchQuery && (
                            <button 
                                onClick={() => setSearchQuery('')}
                                className="absolute right-3 top-1/2 -translate-y-1/2 text-text-muted hover:text-text-primary p-0.5"
                            >
                                <X size={16} />
                            </button>
                        )}
                    </div>
                    <button 
                        onClick={handleCloseSearch} 
                        className="text-sm font-semibold text-primary px-1 hover:text-primary-dark transition-colors"
                    >
                        Cancel
                    </button>
                </header>
            ) : (
                <header className="sticky top-0 bg-white z-30 px-4 py-4 flex items-center justify-between border-b border-border">
                    <div className="flex items-center">
                        <h1 className="text-2xl font-bold text-text-primary">Home</h1>
                    </div>
                    <div className="flex items-center gap-3 text-text-secondary">
                        <button onClick={() => setShowSearch(true)} className="p-1 hover:bg-gray-100 rounded-full transition-colors relative">
                            <Search size={22} />
                        </button>
                        <button className="p-1 hover:bg-gray-100 rounded-full transition-colors">
                            <Bell size={22} />
                        </button>
                    </div>
                </header>
            )}



            {/* Feed */}
            <div className="mt-2">
                {loading && (
                    <div className="flex justify-center py-16">
                        <div className="w-8 h-8 border-2 border-primary border-t-transparent rounded-full animate-spin" />
                    </div>
                )}
                {!loading && workouts.length === 0 && !showSearch && (
                    <div className="flex flex-col items-center py-20 px-8 text-center">
                        <span className="text-5xl mb-4">🏋️</span>
                        <p className="text-lg font-bold text-text-primary">No workouts yet</p>
                        <p className="text-sm text-text-secondary mt-1">Complete your first workout to see it here.</p>
                    </div>
                )}
                {!loading && workouts.length === 0 && showSearch && (
                    <div className="flex flex-col items-center py-20 px-8 text-center">
                        <Search className="w-12 h-12 text-gray-300 mb-4" />
                        <p className="text-lg font-bold text-text-primary">No results found</p>
                        <p className="text-sm text-text-secondary mt-1">Try searching for a different workout name.</p>
                    </div>
                )}
                {workouts.map(w => (
                    <WorkoutCard
                        key={w.id}
                        workout={w}
                        onDelete={(deletedId) => setWorkouts(prev => prev.filter(work => work.id !== deletedId))}
                    />
                ))}
            </div>

            <BottomNav />
        </div>
    );
}
