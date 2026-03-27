import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import PageHeader from '../../components/layout/PageHeader';
import { useAuth } from '../../context/AuthContext';
import { useToast } from '../../context/ToastContext';
import { updateUsername, updateEmail, updatePassword, updateWeightUnit } from '../../api/user';
import { parseErrorMessage } from '../../utils/formatters';

function SettingsSection({ title, children }) {
    return (
        <div className="mb-4">
            <p className="text-xs font-bold text-text-secondary uppercase tracking-wider px-4 py-2">{title}</p>
            <div className="bg-white border-t border-b border-border">{children}</div>
        </div>
    );
}

function EditRow({ label, onSave, type = 'text', placeholder }) {
    const [editing, setEditing] = useState(false);
    const [value, setValue] = useState('');
    const { addToast } = useToast();
    const [loading, setLoading] = useState(false);

    const handleSave = async () => {
        setLoading(true);
        try { await onSave(value); setEditing(false); setValue(''); addToast(`${label} updated!`, 'success'); }
        catch (err) { addToast(parseErrorMessage(err)); }
        finally { setLoading(false); }
    };

    if (editing) {
        return (
            <div className="px-4 py-3 border-b border-border flex items-center gap-3">
                <input
                    className="flex-1 text-sm outline-none border-b border-primary py-1"
                    type={type}
                    value={value}
                    onChange={e => setValue(e.target.value)}
                    placeholder={placeholder || label}
                    autoFocus
                />
                <button onClick={() => setEditing(false)} className="text-text-secondary text-sm">Cancel</button>
                <button onClick={handleSave} disabled={loading} className="text-primary text-sm font-semibold">Save</button>
            </div>
        );
    }

    return (
        <button onClick={() => setEditing(true)} className="w-full flex items-center justify-between px-4 py-3.5 border-b border-border last:border-0 hover:bg-gray-50 text-left">
            <span className="text-sm text-text-primary">{label}</span>
            <span className="text-text-muted">›</span>
        </button>
    );
}

export default function SettingsPage() {
    const navigate = useNavigate();
    const { logout, currentUser, weightUnit, updateWeightUnit: setUnit } = useAuth();
    const { addToast } = useToast();
    const [pwForm, setPwForm] = useState({ current: '', new: '' });

    const handlePassword = async () => {
        try {
            await updatePassword(pwForm.current, pwForm.new);
            addToast('Password updated!', 'success');
            setPwForm({ current: '', new: '' });
        } catch (err) {
            addToast(parseErrorMessage(err));
        }
    };

    const handleWeightUnit = async (unit) => {
        try {
            await updateWeightUnit(unit);
            setUnit(unit);
            addToast(`Weight unit set to ${unit}`, 'success');
        } catch (err) {
            addToast(parseErrorMessage(err));
        }
    };

    return (
        <div className="min-h-screen bg-background">
            <PageHeader title="Settings" />

            <SettingsSection title="Account">
                <EditRow label="Change Username" onSave={v => updateUsername(v)} placeholder="New username" />
                <EditRow label="Change Email" onSave={v => updateEmail(v)} type="email" placeholder="New email" />
                <div className="px-4 py-3 flex flex-col gap-2 border-b border-border">
                    <p className="text-sm text-text-primary font-medium">Change Password</p>
                    <input
                        className="input-field text-sm py-2"
                        type="password"
                        placeholder="Current password"
                        value={pwForm.current}
                        onChange={e => setPwForm(f => ({ ...f, current: e.target.value }))}
                    />
                    <input
                        className="input-field text-sm py-2"
                        type="password"
                        placeholder="New password"
                        value={pwForm.new}
                        onChange={e => setPwForm(f => ({ ...f, new: e.target.value }))}
                    />
                    <button onClick={handlePassword} className="btn-primary mt-1">Update Password</button>
                </div>
            </SettingsSection>

            <SettingsSection title="Preferences">
                <div className="px-4 py-3.5 flex items-center justify-between border-b border-border">
                    <span className="text-sm text-text-primary">Weight Unit</span>
                    <div className="flex gap-1 p-1 bg-background rounded-xl">
                        {['KG', 'LB'].map(u => (
                            <button
                                key={u}
                                onClick={() => handleWeightUnit(u)}
                                className={`px-4 py-1.5 rounded-lg text-sm font-semibold transition-colors ${weightUnit === u ? 'bg-white shadow text-primary' : 'text-text-secondary'}`}
                            >
                                {u}
                            </button>
                        ))}
                    </div>
                </div>
            </SettingsSection>

            <SettingsSection title="Danger Zone">
                <button
                    onClick={logout}
                    className="w-full text-left px-4 py-3.5 text-danger text-sm font-semibold hover:bg-red-50 transition-colors"
                >
                    Logout
                </button>
            </SettingsSection>
        </div>
    );
}
