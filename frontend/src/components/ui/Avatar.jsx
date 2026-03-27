export default function Avatar({ name, size = 10 }) {
    const letter = name ? name[0].toUpperCase() : '?';
    return (
        <div
            className={`w-${size} h-${size} rounded-full bg-gray-400 flex items-center justify-center text-white font-bold text-base flex-shrink-0`}
        >
            {letter}
        </div>
    );
}
