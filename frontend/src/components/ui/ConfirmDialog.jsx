export default function ConfirmDialog({ title, message, onConfirm, onCancel, confirmText = 'Confirm', confirmClass = 'bg-danger text-white' }) {
    return (
        <div className="fixed inset-0 bg-black/50 z-50 flex items-end justify-center p-4">
            <div className="bg-white rounded-2xl w-full max-w-sm p-6 flex flex-col gap-4">
                <div>
                    <h3 className="text-lg font-bold text-text-primary">{title}</h3>
                    {message && <p className="text-sm text-text-secondary mt-1">{message}</p>}
                </div>
                <div className="flex flex-col gap-2">
                    <button onClick={onConfirm} className={`w-full py-3 rounded-xl font-semibold ${confirmClass}`}>
                        {confirmText}
                    </button>
                    <button onClick={onCancel} className="w-full py-3 rounded-xl font-semibold border border-border text-text-primary">
                        Cancel
                    </button>
                </div>
            </div>
        </div>
    );
}
