export default function PrBadge({ label }) {
    return (
        <span className="inline-flex items-center gap-1 bg-amber-100 text-amber-700 text-xs px-2 py-0.5 rounded-full font-medium">
            🏅 {label}
        </span>
    );
}
