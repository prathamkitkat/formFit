import { useNavigate } from 'react-router-dom';
import { ChevronLeft } from 'lucide-react';

export default function PageHeader({ title, onBack, rightElement }) {
    const navigate = useNavigate();

    return (
        <header className="sticky top-0 bg-white border-b border-border z-30 flex items-center h-14 px-4">
            <button
                onClick={onBack || (() => navigate(-1))}
                className="w-8 h-8 flex items-center justify-center text-text-primary"
            >
                <ChevronLeft size={24} />
            </button>
            <h1 className="flex-1 text-center text-base font-semibold text-text-primary">{title}</h1>
            <div className="w-8 h-8 flex items-center justify-center">
                {rightElement || null}
            </div>
        </header>
    );
}
