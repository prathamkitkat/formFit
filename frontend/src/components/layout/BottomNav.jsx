import { useNavigate, useLocation } from 'react-router-dom';
import { Home, Dumbbell, User } from 'lucide-react';

const TABS = [
    { path: '/home', label: 'Home', icon: Home },
    { path: '/workout', label: 'Workout', icon: Dumbbell },
    { path: '/profile', label: 'Profile', icon: User },
];

export default function BottomNav() {
    const navigate = useNavigate();
    const { pathname } = useLocation();

    return (
        <nav className="fixed bottom-0 left-1/2 -translate-x-1/2 w-full max-w-[480px] bg-white border-t border-border h-16 flex items-center z-40">
            {TABS.map(({ path, label, icon: Icon }) => {
                const active = pathname.startsWith(path);
                return (
                    <button
                        key={path}
                        onClick={() => navigate(path)}
                        className={`flex-1 flex flex-col items-center justify-center gap-0.5 h-full transition-colors ${active ? 'text-primary' : 'text-text-secondary'
                            }`}
                    >
                        <Icon size={22} strokeWidth={active ? 2.5 : 1.8} />
                        <span className="text-xs font-medium">{label}</span>
                    </button>
                );
            })}
        </nav>
    );
}
