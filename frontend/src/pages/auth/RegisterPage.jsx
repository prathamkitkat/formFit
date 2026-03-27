import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { register as registerApi } from '../../api/auth';
import { parseErrorMessage } from '../../utils/formatters';

export default function RegisterPage() {
    const navigate = useNavigate();
    const [form, setForm] = useState({ username: '', email: '', password: '' });
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);
    const timezone = Intl.DateTimeFormat().resolvedOptions().timeZone;

    const set = (k) => (e) => setForm(f => ({ ...f, [k]: e.target.value }));

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setLoading(true);
        try {
            await registerApi(form.username, form.email, form.password, timezone);
            navigate('/login');
        } catch (err) {
            setError(parseErrorMessage(err) || 'Registration failed');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="min-h-screen bg-background flex flex-col items-center justify-center px-4">
            <div className="w-full max-w-sm">
                <div className="text-center mb-10">
                    <div className="w-16 h-16 rounded-2xl bg-primary flex items-center justify-center mx-auto mb-4 shadow-lg">
                        <span className="text-white text-2xl font-bold">FF</span>
                    </div>
                    <h1 className="text-3xl font-bold text-text-primary">Create Account</h1>
                    <p className="text-text-secondary text-sm mt-1">Join FormFit today</p>
                </div>

                <form onSubmit={handleSubmit} className="flex flex-col gap-3">
                    <input className="input-field" placeholder="Username" value={form.username} onChange={set('username')} required />
                    <input className="input-field" type="email" placeholder="Email" value={form.email} onChange={set('email')} required />
                    <input className="input-field" type="password" placeholder="Password" value={form.password} onChange={set('password')} required />
                    <div className="input-field bg-background text-text-muted text-sm cursor-not-allowed">
                        {timezone}
                    </div>

                    {error && <p className="text-danger text-sm text-center">{error}</p>}
                    <button type="submit" className="btn-primary mt-2" disabled={loading}>
                        {loading ? 'Creating account…' : 'Register'}
                    </button>
                </form>

                <p className="text-center text-sm text-text-secondary mt-6">
                    Already have an account?{' '}
                    <Link to="/login" className="text-primary font-semibold">Login</Link>
                </p>
            </div>
        </div>
    );
}
