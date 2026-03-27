import { formatDistanceToNow, format, parseISO } from 'date-fns';

export function formatDuration(minutes) {
    if (!minutes && minutes !== 0) return '—';
    const h = Math.floor(minutes / 60);
    const m = minutes % 60;
    if (h > 0 && m > 0) return `${h}h ${m}min`;
    if (h > 0) return `${h}h`;
    return `${m}min`;
}

export function formatVolume(kg) {
    if (kg === null || kg === undefined) return '0 kg';
    return kg.toLocaleString('en-US', { maximumFractionDigits: 1 }) + ' kg';
}

export function timeAgo(dateStr) {
    if (!dateStr) return '';
    try {
        return formatDistanceToNow(parseISO(dateStr), { addSuffix: true });
    } catch {
        return '';
    }
}

export function formatWorkoutDate(startedAt, endedAt) {
    if (!startedAt) return '';
    try {
        const start = parseISO(startedAt);
        const dateStr = format(start, 'EEEE, MMM d');
        const startTime = format(start, 'h:mma').toLowerCase();
        if (endedAt) {
            const endTime = format(parseISO(endedAt), 'h:mma').toLowerCase();
            return `${dateStr} · ${startTime} – ${endTime}`;
        }
        return `${dateStr} · ${startTime}`;
    } catch {
        return '';
    }
}

export function formatDate(dateStr) {
    if (!dateStr) return '';
    try {
        return format(parseISO(dateStr), 'MMM d, yyyy');
    } catch {
        return dateStr;
    }
}

export function parseErrorMessage(err) {
    // Backend throws RuntimeException which returns 500 with message in body
    if (err.response?.data) {
        const data = err.response.data;
        if (typeof data === 'string') return data;
        if (data.message) return data.message;
    }
    return err.message || 'Something went wrong';
}
